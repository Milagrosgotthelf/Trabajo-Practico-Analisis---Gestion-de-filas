package modelo;

import java.util.LinkedList;

public class Pantalla  {
	
	private static Pantalla instancia = null;
	private Receptor receptor = null;
	private LinkedList<String> clientes = null;
	
	private Pantalla() {
		this.clientes = new LinkedList<String>();
		
	}
	
	public static Pantalla getInstance() {
		if (instancia == null) {
			instancia = new Pantalla();
		}
		return instancia;
		
	}
	
	public void escucharEmpleado() {
		if (receptor == null) 
			this.receptor = new Receptor(Utils.PUERTO_PANTALLA);
		receptor.recibir();
		
	}

	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	public void rutinaTest() {
		this.clientes.add(this.receptor.getMensaje());
		
		System.out.println("Lista clientes: "+ this.clientes);
		
	}
	
	
	
	

}
