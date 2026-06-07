package sfd;

import java.net.BindException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



import factory.FactoryArchs;
import factory.IAbstractFactory;
import persistencia.IPersistencia.ColaPersistencia;
import persistencia.IPersistencia.NotificacionPersistencia;
import seguridad.GestorSeguridad;
public class Servidor {
	
	private Receptor receptor_registro; //
	private Receptor receptor_empleado;
	private Receptor receptor_server_heartbeat = null;
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Emisor emisor_registro = new Emisor();
	
	private Emisor emisor_server_heartbeat = null;
	private Object lock = new Object();
	private LinkedList<String> clientes= new LinkedList<String>();
	
	private ArrayList<String> listaEmpleados = new ArrayList<String>();
	private int contadorReg = 1;
	private Thread hiloRec;
	private volatile boolean estadoSec = false ;
	private boolean primerHeartBeat = true; 
	
	private ArrayList<Object> semaforoEmpleados = new ArrayList<Object>();
	private GestorSeguridad gestorSeguridad = new GestorSeguridad();
	
	private IAbstractFactory factory; 
	private ColaPersistencia colaAux;
	private Map<String, Long> latidosEmpleados = new ConcurrentHashMap<>();
	private NotificacionPersistencia gestorNotificacion;
	private Map<String, String> clientesEnAtencion = new ConcurrentHashMap<>();
	 
	public Servidor() {
		System.out.println("Servidor iniciado");
		factory= FactoryArchs.getFormato();
	    
		try {
			this.gestorNotificacion = factory.crearNotificacionPersistencia();
			iniciaReceptores();
			this.hilosPpales();
			this.hiloHeartbeat();
			this.colaAux = factory.crearColaPersistencia();
			this.clientes = this.colaAux.recuperarCola();
			
			
			
		} catch (BindException e) {
			System.out.println("Iniciando servidor secundario");
			
			factory= FactoryArchs.getFormato();
		    inicioSecundario();
		}
	}
	
	
	
