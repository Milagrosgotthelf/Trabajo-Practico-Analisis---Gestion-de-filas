package notificacion;

import java.util.LinkedList;

import factory.FactoryArchs;
import factory.IAbstractFactory;
import factory.JsonFactory;
import factory.TxtFactory;
import factory.XmlFactory;
import persistencia.IPersistencia.MonitorPersistencia;
import sfd.Utils;

public class FacadePantalla {
	private IAbstractFactory factory;
	private MonitorPersistencia gestorPersistencia;
	private Pantalla pantalla = null;
    
    
	public FacadePantalla() {
		factory= FactoryArchs.getFormato();
	    this.gestorPersistencia = factory.crearMonitorPersistencia();
	    
	    this.pantalla = Pantalla.getInstance();
	}


	public void setClientes() {
        this.pantalla.setClientes(this.gestorPersistencia.recuperarHistorial());
		
	}


	public LinkedList<String> getClientes() {
		
		return pantalla.getClientes();
	}


	public boolean escucharEmpleado() {
		return pantalla.escucharEmpleado();
	}


	public void guardarHistorial() {
    	gestorPersistencia.guardarHistorial(pantalla.getClientes());

		
	}


	public void cerrarPantalla() {
    	pantalla.cerrarPantalla(); 
	}

	
	
	
    
}
