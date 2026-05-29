 package persistencia;

import java.util.HashMap;
import java.util.Map;

import persistencia.IPersistencia.NotificacionPersistencia;

public class XmlNotificacionPersistencia extends TPersistencia<Map<String, Integer>> implements NotificacionPersistencia {

	public XmlNotificacionPersistencia() {
        super("datos/re_notificaciones.xml");
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
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<notificaciones>\n");
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            xml.append("  <notificacion>\n");
            xml.append("    <dni>").append(entry.getKey()).append("</dni>\n");
            xml.append("    <intentos>").append(entry.getValue()).append("</intentos>\n");
            xml.append("  </notificacion>\n");
        }
        xml.append("</notificaciones>");
        return xml.toString();
    }

    @Override
    protected Map<String, Integer> parsearDatos(String contenido) {
        Map<String, Integer> mapa = new HashMap<>();
        String[] bloques = contenido.split("<notificacion>");
        
        for (int i = 1; i < bloques.length; i++) {
            String bloque = bloques[i];
            
            // Extraer DNI
            int inicioDni = bloque.indexOf("<dni>") + 5;
            int finDni = bloque.indexOf("</dni>");
            
            // Extraer Intentos
            int inicioIntentos = bloque.indexOf("<intentos>") + 10;
            int finIntentos = bloque.indexOf("</intentos>");
            
            if (inicioDni > 4 && finDni != -1 && inicioIntentos > 9 && finIntentos != -1) {
                String dni = bloque.substring(inicioDni, finDni).trim();
                int intentos = Integer.parseInt(bloque.substring(inicioIntentos, finIntentos).trim());
                mapa.put(dni, intentos);
            }
        }
        return mapa;
    }

    @Override
    protected Map<String, Integer> obtenerObjetoVacio() {
        return new HashMap<>();
    }

}
