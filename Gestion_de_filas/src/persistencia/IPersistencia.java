package persistencia;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface IPersistencia {
	
	public interface ColaPersistencia {
	    void guardarCola(LinkedList<String> dniClientes);
	    LinkedList<String> recuperarCola();
	}

	public interface MonitorPersistencia {
	    void guardarHistorial(LinkedList<String> historial);
	    LinkedList<String> recuperarHistorial();
	}

	public interface NotificacionPersistencia {
	    void guardarIntentos(Map<String, Integer> intentos);
	    Map<String, Integer> recuperarIntentos();
	}

}
