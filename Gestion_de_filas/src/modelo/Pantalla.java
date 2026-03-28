package modelo;

public class Pantalla {
	
	private Pantalla instancia = null;
	
	private Pantalla() {
		
	}
	
	public Pantalla getInstance() {
		if (instancia == null) {
			instancia = new Pantalla();
		}
		return instancia;
		
	}
	
	

}
