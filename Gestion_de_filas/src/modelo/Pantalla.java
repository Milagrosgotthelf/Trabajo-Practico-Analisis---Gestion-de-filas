package modelo;

import java.util.LinkedList;

public class Pantalla  {
	
	private static Pantalla instancia = null;
	private Receptor receptor = null;
	private LinkedList<String> clientes = null;
	
	private Pantalla() {
		this.clientes = new LinkedList<String>();
		this.receptor = new Receptor(Utils.PUERTO_PANTALLA);
		this.receptor.recibir();
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
	    String mensajeRecibido = this.receptor.getMensaje();
	    
	    if (mensajeRecibido != null) {
	        // VALIDACIÓN: Si el DNI nuevo es igual al anterior, no lo agregamos de nuevo
	        if (!this.clientes.isEmpty() && mensajeRecibido.equals(this.clientes.getFirst())) {
	            return;
	        }

	        this.clientes.addFirst(mensajeRecibido);
	        
	        // Mantener siempre 5 elementos
	        if (this.clientes.size() > 5) {
	            this.clientes.removeLast();
	        }
	    }
	}

	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	
	
	

}
