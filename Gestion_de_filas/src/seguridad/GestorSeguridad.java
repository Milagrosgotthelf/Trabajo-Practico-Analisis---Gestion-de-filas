package seguridad;

import sfd.Utils;

/**
 *en patron Strategy: Context 
 * la idea seria que se cifre el dni cuando la TerminalRegistro se lo manda al Servidor
 * y se descifra cuando el empleado quiere el dniActual para mostrarlo en su PC y cuando la Pantalla lo quiere mostrar
 */
public class GestorSeguridad {
	private IEstrategiaCifrado estrategiaCifrado;
	private String claveSecreta; //no le pongo final porque se supone que la vamos a poder cambiar durante la ejecucion
	private Utils utils = new Utils();
	
	public GestorSeguridad() {
		utils.cargarConfiguracionSeguridad(); //ACA ESTOY ABRIENDO SOLO UNA VEZ EL ARCHIVO. NO ME APRECE QUE EN CADA OPERACION DE PASO DE MENSAJES SE DEBA ABRIR EL ARCHIVO PORQUE VA A ATRASAR TODAS LAS OPERACIONES
		
		this.claveSecreta = utils.getClaveSecreta();
		
		String algoritmo = utils.getAlgoritmo();
		if (algoritmo.equalsIgnoreCase("DES"))
			estrategiaCifrado = new CifradoDES();
		else if (algoritmo.equalsIgnoreCase("AES"))
			estrategiaCifrado = new CifradoAES();
		else if (algoritmo.equalsIgnoreCase("BLOWFISH"))
			estrategiaCifrado = new CifradoBlowfish();
	}
	
	public String protegerDNI(String DNI) {
		return estrategiaCifrado.encriptar(DNI, claveSecreta);
	}
	
	public String recuperarDNI(String DNI) {
		return estrategiaCifrado.desencriptar(DNI, claveSecreta);
	}

	/** IMPORTANTE 
	 * para permitir el cambio en tiempo de ejecucion podriamos hacer:
	 * 1) observer: podemos tener una clase que monitoree el archivo y cuando cambie avise a GestorSeguridad para que actualice sus atributos 
	 * 2) tener otra ventana donde pongamos claveSecreta y seleccionemos con radiobutton DES/AES/Blowfish en vez de trabajar con archivos
	 */
}
