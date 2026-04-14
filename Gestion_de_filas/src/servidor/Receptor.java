package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Receptor implements Runnable {
    private String mensaje = null;
    private ServerSocket s = null;
    
    public Receptor(String puerto) {
        try {
            this.s = new ServerSocket(Integer.parseInt(puerto));
            System.out.println("Receptor iniciado en el puerto " + puerto);
            
            Thread hiloEscucha = new Thread(this);
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();
        } catch (Exception e) {
            System.out.println("Excepcion al iniciar el receptor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (true) {
            try (Socket soc = this.s.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()))) {
                
                String leido;
                while ((leido = in.readLine()) != null) {  
                    if (leido != null) {
                        synchronized (this) {
                            this.mensaje = leido;
                            this.notifyAll();  
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Excepcion en el receptor: " + e.getMessage());
            }
        }
    }

    public synchronized String getMensaje() {
        while (this.mensaje == null) {
            try {
            	System.out.println("Waiting...");
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        String aux = this.mensaje;
        System.out.println("Receptor getMensaje " + aux);
        this.mensaje = null; 
        return aux;
    }
}