package modelo;

import static modelo.Utils.*;
public class Empleado {
	private Receptor receptor = null;
	private Emisor emisor = null;
	private String dniActual = null;
	
	public Empleado() {
		this.receptor = new Receptor(PUERTO);
		this.emisor = new Emisor();
		this.escucharTerminal();
		
		
	}
	
	public void llamarCliente() {
		this.dniActual = this.receptor.getMensaje();
		System.out.println("Empleado 20 " + this.dniActual);
		//Hasta aca
		this.emisor.enviar(this.dniActual, PUERTO_PANTALLA);
		
	}
	
	
	//Hay que ver lo de rellamar pero no recuerdo si se hacia en esta iteracion
	public void escucharTerminal() {
		receptor.recibir();
	}
	public Receptor getReceptor() {
		return receptor;
	}
 
	public Emisor getEmisor() {
		return emisor;
	}

	public String getDniActual() {
		return dniActual;
	}
	
	
}