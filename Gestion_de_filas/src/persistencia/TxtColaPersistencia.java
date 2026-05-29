package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.IPersistencia.ColaPersistencia;

public class TxtColaPersistencia extends TPersistencia<List<String>> implements ColaPersistencia {

    public TxtColaPersistencia() {
        super("datos/cola_espera.txt");
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
        // Une todos los elementos de la lista separados por un salto de línea
        return String.join("\n", datos);
    }

    @Override
    protected List<String> parsearDatos(String contenido) {
        List<String> lista = new ArrayList<>();
        
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
    protected List<String> obtenerObjetoVacio() {
        return new ArrayList<>();
    }
}
