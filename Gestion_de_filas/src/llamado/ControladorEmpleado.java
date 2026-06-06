package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import sfd.Utils;


public class ControladorEmpleado implements ActionListener{
	
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="", proxdni ="", estadoCola="";
	private volatile boolean clienteAtendido = false;
	private boolean ventanaEstado = false;
	private java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();
	private final Object lockEstado = new Object();
	private final Object lockButton = new Object();
	private FacadeEmpleado facadeEmp= new FacadeEmpleado();
	
	private volatile boolean pidiendoCliente = false;
	
	
	public ControladorEmpleado()  {}
	
	public void setVistas(Ventana_empleado emp) {
	    this.vistaEmpleado = emp;
	    this.vistaEmpleado.setActionListener(this);
	    
	    // Capturamos el evento de la "X"
	    this.vistaEmpleado.setWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent e) {
	            cerrarTerminal();
	        }
	    });
	}

	private void cerrarTerminal() {
	    try {
	        facadeEmp.desconectar();
	        } catch (ConnectException ex) {}
	    
	    System.exit(0);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		try {
			manejarEmpleado(comando);
		} catch(ConnectException ex) {
			this.vistaEmpleado.mostrarMensaje(ex.getMessage());
		}
	}
	
	
	
	private void manejarEmpleado(String comando) throws ConnectException {
		
		if (comando.equals("INICIAR")){
			
			String nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			try {
				//
				facadeEmp.iniciarPuesto(nroPuesto);
				ventanaLlamadaDefecto();
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
	    
	    if (intentos > 0) {
	        vistaEmpleado.activarBtnLlamar(false);
	        String dni_llamar = this.dniActual_emp;
	        this.vistaEmpleado.notificarLlamada(4 - intentos);
	        
	        ejecutarEnvioConReintentos(dni_llamar);

	    } else if (intentos <= 0) {
	        if (!this.proxdni.equals("-")) 
	            vistaEmpleado.mostrarMensaje("El cliente no se ha presentado tras 3 llamados...");
	        vistaEmpleado.activarBtnLlamar(true);
	        ventanaLlamadaDefecto(); 
	        synchronized(lockEstado) {
	            lockEstado.notifyAll();
	        }
	        ventanaEstado();
	    }
	}
	private void ejecutarEnvioConReintentos(String dni_llamar) {
	    try {
	        facadeEmp.llamarCliente(this.dniActual_emp);
	        
	        rellamarCliente(); 

	        javax.swing.Timer timerReintento = new javax.swing.Timer(Utils.TiempoRellamado, e -> {
	            if (clienteAtendido && !this.dniActual_emp.equals("-") && this.dniActual_emp.equals(dni_llamar)) {
	                cicloLlamada();
	            }
	        });
	        timerReintento.setRepeats(false);
	        timers.add(timerReintento);
	        timerReintento.start();

	    } catch (ConnectException e) {
	        // Si falla la conexión, mostramos un aviso y esperamos 5 segundos usando un Timer
	        this.vistaEmpleado.mostrarMensaje("Fallo de conexión al servidor. Reintentando envío en 5 segundos...");
	        
	        javax.swing.Timer timerEsperaConexion = new javax.swing.Timer(5000, evt -> {
	            ejecutarEnvioConReintentos(dni_llamar); // Llamada recursiva tras la pausa
	        });
	        timerEsperaConexion.setRepeats(false);
	        timers.add(timerEsperaConexion);
	        timerEsperaConexion.start();
	    }
	}

	private void mostrarSigCliente(String dni) {
		ventanaEstado = false;
		this.proxdni = dni;
        vistaEmpleado.setProximoDni(this.proxdni);
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

	        String aux = facadeEmp.obtenerSiguienteCliente();
	        if(aux != null && !aux.equals("HAY_CLIENTES") && !aux.equals("LISTA_VACIA")) {
	        	if(aux.split("/").length > 1) {
	        		dniActual_emp = aux.split("/")[0];
	        		intentos = Integer.parseInt(aux.split("/")[1]);
	        	}
	        	else {
			        dniActual_emp = aux;
			        intentos = 3;
	        	}
	        	mostrarSigCliente(dniActual_emp);
	        }
	        else if(aux == null) {
	        	
	        	this.vistaEmpleado.mostrarMensaje("Fallo de conexión con el servidor. Abortando solicitud...");
	        }
	        synchronized (lockEstado) {
	            pidiendoCliente = false;
	            lockEstado.notifyAll(); 
	    }
	    //Si falla desbloquea el hilo para que pueda solicitar estado 
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
	                estadoCola = facadeEmp.obtenerEstadoCola();
	                
	                if (estadoCola != null && (!ventanaEstado || !estadoCola.equals(auxAnt))) {
	                    auxAnt = estadoCola;
	                    ventanaEstado();
	                }
	                else if (estadoCola == null) {
	                	this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Esperando...");
	                	Thread.sleep(10000);
	                }
	                Thread.sleep(1000);
	                //O: Este sleep existe porque a veces no daba el tiempo para detener este hilo para llamar
	                //Entiendo que alcanza para evitar fallos

	            }  catch (InterruptedException e) {
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


}
