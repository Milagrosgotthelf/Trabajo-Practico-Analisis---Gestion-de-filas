package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class TPersistencia<T> {
	protected String rutaArchivo;

    public TPersistencia(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public final void guardar(T datos) {
        File archivo = new File(rutaArchivo);
        
        if (archivo.getParentFile() != null) {
            archivo.getParentFile().mkdirs(); 
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            String contenidoFormateado = formatearDatos(datos);
            writer.write(contenidoFormateado);
        } catch (IOException e) {
            System.err.println("Error crítico al persistir los datos: " + e.getMessage());
        }
    }

    public final T recuperar() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
        	return obtenerObjetoVacio();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            return parsearDatos(sb.toString());
        } catch (IOException e) {
            System.err.println("Error al recuperar los datos: " + e.getMessage());
        }
        catch (Exception e) {
			System.err.println("Error inesperado al recuperar los datos: " + e.getMessage());
		}
        return obtenerObjetoVacio();
    }

    // Pasos "gancho" (primitive operations) que las subclases deben implementar
    protected abstract String formatearDatos(T datos);
    protected abstract T parsearDatos(String contenido);
    protected abstract T obtenerObjetoVacio();

}
