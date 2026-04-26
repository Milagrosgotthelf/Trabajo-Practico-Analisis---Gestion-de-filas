package modelo;


import servidor.Emisor;
import servidor.Receptor;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor receptor = null;
	private int numTerminal;
	
	
	public TerminalRegistro(int id) {
		solicitarNumero();
	}
	
	public void solicitarNumero() {
		
		this.receptor =  new Receptor(Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
		
		emisor.enviar("TerminalActiva", Integer.toString(Integer.parseInt(Utils.Registro_to_Server)));
		
		
		String respuesta = receptor.getMensaje();
		this.numTerminal = Integer.parseInt(respuesta);
		this.receptor.kill();
		this.receptor =  new Receptor(Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION) + this.numTerminal));
	

	}
	public boolean agregarCliente(String cliente){
		emisor.enviar(cliente+"/"+Integer.toString(this.numTerminal), Integer.toString(Integer.parseInt(Utils.Registro_to_Server)));
		
		String respuesta = receptor.getMensaje();
		
		return "OK".equals(respuesta);		
	}
	 
	public Emisor getEmisor() {
		return emisor;
	}
		
	public Receptor getReceptor() {
		return this.receptor;
	}
		
}
