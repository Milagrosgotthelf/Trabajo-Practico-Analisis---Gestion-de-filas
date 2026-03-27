package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import static modelo.Utils.IP;
import static modelo.Utils.PUERTO;
public class TerminalRegistro {
	LinkedList<Cliente> clientes=null;
	
	private static TerminalRegistro instancia = null;
	
	private TerminalRegistro() {
		 clientes = new LinkedList<Cliente>();
		 //Usualmente se usa este tipo de lista y no ArrayList
		 //Es una lista enlazada que tiene metodos de colas
		
	}
	
	public static TerminalRegistro getInstance() {
		if(instancia == null) {
			instancia = new TerminalRegistro();
		}
		return instancia;
	}
	
	public void agregarCliente(Cliente cliente) {
		clientes.add(cliente);
	}
	
	public void enviarCliente() {
		if (clientes.isEmpty()) {
            System.out.println("No hay clientes en la cola para enviar.");
            return;
        }
		try {
			//IP no se cual podria ser pero puerto seria el del puesto de atencion
			System.out.println("Inicia emisor en puerto " + PUERTO);
            Socket socket = new Socket(IP,Integer.parseInt(PUERTO));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Cliente siguiente = this.clientes.pop();
            out.println(siguiente.getDni());
            //Esta linea envia el DNI al receptor
            out.close();
            socket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        
    }
		
}
