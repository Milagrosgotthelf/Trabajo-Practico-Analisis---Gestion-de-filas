package seguridad;

import java.util.Arrays;
/**
 *en patron Strategy: ConcrateStrategies 
 *en patron Template: ConcrateClass
 */
public class CifradoDES extends CifradoSimetricoBase{
	@Override
	protected String getNombreAlgoritmo() {
		return "DES";
	}

	@Override
	protected byte[] ajustarClaveSecreta(String claveSecreta) {
		byte[] claveSecretaBytes = claveSecreta.getBytes();
		return Arrays.copyOf(claveSecretaBytes, 8); //es estrictamente 8 bytes
	}

}
