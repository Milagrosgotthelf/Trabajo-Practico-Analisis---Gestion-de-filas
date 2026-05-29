package persistencia;

import java.util.LinkedList;

import persistencia.IPersistencia.ColaPersistencia;

public class TxtColaPersistencia extends TPersistencia<LinkedList<String>> implements ColaPersistencia {

    public TxtColaPersistencia() {
        super("datos/cola_espera.txt");
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
        // Une todos los elementos de la lista separados por un salto de línea
        return String.join("\n", datos);
    }

    @Override
    protected LinkedList<String> parsearDatos(String contenido) {
    	LinkedList<String> lista = new LinkedList<>();
        
        // Verifica que el archivo no esté vacío
        if (contenido != null && !contenido.trim().isEmpty()) {
            // Separa el contenido por saltos de línea
            String[] dnis = contenido.split("\n");
            for (String dni : dnis) {
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
