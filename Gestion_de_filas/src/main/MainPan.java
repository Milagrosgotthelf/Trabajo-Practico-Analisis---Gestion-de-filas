package main;

import controlador.ControladorPantalla;
import vista.Ventana_pantalla;

public class MainPan {

	public static void main(String[] args) {
		
		Ventana_pantalla vistaPantalla = new Ventana_pantalla();
		ControladorPantalla controlador = new ControladorPantalla(vistaPantalla);
		vistaPantalla.setVisible(true);

	}

}
