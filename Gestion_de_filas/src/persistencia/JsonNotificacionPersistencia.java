package persistencia;

import java.util.HashMap;
import java.util.Map;

import persistencia.IPersistencia.NotificacionPersistencia;

public class JsonNotificacionPersistencia extends TPersistencia<Map<String, Integer>> implements NotificacionPersistencia {

    public JsonNotificacionPersistencia() {
        super("datos/re_notificaciones.json");
    }

    @Override
    public void guardarIntentos(Map<String, Integer> intentos) {
        guardar(intentos); // Llama al template method
    }

    @Override
    public Map<String, Integer> recuperarIntentos() {
        return recuperar(); // Llama al template method
    }

    @Override
    protected String formatearDatos(Map<String, Integer> datos) {
        StringBuilder json = new StringBuilder("{\n");
        int count = 0;
        
        // Itera sobre el mapa para armar los pares "clave": valor
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            json.append("  \"").append(entry.getKey()).append("\": ").append(entry.getValue());
            
            // Agrega coma si no es el último elemento
            if (count < datos.size() - 1) {
                json.append(",");
            }
            json.append("\n");
            count++;
        }
        json.append("}");
        return json.toString();
    }

    @Override
    protected Map<String, Integer> parsearDatos(String contenido) {
        Map<String, Integer> mapa = new HashMap<>();
        
        // Limpia las llaves del objeto JSON y las comillas de las claves
        String limpia = contenido.replace("{", "").replace("}", "").replace("\"", "").trim();
        
        if (!limpia.isEmpty()) {
            // Separa cada par clave-valor por la coma
            String[] pares = limpia.split(",");
            for (String par : pares) {
                // Separa la clave del valor usando los dos puntos
                String[] claveValor = par.split(":");
                if (claveValor.length == 2) {
                    String dni = claveValor[0].trim();
                    // Convierte el valor (String) a Integer
                    int intentos = Integer.parseInt(claveValor[1].trim());
                    mapa.put(dni, intentos);
                }
            }
        }
        return mapa;
    }

    @Override
    protected Map<String, Integer> obtenerObjetoVacio() {
        return new HashMap<>();
    }
}
