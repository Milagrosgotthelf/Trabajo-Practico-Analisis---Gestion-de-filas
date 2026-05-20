package notificacion;

import java.net.BindException;
import java.util.LinkedList;

import sfd.GestorSeguridad;
import sfd.Receptor;
import sfd.Utils;

public class Pantalla  {
	
	private static Pantalla instancia = null;
	private Receptor receptor = null;
	private LinkedList<String> clientes = null;
	private GestorSeguridad gestorSeguridad = new GestorSeguridad();

	
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
	    	String[] partes = mensajeRecibido.split("/");
            String dniEncriptado = partes[0]; 
            //asi le enviamos el dni desencriptado a la ventana_pantalla pero todavia teniendo el nropuesto en el msj
            // no poner esta logica en ventana_pantalla porque no me parece correcto que la ventana tenga al gestor de seguridad
            String mensajeRecibidoDesencriptado = gestorSeguridad.recuperarDNI(dniEncriptado)+"/"+partes[1];
            
	        if (!this.clientes.isEmpty() && mensajeRecibidoDesencriptado.equals(this.clientes.getFirst())) {
	            return;
	        }
	        this.clientes.remove(mensajeRecibidoDesencriptado);
	        this.clientes.addFirst(mensajeRecibidoDesencriptado);
	        
	        if (this.clientes.size() > 5) {
	            this.clientes.removeLast();
	        }
	    }
	}

	public LinkedList<String> getClientes() {
		return clientes;
	}
	public void cerrarPantalla() {
	    if (this.receptor != null) {
	        this.receptor.kill(); // Cierra el ServerSocket del receptor
	    }
	    instancia = null; // Seteamos a null para que la próxima vez cree una nueva
	}
	
	
	

}
