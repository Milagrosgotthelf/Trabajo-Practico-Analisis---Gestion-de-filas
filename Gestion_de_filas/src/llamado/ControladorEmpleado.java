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
	private boolean clienteAtendido = false;
	private java.util.List<javax.swing.Timer> timers = new java.util.ArrayList<>();
	
	private boolean pidiendoCliente = false;
	
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
			if(!clienteAtendido) {
				pedirSigCliente();
				System.out.println(this.intentos);
				cicloLlamada();
			}
				
			     
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
			detenerTodosLosTimers();
		}
		else if (comando.equals("Finalizar turno")) {
			
			ventanaLlamadaDefecto();
			pedirEstado();
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
	    	pedirEstado();
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
	    } 
	    else if (this.estadoCola.equals("HAY_CLIENTES") && (intentos<3)) {
	    	vistaEmpleado.actualizarEstadoEspera(true);
	    	vistaEmpleado.activarBtnLlamar(true);
	    	
	    } 
	}

	private void pedirSigCliente() {
		while(!pidiendoCliente) {
    		pidiendoCliente = true;
            try {
            	String aux = empleado.llamarCliente();
            	if(!aux.equals("HAY_CLIENTES") && !aux.equals("LISTA_VACIA")){
            		dniActual_emp = aux;
            		pidiendoCliente = false;
            		mostrarSigCliente();
            		break;
            	}
        } catch (ConnectException e) {
            	reconexionServer();
            }
    	}
	}
	
	private void pedirEstado() {
		Thread hiloEstado = new Thread(new Runnable() {
			@Override
	        public void run() {
	        	String aux = null, auxAnt = null;
	        	while(aux == null || !aux.equals("HAY_CLIENTES")) {
	        		
	                try {
	                	aux = empleado.pedirEstado();
	                	System.out.println("PEDIRESTADO "+aux);
	                	estadoCola = aux;
	                	if(!aux.equals(auxAnt)) {
	                		auxAnt=aux;
	                		ventanaEstado();
	                	}
	                } catch (ConnectException e) {
	                	reconexionServer();
	                }
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
