package llamado;


import java.net.BindException;
import java.net.ConnectException;

import sfd.Emisor;
import sfd.GestorSeguridad;
import sfd.Receptor;
import sfd.Utils;
public class Empleado {
	private String dniActual = null;
	private int numeroDePuesto;
	private String puertoEmisor = Utils.Empleado_to_Server;
	private String puertoReceptor;
	private Emisor emisor_server = new Emisor();
	private Receptor receptor_server = null;
	private GestorSeguridad gestorSeguridad = new GestorSeguridad();

	
	public Empleado() {
	}
	
	public String llamarCliente() throws ConnectException {
		System.out.println("Solicitando cliente para el puesto " + this.numeroDePuesto);
		this.emisor_server.enviar("Cliente/"+this.numeroDePuesto, this.puertoEmisor);
		String msjEncriptado  = this.receptor_server.getMensaje(); //msj es el DNI encriptado enviado por el servidor
		return this.gestorSeguridad.recuperarDNI(msjEncriptado); // de esta manera siempre que lo usemos dentro del controladorEmpleado ya sera el DNI real
	} 
	
	public String pedirEstado() throws ConnectException {
		System.out.println("Solicitando estado del puesto " + this.numeroDePuesto);
		this.emisor_server.enviar("Estado/"+this.numeroDePuesto, this.puertoEmisor);
		return this.receptor_server.getMensaje();
	} 
	
	
	public void enviarCliente_Server(String msj) throws ConnectException {
		if (msj != null) {
			//a este punto llega el dni desencriptado porque de essa manera lo trata el controladorEmpleado pero al enviarselo al Servidor hay que encriptarlo no solo porque 
			//el servidor tiene la cola con los dnis encriptados sino porque hay que protegerlo en la comunicacion por sockets
			String msjEncriptado = gestorSeguridad.protegerDNI(msj);
			this.emisor_server.enviar(msjEncriptado + "/" + this.numeroDePuesto, this.puertoEmisor);
		}
			
	}
	
	public void setNumeroDePuesto(int numeroDePuesto) throws BindException{
		this.puertoEmisor = Utils.Empleado_to_Server;
		this.numeroDePuesto = numeroDePuesto;
		this.puertoReceptor = Integer.toString(Integer.parseInt(Utils.Server_to_Empleado_base) + this.numeroDePuesto);
		this.receptor_server = new Receptor(this.puertoReceptor);
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