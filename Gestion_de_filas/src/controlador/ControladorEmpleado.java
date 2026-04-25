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
		String nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
		if (comando.equals("INICIAR")){
			nroPuesto=this.vistaEmpleado.getTextField_numeroPuesto();
			
			this.empleado.enviarNroPuesto_Server(nroPuesto);
			
			this.empleado.setNumeroDePuesto(Integer.parseInt(nroPuesto));
			ventanaLlamadaDefecto();
			pedirSigCliente();
		}
		else if (comando.equals("Llamar")) {
			if (intentos == 3 && this.vistaEmpleado.getProximoDni() != null && this.vistaEmpleado.getProximoDni().equals("-"))
				mostrarSigCliente();
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

	        javax.swing.Timer timerReintento = new javax.swing.Timer(3000, e -> {
	            if (!clienteAtendido && this.dniActual_emp==dni_llamar) {
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
			//vistaEmpleado.mostrarMensaje("Aguarde...");
			vistaEmpleado.mostrarPantalla("Llamada");	
		}
	
	
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
