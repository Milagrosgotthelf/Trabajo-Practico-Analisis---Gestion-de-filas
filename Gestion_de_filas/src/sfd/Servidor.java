package sfd;

import java.net.BindException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.LinkedList;
public class Servidor {
	
	private Receptor receptor_registro; //
	private Receptor receptor_empleado;
	private Receptor receptor_server_heartbeat = null;
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Emisor emisor_registro = new Emisor();
	
	private Emisor emisor_server_heartbeat = null;
	private Object lock = new Object();
	private Object lock2 = new Object();
	private LinkedList<String> clientes= new LinkedList<String>();
	
	private ArrayList<String> listaEmpleados = new ArrayList<String>();
	private int contadorReg = 1;
	private Thread hiloRec, hiloEstadoCol;
	private volatile boolean estadoSec = false ;
	private boolean primerHeartBeat = true; 
	
	private ArrayList<Object> semaforoEmpleados = new ArrayList<Object>();
	private GestorSeguridad gestorSeguridad = new GestorSeguridad();

	 
	public Servidor() {
		System.out.println("Servidor iniciado");
		try {
			iniciaReceptores();
			this.hilosPpales();
			this.hiloHeartbeat();
		} catch (BindException e) {
			System.out.println("Iniciando servidor secundario");
			inicioSecundario();
		}
	}
	
	
	
	public void agregarCliente(String cliente) {
		clientes.addLast(cliente);
	}
	
