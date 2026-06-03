package persistencia;

import java.util.LinkedList;

import persistencia.IPersistencia.MonitorPersistencia;

public class XmlMonitorPersistencia extends TPersistencia<LinkedList<String>> implements MonitorPersistencia {

	public XmlMonitorPersistencia() {
        super("datos/historial_monitor.xml");
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
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<historial>\n");
        for (String llamado : datos) {
            xml.append("  <llamado>").append(llamado).append("</llamado>\n");
        }
        xml.append("</historial>");
        return xml.toString();
    }

    @Override
    protected LinkedList<String> parsearDatos(String contenido) {
    	LinkedList<String> lista = new LinkedList<>();
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
    protected LinkedList<String> obtenerObjetoVacio() {
        return new LinkedList<>();
    }

}
