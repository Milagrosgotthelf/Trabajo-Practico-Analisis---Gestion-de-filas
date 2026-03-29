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
		clientes.add(cliente);
	}
	
	public void enviarCliente() {
		if (clientes.isEmpty()) {
            System.out.println("No hay clientes en la cola para enviar.");
        }
		else {
			emisor.enviar(this.clientes.pop().getDni(), PUERTO);
		}
    }
	
	public void rutinaTest() {
		this.agregarCliente(new Cliente("44635069"));
		this.enviarCliente();
		System.out.println("Cliente enviado");
	}
	
 
	public Emisor getEmisor() {
		return emisor;
	}
	
	public LinkedList<Cliente> getClientes() {
		return clientes;
	}
	
	
		
}
