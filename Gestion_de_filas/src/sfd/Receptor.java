package sfd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receptor implements Runnable {
    private String mensaje = null;
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
                    synchronized (this) {
                        this.mensaje = leido;
                        this.notifyAll(); 
                    }
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
        while (this.mensaje == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        String aux = this.mensaje;
        this.mensaje = null; 
        return aux;
    }
    
    public void kill() {
		try {
			this.s.close();
			
		} catch (Exception e) {
			System.out.println("Excepcion al cerrar el receptor: " + e.getMessage());
			
		}
	}
    
}