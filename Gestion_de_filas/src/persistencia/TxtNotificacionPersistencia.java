package persistencia;

import java.util.HashMap;
import java.util.Map;

import persistencia.IPersistencia.NotificacionPersistencia;

public class TxtNotificacionPersistencia extends TPersistencia<Map<String, Integer>> implements NotificacionPersistencia {

	public TxtNotificacionPersistencia() {
        super("datos/re_notificaciones.txt");
    }

    @Override
    public void guardarIntentos(Map<String, Integer> intentos) {
        guardar(intentos); 
    }

    @Override
    public Map<String, Integer> recuperarIntentos() {
        return recuperar(); 
    }

    @Override
    protected String formatearDatos(Map<String, Integer> datos) {
        StringBuilder txt = new StringBuilder();
        
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            txt.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        
        return txt.toString();
    }

    @Override
    protected Map<String, Integer> parsearDatos(String contenido) {
        Map<String, Integer> mapa = new HashMap<>();
        
        // Si el contenido está vacío, retornamos el mapa vacío directamente
        if (contenido == null || contenido.trim().isEmpty()) {
            return mapa;
        }

        String[] spliteado = contenido.split("\n");
        
        for(int i = 0; i < spliteado.length; i++) {
            String linea = spliteado[i].trim();
            
            // Solo procesamos la línea si tiene contenido
            if (!linea.isEmpty()) {
                String[] partes = linea.split(":");
                // Aseguramos que existan ambas partes (Clave y Valor) antes de asignar
                if (partes.length == 2) {
                    mapa.put(partes[0], Integer.parseInt(partes[1]));
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
