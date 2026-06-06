package llamado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.BindException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import factory.IAbstractFactory;
import factory.JsonFactory;
import factory.TxtFactory;
import factory.XmlFactory;
import persistencia.IPersistencia.NotificacionPersistencia;
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
	
	private IAbstractFactory factory;
	private NotificacionPersistencia gestorPersistencia;
	
	public ControladorEmpleado()  {	
		this.empleado = new Empleado();
		
		if (Utils.Formato.toUpperCase().trim().equals("JSON"))
            factory = new JsonFactory();
        else if (Utils.Formato.toUpperCase().trim().equals("XML"))
            factory = new XmlFactory();
        else if (Utils.Formato.toUpperCase().trim().equals("TXT"))
            factory = new TxtFactory();
        else
        	throw new IllegalArgumentException("Formato no soportado: " + Utils.Formato);
		
		//this.gestorPersistencia = factory.crearNotificacionPersistencia();
	}
	
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
	    if (this.empleado.getNumeroDePuesto() != 0) {
	        try {
	            this.empleado.enviarDesconexion_Server("Desconectar");
	        } catch (ConnectException ex) {}
	    }
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
	
	public void enviarCliente_Server_Reintento(String msj) throws ConnectException {
		int intentos=Utils.Intentos;
		while(intentos>0) {
			try {
				//ACA EL MSJ ES EL DNI DEL CLIENTE QUE ESTA DESENCRIPTADO
				//EL SERVIDOR TIENE LA FILA CON LOS DNI ENCRIPTADOS POR LO QUE DEBEMOS ENCRIPTARLO ANTES DE ENVIARSELO EN EL METODO DE EMPLEADO
				this.empleado.enviarCliente_Server(msj);
				return;
			} catch (ConnectException e) {
				intentos--;
				//this.vistaEmpleado.mostrarMensaje("Reintentando conexión...");
				try {
				Thread.sleep(2000);}catch(InterruptedException e1) {}
				}
			}
		throw new ConnectException("No se pudo conectar al servidor después de varios intentos.");
		
	}
	
	private void manejarEmpleado(String comando) throws ConnectException {
		
		if (comando.equals("INICIAR")){
			
			String nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			try {
			//
			this.enviarCliente_Server_Reintento(nroPuesto);
			//Fallo al enviar el mensaje por servidor caido
			//Deberia esperar y re intentar antes de cambiar la conexión.
			
			
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
	
	private void cicloLlamada() { // Podés quitar el "throws ConnectException" de la firma
	    this.guardarReintentos();
	    
	    if (intentos > 0) {
	        vistaEmpleado.activarBtnLlamar(false);
	        String dni_llamar = this.dniActual_emp;
	        this.vistaEmpleado.notificarLlamada(4 - intentos);
	        
	        // Iniciamos el proceso de envío con manejo de reconexión asíncrono
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
	        // Intenta enviar el DNI al servidor (este método ya tiene sus propios intentos rápidos)
	        this.enviarCliente_Server_Reintento(this.dniActual_emp);
	        
	        // Si el envío es exitoso, continuamos con el flujo normal
	        rellamarCliente(); 

	        // Configuramos el timer de 30 segundos para la PRÓXIMA llamada de este mismo cliente
	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
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
	
	private void guardarReintentos() {
		System.out.println("Guardando cliente...");
		Map<String, Integer> clientes = new HashMap<String, Integer>();
		clientes.put(dniActual_emp, this.intentos);
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
	
	
	private String llamarCliente_reintento() {
		int intentos = Utils.Intentos;
		while(intentos>0) {
			try {
				return empleado.llamarCliente();
			} catch (ConnectException e) {
				intentos--;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Esperando...");
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
	
	private String pedirEstado_reintento() throws InterruptedException {
		
		int intentos = Utils.Intentos;
			while(intentos>0) {
				try {
					return empleado.pedirEstado();
				}catch (ConnectException e) {
					intentos--;
					Thread.sleep(2000);
					
				}
			}
			this.vistaEmpleado.mostrarMensaje("Reconexión fallida. Esperando...");
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
