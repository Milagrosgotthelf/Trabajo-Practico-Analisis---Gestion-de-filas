package modelo;


import servidor.Emisor;
import servidor.Receptor;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor recetor = new Receptor(Utils.PUERTO_CONFIRMACION);
	
	private static TerminalRegistro instancia = null;
	
	private TerminalRegistro() {
		
	}
	
	public static TerminalRegistro getInstance() {
		if(instancia == null) {
			instancia = new TerminalRegistro();
		}
		return instancia;
	}
	
	public void agregarCliente(String cliente){
		emisor.enviar(cliente, Utils.Registro_to_Server);
		
	}
	 
	public Emisor getEmisor() {
		return emisor;
	}
		
	public Receptor getReceptor() {
		return this.recetor;
	}
		
}
