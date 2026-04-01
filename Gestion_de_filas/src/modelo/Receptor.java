package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            	//Thread.sleep(30);
                this.soc = this.s.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                synchronized (this) {
                    this.mensaje = in.readLine();
                    this.notifyAll(); // Avisa a getMensaje() que ya llegó el dato
                }
                //this.mensaje = in.readLine();
                //Thread.sleep(30);
                in.close();
                this.soc.close();
            }

        } catch (Exception e) {
        	System.out.println("Excepcion en el receptor: " + e.getMessage());
            //e.printStackTrace();
        }

    }

	public synchronized String getMensaje() {
        while (this.mensaje == null) {
            try {
                this.wait(); // Se queda "durmiendo" hasta que notifyAll() lo despierte
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String aux = this.mensaje;
        this.mensaje = null; // Lo reseteamos para la próxima recepción
        return aux;
    }

}
