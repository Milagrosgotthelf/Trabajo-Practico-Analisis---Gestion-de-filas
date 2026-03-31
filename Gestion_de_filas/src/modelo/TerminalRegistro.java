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
		System.out.println(cliente.getDni());
		clientes.addLast(cliente);
	}
	
	public void enviarCliente() {
	    if (clientes.isEmpty()) {
	        System.out.println("No hay clientes en la cola para enviar.");
	    } else {
	        // No removemos todavía
	        System.out.println("TerminalRegistro 36 - Primer dni de la cola de clientes:" + clientes.getFirst().getDni());
	        emisor.enviar(this.clientes.getFirst().getDni(), PUERTO);
	        //O: Ahora le envia el primero como corresponde pero no lo elimina
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
