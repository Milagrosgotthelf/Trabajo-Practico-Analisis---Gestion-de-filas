package modelo;

import static modelo.Utils.IP;

import java.io.PrintWriter;
import java.net.Socket;

public class Emisor {
	Socket socket = null;
	
	
	/*
	 * Este metodo se encarga de enviar el DNI del cliente al receptor
	 * Habra que crear otro con parametro tipo cliente en un futuro
	 * @param cliente
	 */
	public void enviar(String cliente, String puerto) {
		try {
			
            this.socket = new Socket(IP,Integer.parseInt(puerto));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(cliente);
            //Esta linea envia el DNI al receptor
            out.close();
            socket.close();
            Thread.sleep(30);
        } catch (Exception e) {
        	System.out.println("Excepcion en el emisor: " + puerto + " " + e.getMessage());
            //e.printStackTrace(); 
        }
		
	}

}
