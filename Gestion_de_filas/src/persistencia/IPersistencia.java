package persistencia;

import java.util.List;
import java.util.Map;

public interface IPersistencia {
	
	public interface ColaPersistencia {
	    void guardarCola(List<String> dniClientes);
	    List<String> recuperarCola();
	}

	public interface MonitorPersistencia {
	    void guardarHistorial(List<String> historial);
	    List<String> recuperarHistorial();
	}

	public interface NotificacionPersistencia {
	    void guardarIntentos(Map<String, Integer> intentos);
	    Map<String, Integer> recuperarIntentos();
	}

}
