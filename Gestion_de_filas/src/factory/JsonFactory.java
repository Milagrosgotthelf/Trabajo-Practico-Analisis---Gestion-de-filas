package factory;

import persistencia.IPersistencia.*;
import persistencia.JsonColaPersistencia;
import persistencia.JsonMonitorPersistencia;
import persistencia.JsonNotificacionPersistencia;

public class JsonFactory implements AFPersistencia {
	@Override
    public ColaPersistencia crearColaPersistencia() {
        return new JsonColaPersistencia();
    }
    @Override
    public MonitorPersistencia crearMonitorPersistencia() {
        // Se implementaría de forma homóloga al ejemplo anterior
        return new JsonMonitorPersistencia(); 
    }
    @Override
    public NotificacionPersistencia crearNotificacionPersistencia() {
        return new JsonNotificacionPersistencia();
    }

}
