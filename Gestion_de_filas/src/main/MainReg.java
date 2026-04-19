package main;

import java.util.ArrayList;

import controlador.ControladorEmpleado;
import controlador.ControladorRegistro;
import modelo.Utils;
import vista.Ventana_empleado;
import vista.Ventana_terminal_registro;

public class MainReg {

	public static void main(String[] args) {
		ArrayList<ControladorRegistro> controladores = new ArrayList<ControladorRegistro>();
		ArrayList<Ventana_terminal_registro> vistasReg = new ArrayList<Ventana_terminal_registro>();
		
		for (int i=0; i< Utils.cantidad_Terminales_Registro; i++) {
			vistasReg.add(new Ventana_terminal_registro(i+1));
			controladores.add(new ControladorRegistro(i+1,vistasReg.get(i)));
			vistasReg.get(i).setVisible(true);
		}
		
	}

}
