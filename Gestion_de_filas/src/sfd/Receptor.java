package sfd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Receptor implements Runnable {
	private LinkedBlockingQueue<String> mensajes = new LinkedBlockingQueue<>();
	private ServerSocket s = null;

    public Receptor(String puerto) throws BindException {
        try {
            this.s = new ServerSocket(Integer.parseInt(puerto));

            Thread hiloEscucha = new Thread(this);
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();
        } catch (BindException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("Excepcion al iniciar el receptor: " + e.getMessage());
		}
    }

    @Override
    public void run() {
        while (true) {
            try (Socket soc = this.s.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()))) {

                String leido = in.readLine();
                if (leido != null) {
                	this.mensajes.put(leido);
                }
            } catch (Exception e) {
            	System.out.println("Excepcion en el receptor: " + e.getMessage()); 
            	if (e.getMessage().equals("Socket is closed")) {
					break; 
				}
            }
        }
    }

    public synchronized String getMensaje() {
            try {
                return this.mensajes.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
    }
        
    
    public String getHeartbeat(){     
        try {
            // poll() espera hasta 10 segundos por un mensaje, retorna null si no llega nada
            return this.mensajes.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    public void kill() {
		try {
			this.s.close();
			
		} catch (Exception e) {
			System.out.println("Excepcion al cerrar el receptor: " + e.getMessage());
			
		}
	}
    
}