package notificacion;


public class ControladorPantalla {
	
	private Pantalla pantalla = null;
	private Ventana_pantalla vistaPantalla = null;
	public ControladorPantalla(Ventana_pantalla vista) {
		
	        this.vistaPantalla = vista;
	        this.pantalla = Pantalla.getInstance();
	        new Thread(() -> {
	            while (true) {
	                pantalla.escucharEmpleado(); 
	                java.awt.EventQueue.invokeLater(() -> {
	                    vista.actualizarTurnos(pantalla.getClientes());
	                });
	            }
	        }).start();
	    }
	
	}
