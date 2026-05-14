package sfd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Utils {
	public static  String IP;
	public static  String Registro_to_Server;
	public static  String PUERTO_CONFIRMACION; //este es de server a registro
	
	public static String Server_to_Pantalla;
	
	public static String Server_to_Empleado_base;
	public static String Empleado_to_Server; //de aca para adelante puede crecer idenfinidamente
	
	public static String Empleado_to_Server2;
	public static String Registro_to_Server2;
	
	public static String Server_to_Server2;
	public static String Server2_to_Server;
	
	public static final int Intentos=4;
	
	//asi se ejecuta automaticamente al ejecutar la clase
	static {
		Properties prop = new Properties();
		try {
			FileInputStream input = new FileInputStream("config.properties");
			
			IP = prop.getProperty("IP");
			Registro_to_Server = prop.getProperty("Registro_to_Server");
			PUERTO_CONFIRMACION = prop.getProperty("PUERTO_CONFIRMACION");
			
			Server_to_Pantalla = prop.getProperty("Server_to_Pantalla");
			
			Server_to_Empleado_base = prop.getProperty("Server_to_Empleado_base");
			Empleado_to_Server = prop.getProperty("Empleado_to_Server");
			
			Empleado_to_Server2 = prop.getProperty("Empleado_to_Server2");
			Registro_to_Server2 = prop.getProperty("Registro_to_Server2");
			
			Server_to_Server2 = prop.getProperty("Server_to_Server2");
			Server2_to_Server = prop.getProperty("Server2_to_Server");
			
			
		} catch (FileNotFoundException e) {
			
			//e.printStackTrace();
		}
	}
	
	}
