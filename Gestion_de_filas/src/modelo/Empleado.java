package modelo;

import static modelo.Utils.*;
public class Empleado {
	private Receptor receptor = null;
	private Emisor emisor_pantalla = null;
	private Emisor emisor_terminal = null;
	private String dniActual = null;
	
	public Empleado() {
		this.receptor = new Receptor(PUERTO);
		this.emisor_pantalla = new Emisor();
		this.emisor_terminal = new Emisor();
		this.receptor.recibir();
		//this.escucharTerminal();
	}
	
	public void llamarCliente() {
		this.dniActual = this.recibirMensaje();
		if (dniActual != null)
			this.emisor_pantalla.enviar(this.dniActual, PUERTO_PANTALLA);
	}
	
	
	public String recibirMensaje() {
		this.emisor_terminal.enviar("----", Utils.PUERTO_CONFIRMACION);
		System.out.println("Empleado 27");
		this.receptor.recibir();
		return this.receptor.getMensaje();
	}
	
	public String getDniActual() {
		return dniActual;
	}

	public Emisor getEmisor_pantalla() {
		return emisor_pantalla;
	}

	public Emisor getEmisor_terminal() {
		return emisor_terminal;
	}

	public Receptor getReceptor() {
		return receptor;
	}
	
	
	
	
}