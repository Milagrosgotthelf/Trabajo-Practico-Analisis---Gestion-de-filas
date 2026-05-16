package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.ConnectException;

import sfd.Utils;
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
	
	public void enviarCliente_Server_Reintento(String msj) {
		int intentos=Utils.Intentos;
		while(intentos>0) {
			try {
				this.empleado.enviarCliente_Server(msj);
				return;
			} catch (ConnectException e) {
				intentos--;
				this.vistaEmpleado.mostrarMensaje("Reintentando conexión...");
				try {
				Thread.sleep(2000);}catch(InterruptedException e1) {}
				}
			}
		this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Cerrando terminal.");
		System.exit(-1);
	
	}
	
	private void manejarEmpleado(String comando) {
		
		if (comando.equals("INICIAR")){
			
			String nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			
			//
			this.enviarCliente_Server_Reintento(nroPuesto);
			//Fallo al enviar el mensaje por servidor caido
			//Deberia esperar y re intentar antes de cambiar la conexión.
			
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
			
			this.enviarCliente_Server_Reintento(this.dniActual_emp);
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

	        String aux = this.llamarCliente_reintento();
	        if(aux != null && !aux.equals("HAY_CLIENTES") && !aux.equals("LISTA_VACIA")) {
		        dniActual_emp = aux;
	            mostrarSigCliente();
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
	
	
	private String llamarCliente_reintento() {
		int intentos = Utils.Intentos;
		while(intentos>0) {
			try {
				return empleado.llamarCliente();
			} catch (ConnectException e) {
				intentos--;
			}
		}
		this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Cerrando terminal.");
		return null;
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
	                estadoCola = this.pedirEstado_reintento();
	                
	                if (estadoCola != null && (!ventanaEstado || !estadoCola.equals(auxAnt))) {
	                    auxAnt = estadoCola;
	                    ventanaEstado();
	                }
	                else if (estadoCola == null) {
	                	System.exit(-1);
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
	
	private String pedirEstado_reintento() throws InterruptedException {
		
		int intentos = Utils.Intentos;
			while(intentos>0) {
				try {
					return empleado.pedirEstado();
				}catch (ConnectException e) {
					intentos--;
					this.vistaEmpleado.mostrarMensaje("Reintentando conexión...");
					Thread.sleep(2000);
					
				}
			}
			this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Cerrando terminal.");
			return null;
			
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
