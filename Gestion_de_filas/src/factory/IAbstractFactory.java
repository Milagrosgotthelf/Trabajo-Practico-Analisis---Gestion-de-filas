package factory;
//AF: Abstract Factory

import persistencia.IPersistencia.ColaPersistencia;
import persistencia.IPersistencia.MonitorPersistencia;
import persistencia.IPersistencia.NotificacionPersistencia;

//Esta clase es la fábrica abstracta de los objetos de persistencia para TXT, XML y JSON.
//Va a haber una clase que persista los tres objetos para cada formato
public interface IAbstractFactory {
  ColaPersistencia crearColaPersistencia();
  MonitorPersistencia crearMonitorPersistencia();
  NotificacionPersistencia crearNotificacionPersistencia();

}
