package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;
public class ControladorRegistro implements ActionListener{
	
	private TerminalRegistro terminal = null;
	private Ventana_terminal_registro ventana_registro;
	private boolean escuchando=false;
	
	public ControladorRegistro(Ventana_terminal_registro reg)  {
		this.ventana_registro = reg;
		this.terminal = TerminalRegistro.getInstance();
		this.ventana_registro.setActionListener(this);
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
		if(!this.escuchando) {
			this.EscucharConfirmaciones();
			System.out.println("ControladorRegistro 49: Llamada funcion de escucha");
		}
	}
	
	private void EscucharConfirmaciones() {
		this.escuchando = true;
		Thread hiloLlamada = new Thread(new Runnable() {
			@Override
			public void run() {

				System.out.println("ControladorRegistro 59: " + terminal.getReceptor().getMensaje());
				terminal.enviarCliente();
				System.out.println("ControladorRegistro 56: Cliente enviado");
				
			}
		});
		hiloLlamada.start();
		this.escuchando = false;
		//O: Puede que haya que llamar a esta funcion de nuevo en otro lado
	}
}
