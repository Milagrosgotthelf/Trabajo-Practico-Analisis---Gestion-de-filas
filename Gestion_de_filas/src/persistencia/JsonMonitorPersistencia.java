package persistencia;

import java.util.List;

import persistencia.IPersistencia.MonitorPersistencia;

import java.util.ArrayList;
import java.util.List;

public class JsonMonitorPersistencia extends TPersistencia<List<String>> implements MonitorPersistencia {

    public JsonMonitorPersistencia() {
        super("datos/historial_monitor.json");
    }

    @Override
    public void guardarHistorial(List<String> historial) {
        guardar(historial); // Llama al template method
    }

    @Override
    public List<String> recuperarHistorial() {
        return recuperar(); // Llama al template method
    }

    @Override
    protected String formatearDatos(List<String> datos) {
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
    protected List<String> parsearDatos(String contenido) {
        List<String> lista = new ArrayList<>();
        String limpia = contenido.replace("[", "").replace("]", "").replace("\"", "").trim();
        
        if (!limpia.isEmpty()) {
            for (String item : limpia.split(",")) {
                lista.add(item.trim());
            }
        }
        return lista;
    }

    @Override
    protected List<String> obtenerObjetoVacio() {
        return new ArrayList<>();
    }
}
