package modelo;


import servidor.Emisor;
import servidor.Receptor;
public class Empleado {
	private String dniActual = null;
	
	
	private Emisor emisor_server = new Emisor();
	private Receptor receptor_server = new Receptor(Utils.Server_to_Empleado_base);
	
	public Empleado() {
	}
	
	public String llamarCliente() {
		this.emisor_server.enviar("----", Utils.Empleado_to_Server);
		return this.receptor_server.getMensaje();
	} 
	
	
	public void enviarCliente_Server(String msj) {
		if (msj != null) {
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