package modelo;

import java.util.LinkedList;
import static modelo.Utils.PUERTO;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	
	LinkedList<Cliente> clientes=null;
	private static TerminalRegistro instancia = null;
	
	private TerminalRegistro() {
		 clientes = new LinkedList<Cliente>();
		 //Usualmente se usa este tipo de lista y no ArrayList
		 //Es una lista enlazada que tiene metodos de colas
		
	}
	
	public static TerminalRegistro getInstance() {
		if(instancia == null) {
			instancia = new TerminalRegistro();
		}
		return instancia;
	}
	
	public void agregarCliente(Cliente cliente) {
		clientes.addLast(cliente);
	}
	
	public String enviarCliente() {
	    if (clientes.isEmpty()) {
	        System.out.println("No hay clientes en la cola para enviar.");
	        return null;
	    } else {
	        // No removemos todavía
	        return clientes.getFirst().getDni();
	    }
	}

	// Nuevo método para remover el cliente cuando inicia turno
	public void removerClienteActual() {
	    if (!clientes.isEmpty()) {
	        clientes.removeFirst();
	    }
	}
 
	public Emisor getEmisor() {
		return emisor;
	}
	
	public LinkedList<Cliente> getClientes() {
		return clientes;
	}
	
	
		
}
