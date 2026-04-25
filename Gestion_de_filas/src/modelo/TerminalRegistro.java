package modelo;


import servidor.Emisor;
import servidor.Receptor;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor receptor = null;
	private int numTerminal;
	
	private static TerminalRegistro instancia = null;
	
	public TerminalRegistro(int id) {
		this.numTerminal = id;
		this.receptor =  new Receptor(Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
	}
	
	public boolean agregarCliente(String cliente){
		emisor.enviar(cliente, Integer.toString(Integer.parseInt(Utils.Registro_to_Server)));
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
