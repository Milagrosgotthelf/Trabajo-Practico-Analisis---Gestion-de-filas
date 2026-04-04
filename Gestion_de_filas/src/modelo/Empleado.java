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
	}
	
	
	public String llamarCliente() {
		this.emisor_terminal.enviar("----", Utils.PUERTO_CONFIRMACION);
		return this.receptor.getMensaje();
	}
	
	public void enviarCliente_pantalla(String msj) {
		if (msj != null)
			this.emisor_pantalla.enviar(msj, PUERTO_PANTALLA);
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