package llamado;


import java.net.BindException;
import java.net.ConnectException;

import sfd.Emisor;
import sfd.Receptor;
import sfd.Utils;
public class Empleado {
	private String dniActual = null;
	private int numeroDePuesto;
	private String puertoEmisor = Utils.Empleado_to_Server;
	private String puertoReceptor;
	private Emisor emisor_server = new Emisor();
	private Receptor receptor_server = null;
	private boolean configServer = true;
	
	
	public Empleado() {
	}
	
	public String llamarCliente() throws ConnectException {
		this.emisor_server.enviar("----/"+this.numeroDePuesto, this.puertoEmisor);
		return this.receptor_server.getMensaje();
	} 
	
	
	public void enviarCliente_Server(String msj) throws ConnectException {
		if (msj != null) {
			this.emisor_server.enviar(msj + "/" + this.numeroDePuesto, this.puertoEmisor);
		}
			
	}
	
	public void setNumeroDePuesto(int numeroDePuesto) throws BindException{
		this.puertoEmisor = Utils.Empleado_to_Server;
		this.numeroDePuesto = numeroDePuesto;
		this.puertoReceptor = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + this.numeroDePuesto);
		this.receptor_server = new Receptor(this.puertoReceptor);
	}
	
	public void cambiarConexion() {
		System.out.println("Cambio de conexion empleado");
		this.configServer = !this.configServer; 
		if(this.configServer)
			this.puertoEmisor = Utils.Empleado_to_Server;
		else 
			this.puertoEmisor = Utils.Empleado_to_Server2;
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
	
	public int getNumeroDePuesto() {
		return numeroDePuesto;
	}
	
	
	
}