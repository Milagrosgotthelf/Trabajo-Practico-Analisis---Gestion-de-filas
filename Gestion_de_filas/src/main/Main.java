package main;

import modelo.Cliente;
import modelo.Empleado;
import modelo.TerminalRegistro;

public class Main {

	public static void main(String[] args) {
		
		TerminalRegistro terminal = TerminalRegistro.getInstance();
		Empleado empleado = new Empleado();
		
		empleado.escucharTerminal();
		
		terminal.agregarCliente(new Cliente("44635069"));
		terminal.enviarCliente();
		
		
		
		// TODO Auto-generated method stub

	}

}
