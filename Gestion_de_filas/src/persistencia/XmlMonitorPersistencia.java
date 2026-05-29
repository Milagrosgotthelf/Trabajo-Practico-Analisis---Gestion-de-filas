package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.IPersistencia.MonitorPersistencia;

public class XmlMonitorPersistencia extends TPersistencia<List<String>> implements MonitorPersistencia {

	public XmlMonitorPersistencia() {
        super("datos/historial_monitor.xml");
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
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<historial>\n");
        for (String llamado : datos) {
            xml.append("  <llamado>").append(llamado).append("</llamado>\n");
        }
        xml.append("</historial>");
        return xml.toString();
    }

    @Override
    protected List<String> parsearDatos(String contenido) {
        List<String> lista = new ArrayList<>();
        String[] lineas = contenido.split("<llamado>");
        
        for (int i = 1; i < lineas.length; i++) { 
            int finEtiqueta = lineas[i].indexOf("</llamado>");
            if (finEtiqueta != -1) {
                lista.add(lineas[i].substring(0, finEtiqueta).trim());
            }
        }
        return lista;
    }

    @Override
    protected List<String> obtenerObjetoVacio() {
        return new ArrayList<>();
    }

}
