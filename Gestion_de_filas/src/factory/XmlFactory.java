package factory;

import persistencia.XmlColaPersistencia;
import persistencia.XmlMonitorPersistencia;
import persistencia.XmlNotificacionPersistencia;
import persistencia.IPersistencia.ColaPersistencia;
import persistencia.IPersistencia.MonitorPersistencia;
import persistencia.IPersistencia.NotificacionPersistencia;

public class XmlFactory implements IAbstractFactory{

	@Override
	public ColaPersistencia crearColaPersistencia() {
		return new XmlColaPersistencia();
	}

	@Override
	public MonitorPersistencia crearMonitorPersistencia() {
		return new XmlMonitorPersistencia();
	}

	@Override
	public NotificacionPersistencia crearNotificacionPersistencia() {
		return new XmlNotificacionPersistencia();
	}
	

}
