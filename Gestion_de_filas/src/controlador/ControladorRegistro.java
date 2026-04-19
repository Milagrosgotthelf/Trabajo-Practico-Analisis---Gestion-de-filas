package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import modelo.*;
import vista.Ventana_terminal_registro;

public class ControladorRegistro implements ActionListener {
	
	private TerminalRegistro terminal = null;
	private Ventana_terminal_registro ventana_registro;
	private int numTerminal;
	
	public ControladorRegistro(int id, Ventana_terminal_registro reg) {
		this.numTerminal = id;
		this.ventana_registro = reg;
		this.terminal = new TerminalRegistro(this.numTerminal);
		this.ventana_registro.setActionListener(this);
		
		// Iniciamos la escucha en segundo plano desde el primer momento
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		String dniActual = ventana_registro.getDni();
		
		if (comando.matches("\\d")) { 
			dniActual += comando;
			ventana_registro.setDni(dniActual);
			
		} else if (comando.equals("←")) {
			if (dniActual.length() > 0) {
				dniActual = dniActual.substring(0, dniActual.length() - 1);
				ventana_registro.setDni(dniActual);
			}
			
		} else if (comando.equals("Aceptar")) {
			ventana_registro.resetearPantalla();
			int opcion = ventana_registro.confirmarDNI(dniActual);
			if (opcion == JOptionPane.YES_OPTION) {
				
					boolean agregado = this.terminal.agregarCliente(dniActual);
					
						if (agregado){
							this.ventana_registro.mostrarMensajeTemporal("   Usted ha sido registrado con exito.  ", 155, 100, 240, 50);	
						}
						else{
							this.ventana_registro.mostrarMensajeTemporal("   DNI REPETIDO: Por favor, aguarde a ser llamado.  ", 155, 100, 322, 50);	
						}
			}
			this.ventana_registro.setDni("");
		}
		this.ventana_registro.validarLongitud();
	}
	
}