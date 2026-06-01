package seguridad;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
 * patron template: AbstractClass
 */
public abstract class CifradoSimetricoBase implements IEstrategiaCifrado {

	/**
	 * La claveSecreta se tiene que pasar a bytes porque los algoritmos de cifrado no trabajan con Strings 
	 * En esta clase concreta de estrategia usamos el algoritmo de cifrado simetrico AES (clave de 16,24 o 32 bytes es decir 16,24,32 caracteres de claveSecreta)
	 * El dato es el DNI del cliente String por lo que tambien hay que pasarlo a bytes y ahora si el motor realiza los calculos matematicos correspondientes para obtener el arreglo de bytes ilegibles que es la encriptacion
	 * El base64 es un sist de codificacion que toma el encriptado binario y lo traduce a un formato de caracteres
	 */
	@Override
	public final String encriptar(String dato, String claveSecreta) {
		try {
            /*  ajusta la longitud de las claves secretas para que sean compatibles los metodos de las distintas
				estrategias de cifrado simetrico y se pueda aplicar el patron Strategy sin problemas	*/ 
            byte[] claveAjustada = ajustarClaveSecreta(claveSecreta);
            SecretKeySpec key = new SecretKeySpec(claveAjustada, getNombreAlgoritmo());
            
            Cipher cipher = Cipher.getInstance(getNombreAlgoritmo());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] encriptado = cipher.doFinal(dato.getBytes());
            return Base64.getEncoder().encodeToString(encriptado);
            
        } catch(Exception e) {
            // VER>>>>>> catch para badpadding (clave incorrecta)
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public final String desencriptar(String datoEncriptado, String claveSecreta) {
		try {
            byte[] claveAjustada = ajustarClaveSecreta(claveSecreta);
            SecretKeySpec key = new SecretKeySpec(claveAjustada, getNombreAlgoritmo());
            
            Cipher cipher = Cipher.getInstance(getNombreAlgoritmo());
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] bytesCifrados = Base64.getDecoder().decode(datoEncriptado);
            byte[] desencriptado = cipher.doFinal(bytesCifrados);
            return new String(desencriptado);
            
        } catch(Exception e) {
            // VER>>>>>> catch para badpadding (clave incorrecta)
            e.printStackTrace();
            return null;
        }
	}
	
	//metodos gancho
	protected abstract String getNombreAlgoritmo();
	protected abstract byte[] ajustarClaveSecreta(String claveSecreta);	

}
