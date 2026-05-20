package registro;


import java.net.BindException;
import java.net.ConnectException;

import sfd.Emisor;
import sfd.GestorSeguridad;
import sfd.Receptor;
import sfd.Utils;

public class TerminalRegistro {
	Emisor emisor = new Emisor();
	Receptor receptor = null;
	private int numTerminal;
	private GestorSeguridad gestorSeguridad = new GestorSeguridad();

	
	public TerminalRegistro(int id) throws ConnectException, BindException {
		solicitarNumero();
	}
	
	public void solicitarNumero() throws ConnectException, BindException {
		
			this.receptor =  new Receptor(Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION)));
			emisor.enviar("TerminalActiva", Integer.toString(Integer.parseInt(Utils.Registro_to_Server)));
			String respuesta = receptor.getMensaje();
			this.numTerminal = Integer.parseInt(respuesta);
			this.receptor.kill();
			this.receptor =  new Receptor(Integer.toString(Integer.parseInt(Utils.PUERTO_CONFIRMACION) + this.numTerminal));
	

	}
	public boolean agregarCliente(String cliente) throws ConnectException{
		String clienteEncriptado = gestorSeguridad.protegerDNI(cliente); //ENVIAMOS EL DNI ENCRIPTADO AL SERVIDOR
		emisor.enviar(clienteEncriptado+"/"+Integer.toString(this.numTerminal), Integer.toString(Integer.parseInt(Utils.Registro_to_Server)));
		
		String respuesta = receptor.getMensaje();
		
		return "OK".equals(respuesta);		
	}
	 
	public Emisor getEmisor() {
		return emisor;
	}
		
	public Receptor getReceptor() {
		return this.receptor;
	}
		
}
