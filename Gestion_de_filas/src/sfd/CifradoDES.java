package sfd;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *en patron Strategy: ConcrateStrategies 
 */
public class CifradoDES implements IEstrategiaCifrado {

	/**
	 * para que los metodos de cifrado sean compatibles y poder aplicar Strategy, debe funcionar con cualquier clave proporcinada
	 * Entonces ajustamos la claveSecreta proporcionada para que sea compatible tambien con este metodo
	 */
	private byte[] ajustarClaveSecreta(String claveSecreta) {
		byte[] claveSecretaBytes = claveSecreta.getBytes();
		return Arrays.copyOf(claveSecretaBytes, 8); //es estrictamente 8 bytes
	}
	
	@Override
	public String encriptar(String dato, String claveSecreta) {
		try {
			SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "DES");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encriptado = cipher.doFinal(dato.getBytes());
			return Base64.getEncoder().encodeToString(encriptado);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String desencriptar(String datoEncriptado, String claveSecreta) {
		try {
			SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "DES");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] desencriptado = cipher.doFinal(Base64.getDecoder().decode(datoEncriptado));
			return new String(desencriptado);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
