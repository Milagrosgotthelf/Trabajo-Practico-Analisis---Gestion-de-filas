package modelo;


import servidor.Emisor;
import servidor.Receptor;
public class Empleado {
	private String dniActual = null;
	
	
	private Emisor emisor_server = new Emisor();
	private Receptor receptor_server = new Receptor(Utils.Server_to_Empleado_base);
	
	public Empleado() {
	}
	
	
	public void enviarCliente_Server(String msj) {
		System.out.println("enviarCliente_server");
		if (msj != null) {
			System.out.println("Empleado 19: " + msj);
			this.emisor_server.enviar(msj, Utils.Empleado_to_Server);
		}
			
	}
	public String getDniActual() {
		return dniActual;
	}

	public Emisor getEmisor_server() {
		return emisor_server;
	}

	public Receptor getReceptor_server() {
		return receptor_server;
	}

	public Receptor getReceptor() {
		return receptor_server;
	}
	
	
	
	
}