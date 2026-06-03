package persistencia;

import java.util.LinkedList;

import persistencia.IPersistencia.MonitorPersistencia;

public class TxtMonitorPersistencia extends TPersistencia<LinkedList<String>> implements MonitorPersistencia {

	public TxtMonitorPersistencia() {
		super("datos/historial_monitor.txt");
		// TODO Auto-generated constructor stub
	}

	@Override
    public void guardarHistorial(LinkedList<String> historial) {
        guardar(historial); 
    }

    @Override
    public LinkedList<String> recuperarHistorial() {
        return recuperar(); 
    }

    @Override
    protected String formatearDatos(LinkedList<String> datos) {
        return String.join("\n", datos);
    }

    @Override
    protected LinkedList<String> parsearDatos(String contenido) {
    	LinkedList<String> lista = new LinkedList<>();
        if (contenido != null && !contenido.trim().isEmpty()) {
            String[] llamados = contenido.split("\n");
            for (String llamado : llamados) {
                lista.add(llamado.trim());
            }
        }
        return lista;
    }

    @Override
    protected LinkedList<String> obtenerObjetoVacio() {
        return new LinkedList<>();
    }

}
