package main;

import controlador.ControladorEmpleado;
import vista.Ventana_empleado;

public class MainEmp {

	public static void main(String[] args) {
		Ventana_empleado vistaEmp = new Ventana_empleado();
		ControladorEmpleado controlador =new ControladorEmpleado();
		controlador.setVistas(vistaEmp);
		vistaEmp.setVisible(true);
	}
}