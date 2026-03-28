package main;

import modelo.Cliente;
import modelo.Empleado;
import modelo.Pantalla;
import modelo.TerminalRegistro;

public class Main {

	public static void main(String[] args) {
		
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

	}

}
