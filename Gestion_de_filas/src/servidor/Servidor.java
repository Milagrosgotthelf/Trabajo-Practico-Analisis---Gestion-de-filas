package servidor;

import java.util.LinkedList;

import modelo.Cliente;
import modelo.Utils;
public class Servidor {
	private Receptor receptor_registro = new Receptor(Utils.Registro_to_Server); //
	private Receptor receptor_empleado = new Receptor(Utils.Empleado_to_Server);
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Emisor emisor_registro = new Emisor();
	
	private Object lock = new Object();
	
	private LinkedList<Cliente> clientes= new LinkedList<Cliente>();
	
	private Thread hiloRec;
	public Servidor() {
		System.out.println("Servidor iniciado");
		this.hiloReg(this);
		this.hiloRecEmp(this);
	}

	
	public void agregarCliente(Cliente cliente) {
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
	                        
	                        if (!server.existeCliente(msj)) {
	                            System.out.println("Servidor: Registrando nuevo cliente: " + msj);
	                            server.clientes.addLast(new Cliente(msj));
	                            emisor_registro.enviar("OK", Utils.PUERTO_CONFIRMACION);
	                            //con esto avisamos que hay un nuevo cliente (asi actualiza cuando vuelve a haber clientes dsp de estar vacia)
	                            synchronized (lock) {
	                                lock.notifyAll(); 
	                            }
	                            
	                        }
	                        else {
	                            System.out.println("Servidor: DNI REPETIDO detectado: " + msj);
	                            emisor_registro.enviar("REPETIDO", Utils.PUERTO_CONFIRMACION);
	                        }
	                        
	                        msjAnterior = msj;
	                    }
	                    
	                } catch (Exception e) {
	                    System.out.println("Excepcion en hilo receptor del registro " + e.getMessage());
	                }
	            }
	        }
	    });
	    hilo.setDaemon(true); 
	    hilo.start();
	}
	
	protected boolean existeCliente(String msj) {
		for (Cliente c : clientes) {
	        if (c.getDni().equals(msj)) {
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
							if("----".equals(msj)) {
								synchronized (lock) {
		                            while (server.getClientes().isEmpty()) {
		                                lock.wait(); // Espera a que hiloReg haga notifyAll()
		                            }
		                        }
								
								System.out.println("Servidor 71 if ---- =" + msj);
								emisor_empleado.enviar(server.getClientes().removeFirst().getDni(), Utils.Server_to_Empleado_base);
							}
							else {
								System.out.println("Sevidor if null != " + msj);
								emisor_pantalla.enviar(msj, Utils.Server_to_Pantalla);
								/*synchronized (lock) {
			                        lock.notifyAll();  
								}*/
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
		this.hiloRec.setDaemon(true); 
		this.hiloRec.start();
	}


	public LinkedList<Cliente> getClientes() {
		return clientes;
	}
	
}
