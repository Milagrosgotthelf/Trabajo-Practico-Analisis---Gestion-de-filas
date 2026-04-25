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
	
	public ControladorEmpleado(int id)  {	
		this.empleado = new Empleado(id);
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
        	ventanaLlamadaDefecto();

		}
		else if (comando.equals("Llamar")) {
			if (intentos == 3 && this.vistaEmpleado.getProximoDni() != null && this.vistaEmpleado.getProximoDni().equals("-"))
				pedirSigCliente();
			
			else
				cicloLlamada();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			ventanaLlamadaDefecto();
		}
	}
	
	
	private void cicloLlamada() {
		if (intentos>0 && !clienteAtendido) {
			vistaEmpleado.activarBtnLlamar(false);
			String dni_llamar = this.dniActual_emp;
			this.vistaEmpleado.notificarLlamada(4-intentos);
			this.empleado.enviarCliente_Server(this.dniActual_emp);
	        rellamarCliente(); //desciento intentos y actualzo la vista

	        //ACA ESTA LO NUEVO PARA VER SI SE PRESENTA
	        javax.swing.Timer timerReintento = new javax.swing.Timer(3000, e -> {
	            if (!clienteAtendido && this.dniActual_emp==dni_llamar) {
	                cicloLlamada(); 
	            }
	            else {
	            	System.out.println("Rellamado rechazado por dni ");
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
			vistaEmpleado.mostrarPantalla("Llamada");	
		}
	
	private void verSiEsAusente() {
		clienteAtendido = false;
		javax.swing.Timer timer = new javax.swing.Timer(300, e -> { 
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
	}

	private void pedirSigCliente() {
		Thread hiloEscucha = new Thread(new Runnable() {
	        @Override
	        public void run() {
	                try {
	                	dniActual_emp = empleado.llamarCliente(); 
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
