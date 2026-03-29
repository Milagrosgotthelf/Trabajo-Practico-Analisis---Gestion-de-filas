package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receptor implements Runnable{
	private String mensaje;
	private String puerto;
	private Socket soc = null;
	private ServerSocket s = null;
	
	public Receptor(String puerto) {
		this.puerto = puerto;
		try {
			this.s = new ServerSocket(Integer.parseInt(puerto));
			System.out.println("21 Constructor Receptor \n Receptor iniciado en el puerto " + puerto);
		} catch (Exception e) {
			System.out.println("Excepcion al iniciar el receptor: " + e.getMessage());
			e.printStackTrace();
			}
	}
	
	public void recibir() {
        Thread hilo = new Thread(this);
        hilo.start();
	}
	
	
	@Override
    public void run() {
        try {

            while (true) {

                this.soc = this.s.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                this.mensaje = in.readLine();
                System.out.println(this.mensaje);
                soc.close();
                in.close();
                Thread.sleep(30);
                
            }

        } catch (Exception e) {
        	System.out.println("Excepcion en el receptor: " + e.getMessage());
            e.printStackTrace();
        }

    }

	public synchronized String getMensaje() {
        return mensaje;
    }

}
