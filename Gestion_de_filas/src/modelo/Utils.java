package modelo;

public class Utils {
	public static final int cantidad_Puestos_Empleados = 5;
	public static final int cantidad_Terminales_Registro = 3;
	
	public static final String IP = "127.0.0.1";
	//Segunda etapa
	//ClaseEmisora_to_ClaseReceptora
	
	//Registro_to_Server y PUERTO_CONFIRMACION tienen la misma cantidad
	public static final String Registro_to_Server = "6000";
	public static final String PUERTO_CONFIRMACION = "7000"; //este es de server a registro
	
	public static final String Server_to_Pantalla = "5000";
	
	//Server_to_Empleado y Empleado_to_Server tienen la misma cantidad
	public static final String Server_to_Empleado_base = "4050";
	public static final String Empleado_to_Server = "5050"; //de aca para adelante puede crecer idenfinidamente
}
