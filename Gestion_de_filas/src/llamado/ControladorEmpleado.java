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
	private int estadoAtencion = 0;
	
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
			if (this.estadoAtencion == 0)
				pedirSigCliente();
			else
				cicloLlamada();
			     
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			detenerTodosLosTimers();
			ventanaLlamadaDefecto();
			ventanaConsulta();
			
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

	        javax.swing.Timer timerReintento = new javax.swing.Timer(3000, e -> {
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
    	estadoAtencion = 1;
    	this.proxdni = dniActual_emp;
    	vistaEmpleado.setProximoDni(this.proxdni);
    	vistaEmpleado.setLabelsVisibles(true);
        intentos = 3;
        vistaEmpleado.setIntentos(intentos);
        vistaEmpleado.activarBtnLlamar(true);
        vistaEmpleado.activarBtnIniciarTurno(false);
        vistaEmpleado.mostrarPantalla("Llamada");
	}
	
	private void ventanaConsulta() {
		if(this.estadoActual.equals(Utils.FILA_VACIA)) {
			vistaEmpleado.actualizarEstadoEspera(false);
		}
		else if (this.estadoActual.equals(Utils.HAY_CLIENTES)){
			vistaEmpleado.actualizarEstadoEspera(true);
		}
		
	}
	        
	private void ventanaLlamadaDefecto() {
		estadoAtencion = 0;
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
<<<<<<< Updated upstream
	                try {
	                	System.out.println("PEDIRSIGCLIENTE");
                		dniActual_emp = empleado.llamarCliente();
	                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	                        @Override
	                        public void run() {
	                            mostrarSigCliente();
	                        }
	                    });
	                } catch (Exception e) {
	                    System.out.println("Error en pedirSigCliente: " + e.getMessage());
=======
	        	while(true) {
	        		
	                try {
	                	String aux = empleado.llamarCliente();
	                	if(aux.equals("LISTA_VACIA") || aux.equals("HAY_CLIENTES")) {
	                		estadoCola = aux;
	                		ventanaEstado();
	                		break;
	                	}
	                	else {
	                		dniActual_emp = aux;
	                		mostrarSigCliente();
	                		break;
	                	}
	                } catch (ConnectException e) {
	                	System.out.println("EXCEPCION EN PEDIR SIG");
	                	reconexionServer();
>>>>>>> Stashed changes
	                }
	        }
	    });
	    hiloEscucha.setDaemon(true);
	    hiloEscucha.start();
	}
	
<<<<<<< Updated upstream
	private synchronized void pedirEstado() {
		Thread hiloEscuchaFila = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	while(true) {
	                try {
	                	if(estadoAtencion != 1) {
	                		if (estadoActual.equals("") || estadoActual.equals(Utils.FILA_VACIA)) {
		                		estadoActual = empleado.pedirEstado();
			                	javax.swing.SwingUtilities.invokeLater(new Runnable() {
			                        @Override
			                        public void run() {
			                        	ventanaConsulta();
			                        }
			                    });
	                		}
	                		else 
	                			Thread.sleep(30);
	                	}
	                } catch (Exception e) {
	                    System.out.println("Error en pedirSigCliente: " + e.getMessage());
	                }
	        }
	       }
	    });
	    hiloEscuchaFila.setDaemon(true);
	    hiloEscuchaFila.start();
	}
=======
	
>>>>>>> Stashed changes
	
	private void detenerTodosLosTimers() {
		for (javax.swing.Timer timer : timers) {
			if (timer.isRunning()) {
				timer.stop();
			}
		}
		timers.clear();
	}
}