	private void hiloReg(Servidor server) {
	    Thread hilo = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    String msj = receptor_registro.getMensaje(); 
	                    if (msj != null) {
	                        if(!msj.equals("TerminalActiva")) {
	                        	int puesto = Integer.parseInt(getPuestoMsj(msj));
	                        	msj = getDniMsj(msj); //ENCRIPTADO
	                        	String msjDesencriptado = gestorSeguridad.recuperarDNI(msj);
	                        	System.out.println("TERMINAL REGISTRO --- Solicitud de ingreso para DNI: " + msj + " desde puesto: " + puesto);
		                        if (!server.existeCliente(msjDesencriptado)) {
		                        	
		                            server.clientes.addLast(msj); //GUARDAMOS EL ENCRIPTADO
		                            System.out.println("TERMINAL REGISTRO --- Cliente agregado exitosamente. ");
		                            try {
			                            server.emisor_server_heartbeat.enviar("Agregar/" + msj, Utils.Server_to_Server2);
			                            System.out.println("HEARTBEAT --- Enviada orden 'Agregar Cliente' al servidor secundario.");
		                            }
	                            	catch(Exception e) {
	                            		
	                            	}
		                            finally {
		                            	String puerto = Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION) + puesto);
			                            server.enviarReintento(emisor_registro, "OK", puerto);
			                            synchronized (lock) {
			                                lock.notifyAll(); 
			                            }
		                            }
		                        }
		                        else {
		                        	System.out.println("TERMINAL REGISTRO --- Alerta: Intento de agregar DNI repetido (" + msj + "). Enviando rechazo.");
		                        	server.enviarReintento(emisor_registro, "REPETIDO", Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)+ puesto));
		                        }
	                        }
	                        else {
	                        	System.out.println("TERMINAL REGISTRO --- Terminal activa detectada. Asignando ID de Registro: " + server.contadorReg);
	                        	server.enviarReintento(emisor_registro, Integer.toString(server.contadorReg), Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	                        	server.contadorReg = server.contadorReg + 1;
	                        }
	                    }
	                    
	                } catch (Exception e) {
	                   
	                }
	            }
	        }
	    });
	    //hilo.setDaemon(true); 
	    hilo.start();
	}
	
	protected boolean existeCliente(String msj) {
		for (String c : clientes) {
	        if (c.equals(msj)) {
	            return true;
	        }
	    }
	    return false;
	}

	private void hiloRecEmp(Servidor server) {
		this.hiloRec = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String msj = receptor_empleado.getMensaje(); 
						String[] vector = msj.split("/");
						msj = vector[0];
						if(msj != null) {
							if(msj.equals("Cliente")) {
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(vector[1]));
								System.out.println("EMPLEADO --- Puesto " + vector[1] + " está solicitando el siguiente cliente.");
								//Si se atrasa esto se come al dni
								Object lockDelEmpleado = semaforoEmpleados.get(listaEmpleados.indexOf(vector[1]));
								
								synchronized (lockDelEmpleado) {
									if (!server.getClientes().isEmpty()) {
									    String dni = server.getClientes().removeFirst();
									    System.out.println("EMPLEADO --- Asignando DNI " + dni + " al Puesto " + vector[1] + ". Quedan " + server.getClientes().size() + " en cola.");
									    server.enviarReintento(emisor_empleado, dni, puerto); //LO ENVIAMOS ENCRIPTADO
									    try {
									    	emisor_server_heartbeat.enviar("Eliminar/"+dni, Utils.Server_to_Server2); //LO ENVIAMOS ENCRIPTADO Y COMO LO GUARDO ENCRIPTADO NO DEBERIA DE HABER PROBLEMA
									    	System.out.println("HEARTBEAT --- Enviada orden 'Eliminar DNI' al servidor secundario.");
									    }catch(Exception e) {//Esto está para que no moleste cuando no hay un servidor secundario
									    	
									    }
									}
								}
								

							}
							else if (msj.equals("Estado")) {
								String puesto = vector[1];
							    int index = listaEmpleados.indexOf(puesto);
							    
							    if (index != -1) {
							        String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(puesto));
							        Object lockDelEmpleado = semaforoEmpleados.get(index);
							        
							        synchronized (lockDelEmpleado) {
							            boolean bool;
							            if (server.getClientes().isEmpty()) {
							                bool = server.enviarReintento(emisor_empleado, "LISTA_VACIA", puerto);
							            } else {
							                bool = server.enviarReintento(emisor_empleado, "HAY_CLIENTES", puerto);
							            }
							            
							            if(!bool) {
							                try {
							                    emisor_server_heartbeat.enviar("Eliminar empleado/" + puesto, Utils.Server_to_Server2);
							                    System.out.println("HEARTBEAT --- Enviada orden 'Eliminar empleado' al servidor secundario.");
							                } catch(Exception e) {}
							                server.listaEmpleados.remove(index);
							                server.semaforoEmpleados.remove(index);
							                System.out.println("Empleado " + puesto + " desconectado y eliminado.");
							            }
							        }
							    }
							}
							else if (msj.equals("Desconectar")) {
							    String puesto = vector[1];
							    int index = listaEmpleados.indexOf(puesto);
							    
							    if (index != -1) {
							        Object lockDelEmpleado = semaforoEmpleados.get(index);
							        synchronized (lockDelEmpleado) {
							            try {
							                emisor_server_heartbeat.enviar("Eliminar empleado/" + puesto, Utils.Server_to_Server2);
							            } catch (Exception e) {}
							            
							            server.listaEmpleados.remove(index);
							            server.semaforoEmpleados.remove(index);
							            System.out.println("EMPLEADO --- Puesto " + puesto + " se ha desconectado voluntariamente.");
							        }
							    }
							}
							else if ((msj.length() < 7) && !server.existeEmpleado(msj)) {
								//Aca entran los numeros de puesto
								System.out.println("EMPLEADO --- Registrando nueva terminal de atención física. Puesto: " + msj);
		                        listaEmpleados.add(msj);
		                        semaforoEmpleados.add(new Object());
	                        	try {
	                        		emisor_server_heartbeat.enviar("Agregar empleado/"+msj,Utils.Server_to_Server2);
	                        		System.out.println("HEARTBEAT --- Enviada orden 'Agregar Empleado' al servidor secundario.");
	                        	}catch(Exception e) {//Esto está para que no moleste cuando no hay un servidor secundario
	                        	}
	                        }
							else if (msj.length() >= 7) {
								//Aca entran los dni
								System.out.println("PANTALLA --- Enviando DNI " + msj + " (Puesto " + vector[1] + ") hacia la pantalla central.");
								server.enviarReintento(emisor_pantalla, msj+"/"+vector[1], Utils.Server_to_Pantalla); //VIAJA ENCRIPTADO A LA PANTALLA
							}
						}
						else {
							System.out.println("Servidor 82 msj null");
						}
					}
						catch (Exception e) {
						System.out.println("Excepcion en hilo receptor del empleado" + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		});
		//this.hiloRec.setDaemon(true); 
		this.hiloRec.start();
	}
	
	private void hiloEstadoCola(Servidor server) {
		this.hiloEstadoCol = new Thread(new Runnable() {
			@Override
			public void run() {
				
				System.out.println("Iniciando hilo estado de la cola");
					while (true) {
						try {
							for (int i=0; i<listaEmpleados.size(); i++) {
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(listaEmpleados.get(i)));
								boolean bool=false;
								
									Object lockDelEmpleado = semaforoEmpleados.get(i);
									synchronized(lockDelEmpleado) {
										if (server.clientes.isEmpty()) {
									    	bool = server.enviarReintento(emisor_empleado, "LISTA_VACIA", puerto);
									    }
									    else {
									    	bool =server.enviarReintento(emisor_empleado, "HAY_CLIENTES", puerto);
									    }
										if(!bool) {
											server.enviarReintento(emisor_server_heartbeat, "Eliminar empleado/"+listaEmpleados.get(i), Utils.Server_to_Server2);
											server.listaEmpleados.remove(i);
											server.semaforoEmpleados.remove(i);
										}
									}
								
							}
							//Thread.sleep no puede estar dentro del for, ni fuera del while
							Thread.sleep(300);
						} catch (Exception e) {
							System.out.println("Excepcion en hilo estado de la cola " + e.getMessage());
						}
					}
				}
			});
		//this.hiloEstadoCol.setDaemon(true); 
		this.hiloEstadoCol.start();
	}
		
	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	public boolean existeEmpleado(String emp) {
		return this.listaEmpleados.contains(emp);
		
	}
	
	public String getPuestoMsj(String msj) {
		String puesto = "";
		int i=0;
		while(i<msj.length() && msj.charAt(i)!= '/') {
			i++;
		}
		i++;
		while(i<msj.length()) {
			puesto += msj.charAt(i);
			i++;
		}
		return puesto;
	}
	
	public String getDniMsj(String msj) {
		String dni = "";
		int i=0;
		while(i<msj.length() && msj.charAt(i)!= '/') {
			dni += msj.charAt(i);
			i++;
		}
		return dni;
	}
	
	//ITERACION 3
	
	public void hiloHeartbeat() {
	    Thread hiloHeartbeat = new Thread(() -> {
	        while (true) {
	            try {
	                if (estadoSec) {
	                    String msj = receptor_server_heartbeat.getHeartbeat();
	                    if(msj != null) {
		                    if ("HEARTBEAT".equals(msj)) {
		                        //servidorPpalVivo();
		                    }
		                    else {
		                    	String[] vector = msj.split("/");
		                    	String orden = vector[0];
		                    	String dni = vector[1];
		                    	System.out.println("SERVIDOR SECUNDARIO --- Recibida orden de sincronización: " + orden);
		                    	if(orden.equals("Agregar")) {
		                    		this.clientes.addLast(dni);
		                    	}
		                    	else if (orden.equals("Eliminar")) {
		                    		this.clientes.remove(dni);
		                    	}
		                    	else if(orden.equals("Agregar empleado")){
		                    		listaEmpleados.add(dni);
		                    		semaforoEmpleados.add(new Object());
		                    	}
		                    	else if(orden.equals("Eliminar empleado")) {
		                    		int index = listaEmpleados.indexOf(dni);
		                    		listaEmpleados.remove(index);
		                    		semaforoEmpleados.remove(index);
		                    	}
		                    	else if(orden.equals("SincronizacionDni")) {
		                    		//Testear inicializando el vector aca, para que en un caso hipotetico
		                    		//de problema temporal de conexion no se dupliquen datos
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.clientes.addLast(vector[i]);
		                    		}
		                    	}
		                    	else if(orden.equals("SincronizacionEmp")) {
		                    		//Testear inicializando el vector aca, para que en un caso hipotetico
		                    		//de problema temporal de conexion no se dupliquen datos
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.listaEmpleados.add(vector[i]);
		                    			this.semaforoEmpleados.add(new Object());
		                    		}
		                    	}
		                    }
	                    }
	                    else {
	                    	
	                        servidorPpalMuerto();
	                        estadoSec = false;
	                    }
	                } else {
                		emisor_server_heartbeat.enviar(".", Utils.Server_to_Server2);
                		if(this.primerHeartBeat) {
	                		String listaDNI = "";
	                		System.out.println("SERVIDOR PRIMARIO --- Sincronizando estado al Servidor Secundario");
	                		if(!this.clientes.isEmpty()) {
		                		for(int i=0; i<this.clientes.size();i++) {
		                			listaDNI+=clientes.get(i)+"/";
		                		}
		                        emisor_server_heartbeat.enviar("SincronizacionDni/"+listaDNI, Utils.Server_to_Server2);
		                        System.out.println("SERVIDOR PRIMARIO --- Sincronizando DNI'S del Servidor Primario al Servidor Secundario");
	                		}
	                        String listaPuestoEmp = "";
	                        if(!this.listaEmpleados.isEmpty()) {
		                        for(int i=0; i<this.listaEmpleados.size();i++) {
		                        	listaPuestoEmp+=listaEmpleados.get(i)+"/";
		                		}
		                        emisor_server_heartbeat.enviar("SincronizacionEmp/"+listaPuestoEmp, Utils.Server_to_Server2);
		                        System.out.println("SERVIDOR PRIMARIO --- Sincronizando empleados del Servidor Primario al Servidor Secundario");
	                        }
	                        this.primerHeartBeat=false;
	                	}
                	else {
	                    emisor_server_heartbeat.enviar("HEARTBEAT", Utils.Server_to_Server2);
                		}
	                }
	            }catch(ConnectException e) {
	            	this.primerHeartBeat = true;
	            	
	            }catch(IndexOutOfBoundsException e) {
	            	
	            }
	            catch (Exception e) {
	            	
	            	System.out.println("Excepcion hilo Heartbeat "+e.getMessage());
	                
	            }
	        }
	    });
	    hiloHeartbeat.start();
	}
	
	public void servidorPpalVivo() {
		System.out.println("Servidor principal vivo");
		System.out.println(this.listaEmpleados);
		System.out.println(this.clientes);
	}
	
	public void servidorPpalMuerto() {
		System.out.println("¡¡¡Servidor principal muerto, iniciando servidor secundario!!!");
		try {
			estadoSec = false;
			iniciaReceptores();
			this.receptor_server_heartbeat.kill();
			this.hilosPpales();
		} catch (BindException e) { 
			System.out.println(e.getMessage());
			System.out.println("Servidor Ppal activo");
			System.exit(5);
		}
	}
	
	public void hilosPpales() {
		this.emisor_server_heartbeat = new Emisor();

		//this.hiloEstadoCola(this);
		this.hiloRecEmp(this);
		this.hiloReg(this);
	}

	public void iniciaReceptores() throws BindException {
		this.receptor_registro = new Receptor(Utils.Registro_to_Server);
		this.receptor_empleado = new Receptor(Utils.Empleado_to_Server);
	}
	
	public void inicioSecundario() {
		try {
			this.estadoSec = true;
			this.receptor_server_heartbeat = new Receptor(Utils.Server_to_Server2);
			this.hiloHeartbeat();
			
		}
		 catch (Exception e) {
			 System.out.println("Excepcion al iniciar los receptores: " + e.getMessage());
			 System.exit(1);
		 }
	}
	
	public boolean enviarReintento(Emisor em,String msj, String puerto) {
		int intentos = Utils.Intentos;
		
			while(intentos>0) {
			try {
				intentos--;
				em.enviar(msj, puerto);
				return true;
			} catch (ConnectException e) {
				System.out.println("Fallo de conexion con empleado "+e.getMessage());;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			System.out.println("Falla al intentar enviar desde Servidor el mensaje: "+msj);	
			
		}
			return false;
		
	}
}	