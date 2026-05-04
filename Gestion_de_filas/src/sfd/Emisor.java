package sfd;

import static sfd.Utils.IP;

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
			if (this.socket == null || this.socket.isClosed()) {
				this.socket = new Socket(IP,Integer.parseInt(puerto));
			}
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Thread.sleep(30);
            out.println(cliente);
            System.out.println(cliente + " enviado al puerto " + puerto);
            //Esta linea envia el DNI al receptor
            out.close();
            
        } catch (Exception e) {
        	System.out.println("Excepcion en el emisor: " + puerto + " " + e.getMessage());
        	e.printStackTrace();
            //e.printStackTrace(); 
        }
		
	}

}
