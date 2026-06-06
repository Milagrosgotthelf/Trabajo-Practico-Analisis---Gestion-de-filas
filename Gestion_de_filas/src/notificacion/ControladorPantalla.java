package notificacion;

import factory.IAbstractFactory;
import factory.JsonFactory;
import factory.TxtFactory;
import factory.XmlFactory;
import persistencia.IPersistencia.MonitorPersistencia;
import sfd.Utils;

public class ControladorPantalla {
    
    private Ventana_pantalla vistaPantalla = null;
    private FacadePantalla facadePant = new FacadePantalla();
    public ControladorPantalla(Ventana_pantalla vista) {
        
                
        this.vistaPantalla = vista;
        
        
        // 1. Recuperamos el historial al iniciar
        facadePant.setClientes();

        
        // 2. Actualizamos la vista inicial por si recuperó datos de la sesión anterior
        vista.actualizarTurnos(facadePant.getClientes());

        new Thread(() -> {
            while (true) {
                // 3. Verificamos si hubo un nuevo llamado
                boolean huboActualizacion = facadePant.escucharEmpleado(); 
                
                if (huboActualizacion) {
                    // GUARDADO INMEDIATO: Cumple el requisito de tolerancia a fallas críticas
                    facadePant.guardarHistorial();
                    
                    java.awt.EventQueue.invokeLater(() -> {
                        vista.actualizarTurnos(facadePant.getClientes());
                    });
                }
            }
        }).start();

        this.vistaPantalla.CerrarVentana(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Guardado por precaución al cerrar de forma ordenada
            	facadePant.guardarHistorial();
                facadePant.cerrarPantalla();

            }
        });
    }
}