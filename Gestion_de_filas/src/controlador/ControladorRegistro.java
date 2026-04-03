package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;
public class ControladorRegistro implements ActionListener{
	
	private static ControladorRegistro instancia = null;
	private TerminalRegistro terminal = null;
	private Ventana_terminal_registro ventana_registro;
	
	public ControladorRegistro(Ventana_terminal_registro reg)  {
		this.ventana_registro = reg;
		this.terminal = TerminalRegistro.getInstance();
		this.ventana_registro.setActionListener(this);
		this.EscucharConfirmaciones();
	}
	
		
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		String dniActual =ventana_registro.getDni();
		if (comando.matches("\\d")) { //busca si es igual a cualquier digito decimal
			dniActual += comando;
			ventana_registro.setDni(dniActual);
		}
		else if (comando.equals("←")) {
			if (dniActual.length()>0) {
				dniActual = dniActual.substring(0, dniActual.length()-1); //le saco el ultimo digito
				ventana_registro.setDni(dniActual);
			}
		}
		else if (comando.equals("Aceptar")) {
			this.agregarCliente(dniActual);
			ventana_registro.setDni("");
		}
		this.ventana_registro.validarLongitud();
	}
	
	public void agregarCliente(String dni) {
		this.terminal.agregarCliente(new Cliente(dni));	
		this.ventana_registro.mostrarMensajeTemporal("   Usted ha sido registrado con exito.  ",155, 100, 240, 50);	
	}
	
	private void EscucharConfirmaciones() {
		Thread hiloLlamada = new Thread(new Runnable() {
			@Override
			public void run() {
				terminal.getReceptor().recibir();
				System.out.println("ControladorRegistro 55: " + terminal.getReceptor().getMensaje());
				terminal.enviarCliente();
				System.out.println("ControladorRegistro 57");
				
			}
		});
		hiloLlamada.start();
		//O: Puede que haya que llamar a esta funcion de nuevo en otro lado
	}
}
