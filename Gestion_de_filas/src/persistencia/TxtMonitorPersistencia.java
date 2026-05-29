package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.IPersistencia.MonitorPersistencia;

public class TxtMonitorPersistencia extends TPersistencia<List<String>> implements MonitorPersistencia {

	public TxtMonitorPersistencia() {
		super("datos/historial_monitor.txt");
		// TODO Auto-generated constructor stub
	}

	@Override
    public void guardarHistorial(List<String> historial) {
        guardar(historial); 
    }

    @Override
    public List<String> recuperarHistorial() {
        return recuperar(); 
    }

    @Override
    protected String formatearDatos(List<String> datos) {
        return String.join("\n", datos);
    }

    @Override
    protected List<String> parsearDatos(String contenido) {
        List<String> lista = new ArrayList<>();
        if (contenido != null && !contenido.trim().isEmpty()) {
            String[] llamados = contenido.split("\n");
            for (String llamado : llamados) {
                lista.add(llamado.trim());
            }
        }
        return lista;
    }

    @Override
    protected List<String> obtenerObjetoVacio() {
        return new ArrayList<>();
    }

}
