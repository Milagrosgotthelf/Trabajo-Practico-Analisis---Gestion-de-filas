package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;
public class ControladorEmpleado implements ActionListener{
	
	private static ControladorEmpleado instancia = null;
	private Empleado empleado = null;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="";
	private boolean clienteAtendido = false;
	private boolean sistemaIniciado = false;
	
	private ControladorEmpleado()  {		
	}
	
	public static ControladorEmpleado getInstance() {
		if(instancia == null) {
			instancia = new ControladorEmpleado();
			instancia.empleado = new Empleado();
		}
		return instancia;	
	}
	

	public void setVistas(Ventana_empleado emp) {
	    this.vistaEmpleado = emp;
	    this.vistaEmpleado.setActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		manejarEmpleado(comando);
	}
	
	private void manejarEmpleado(String comando) {
		if (comando.equals("INICIAR")){
			sistemaIniciado = true;
			pedirSigCliente();
		}
		else if (comando.equals("Llamar")) {
			if (intentos == 3 && this.vistaEmpleado.getProximoDni() != null && this.vistaEmpleado.getProximoDni().equals("-"))
				mostrarSigCliente();
			else if (intentos>0) { 
				this.empleado.enviarCliente_pantalla(this.dniActual_emp);
				rellamarCliente();
			}
			else {
				verSiEsAusente();
				System.out.println("manejarEmpleado intentos=0");
				pedirSigCliente();
				
			}
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}
	
	
	
	private void mostrarSigCliente() {
			this.proxdni = dniActual_emp;
	        vistaEmpleado.setProximoDni(this.proxdni);
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	        vistaEmpleado.mostrarPantalla("Llamada");
	    } 
	private void ventanaLlamadaDefecto() {
			this.proxdni = "-";
			vistaEmpleado.setProximoDni(this.proxdni);
			intentos = 0;
			vistaEmpleado.setIntentos(intentos);
			vistaEmpleado.activarBtnLlamar(true);
			vistaEmpleado.activarBtnIniciarTurno(false);
			vistaEmpleado.mostrarMensaje("Aguarde...");
			sistemaIniciado = false;
			vistaEmpleado.mostrarPantalla("Llamada");	
		}
	
	private void verSiEsAusente() {
		clienteAtendido = false;
		javax.swing.Timer timer = new javax.swing.Timer(3000, e -> { // NO SE SI NO SON MUCHOS SEGUNDOS O POCOS||| Son 0.6 segundos, realmente deberian ser mas
	        if (!clienteAtendido) {
	            vistaEmpleado.mostrarPantalla("Llamada");
	        }
	    });
	    timer.setRepeats(false);
	    timer.start();
	}
	
	/*
	 * Rellamar tiene las mismas respponsabilidades que llamarCliente(), llamarCliente no es mas usada por ese motivo
	 * 
	 */
	private void rellamarCliente() {
		intentos--;
		vistaEmpleado.setIntentos(intentos);
		vistaEmpleado.activarBtnIniciarTurno(true); 
	}

	/*
	 * Iniciar turno no debe activar la comunicacion, se supone que todo lo que necesita lo tiene.
	 * Solo debe cambiar la ventana. Luego finalizar turno va a ocuparse de lo demas
	 */
	private void iniciarTurno() {
        vistaEmpleado.setDniActual(this.proxdni);
        vistaEmpleado.mostrarPantalla("Atencion");
        clienteAtendido = true;
	}


	private void finalizarTurno() {
	    vistaEmpleado.mostrarPantalla("Llamada");
	    vistaEmpleado.setDniActual("-");
	    System.out.println("ControladorEmpleado finalizarTurno");
	    pedirSigCliente(); 
	}

	private void pedirSigCliente() {
        Thread hiloEscucha = new Thread(new Runnable() {
            @Override
            public void run() {
            		ventanaLlamadaDefecto();
                	dniActual_emp = empleado.llamarCliente(); 
                	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    					@Override
    					public void run() {
    						mostrarSigCliente(); // Llamamos a la parte visual
    					}
    				});
            }
        });
        hiloEscucha.setDaemon(true);
        hiloEscucha.start();
    }
}
