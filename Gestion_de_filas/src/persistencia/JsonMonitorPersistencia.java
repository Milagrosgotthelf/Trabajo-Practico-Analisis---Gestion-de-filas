package persistencia;


import persistencia.IPersistencia.MonitorPersistencia;
import java.util.LinkedList;

public class JsonMonitorPersistencia extends TPersistencia<LinkedList<String>> implements MonitorPersistencia {

    public JsonMonitorPersistencia() {
        super("datos/historial_monitor.json");
    }

    @Override
    public void guardarHistorial(LinkedList<String> historial) {
        guardar(historial); // Llama al template method
    }

    @Override
    public LinkedList<String> recuperarHistorial() {
        return recuperar(); // Llama al template method
    }

    @Override
    protected String formatearDatos(LinkedList<String> datos) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < datos.size(); i++) {
            json.append("  \"").append(datos.get(i)).append("\"");
            if (i < datos.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    @Override
    protected LinkedList<String> parsearDatos(String contenido) {
    	LinkedList<String> lista = new LinkedList<>();
        String limpia = contenido.replace("[", "").replace("]", "").replace("\"", "").trim();
        
        if (!limpia.isEmpty()) {
            for (String item : limpia.split(",")) {
                lista.add(item.trim());
            }
        }
        return lista;
    }

    @Override
    protected LinkedList<String> obtenerObjetoVacio() {
        return new LinkedList<String>();
    }
}
