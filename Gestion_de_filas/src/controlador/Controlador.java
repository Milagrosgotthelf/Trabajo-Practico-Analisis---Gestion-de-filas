package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
public class Controlador implements ActionListener{
	
	private static Controlador instancia = null;
	private Empleado empleado = null;
	private TerminalRegistro terminal = null;
	private Pantalla pantalla = null;
	
	private Controlador()  {
		
	}
	
	public static Controlador getInstance() {
		if(instancia == null) {
			instancia = new Controlador();
			 instancia.terminal = TerminalRegistro.getInstance();
			 instancia.empleado = new Empleado();
			 instancia.pantalla = Pantalla.getInstance();
			 instancia.empleado.escucharTerminal();
			 instancia.pantalla.escucharEmpleado();
		}
		return instancia;	
	}
	
	public void agregarCliente(String dni) {
		if (this.validacionDNI(dni)) {
			this.terminal.agregarCliente(new Cliente(dni));			
		}
		else {
			System.out.println("DNI no valido");
			
		}
		TerminalRegistro.getInstance().agregarCliente(new Cliente(dni));
	}

	public boolean validacionDNI(String dni) {
		return dni.length() == 8 && dni.matches("\\d+") && this.dniNoRepetido(dni);
		// El DNI debe tener exactamente 8 dígitos y solo contener números, faltan validaciones
	}
	public boolean dniNoRepetido(String dni) {
		for (Cliente cliente : this.terminal.getClientes()) {
			if (cliente.getDni().equals(dni)) {
				return false; // El DNI ya existe en la lista de clientes
			}
		}
		return true; // El DNI no está repetido
	}
	public void llamarCliente() {
		this.empleado.llamarCliente();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
