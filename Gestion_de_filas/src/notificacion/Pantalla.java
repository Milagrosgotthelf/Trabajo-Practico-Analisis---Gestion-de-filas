package notificacion;

import java.net.BindException;
import java.util.LinkedList;

import sfd.Receptor;
import sfd.Utils;

public class Pantalla  {
	
	private static Pantalla instancia = null;
	private Receptor receptor = null;
	private LinkedList<String> clientes = null;
	
	private Pantalla() throws BindException {
		this.clientes = new LinkedList<String>();
		this.receptor = new Receptor(Utils.Server_to_Pantalla);
		//se inicializa con 5 lugares vacios
		for (int i=0; i< 5; i++) {
			clientes.add("-");
		}
		
		
	}
	
	public static Pantalla getInstance() {
		if (instancia == null) {
			try {
				instancia = new Pantalla();
			} catch (BindException e) {
				e.printStackTrace();
			}
		}
		return instancia;
		
	}
	
	public void escucharEmpleado() {
		
	    String mensajeRecibido = this.receptor.getMensaje();
	    if (mensajeRecibido != null) {
	        if (!this.clientes.isEmpty() && mensajeRecibido.equals(this.clientes.getFirst())) {
	            return;
	        }
	        this.clientes.remove(mensajeRecibido);
	        this.clientes.addFirst(mensajeRecibido);
	        
	        if (this.clientes.size() > 5) {
	            this.clientes.removeLast();
	        }
	    }
	}

	public LinkedList<String> getClientes() {
		return clientes;
	}
	
	
	
	

}
