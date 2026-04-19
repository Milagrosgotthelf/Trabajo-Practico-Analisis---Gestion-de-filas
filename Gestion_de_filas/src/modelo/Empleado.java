package modelo;


import servidor.Emisor;
import servidor.Receptor;
public class Empleado {
	private String dniActual = null;
	private int numeroDePuesto;
	private String puertoReceptor;
	private String puertoEmisor;
	private Emisor emisor_server = new Emisor();
	private Receptor receptor_server = null;
	
	
	public Empleado(int id) {
		this.numeroDePuesto = id;
		this.puertoReceptor = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + this.numeroDePuesto);
		this.puertoEmisor = Integer.toString(Integer.parseInt(Utils.Empleado_to_Server) + this.numeroDePuesto);
		this.receptor_server = new Receptor(this.puertoReceptor);
	}
	
	public String llamarCliente() {
		this.emisor_server.enviar("----", this.puertoEmisor);
		return this.receptor_server.getMensaje();
	} 
	
	
	public void enviarCliente_Server(String msj) {
		if (msj != null) {
			this.emisor_server.enviar(msj, this.puertoEmisor);
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