package main;

import java.util.ArrayList;

import controlador.ControladorEmpleado;
import modelo.Utils;
import vista.Ventana_empleado;

public class MainEmp {

	public static void main(String[] args) {
		ArrayList<ControladorEmpleado> controladores = new ArrayList<ControladorEmpleado>();
		ArrayList<Ventana_empleado> vistasEmp = new ArrayList<Ventana_empleado>();
		
		for (int i=0; i< Utils.cantidad_Puestos_Empleados; i++) {
			vistasEmp.add(new Ventana_empleado(i+1));
			controladores.add(new ControladorEmpleado(i+1));
			controladores.get(i).setVistas(vistasEmp.get(i));
			vistasEmp.get(i).setVisible(true);
		}
	}
}