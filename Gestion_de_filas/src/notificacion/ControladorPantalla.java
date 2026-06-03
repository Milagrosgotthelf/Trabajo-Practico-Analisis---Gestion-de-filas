package notificacion;

import factory.IAbstractFactory;
import factory.JsonFactory;
import factory.TxtFactory;
import factory.XmlFactory;
import persistencia.IPersistencia.MonitorPersistencia;
import sfd.Utils;

public class ControladorPantalla {
    
    private Pantalla pantalla = null;
    private Ventana_pantalla vistaPantalla = null;
    private IAbstractFactory factory;
    private MonitorPersistencia gestorPersistencia;
    
    public ControladorPantalla(Ventana_pantalla vista) {
        
        if (Utils.Formato.toUpperCase().trim().equals("JSON"))
            factory = new JsonFactory();
        else if (Utils.Formato.toUpperCase().trim().equals("XML"))
            factory = new XmlFactory();
        else if (Utils.Formato.toUpperCase().trim().equals("TXT"))
            factory = new TxtFactory();
        else
            throw new IllegalArgumentException("Formato no soportado: " + Utils.Formato);
            
        this.gestorPersistencia = factory.crearMonitorPersistencia();
                
        this.vistaPantalla = vista;
        this.pantalla = Pantalla.getInstance();
        
        // 1. Recuperamos el historial al iniciar
        this.pantalla.setClientes(this.gestorPersistencia.recuperarHistorial());
        
        // 2. Actualizamos la vista inicial por si recuperó datos de la sesión anterior
        vista.actualizarTurnos(pantalla.getClientes());

        new Thread(() -> {
            while (true) {
                // 3. Verificamos si hubo un nuevo llamado
                boolean huboActualizacion = pantalla.escucharEmpleado(); 
                
                if (huboActualizacion) {
                    // GUARDADO INMEDIATO: Cumple el requisito de tolerancia a fallas críticas
                    gestorPersistencia.guardarHistorial(pantalla.getClientes());
                    
                    java.awt.EventQueue.invokeLater(() -> {
                        vista.actualizarTurnos(pantalla.getClientes());
                    });
                }
            }
        }).start();

        this.vistaPantalla.CerrarVentana(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Guardado por precaución al cerrar de forma ordenada
                gestorPersistencia.guardarHistorial(pantalla.getClientes());
                pantalla.cerrarPantalla(); 
            }
        });
    }
}