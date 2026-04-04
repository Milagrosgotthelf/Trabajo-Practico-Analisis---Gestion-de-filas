package main;

import controlador.ControladorRegistro;
import vista.Ventana_terminal_registro;

public class MainReg {

	public static void main(String[] args) {
		Ventana_terminal_registro registro = new Ventana_terminal_registro();
		ControladorRegistro controlador =new ControladorRegistro(registro);
		registro.setVisible(true);
	}

}
