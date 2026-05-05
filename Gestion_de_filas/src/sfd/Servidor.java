package sfd;

import java.net.BindException;
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
	
	 
	public Servidor() {
		System.out.println("Servidor iniciado");
		try {
			iniciaReceptores();
			this.emisor_server_heartbeat = new Emisor();
			this.hiloRecEmp(this);
			this.hiloReg(this);
			this.hiloEstadoCola(this);
			this.hiloHeartbeat();
		} catch (BindException e) {
			System.out.println("Iniciando servidor secundario");
			inicioSecundario();
		}
		
		
	
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
		catch (BindException e) {
			System.out.println("Puerto en uso, no se pudo iniciar el servidor: " + e.getMessage());
			System.exit(1);
		}
		 catch (Exception e) {
			 System.out.println("Excepcion al iniciar los receptores: " + e.getMessage());
			 System.exit(1);
		 }
	}
	
	public void agregarCliente(String cliente) {
		clientes.addLast(cliente);
	}
	
	private void hiloReg(Servidor server) {
	    Thread hilo = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            String msjAnterior = "";
	            while (true) {
	                try {
	                    String msj = receptor_registro.getMensaje(); 
	                    if (msj != null) {
	                        if(!msj.equals("TerminalActiva")) {
	                        	int puesto = Integer.parseInt(getPuestoMsj(msj));
	                        	msj = getDniMsj(msj);
		                        if (!server.existeCliente(msj)) {
		                            server.clientes.addLast(msj);
		                            
		                            String puerto = Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION) + puesto);
		                            
		                            emisor_registro.enviar("OK", puerto);
		                            synchronized (lock) {
		                                lock.notifyAll(); 
		                            }
		                            
		                        }
		                        else
		                            emisor_registro.enviar("REPETIDO", Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)+ puesto));
	                        }
	                        else {
	                        	//System.out.println("ELSE TERMINAL ACTIVA");
	                        	emisor_registro.enviar(Integer.toString(server.contadorReg), Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	                        	server.contadorReg = server.contadorReg + 1;
	                        }
	                        msjAnterior = msj;
	                    }
	                    
	                } catch (Exception e) {
	                    //System.out.println("Excepcion en hilo receptor del registro " + e.getMessage());
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
						if(msj != null)
							if(msj.startsWith("----")) {
								
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(getPuestoMsj(msj)));
								if (msj.startsWith("----") && !server.getClientes().isEmpty()) {
								    emisor_empleado.enviar(server.getClientes().removeFirst(), puerto);
								}

							}
							else if (msj.length() < 7 && !server.existeEmpleado(msj)) {
								
	                        	listaEmpleados.add(getDniMsj(msj));
	                        	
	                        }
							else {
								emisor_pantalla.enviar(msj, Utils.Server_to_Pantalla); 
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
							//System.out.println(puerto);
							synchronized (lock2) {
							    if (server.clientes.isEmpty())
							    	emisor_empleado.enviar("LISTA_VACIA", puerto);
							    else
							    	emisor_empleado.enviar("HAY_CLIENTES", puerto);
								lock2.wait(300);
								//O:Cuando quiere enviar un mensaje pero no hay nadie que lo escuche sale excepcion
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
	                	System.out.println("HILOHEARTHBEAT SECUNDARIO");
	                    String msj = receptor_server_heartbeat.getHeartbeat();
	                    
	                    if ("HEARTBEAT".equals(msj)) {
	                        servidorPpalVivo();
	                    } else {
	                        servidorPpalMuerto();
	                    }
	                } else {
	                    Thread.sleep(2000); 
	                    emisor_server_heartbeat.enviar("HEARTBEAT", Utils.Server_to_Server2);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	    hiloHeartbeat.start();
	}
	
	/*
	 * public void hiloRecHeartbeat() {
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						
						String msj = receptor_server_heartbeat.getHeartbeat();
						if (msj != null && msj.equals("HEARTBEAT")) {
							servidorPpalVivo();
						} else {
							servidorPpalMuerto();
						}
						msj = null;
					} catch (Exception e) {
						System.out.println("Excepcion en hilo receptor del heartbeat: " + e.getMessage());
					}
				}
			}
		});
		hilo.start();
	}
	
	 */
	
	public void servidorPpalVivo() {
		System.out.println("Servidor principal vivo");
	}
	
	public void servidorPpalMuerto() {
		System.out.println("Servidor principal muerto, iniciando servidor secundario");
	}
}	