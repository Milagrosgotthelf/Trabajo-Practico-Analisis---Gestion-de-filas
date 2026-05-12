package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.ConnectException;
public class ControladorEmpleado implements ActionListener{
	
	private Empleado empleado = null;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="", estadoCola="";
	private volatile boolean clienteAtendido = false;
	private boolean ventanaEstado = false;
	private java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();
	private final Object lockEstado = new Object();
	private final Object lockButton = new Object();
	
	private volatile boolean pidiendoCliente = false;
	
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
			
			try {
				this.empleado.enviarCliente_Server(nroPuesto);
			} catch (ConnectException e) {
				reconexionServer();
			}
			
			try {
				this.empleado.setNumeroDePuesto(Integer.parseInt(nroPuesto));
				ventanaLlamadaDefecto();
				//pedirSigCliente();
				pedirEstado();
				
			} catch (BindException e) {
				this.vistaEmpleado.mostrarMensaje("Número de Puesto ocupado");
				this.vistaEmpleado.cleanTextField_numeroPuesto();
			}
		}
		else if (comando.equals("Llamar")) {
			synchronized(lockButton) {
				if(!clienteAtendido && !pidiendoCliente) {
					pedirSigCliente();
					cicloLlamada();
				}
			}
				
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
			detenerTodosLosTimers();
		}
		else if (comando.equals("Finalizar turno")) {
			
			ventanaLlamadaDefecto();
			synchronized(lockEstado) {
				lockEstado.notifyAll();
			}
			ventanaEstado();
		}
	}
	
	private void cicloLlamada() {
		if (intentos>0) {
			vistaEmpleado.activarBtnLlamar(false);
			String dni_llamar = this.dniActual_emp;
			this.vistaEmpleado.notificarLlamada(4-intentos);
			
			try {
				this.empleado.enviarCliente_Server(this.dniActual_emp);
			} catch (ConnectException e) {
				System.out.println("EXCEPCION EN CICLO LLAMADA");
				reconexionServer();
			}
	        rellamarCliente(); 

	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
	            if (clienteAtendido && !this.dniActual_emp.equals("-") && this.dniActual_emp.equals(dni_llamar)) {
	                cicloLlamada(); 
	            }
	        });
	        timerReintento.setRepeats(false);
	        timers.add(timerReintento);
	        timerReintento.start();
	    } else if (intentos<=0) {
	    	vistaEmpleado.activarBtnLlamar(true);
	    	ventanaLlamadaDefecto(); 
	    	ventanaEstado();
	    }
	}

	private void mostrarSigCliente() {
		ventanaEstado = false;
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
		ventanaEstado = false;
		this.proxdni = "-";
		this.clienteAtendido = false;
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

	private void ventanaEstado() {
		
		if (this.estadoCola.equals("LISTA_VACIA")) {
			ventanaEstado = true;
			vistaEmpleado.actualizarEstadoEspera(false);
	    } 
	    else if (this.estadoCola.equals("HAY_CLIENTES") && (intentos<3)) {
	    	ventanaEstado = true;
	    	vistaEmpleado.actualizarEstadoEspera(true);
	    	vistaEmpleado.activarBtnLlamar(true);
	    	
	    } 
	}

	private void pedirSigCliente() {
	    synchronized (lockEstado) {
	        if (pidiendoCliente) return;
	        pidiendoCliente = true;
	    }

	    try {
	        String aux = empleado.llamarCliente();
	        if(!aux.equals("HAY_CLIENTES") && !aux.equals("LISTA_VACIA")) {
		        dniActual_emp = aux;
	            mostrarSigCliente();
	        }
	    } catch (ConnectException e) {
	        reconexionServer(); 
	    } finally {
	        synchronized (lockEstado) {
	            pidiendoCliente = false;
	            lockEstado.notifyAll(); 
	        }
	    }
	}
	
	private void pedirEstado() {
	    Thread hiloEstado = new Thread(() -> {
	        String auxAnt = "";
	        while (true) {
	            try {
	                synchronized (lockEstado) {
	                    while (pidiendoCliente || clienteAtendido) {
	                        lockEstado.wait(); 
	                    }
	                }
	                estadoCola = empleado.pedirEstado();
	                System.out.println(estadoCola);
	                if (!ventanaEstado || !estadoCola.equals(auxAnt)) {
	                    auxAnt = estadoCola;
	                    ventanaEstado();
	                }
	                Thread.sleep(1000);
	                //O: Este sleep existe porque a veces no daba el tiempo para detener este hilo para llamar
	                //Entiendo que alcanza para evitar fallos

	            } catch (ConnectException e) {
	                reconexionServer();
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                break;
	            }
	        }
	    });
	    hiloEstado.setDaemon(true);
	    hiloEstado.start();
	}
	
	private void detenerTodosLosTimers() {
		for (javax.swing.Timer timer : timers) {
			if (timer.isRunning()) {
				timer.stop();
			}
		}
		timers.clear();
	}

	public void reconexionServer() {
		this.vistaEmpleado.mostrarMensaje("Error de conexión con el servidor");
		this.empleado.cambiarConexion();
	}

}
