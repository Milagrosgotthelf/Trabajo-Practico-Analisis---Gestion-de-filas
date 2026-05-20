package sfd;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;

//VER>>>>>> cuando vean las clases de Cifrado AES DES y Blowfish van a notar que se repite mucho codigo, podriamos aplicar Template aca no??
/**
 *en patron Strategy: ConcrateStrategies 
 */
public class CifradoAES implements IEstrategiaCifrado {
	
	/**
	 * para que los metodos de cifrado sean compatibles y poder aplicar Strategy, debe funcionar con cualquier clave proporcinada
	 * Entonces ajustamos la claveSecreta proporcionada para que sea compatible tambien con este metodo
	 */
	private byte[] ajustarClaveSecreta(String claveSecreta) {
		byte[] claveSecretaBytes = claveSecreta.getBytes();
		//estrictamente es 16,24 o 32 bytes
		if(claveSecreta.length()<=16)
			return Arrays.copyOf(claveSecretaBytes, 16);
		else if(claveSecreta.length()<=24)
			return Arrays.copyOf(claveSecretaBytes, 24);
		else
			return Arrays.copyOf(claveSecretaBytes, 32);
	}
	
	/**
	 * La claveSecreta se tiene que pasar a bytes porque los algoritmos de cifrado no trabajan con Strings 
	 * En esta clase concreta de estrategia usamos el algoritmo de cifrado simetrico AES (clave de 16,24 o 32 bytes es decir 16,24,32 caracteres de claveSecreta)
	 * El motor criptografico es cipher y puede operar en modo ecriptar segun un algoritmo especifico (AES)
	 * El dato es el DNI del cliente String por lo que tambien hay que pasarlo a bytes y ahora si el motor realiza los calculos matematicos correspondientes para obtener el arreglo de bytes ilegibles que es la encriptacion
	 * El base64 es un sist de codificacion que toma el encriptado binario y lo traduce a un formato de caracteres
	 */
	@Override
	public String encriptar(String dato, String claveSecreta) {
		SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
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
		SecretKeySpec key = new SecretKeySpec(ajustarClaveSecreta(claveSecreta), "AES");
		try {
			Cipher cipher = Cipher.getInstance("AES");
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
