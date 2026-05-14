package registro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.ConnectException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import sfd.Utils;

public class ControladorRegistro implements ActionListener {
	
	private TerminalRegistro terminal = null;
	private Ventana_terminal_registro ventana_registro;
	private int numTerminal;
	
	public ControladorRegistro(int id, Ventana_terminal_registro reg) {
		this.numTerminal = id;
		this.ventana_registro = reg;
		
		
		this.terminal = this.nuevaTerminal();
		this.ventana_registro.setActionListener(this);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e)  {
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
				
				
				 int agregado = this.agregarCliente(dniActual);
				
				
				if (agregado==1){
					this.ventana_registro.mostrarMensajeTemporal("   Usted ha sido registrado con exito.  ", 155, 100, 240, 50);	
				}
				else if (agregado == 0){
					this.ventana_registro.mostrarMensajeTemporal("   DNI REPETIDO: Por favor, aguarde a ser llamado.  ", 155, 100, 322, 50);	
				}
				else if (agregado==2) {
					this.ventana_registro.mostrarMensajeTemporal("   PROBLEMAS CON LA CONEXION  ", 155, 100, 322, 50);
					//Deberiamos cerrarlo?
				}
				else {
					this.ventana_registro.mostrarMensajeTemporal("   DNI INVALIDO: Fuera de rango etario.  ", 155, 100, 260, 50);
				}
			}
			this.ventana_registro.setDni("");
		}
		this.ventana_registro.validarLongitud();
	}
	
	public int agregarCliente(String dniActual) {
		//Zona de posible error
		
		int intentos = Utils.Intentos;;
		boolean agregado = false;
		if (dniActual.startsWith("00"))
			return 3;
		else {
			while(intentos > 0 && !agregado) {
				try {
					agregado = this.terminal.agregarCliente(dniActual);
				} catch (ConnectException e1) {
					System.out.println(agregado);
					intentos--;
					if (intentos>0) {
						//Si salta ConnectException es porque no hay servidor que escuche
						try {
							if (intentos<Utils.Intentos)
								JOptionPane.showMessageDialog(ventana_registro, "Reintentando");
							Thread.sleep(5000);
							
						}catch(InterruptedException e) {}
						
					}
					else {
						JOptionPane.showMessageDialog(ventana_registro, "No se pudo conectar al servidor.");
						return 2;
					}
					
				}
			}
			return (agregado) ? 1 : 0;
		}
	}
	
	public TerminalRegistro nuevaTerminal() {
		TerminalRegistro terminal = null;
		int intentos = Utils.Intentos;
		boolean conectado = false;
		while(intentos > 0 && !conectado)
			try {
				intentos--;
				terminal = new TerminalRegistro(this.numTerminal);
				conectado = true;
			} catch (ConnectException e) {
				System.out.println("No se puede conectar al servidor");
				try {
					System.out.println("Espera");
					Thread.sleep(5000); // Espera 5 segundos antes de intentar reconectar
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}catch(BindException e) {
				System.out.println(e.getMessage() + " Cerrando...");
				System.exit(1);
				
			}
		return terminal;
	}
	
}