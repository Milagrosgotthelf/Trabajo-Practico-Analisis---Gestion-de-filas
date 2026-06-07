package registro;

import java.net.BindException;
import java.net.ConnectException;

import sfd.Utils;

public class FacadeRegistro {
	private TerminalRegistro terminal = null;
	private int numTerminal;
	public FacadeRegistro(int id) {
		this.numTerminal = id;
		this.terminal = this.nuevaTerminal();
	}
	
	private TerminalRegistro nuevaTerminal() {
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
			}catch(BindException e) {}
		return terminal;
	}

	public boolean agregarCliente(String dniActual) throws ConnectException {
		return this.terminal.agregarCliente(dniActual);
	}


}
