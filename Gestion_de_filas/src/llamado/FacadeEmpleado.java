package llamado;

import java.net.BindException;
import java.net.ConnectException;

import factory.IAbstractFactory;
import sfd.Utils;

public class FacadeEmpleado {
	private Empleado empleado;
	private IAbstractFactory factory;
	

	public FacadeEmpleado() {
		
		this.empleado = new Empleado();
	}

	public void desconectar() throws ConnectException {
        if (empleado.getNumeroDePuesto()!=0) {
        	empleado.enviarDesconexion_Server("Desconectar");
        }
		
		
	}

/*	public void enviarCliente_Server(String msj) throws ConnectException {
		empleado.enviarCliente_Server(msj);
	}
*/

/*	public String pedirEstado() throws ConnectException {
		
		return empleado.pedirEstado();
	}
*/
	public void llamarCliente(String dni) throws ConnectException {
		enviarConReintentos(dni);
		
	}

	
/*	public int getNumeroDePuesto() {
		return empleado.getNumeroDePuesto();
	}
*/
	public void iniciarPuesto(String nroPuesto) throws BindException, NumberFormatException, ConnectException {
		enviarConReintentos(nroPuesto);
		empleado.setNumeroDePuesto(Integer.parseInt(nroPuesto));
	}

	private void enviarConReintentos(String msj) throws ConnectException {
		int intentos=Utils.Intentos;
		while(intentos>0) {
			try {
				//ACA EL MSJ ES EL DNI DEL CLIENTE QUE ESTA DESENCRIPTADO
				//EL SERVIDOR TIENE LA FILA CON LOS DNI ENCRIPTADOS POR LO QUE DEBEMOS ENCRIPTARLO ANTES DE ENVIARSELO EN EL METODO DE EMPLEADO
				empleado.enviarCliente_Server(msj);
				return;
			} catch (ConnectException e) {
				intentos--;
				//this.vistaEmpleado.mostrarMensaje("Reintentando conexión...");
				try {
				Thread.sleep(2000);}catch(InterruptedException e1) {}
				}
			}
		throw new ConnectException("No se pudo conectar al servidor después de varios intentos.");

	}

	public String obtenerEstadoCola() throws InterruptedException {
		int intentos = Utils.Intentos;
		while(intentos>0) {
			try {
				return empleado.pedirEstado();
						
			}catch (ConnectException e) {
				intentos--;
				Thread.sleep(2000);
				
			}
		}
		
		return null;
	}

	public String obtenerSiguienteCliente() {
		int intentos = Utils.Intentos;
		while(intentos>0) {
			try {
				return empleado.llamarCliente();
			} catch (ConnectException e) {
				intentos--;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return null;
		

	}
	

}
