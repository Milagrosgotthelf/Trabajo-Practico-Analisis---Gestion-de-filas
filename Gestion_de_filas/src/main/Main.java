package main;

import controlador.Controlador;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Pantalla;
import modelo.TerminalRegistro;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;

public class Main {

	public static void main(String[] args) {
		Ventana_terminal_registro registro = new Ventana_terminal_registro();
		Ventana_empleado vistaEmp = new Ventana_empleado();
		Ventana_pantalla vistaPant = new Ventana_pantalla();
		Controlador controlador =Controlador.getInstance();
		controlador.setVistas(registro,vistaEmp,vistaPant);
		registro.setVisible(true);
		vistaEmp.setVisible(true);
		vistaPant.setVisible(true);
		
		/*
		TerminalRegistro terminal = TerminalRegistro.getInstance();
		Empleado empleado = new Empleado();
		Pantalla pantalla = Pantalla.getInstance();

		pantalla.escucharEmpleado();
		terminal.rutinaTest();
		empleado.rutinaTest();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pantalla.rutinaTest();
		terminal.agregarCliente(new Cliente("44635107"));
		terminal.enviarCliente();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Tiempo de espera obligatorio, sino lee el mensaje anterior
		empleado.rutinaTest();
		pantalla.rutinaTest();
		
		
		
		
		
		// TODO Auto-generated method stub
*/
	}

}