	public void agregarCliente(String cliente) {
		clientes.addLast(cliente);
		this.colaAux.guardarCola(clientes);
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
		                        if (!server.existeCliente(msj)) {
		                            server.agregarCliente(msj);
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

	private String retiraCliente() {
		String dni = this.getClientes().removeFirst();
		this.colaAux.guardarCola(clientes);
		return dni;
	}
	
	private String[] split(String string) {
		int i = string.length()-1;
		while(string.charAt(i) != '/') {
			i--;
		}
		String[] array = {string.substring(0, i),string.substring(i+1,string.length())};
		return array;
	}
	
	private void hiloRecEmp(Servidor server) {
		this.hiloRec = new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, Integer> mapaPersistido = gestorNotificacion.recuperarIntentos();
				while (true) {
					try {
						String msj = receptor_empleado.getMensaje();
						if(msj != null) {
							msj = gestorSeguridad.recuperarDNI(msj);
							String[] vector = server.split(msj); //Este split es redundante ahora
							msj = vector[0];
							String puesto = vector[1];
							
							latidosEmpleados.put(puesto, System.currentTimeMillis());
							
							if(msj.equals("Cliente")) {
								System.out.println("CLIENTE SERVIDOR 179");
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(vector[1]));
								System.out.println("EMPLEADO --- Puesto " + vector[1] + " está solicitando el siguiente cliente.");
								//Si se atrasa esto se come al dni
								Object lockDelEmpleado = null;
								try {
								lockDelEmpleado = semaforoEmpleados.get(listaEmpleados.indexOf(vector[1]));
								}
								catch(IndexOutOfBoundsException e) {
									System.out.println(e.getMessage());
									listaEmpleados.add(vector[1]);
			                        semaforoEmpleados.add(new Object());
			                        lockDelEmpleado = semaforoEmpleados.get(listaEmpleados.indexOf(vector[1]));
								}
								finally {
									String dni = null;
									
									synchronized (lockDelEmpleado) {
									    if(mapaPersistido != null && !mapaPersistido.isEmpty()) {
									        System.out.println("EMPLEADO --- Reintentando enviar cliente desde persistencia.");
									        dni = (String) mapaPersistido.keySet().toArray()[0];
									        int intentos = mapaPersistido.get(dni);
									        server.enviarReintento(emisor_empleado, gestorSeguridad.protegerDNI(gestorSeguridad.recuperarDNI(dni)+"/"+intentos), puerto);
									        
									        // ¡Corrección! Usamos los intentos reales, no /3
									        clientesEnAtencion.put(vector[1], dni + "/" + intentos); 
									        
									        // NO lo borramos de persistencia, porque aún está en atención
									        // y eliminamos todas las llamadas a sincronizarArchivoReintentos
									        mapaPersistido.remove(dni); // Lo sacamos del mapa local temporal para que el proximo empleado no tome el mismo
									    }
									    else if (!server.getClientes().isEmpty()) {
									        dni = server.retiraCliente();
									        System.out.println("EMPLEADO --- Asignando DNI " + dni + " al Puesto " + vector[1]);
									        server.enviarReintento(emisor_empleado, dni, puerto); 
									        
									        clientesEnAtencion.put(vector[1], dni + "/3"); 
									        actualizarPersistencia(dni, 3); // Lo agregamos al archivo físico nuevo
									        
									        try {
									            emisor_server_heartbeat.enviar("Eliminar/"+dni, Utils.Server_to_Server2); 
									        }catch(Exception e) {}
									    }
									    else {
									        System.out.println("LISTA VACIA SERVIDOR");
									        server.enviarReintento(emisor_empleado, "LISTA_VACIA", puerto);
									    }
									}
								}
							}
							else if (msj.equals("Estado")) {
							    if (clientesEnAtencion.containsKey(puesto)) {
							        String[] datos = clientesEnAtencion.get(puesto).split("/");
							        eliminarDePersistencia(datos[0]); // Lo borramos del archivo físico porque ya se atendió
							        clientesEnAtencion.remove(puesto);
							        System.out.println("SERVIDOR --- Turno finalizado en Puesto " + puesto + ". DNI eliminado de persistencia.");
							    }
								
							    int index = listaEmpleados.indexOf(puesto);
							    if (index != -1) {
							        String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(puesto));
							        Object lockDelEmpleado = semaforoEmpleados.get(index);
							        
							        synchronized (lockDelEmpleado) {
							            boolean bool;
							            
							            if (server.getClientes().isEmpty() && (mapaPersistido != null && mapaPersistido.isEmpty())) {
							                bool = server.enviarReintento(emisor_empleado, "LISTA_VACIA", puerto);
							            } else {
							                bool = server.enviarReintento(emisor_empleado, "HAY_CLIENTES", puerto);
							            }
							            
							            if(!bool) {
							                try {
							                    emisor_server_heartbeat.enviar("Eliminar empleado/" + puesto, Utils.Server_to_Server2);
							                    System.out.println("HEARTBEAT --- Enviada orden 'Eliminar empleado' al servidor secundario.");
							                } catch(Exception e) {}
							                finally {
								                server.listaEmpleados.remove(index);
								                server.semaforoEmpleados.remove(index);
								                latidosEmpleados.remove(puesto);
								                System.out.println("Empleado " + puesto + " desconectado y eliminado.");
								                //Nunca va a entrar aca, salvo que el empleado se desconecte justo cuando el servidor le esta enviando el estado
							                }
							            }
							        }
							    }
							}
							else if (msj.equals("Desconectar")) {
								System.out.println("DESCONECTAR SERVIDOR 246");
								clientesEnAtencion.remove(puesto);
							    int index = listaEmpleados.indexOf(puesto);
							    
							    if (index != -1) {
							        Object lockDelEmpleado = semaforoEmpleados.get(index);
							        synchronized (lockDelEmpleado) {
							            try {
							                emisor_server_heartbeat.enviar("Eliminar empleado/" + puesto, Utils.Server_to_Server2);
							            } catch (Exception e) {}
							            
							            server.listaEmpleados.remove(index);
							            server.semaforoEmpleados.remove(index);
							            latidosEmpleados.remove(puesto);
							            System.out.println("EMPLEADO --- Puesto " + puesto + " se ha desconectado voluntariamente.");
							            mapaPersistido = gestorNotificacion.recuperarIntentos();
							        }
							    }
							}
							else if (puesto.equals("0")) {
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
							else {
							    System.out.println("PANTALLA --- Enviando DNI " + msj + " (Puesto " + puesto + ") hacia la pantalla central.");
							    String dniPuestoEncriptado = gestorSeguridad.protegerDNI(msj+"/"+puesto);
							    server.enviarReintento(emisor_pantalla, dniPuestoEncriptado, Utils.Server_to_Pantalla); 
							    
							    if (clientesEnAtencion.containsKey(puesto)) {
							        String[] datos = clientesEnAtencion.get(puesto).split("/");
							        if (gestorSeguridad.recuperarDNI(datos[0]).equals(msj)) { 
							            int intentosRestantes = Integer.parseInt(datos[1]) - 1;
							            clientesEnAtencion.put(puesto, datos[0] + "/" + intentosRestantes);
							            
							            // Actualizamos solo este DNI en el archivo físico
							            actualizarPersistencia(datos[0], intentosRestantes); 
							            System.out.println("SERVIDOR --- Intento descontado. Quedan " + intentosRestantes);
							        }
							    }
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
		                    else if(msj.length()>1){
		                    	String[] vector = msj.split("/");
		                    	String orden = vector[0];
		                    	String dni = vector[1]; //Solo util cuando se envia un dni y no cuando se sincroniza
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
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.clientes.addLast(vector[i]);
		                    		} 
		                    	}
		                    	else if(orden.equals("SincronizacionEmp")) {
		                    		for(int i=1;i<vector.length;i++) {
		                    			this.listaEmpleados.add(vector[i]);
		                    			this.semaforoEmpleados.add(new Object());
		                    		}
		                    	}
		                    	this.colaAux.guardarCola(clientes);
		                    	System.out.println("SERVIDOR SECUNDARIO: Cola persistida");
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
	
	public void servidorPpalMuerto() {
		System.out.println("¡¡¡Servidor principal muerto, iniciando servidor secundario!!!");
		try {
			estadoSec = false;
			
			iniciaReceptores();
			this.receptor_server_heartbeat.kill();
			this.hilosPpales();
			this.colaAux = factory.crearColaPersistencia();
			this.clientes = this.colaAux.recuperarCola();
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
		this.iniciarWatchdogEmpleados();
		
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
			this.colaAux = this.factory.crearColaPersistencia("datos/colaAux2");
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
					e1.printStackTrace();
				}
			}
			System.out.println("Falla al intentar enviar desde Servidor el mensaje: "+msj);	
			
		}
			System.out.println("Mensaje no enviado");
			return false;
		
	}
//Iteración 4
	private void iniciarWatchdogEmpleados() {
	    Thread watchdog = new Thread(() -> {
	        while (true) {
	            try {
	                Thread.sleep(5000); // El vigilante revisa cada 5 segundos
	                long ahora = System.currentTimeMillis();
	                
	                // Iteramos sobre una copia para evitar ConcurrentModificationException
	                for (String puesto : new ArrayList<>(listaEmpleados)) {
	                    Long ultimoLatido = latidosEmpleados.get(puesto);
	                    
	                    //En terminos del sistema 45seg es un monton pero realmente no es tanto tiempo
	                    if (ultimoLatido != null && (ahora - ultimoLatido) > Utils.TiempoRellamado*1.5) {
	                        System.out.println("WATCHDOG --- Empleado en puesto " + puesto + " no responde. Desconectando forzosamente...");
	                        
	                        int index = listaEmpleados.indexOf(puesto);
	                        if (index != -1) {
	                            Object lockDelEmpleado = semaforoEmpleados.get(index);
	                            synchronized (lockDelEmpleado) {
	                                // Mismos pasos de limpieza que en el 'Desconectar'
	                                try {
	                                    emisor_server_heartbeat.enviar("Eliminar empleado/" + puesto, Utils.Server_to_Server2);
	                                } catch (Exception e) {}
	                                
	                                clientesEnAtencion.remove(puesto);
	                                listaEmpleados.remove(index);
	                                semaforoEmpleados.remove(index);
	                                latidosEmpleados.remove(puesto);
	                                gestorNotificacion.recuperarIntentos();
	                            }
	                        }
	                    }
	                }
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                break;
	            }
	        }
	    });
	    watchdog.setDaemon(true);
	    watchdog.start();
	}
	
	private void actualizarPersistencia(String dni, int intentos) {
	    Map<String, Integer> mapa = gestorNotificacion.recuperarIntentos();
	    mapa.put(dni, intentos);
	    gestorNotificacion.guardarIntentos(mapa);
	}

	private void eliminarDePersistencia(String dni) {
	    Map<String, Integer> mapa = gestorNotificacion.recuperarIntentos();
	    if (mapa.containsKey(dni)) {
	        mapa.remove(dni);
	        gestorNotificacion.guardarIntentos(mapa);
	    }
	}
	
	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	public boolean existeEmpleado(String emp) {
		return this.listaEmpleados.contains(emp);
		
	}
	
	public String getPuestoMsj(String msj) {
		return this.split(msj)[1];
	}
	
	public String getDniMsj(String msj) {
		return this.split(msj)[0];
	}
}
