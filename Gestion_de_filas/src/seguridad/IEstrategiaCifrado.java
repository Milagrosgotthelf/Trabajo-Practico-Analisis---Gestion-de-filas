package seguridad;
/** 
 * en patron Strategy: <<interface>> Strategy
 */

public interface IEstrategiaCifrado {
	String encriptar(String dato, String claveSecreta) ;
	String desencriptar(String datoEncriptado, String claveSecreta) ;
}
