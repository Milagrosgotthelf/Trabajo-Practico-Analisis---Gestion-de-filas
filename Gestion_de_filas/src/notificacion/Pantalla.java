package notificacion;

import java.net.BindException;
import java.util.LinkedList;
import java.util.List; // Agregamos este import

import seguridad.GestorSeguridad;
import sfd.Receptor;
import sfd.Utils;

public class Pantalla {
    
    private static Pantalla instancia = null;
    private Receptor receptor = null;
    private LinkedList<String> clientes = null;
    private GestorSeguridad gestorSeguridad = new GestorSeguridad();

    private Pantalla() throws BindException {
        this.clientes = new LinkedList<String>();
        this.receptor = new Receptor(Utils.Server_to_Pantalla);
        // Se inicializa con 5 lugares vacíos por defecto
        for (int i=0; i< 5; i++) {
            clientes.add("-");
        }
    }
    
    public static Pantalla getInstance() {
        if (instancia == null) {
            try {
                instancia = new Pantalla();
            } catch (BindException e) {
                e.printStackTrace();
            }
        }
        return instancia;
    }
    
    // MODIFICADO: Recibe List general y asegura que siempre queden 5 elementos
    public void setClientes(List<String> historialRecuperado) {
        this.clientes.clear();
        
        if (historialRecuperado != null) {
            for (String cliente : historialRecuperado) {
                if (this.clientes.size() < 5) {
                    this.clientes.add(cliente);
                }
            }
        }
        
        // Rellenamos con "-" si la persistencia trajo menos de 5 elementos
        while (this.clientes.size() < 5) {
            this.clientes.add("-");
        }
    }

    // MODIFICADO: Ahora devuelve boolean (true si la lista cambió, false si no)
    public boolean escucharEmpleado() {
        String mensajeRecibido = this.receptor.getMensaje();
        if (mensajeRecibido != null) {
            String mensajeRecibidoDesencriptado = gestorSeguridad.recuperarDNI(mensajeRecibido);

            // Si el cliente es el mismo que ya está primero, no hacemos nada
            if (!this.clientes.isEmpty() && mensajeRecibidoDesencriptado.equals(this.clientes.getFirst())) {
                return false; 
            }
            
            // Removemos el cliente si ya estaba en la lista (para subirlo al primer lugar)
            this.clientes.remove(mensajeRecibidoDesencriptado);
            this.clientes.addFirst(mensajeRecibidoDesencriptado);
            
            if (this.clientes.size() > 5) {
                this.clientes.removeLast();
            }
            
            return true; // Avisamos que hubo una actualización
        }
        return false;
    }

    public LinkedList<String> getClientes() {
        return clientes;
    }

    public void cerrarPantalla() {
        if (this.receptor != null) {
            this.receptor.kill(); 
        }
        instancia = null; 
    }
}