package modelo;

import static modelo.Utils.*;
public class Empleado {
	private Receptor receptor = null;
	private Emisor emisor = null;
	private String dniActual = null;
	
	public Empleado() {
		this.receptor = new Receptor(PUERTO);
		this.emisor = new Emisor();
		this.receptor.recibir();
		//this.escucharTerminal();
		
		
	}
	
	public void llamarCliente() {
		this.dniActual = this.receptor.getMensaje();
		if (dniActual != null)
			this.emisor.enviar(this.dniActual, PUERTO_PANTALLA);
		
	}
	
	
	//Hay que ver lo de rellamar pero no recuerdo si se hacia en esta iteracion
	
	public String getDniActual() {
		return dniActual;
	}
	
	
}