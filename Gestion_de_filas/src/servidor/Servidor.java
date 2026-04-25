package servidor;

import java.util.ArrayList;
import java.util.LinkedList;

import modelo.Cliente;
import modelo.Utils;
public class Servidor {
	private ArrayList<Receptor> receptoresReg = new ArrayList<Receptor>();
	private ArrayList<Receptor> receptoresEmp = new ArrayList<Receptor>();
	
	private Receptor receptor_registro = new Receptor(Utils.Registro_to_Server); //
	private Receptor receptor_empleado = new Receptor(Utils.Empleado_to_Server);
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Emisor emisor_registro = new Emisor();
	
	private Object lock = new Object();
	private LinkedList<String> clientes= new LinkedList<String>();
	private Thread hiloRec;
	private ArrayList<String> listaEmpleados = new ArrayList<String>();
	
	
	public Servidor() {
		System.out.println("Servidor iniciado");
		this.hiloRecEmp(this);
		this.hiloReg(this);
	
	}

	
	public void agregarCliente(String cliente) {
		clientes.addLast(cliente);
	}
	
	public void agregarReceptoresReg(int id) {
		this.receptoresReg.add(new Receptor(Integer.toString(Integer.parseInt(Utils.Registro_to_Server))));
	}
	
	public void agregarReceptoresEmp(int id) {
		this.receptoresEmp.add(new Receptor(Integer.toString(Integer.parseInt(Utils.Empleado_to_Server))));
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
	                        
	                        if (!server.existeCliente(msj)) {
	                            System.out.println("Servidor: Registrando nuevo cliente: " + msj);
	                            server.clientes.addLast(msj);
	                            emisor_registro.enviar("OK", Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	                            //con esto avisamos que hay un nuevo cliente (asi actualiza cuando vuelve a haber clientes dsp de estar vacia)
	                            synchronized (lock) {
	                                lock.notifyAll(); 
	                            }
	                            
	                        }
	                        else {
	                            System.out.println("Servidor: DNI REPETIDO detectado: " + msj);
	                            emisor_registro.enviar("REPETIDO", Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	                        }
	                        
	                        msjAnterior = msj;
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
						System.out.println("Servidor RecEmp " + msj);
						if(msj != null)
							if(msj.startsWith("----")) {
								synchronized (lock) {
		                            while (server.clientes.isEmpty()) {
		                                lock.wait(); // Espera a que hiloReg haga notifyAll()
		                            }
		                        }
								String puerto = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + Integer.parseInt(getPuestoMsj(msj)));
								
								emisor_empleado.enviar(server.getClientes().removeFirst(), puerto);
							}
							else if (msj.length() < 7 && !server.existeEmpleado(msj)) {
	                        	System.out.println("Servidor msj = " + msj);
	                        	listaEmpleados.add(msj);
	                        	
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
	
}
