package modelo;

import java.util.LinkedList;

public class Pantalla  {
	
	private static Pantalla instancia = null;
	private Receptor receptor = null;
	private LinkedList<String> clientes = null;
	
	private Pantalla() {
		this.clientes = new LinkedList<String>();
		this.receptor = new Receptor(Utils.PUERTO_PANTALLA);
		//se inicializa con 5 lugares vacios
		for (int i=0; i< 5; i++) {
			clientes.add("-");
		}
	}
	
	public static Pantalla getInstance() {
		if (instancia == null) {
			instancia = new Pantalla();
		}
		return instancia;
		
	}
	
	public void escucharEmpleado() {
		receptor.recibir();
		this.clientes.addFirst(this.receptor.getMensaje());	
		this.clientes.removeLast();
		
	}

	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	public void rutinaTest() {
		this.clientes.addFirst(this.receptor.getMensaje());	
		this.clientes.removeLast();		
		
	}
	
	
	
	

}
