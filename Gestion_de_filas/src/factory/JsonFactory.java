package factory;

import persistencia.IPersistencia.*;
import persistencia.JsonColaPersistencia;
import persistencia.JsonMonitorPersistencia;
import persistencia.JsonNotificacionPersistencia;

public class JsonFactory implements IAbstractFactory {
	@Override
    public ColaPersistencia crearColaPersistencia() {
        return new JsonColaPersistencia();
    }
    @Override
    public MonitorPersistencia crearMonitorPersistencia() {
        return new JsonMonitorPersistencia(); 
    }
    @Override
    public NotificacionPersistencia crearNotificacionPersistencia() {
        return new JsonNotificacionPersistencia();
    }

}
