package persistencia;

import java.util.LinkedList;

import persistencia.IPersistencia.ColaPersistencia;

public class JsonColaPersistencia extends TPersistencia<LinkedList<String>> implements ColaPersistencia {

    public JsonColaPersistencia() {
        super("datos/cola_espera.json");
    }

    @Override
    public void guardarCola(LinkedList<String> dniClientes) {
        guardar(dniClientes); // Llama al template method
    }

    @Override
    public LinkedList<String> recuperarCola() {
        return recuperar(); // Llama al template method
    }

    @Override
    protected String formatearDatos(LinkedList<String> datos) {
        // Ejemplo simplificado de mapeo a JSON manual o con Gson:
        // En una app real usarías: return new Gson().toJson(datos);
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
        // Lógica para limpiar corchetes y comillas (o usar librería de parseo)
        String limpia = contenido.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (!limpia.isEmpty()) {
            for (String dni : limpia.split(",\n?")) {
                lista.add(dni.trim());
            }
        }
        return lista;
    }

    @Override
    protected LinkedList<String> obtenerObjetoVacio() {
        return new LinkedList<>();
    }
}
