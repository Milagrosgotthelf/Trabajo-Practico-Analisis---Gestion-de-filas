package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
public class ControladorEmpleado implements ActionListener{
	
	private Empleado empleado = null;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="", estadoCola="";
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
			
			try {
				this.empleado.setNumeroDePuesto(Integer.parseInt(nroPuesto));
				ventanaLlamadaDefecto();
				pedirSigCliente();
				
			} catch (BindException e) {
				this.vistaEmpleado.mostrarMensaje("Número de Puesto ocupado");
				this.vistaEmpleado.cleanTextField_numeroPuesto();
			}
		}
		else if (comando.equals("Llamar")) {
			if(clienteAtendido)
				cicloLlamada();
			else {
				pedirSigCliente();
			}
				
			     
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			detenerTodosLosTimers();
			ventanaLlamadaDefecto();
			ventanaEstado();
		}
	}
	
	
	private void cicloLlamada() {
		if (intentos>0 && clienteAtendido) {
			vistaEmpleado.activarBtnLlamar(false);
			String dni_llamar = this.dniActual_emp;
			this.vistaEmpleado.notificarLlamada(4-intentos);
			
			this.empleado.enviarCliente_Server(this.dniActual_emp);
	        rellamarCliente(); 

	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
	            if (clienteAtendido && this.dniActual_emp != "-" && this.dniActual_emp==dni_llamar) {
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
		this.proxdni = dniActual_emp;
        vistaEmpleado.setProximoDni(this.proxdni);
        intentos = 3;
        clienteAtendido = true;
        vistaEmpleado.setLabelsVisibles(true);
        vistaEmpleado.setIntentos(intentos);
        vistaEmpleado.activarBtnLlamar(true);
        vistaEmpleado.activarBtnIniciarTurno(false);
        vistaEmpleado.mostrarPantalla("Llamada");
	}	
	private void ventanaLlamadaDefecto() {
		this.proxdni = "-";
		this.clienteAtendido = false;
	    intentos = 0;
	    this.estadoCola = "HAY_CLIENTES";
	    vistaEmpleado.setIntentos(intentos);
	    vistaEmpleado.setLabelsVisibles(false);         
	    vistaEmpleado.activarBtnLlamar(false);          
	    vistaEmpleado.activarBtnIniciarTurno(false);
	    vistaEmpleado.mostrarPantalla("Llamada");
	}
	
	private void rellamarCliente() {
		intentos--;
		vistaEmpleado.setIntentos(intentos);
		vistaEmpleado.activarBtnIniciarTurno(true); 
	}
	
	private void iniciarTurno() {
        vistaEmpleado.setDniActual(this.proxdni);
        vistaEmpleado.mostrarPantalla("Atencion");
        clienteAtendido = true;
	}

	private void ventanaEstado() {
		if (this.estadoCola.equals("LISTA_VACIA")) {
			vistaEmpleado.actualizarEstadoEspera(false);
	        pedirSigCliente();
	    } 
	    else if (this.estadoCola.equals("HAY_CLIENTES") && (intentos<3)) {
	    	vistaEmpleado.actualizarEstadoEspera(true);
	    	vistaEmpleado.activarBtnLlamar(true);
	    	
	    } 
	}

	private void pedirSigCliente() {
		Thread hiloEscucha = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                	String aux = empleado.llamarCliente();
	                	System.out.println("AUX"+aux);
	                	if(aux.equals("LISTA_VACIA") || aux.equals("HAY_CLIENTES")) {
	                		estadoCola = aux;
	                		ventanaEstado();
	                	}
	                	else {
	                		dniActual_emp = aux;
	                		mostrarSigCliente();
	                	}
	                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	                        @Override
	                        public void run() {
	                            ;
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
