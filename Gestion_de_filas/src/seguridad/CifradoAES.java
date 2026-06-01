package seguridad;

import java.util.Arrays;

/**
 *en patron Strategy: ConcrateStrategies 
 *en patron Template: ConcrateClass
 */
public class CifradoAES extends CifradoSimetricoBase{

	@Override
	protected String getNombreAlgoritmo() {
		return "AES";
	}

	@Override
	protected byte[] ajustarClaveSecreta(String claveSecreta) {
		byte[] claveSecretaBytes = claveSecreta.getBytes();
		//estrictamente es 16,24 o 32 bytes
		if(claveSecreta.length()<=16)
			return Arrays.copyOf(claveSecretaBytes, 16);
		else if(claveSecreta.length()<=24)
			return Arrays.copyOf(claveSecretaBytes, 24);
		else
			return Arrays.copyOf(claveSecretaBytes, 32);
	}

}
