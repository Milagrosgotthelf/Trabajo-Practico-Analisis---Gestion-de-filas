package servidor;

import java.util.LinkedList;

import modelo.Cliente;
import modelo.Utils;
public class Servidor {
	private Receptor receptor_registro = new Receptor(Utils.Registro_to_Server); //
	private Receptor receptor_empleado = new Receptor(Utils.Empleado_to_Server);
	
	private Emisor emisor_empleado = new Emisor();
	private Emisor emisor_pantalla = new Emisor();
	private Object lock = new Object();
	
	private LinkedList<Cliente> clientes= new LinkedList<Cliente>();
	private boolean clienteEnviado = false;
	public Servidor() {
		System.out.println("Servidor iniciado");
		this.hiloReg(this);
		this.hiloRecEmp();
		this.hiloEmiEmp(this);
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
						
						
						if(msj != null && msj != msjAnterior) {
							System.out.println("Servidor 41 recibe de registro: " + msj);
							server.clientes.addLast(new Cliente(msj));
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
	
	private void hiloRecEmp() {
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						
						String msj = receptor_empleado.getMensaje(); 
						System.out.println("Servidor 51 " + msj);
						emisor_pantalla.enviar(msj, Utils.Server_to_Pantalla);
						synchronized (lock) {
	                        clienteEnviado = false;
	                        lock.notifyAll();  // Notify hiloEmiEmp to send next client
	                    }
						
					} catch (Exception e) {
						System.out.println("Excepcion en hilo receptor del empleado" + e.getMessage());
					}
					
				}
				
			}
		});
		hilo.setDaemon(true); 
		hilo.start();
	}
	
	private void hiloEmiEmp(Servidor server) {
		Thread hilo = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						synchronized (lock) {
							while (clienteEnviado || server.clientes.isEmpty()) {
								lock.wait();
							}
							emisor_empleado.enviar(server.clientes.removeFirst().getDni(), Utils.Server_to_Empleado_base);
							clienteEnviado = true;
						}
					} catch (Exception e) {
						System.out.println("Excepcion en hilo emisor del empleado: " + e.getMessage());
					}
				}
			}
		});
		hilo.setDaemon(true);
		hilo.start();
	}
}
