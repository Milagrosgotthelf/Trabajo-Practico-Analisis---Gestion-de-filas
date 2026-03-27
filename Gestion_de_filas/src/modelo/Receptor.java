package modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receptor {
	
	
	public void recibir(String puerto) {
		
		Runnable tareaServidor = new Runnable (){
			@Override
            public void run() {
                try {
                    ServerSocket s = new ServerSocket(Integer.parseInt(puerto));

                    while (true) {

                        Socket soc = s.accept();

                        
                        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

                        String msg = in.readLine();
                        System.out.println(msg);
                        soc.close();
                        
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage() + "\n");
                }
                System.out.println("fin");

            }
        };
        Thread hilo = new Thread(tareaServidor);
        System.out.println("Inicia hilo receptor");
        hilo.start();
	}

}
