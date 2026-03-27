package modelo;

import static modelo.Utils.PUERTO;
public class Empleado {
	private Receptor receptor = null;
	
	public Empleado() {
		this.escucharTerminal();
		
	}
	
	private void llamarCliente() {
		//Aca deberia extraer al cliente de la informacion enviada por la terminal y enviarlo a la pantalla
		
		this.obtenerCliente();
		
	}
	
	private void obtenerCliente() {
		
		
	}
	
	public void escucharTerminal() {
		try {
			if (receptor != null) {
				receptor.recibir(PUERTO);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
}
