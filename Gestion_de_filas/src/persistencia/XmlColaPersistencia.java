package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.IPersistencia.ColaPersistencia;

public class XmlColaPersistencia extends TPersistencia <List<String> >implements ColaPersistencia {

	public XmlColaPersistencia() {
        super("datos/cola_espera.xml");
    }

    @Override
    public void guardarCola(List<String> dniClientes) {
        guardar(dniClientes); // Llama al template method
    }

    @Override
    public List<String> recuperarCola() {
        return recuperar(); // Llama al template method
    }

    @Override
    protected String formatearDatos(List<String> datos) {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<cola>\n");
        for (String dni : datos) {
            xml.append("  <dni>").append(dni).append("</dni>\n");
        }
        xml.append("</cola>");
        return xml.toString();
    }

    @Override
    protected List<String> parsearDatos(String contenido) {
        List<String> lista = new ArrayList<>();
        
        // Parseo manual básico para evitar dependencias externas (como DOM o SAX)
        // Separa el string cada vez que encuentra la etiqueta de apertura
        String[] lineas = contenido.split("<dni>");
        
        // Empieza en 1 para saltearse la cabecera XML y la etiqueta <cola>
        for (int i = 1; i < lineas.length; i++) { 
            int finEtiqueta = lineas[i].indexOf("</dni>");
            if (finEtiqueta != -1) {
                // Extrae solo el número de DNI
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
