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
	                        	msj = getDniMsj(msj);
		                        if (!server.existeCliente(msj)) {
		                        	
		                            server.clientes.addLast(msj);
		                            
		                            try {
			                            server.emisor_server_heartbeat.enviar("Agregar/" + msj, Utils.Server_to_Server2);
		                            }
	                            	catch(Exception e) {
	                            	}
		                            finally {
		                            	String puerto = Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION) + puesto);
			                            
			                            emisor_registro.enviar("OK", puerto);
			                            synchronized (lock) {
			                                lock.notifyAll(); 
			                            }
		                            }
		                            
		                        }
		                        else
		                            emisor_registro.enviar("REPETIDO", Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)+ puesto));
	                        }
	                        else {
	                        	emisor_registro.enviar(Integer.toString(server.contadorReg), Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	                        	server.contadorReg = server.contadorReg + 1;
	                        }
	                    }
	                    
	                } catch (Exception e) {
	                    System.out.println("Excepcion en hilo receptor del registro " + e.getMessage());
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
						if(msj != null)
							if(msj.equals("Cliente")) {
								System.out.println("SERVIDOR ENVIAR CLIENTE");
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(vector[1]));
								if (!server.getClientes().isEmpty()) {
								    String dni = server.getClientes().removeFirst();
									emisor_empleado.enviar(dni, puerto);
									try {
										emisor_server_heartbeat.enviar("Eliminar/"+dni, Utils.Server_to_Server2);
									}
									catch(Exception e) {
									}
								}

							}
							else if (msj.equals("Estado")) {
								Thread.sleep(30);
							}
						
							else if (msj.length() < 7 && !server.existeEmpleado(msj)) {
								//Al llegar aca, msj es el puesto del empleado (vector[0])
	                        	listaEmpleados.add(msj);
	                        	try {
	                        		emisor_server_heartbeat.enviar("Agregar empleado/"+msj,Utils.Server_to_Server2);
	                        	}catch(Exception e) {
	                        	}
	                        	
	                        	
	                        }
							else {
								emisor_pantalla.enviar(msj+"/"+vector[1], Utils.Server_to_Pantalla); 
						}
						else {
							System.out.println("Servidor 82 msj null");
						}
					}
						 catch (Exception e) {
						System.out.println("Excepcion en hilo receptor del empleado" + e.getMessage());
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
						for (int i=0; i<=listaEmpleados.size(); i++) {
							String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(listaEmpleados.get(i)));
							synchronized (lock2) {
							    if (server.clientes.isEmpty())
							    	emisor_empleado.enviar("LISTA_VACIA", puerto);
							    else
							    	emisor_empleado.enviar("HAY_CLIENTES", puerto);
								lock2.wait(300);
							}
						}
					}
					catch(Exception e) {
						//System.out.println("Excepcion en hilo estado de la cola: " + e.getMessage());
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
		                        servidorPpalVivo();
		                    }
		                    else {
		                    	String[] vector = msj.split("/");
		                    	String orden = vector[0];
		                    	//System.out.println("orden"+ orden);
		                    	String dni = vector[1];
		                    	//System.out.println("dni"+ dni);
		                    	
		                    	if(orden.equals("Agregar")) {
		                    		this.clientes.addLast(dni);
		                    		System.out.println("HILOHEARTBEAT AGREGAR");
		                    	}
		                    	else if (orden.equals("Eliminar")) {
		                    		this.clientes.remove(dni);
		                    		System.out.println("HILOHEARTBEAT ELIMINAR");
		                    	}
		                    	else if(orden.equals("Agregar empleado")){
		                    		listaEmpleados.add(dni);
		                    		System.out.println("HILOHEARTBEAT AGREGAREMPLEADO "+listaEmpleados);
		                    	}
		                    	else if(orden.equals("Eliminar empleado")) {
		                    		System.out.println("Elimiar empleado servidor secundario");
		                    	}
		                    	else if(orden.equals("SincronizacionDni")) {
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.clientes.addLast(vector[i]);
		                    		}
		                    	}
		                    	else if(orden.equals("SincronizacionEmp")) {
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.listaEmpleados.add(vector[i]);
		                    		}
		                    
		                    	}
		                    }
	                    }
	                    else {
	                        servidorPpalMuerto();
	                        estadoSec = false;
	                    }
	                } else {
	                	try {
	                		emisor_server_heartbeat.enviar(".", Utils.Server_to_Server2);
	                		if(this.primerHeartBeat) {
		                		String listaDNI = "";
		                		if(!this.clientes.isEmpty()) {
		                			System.out.println("SERVIDOR 289");
			                		for(int i=0; i<this.clientes.size();i++) {
			                			listaDNI+=clientes.get(i)+"/";
			                		}
			                        emisor_server_heartbeat.enviar("SincronizacionDni/"+listaDNI, Utils.Server_to_Server2);
		                		}
		                        String listaPuestoEmp = "";
		                        if(!this.listaEmpleados.isEmpty()) {
			                        for(int i=0; i<this.listaEmpleados.size();i++) {
			                        	listaPuestoEmp+=listaEmpleados.get(i)+"/";
			                		}
			                        emisor_server_heartbeat.enviar("SincronizacionEmp/"+listaPuestoEmp, Utils.Server_to_Server2);
		                        }
		                        this.primerHeartBeat=false;
		                	}
	                	else {
		                    Thread.sleep(5000); 
		                    emisor_server_heartbeat.enviar("HEARTBEAT", Utils.Server_to_Server2);
	                		}
	                	}
	                	catch(Exception e) {
	                	}
	                }
	            }catch(ArrayIndexOutOfBoundsException e) {
	            	System.out.println(e.getMessage());
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	                
	            }
	        }
	    });
	    hiloHeartbeat.start();
	}
	
	public void servidorPpalVivo() {
		System.out.println("Servidor principal vivo");
	}
	
	public void servidorPpalMuerto() {
		System.out.println("Servidor principal muerto, iniciando servidor secundario");
		try {
			iniciaReceptores();
			this.receptor_server_heartbeat.kill();
			this.hilosPpales();
			this.hiloHeartbeat();
		} catch (BindException e) { 
			System.out.println(e.getMessage());
		}
	}
	
	public void hilosPpales() {
		this.emisor_server_heartbeat = new Emisor();
		this.hiloRecEmp(this);
		this.hiloReg(this);
		this.hiloEstadoCola(this);
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
}	