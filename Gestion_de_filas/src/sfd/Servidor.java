package sfd;

import java.net.BindException;
import java.util.ArrayList;
import java.util.LinkedList;
import sfd.Utils;
public class Servidor {
	
	private Receptor receptor_registro; //
	private Receptor receptor_empleado;
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Emisor emisor_registro = new Emisor();
	
	private Object lock = new Object();
	private Object lock2 = new Object();
	private LinkedList<String> clientes= new LinkedList<String>();
	
	private ArrayList<String> listaEmpleados = new ArrayList<String>();
	private ArrayList<Integer> listaEstadosEmpleado = new ArrayList<Integer>();
	private int contadorReg = 1;
	private Thread hiloRec, hiloEstadoCol;
	
	public Servidor() {
		System.out.println("Servidor iniciado");
		try {
			iniciaReptores();
		} catch (BindException e) {
			e.printStackTrace();
		}
		this.hiloRecEmp(this);
		this.hiloReg(this);
		this.hiloEstadoCola(this);
	
	}

	public void iniciaReptores() throws BindException {
		this.receptor_registro = new Receptor(Utils.Registro_to_Server);
		this.receptor_empleado = new Receptor(Utils.Empleado_to_Server);
		
	}
	
	public void agregarCliente(String cliente) {
		clientes.addLast(cliente);
	}
	
	
	/*
	 * Crea un hilo para la comunicación registro-empleado
	 */
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
		                            //con esto avisamos que hay un nuevo cliente (asi actualiza cuando vuelve a haber clientes dsp de estar vacia)
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
					int index;
					try {
						String msj = receptor_empleado.getMensaje(); 
						
						if(msj != null) {
							index = listaEmpleados.indexOf(getPuestoMsj(msj));
							if(msj.startsWith("----")) {
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(getPuestoMsj(msj)));
								System.out.println("Recibido mensaje de empleado: " + msj + " puerto: " + puerto);
								if (msj.startsWith("----") && !server.getClientes().isEmpty()) {
									System.out.println("Enviando cliente al empleado: " + server.getClientes().getFirst() + " puerto: " + puerto);
									listaEstadosEmpleado.set(index, 1);
									System.out.println("LISTA: " + listaEstadosEmpleado.toString());
								    emisor_empleado.enviar(server.getClientes().removeFirst(), puerto);
								}
							}
							else if (msj.length() < 7 && !server.existeEmpleado(msj)) {
	                        	listaEmpleados.add(getDniMsj(msj));
	                        	listaEstadosEmpleado.add(0);
	                        }
							else {
								emisor_pantalla.enviar(msj, Utils.Server_to_Pantalla); 
								listaEstadosEmpleado.set(index, 0);
							}
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
							String msjAnterior = null;
							synchronized (lock2) {
								if(listaEstadosEmpleado.get(i) == 0)
								    if (server.clientes.isEmpty() && (msjAnterior == null || !msjAnterior.equals(Utils.FILA_VACIA))) {
								    	emisor_empleado.enviar(Utils.FILA_VACIA, puerto);
								    	msjAnterior = Utils.FILA_VACIA;
								    }
								    else if ((msjAnterior == null || !msjAnterior.equals(Utils.HAY_CLIENTES))) {
								    	emisor_empleado.enviar(Utils.HAY_CLIENTES, puerto);
								    	msjAnterior = Utils.HAY_CLIENTES;
								    }
								lock2.wait(30);
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
	
}
