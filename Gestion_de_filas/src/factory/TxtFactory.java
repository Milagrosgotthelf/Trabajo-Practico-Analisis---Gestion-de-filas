package factory;

import persistencia.IPersistencia.ColaPersistencia;
import persistencia.IPersistencia.MonitorPersistencia;
import persistencia.IPersistencia.NotificacionPersistencia;
import persistencia.TxtColaPersistencia;
import persistencia.TxtMonitorPersistencia;
import persistencia.TxtNotificacionPersistencia;

public class TxtFactory implements IAbstractFactory{
	@Override
	public ColaPersistencia crearColaPersistencia() {
		return new TxtColaPersistencia();
	}

	@Override
	public MonitorPersistencia crearMonitorPersistencia() {
		return new TxtMonitorPersistencia();
	}

	@Override
	public NotificacionPersistencia crearNotificacionPersistencia() {
		return new TxtNotificacionPersistencia();
	}

}
