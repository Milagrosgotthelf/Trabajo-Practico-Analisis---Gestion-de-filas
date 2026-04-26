package main;

import java.util.ArrayList;

import controlador.ControladorRegistro;
import modelo.Utils;
import vista.Ventana_terminal_registro;

public class MainReg {

	public static void main(String[] args) {

		Ventana_terminal_registro vistasReg = new Ventana_terminal_registro(0);
		ControladorRegistro controladores = new ControladorRegistro(0, vistasReg);
		
		vistasReg.setVisible(true);
		
	}

}
