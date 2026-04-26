package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
public class ControladorEmpleado implements ActionListener{
	
	private Empleado empleado = null;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="";
	private boolean clienteAtendido = false;
	private java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();
	
	public ControladorEmpleado()  {	
		this.empleado = new Empleado();
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
			
			String nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			
			this.empleado.enviarCliente_Server(nroPuesto);
			
			this.empleado.setNumeroDePuesto(Integer.parseInt(nroPuesto));
			

			ventanaLlamadaDefecto();
			
		}
		else if (comando.equals("Llamar")) {
			vistaEmpleado.setLabelsVisibles(true);         
			cicloLlamada();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			detenerTodosLosTimers();
			ventanaLlamadaDefecto();
			
		}
	}
	
	
	private void cicloLlamada() {
		if (intentos>0 && !clienteAtendido) {
			vistaEmpleado.activarBtnLlamar(false);
			String dni_llamar = this.dniActual_emp;
			this.vistaEmpleado.notificarLlamada(4-intentos);
			
			this.empleado.enviarCliente_Server(this.dniActual_emp);
	        rellamarCliente(); 

	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
	            if (!clienteAtendido && this.dniActual_emp != "-" && this.dniActual_emp==dni_llamar) {
	                cicloLlamada(); 
	            }
	        });
	        timerReintento.setRepeats(false);
	        timers.add(timerReintento);
	        timerReintento.start();
	    } else if (intentos<=0 && !clienteAtendido) {
	    	vistaEmpleado.activarBtnLlamar(true);
	    	pedirSigCliente();
	    }
	}

	private void mostrarSigCliente() {
		if (this.dniActual_emp.equals("LISTA_VACIA")) {
			System.out.println("LISTA VACIA MOSTRARSIGCLIENTE");
	        vistaEmpleado.actualizarEstadoEspera(false);
	    } 
	    else if (this.dniActual_emp.equals("HAY_CLIENTES")) {
	    	vistaEmpleado.actualizarEstadoEspera(true);
	    } 
	    else {
	    	System.out.println("ELSE MOSTRARSIGCLIENTE");
	    	vistaEmpleado.actualizarEstadoEspera(true);
	    	this.proxdni = dniActual_emp;
	        vistaEmpleado.setProximoDni(this.proxdni);
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	        vistaEmpleado.mostrarPantalla("Llamada");
	        
	    }
	        
	    } 
	private void ventanaLlamadaDefecto() {
		vistaEmpleado.actualizarEstadoEspera(false);
		this.proxdni = "-";
	    this.clienteAtendido = false;
	    intentos = 0;
	    vistaEmpleado.setIntentos(intentos);
	    vistaEmpleado.setLabelsVisibles(false);         
	    vistaEmpleado.activarBtnLlamar(false);          
	    vistaEmpleado.activarBtnIniciarTurno(false);
	    vistaEmpleado.mostrarPantalla("Llamada");
	    pedirSigCliente();
	    
	    
	}
	
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


	private void pedirSigCliente() {
		//empleado.llamarCliente puede ser el que provoca que inicia y llama.
		Thread hiloEscucha = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                	dniActual_emp = empleado.llamarCliente(); 
	                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	                        @Override
	                        public void run() {
	                            mostrarSigCliente();
	                        }
	                    });
	                } catch (Exception e) {
	                    System.out.println("Error en pedirSigCliente: " + e.getMessage());
	                }
	        }
	    });
	    hiloEscucha.setDaemon(true);
	    hiloEscucha.start();
	}
	
	private void detenerTodosLosTimers() {
		for (javax.swing.Timer timer : timers) {
			if (timer.isRunning()) {
				timer.stop();
			}
		}
		timers.clear();
	}
}
