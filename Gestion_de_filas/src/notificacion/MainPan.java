package notificacion;

public class MainPan {

	public static void main(String[] args) {
		
		Ventana_pantalla vistaPantalla = new Ventana_pantalla();
		ControladorPantalla controlador = new ControladorPantalla(vistaPantalla);
		vistaPantalla.setVisible(true);

	}

}
