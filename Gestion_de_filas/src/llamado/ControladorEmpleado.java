package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;

import sfd.Utils;
public class ControladorEmpleado implements ActionListener{
	
	private Empleado empleado = null;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="";
	private boolean clienteAtendido = false;
	private java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();
	private String estadoActual = "";
	
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
				pedirEstado();
			} catch (BindException e) {
				this.vistaEmpleado.mostrarMensaje("Número de Puesto ocupado");
				this.vistaEmpleado.cleanTextField_numeroPuesto();
				
			}
		}
		else if (comando.equals("Llamar")) {
			pedirSigCliente();
			     
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			detenerTodosLosTimers();
			ventanaLlamadaDefecto();
			pedirEstado();
			
		}
	}
	
	private void info() {
		System.out.println("INTENTOS: " + intentos);
		System.out.println("PROX DNI: " + proxdni);
		System.out.println("dniActual_emp: " + dniActual_emp);
	}
	private void cicloLlamada() {
		if (intentos>0 && !clienteAtendido) {
			vistaEmpleado.activarBtnLlamar(false);
			info();
			String dni_llamar = this.proxdni;
			this.vistaEmpleado.notificarLlamada(4-intentos);
			this.empleado.enviarCliente_Server(dni_llamar);
	        rellamarCliente(); 

	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
	            if (!clienteAtendido && this.proxdni != "-" && this.proxdni==dni_llamar) {
	                cicloLlamada(); 
	            }
	        });
	        timerReintento.setRepeats(false);
	        timers.add(timerReintento);
	        timerReintento.start();
	    } else if (intentos<=0 && !clienteAtendido) {
	    	vistaEmpleado.activarBtnLlamar(true);
	    }
	}
	
		
	private void mostrarSigCliente(){
    	this.proxdni = dniActual_emp;
    	vistaEmpleado.setProximoDni(this.proxdni);
        intentos = 3;
        vistaEmpleado.setIntentos(intentos);
        vistaEmpleado.activarBtnLlamar(true);
        vistaEmpleado.activarBtnIniciarTurno(false);
        vistaEmpleado.mostrarPantalla("Llamada");
        cicloLlamada();
        
	}
	
	
	private void ventanaConsulta() {
		if(this.estadoActual.equals(Utils.FILA_VACIA)) {
			vistaEmpleado.actualizarEstadoEspera(false);
			pedirEstado();
		}
		else if (this.estadoActual.equals(Utils.HAY_CLIENTES)){
			vistaEmpleado.actualizarEstadoEspera(true);
		}
		
	}
	        
	private void ventanaLlamadaDefecto() {
		this.proxdni = "-";
	    intentos = 0;
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


	
	
	private synchronized void pedirSigCliente() {
		Thread hiloEscucha = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                	dniActual_emp = null;
	                	while (dniActual_emp == null || dniActual_emp.equals(Utils.FILA_VACIA) || dniActual_emp.equals(Utils.HAY_CLIENTES)) {
	                		dniActual_emp = empleado.llamarCliente();
	                		System.out.println("DNIACTUAL_EMP"+dniActual_emp);
	                	}
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
	
	private synchronized void pedirEstado() {
		Thread hiloEscuchaFila = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                	estadoActual = empleado.pedirEstado();
	                	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	                        @Override
	                        public void run() {
	                        	ventanaConsulta();
	                        }
	                    });
	                } catch (Exception e) {
	                    System.out.println("Error en pedirSigCliente: " + e.getMessage());
	                }
	        }
	    });
	    hiloEscuchaFila.setDaemon(true);
	    hiloEscuchaFila.start();
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
