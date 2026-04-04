package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_terminal_registro;

public class ControladorRegistro implements ActionListener {
	
	private TerminalRegistro terminal = null;
	private Ventana_terminal_registro ventana_registro;
	
	public ControladorRegistro(Ventana_terminal_registro reg) {
		this.ventana_registro = reg;
		this.terminal = TerminalRegistro.getInstance();
		this.ventana_registro.setActionListener(this);
		
		// Iniciamos la escucha en segundo plano desde el primer momento
		this.iniciarEscuchaConfirmaciones();
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
			this.agregarCliente(dniActual);
			ventana_registro.setDni("");
		}
		this.ventana_registro.validarLongitud();
	}
	
	public void agregarCliente(String dni) {
		this.terminal.agregarCliente(new Cliente(dni));	
		this.ventana_registro.mostrarMensajeTemporal("   Usted ha sido registrado con exito.  ", 155, 100, 240, 50);	
	}
	
	private void iniciarEscuchaConfirmaciones() {
		Thread hiloLlamada = new Thread(new Runnable() {
			@Override
			public void run() {
				
				while (true) {
					try {
						String msj = terminal.getReceptor().getMensaje(); 
						
						if (msj != null) {
							terminal.enviarCliente();
							System.out.println("ControladorRegistro 60: Cliente enviado");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		});
		hiloLlamada.setDaemon(true); // Permite que el hilo muera automáticamente si cierras la ventana principal
		hiloLlamada.start();
	}
}