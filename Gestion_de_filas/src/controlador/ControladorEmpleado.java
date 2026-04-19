package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
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
			/*else if (intentos>0) { 
				this.empleado.enviarCliente_Server(this.dniActual_emp);
				rellamarCliente();
			}
			else {
				verSiEsAusente();
				pedirSigCliente();
				
			}*/
			else
				cicloLlamada();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}
	
	
	private void cicloLlamada() {
		// TODO Auto-generated method stub
		if (intentos>0 && !clienteAtendido) {
			vistaEmpleado.activarBtnLlamar(false);
			
			this.vistaEmpleado.notificarLlamada(4-intentos);
			this.empleado.enviarCliente_Server(this.dniActual_emp);
	        rellamarCliente(); //desciento intentos y actualzo la vista

	        //ACA ESTA LO NUEVO PARA VER SI SE PRESENTA
	        //MILI: no se si debería estar en el controlador o deberia encargarse el server
	        javax.swing.Timer timerReintento = new javax.swing.Timer(30000, e -> {
	            if (!clienteAtendido) {
	                cicloLlamada(); 
	            }
	        });
	        timerReintento.setRepeats(false);
	        timerReintento.start();
	    } else if (intentos<=0 && !clienteAtendido) {
	        //no mas intentos 
	    	vistaEmpleado.mostrarMensaje("Cliente ausente.");
	    	vistaEmpleado.activarBtnLlamar(true);
	    	pedirSigCliente();
	    }
	}

	private void mostrarSigCliente() {
			this.proxdni = dniActual_emp;
	        vistaEmpleado.setProximoDni(this.proxdni);
	        //VER> como ahora hay mas de un puesto de atencion y el proximo en la fila no es necesariamente el dni que va a 
	        //atender el empleado tal vez deberiamos tener un label para Estado: hay o no clientes en la cola y abajo otro label 
	        // que sea cliente llamado que empieza con "-" y toma el valor de dniActual cuando se presiona llamar por primera vez
	        /*if (!proxdni.equals("-"))
	        	vistaEmpleado.setProximoDni("Hay clientes esperando...");*/
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	        vistaEmpleado.mostrarPantalla("Llamada");
	    } 
	private void ventanaLlamadaDefecto() {
			this.proxdni = "-";
			this.clienteAtendido = false;
			vistaEmpleado.setProximoDni(this.proxdni);
			//vistaEmpleado.setProximoDni("No hay clientes por atender");
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
	    pedirSigCliente(); 
	}

	private void pedirSigCliente() {
		Thread hiloEscucha = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                    ventanaLlamadaDefecto();
	                    dniActual_emp = empleado.llamarCliente(); 
	                    System.out.println("ControladorEmpleado 135 " + dniActual_emp);
	                    if (dniActual_emp == null) {
	                        System.out.println("No se recibió un nuevo cliente. Volviendo a esperar...");    
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
}
