package sfd;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *en patron Strategy: ConcrateStrategies 
 */
public class CifradoBlowfish implements IEstrategiaCifrado {		
		/**
		 * para que los metodos de cifrado sean compatibles y poder aplicar Strategy, debe funcionar con cualquier clave proporcinada
		 * Entonces ajustamos la claveSecreta proporcionada para que sea compatible tambien con este metodo
		 */
		private byte[] ajustarClaveSecreta(String claveSecreta) {
			byte[] claveSecretaBytes = claveSecreta.getBytes();
			if(claveSecreta.length()<=4) // muy corta 
				return Arrays.copyOf(claveSecretaBytes, 4);
			else if(claveSecreta.length()>=56) // muy larga
				return Arrays.copyOf(claveSecretaBytes, 56);
			else // cualquier otra longitud valida entre 4-56
				return claveSecretaBytes;
		}
		
		@Override
		public String encriptar(String dato, String claveSecreta) {
			SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "Blowfish");
			try {
				Cipher cipher = Cipher.getInstance("Blowfish");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] encriptado = cipher.doFinal(dato.getBytes());
				return Base64.getEncoder().encodeToString(encriptado);
			} catch(Exception e) {
				//VER>>>>>> catch para badpadding(clave incorrecta)
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public String desencriptar(String datoEncriptado, String claveSecreta) {
			SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "Blowfish");
			try {
				Cipher cipher = Cipher.getInstance("Blowfish");
				cipher.init(Cipher.DECRYPT_MODE, key);
				byte[] desencriptado = cipher.doFinal(Base64.getDecoder().decode(datoEncriptado));
				return new String(desencriptado);
			}catch(Exception e) {
				//VER>>>>>> catch para badpadding(clave incorrecta)
				e.printStackTrace();
				return null;
			}
		}

}
