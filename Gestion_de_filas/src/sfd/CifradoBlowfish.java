package sfd;

import java.util.Arrays;

/**
 *en patron Strategy: ConcrateStrategies 
 *en patron Template: ConcrateClass
 */
public class CifradoBlowfish extends CifradoSimetricoBase {		

		@Override
		protected String getNombreAlgoritmo() {
			return "Blowfish";
		}

		@Override
		protected byte[] ajustarClaveSecreta(String claveSecreta) {
			byte[] claveSecretaBytes = claveSecreta.getBytes();
			if(claveSecreta.length()<=4) // muy corta 
				return Arrays.copyOf(claveSecretaBytes, 4);
			else if(claveSecreta.length()>=56) // muy larga
				return Arrays.copyOf(claveSecretaBytes, 56);
			else // cualquier otra longitud valida entre 4-56
				return claveSecretaBytes;
		}

}
