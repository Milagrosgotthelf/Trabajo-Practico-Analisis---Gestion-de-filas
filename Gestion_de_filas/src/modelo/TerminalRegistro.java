package modelo;

import java.util.LinkedList;
import static modelo.Utils.*;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor recetor = new Receptor(Utils.PUERTO_CONFIRMACION);
	
	LinkedList<Cliente> clientes=null;
	private static TerminalRegistro instancia = null;
	
	private TerminalRegistro() {
		 clientes = new LinkedList<Cliente>();
		
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
	
	public void enviarCliente() throws InterruptedException {
	    if (!clientes.isEmpty()) {
	        System.out.println("TerminalRegistro 32 - Primer dni de la cola de clientes:" + clientes.getFirst().getDni());
	        emisor.enviar(this.clientes.getFirst().getDni(), PUERTO);
	        this.clientes.removeFirst();
	    }
	    else {
	    	System.out.println("TerminalRegistro 36: Sistema vacio");
	    	throw new InterruptedException("No hay clientes en la cola");
	    	
	    }
	}
 
	public Emisor getEmisor() {
		return emisor;
	}
	
	public LinkedList<Cliente> getClientes() {
		return clientes;
	}
	
	public Receptor getReceptor() {
		return this.recetor;
	}
		
}
