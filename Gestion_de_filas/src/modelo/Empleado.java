package modelo;

import static modelo.Utils.*;
public class Empleado {
	private Receptor receptor = null;
	private Emisor emisor = null;
	
	public Empleado() {
		this.escucharTerminal();
		
	}
	
	public void llamarCliente() {
		//Aca deberia extraer al cliente de la informacion enviada por la terminal y enviarlo a la pantalla
		if (emisor == null) 
			this.emisor = new Emisor();
		this.emisor.enviar(this.receptor.getMensaje(), PUERTO_PANTALLA);
		
		
		
	}
	
	public void escucharTerminal() {
		if (receptor == null) 
			this.receptor = new Receptor(PUERTO);
		receptor.recibir();
		
}
	
	public void rutinaTest() {
		this.escucharTerminal();
		this.llamarCliente();
		
	}
	
}