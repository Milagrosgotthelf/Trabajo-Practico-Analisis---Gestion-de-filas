package modelo;


import servidor.Emisor;
import servidor.Receptor;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor receptor = new Receptor(Utils.PUERTO_CONFIRMACION);
	
	private static TerminalRegistro instancia = null;
	
	private TerminalRegistro() {
		
	}
	
	public static TerminalRegistro getInstance() {
		if(instancia == null) {
			instancia = new TerminalRegistro();
		}
		return instancia;
	}
	
	public boolean agregarCliente(String cliente){
		emisor.enviar(cliente, Utils.Registro_to_Server);
		System.out.println("Terminal: Esperando respuesta del servidor...");
		
		String respuesta = receptor.getMensaje();
		System.out.println("Terminal: Respuesta recibida -> " + respuesta);
		
		return "OK".equals(respuesta);		
	}
	 
	public Emisor getEmisor() {
		return emisor;
	}
		
	public Receptor getReceptor() {
		return this.receptor;
	}
		
}
