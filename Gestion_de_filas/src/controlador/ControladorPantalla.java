package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;
public class ControladorPantalla {
	
	private static ControladorPantalla instancia = null;
	private Pantalla pantalla = null;
	private Ventana_pantalla vistaPantalla;
	public ControladorPantalla(Ventana_pantalla vista) {
	        this.vistaPantalla = vista;
	        new Thread(() -> {
	            while (true) {
	                pantalla.escucharEmpleado(); // Bloquea hasta recibir DNI
	                java.awt.EventQueue.invokeLater(() -> {
	                    vista.actualizarTurnos(pantalla.getClientes());
	                });
	            }
	        }).start();
	    }
	
	}
