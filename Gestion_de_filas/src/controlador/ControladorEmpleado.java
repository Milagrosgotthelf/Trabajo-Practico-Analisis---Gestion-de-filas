package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_pantalla;
import vista.Ventana_terminal_registro;
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
			iniciarSistema();
		}
		else if (comando.equals("Llamar")) {
			if (intentos == 3)
				llamadoCliente();
			else if (intentos>0) 
				rellamarCliente();
			else
				mostrarSigCliente();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}
	
	private void llamadoCliente() {
	    
	    
	    this.empleado.llamarCliente();
	    
	    
	    this.intentos--;
	    this.vistaEmpleado.setIntentos(intentos);
	    this.vistaEmpleado.activarBtnIniciarTurno(true);
	}

	/*COMUNICACION
	 * Es necesario que ambas funciones se ejecuten juntas
	 * Llamado le pide a la terminal que envie al cliente y le avisa al empleado que lo devuelva
	 * El empleado siempre esta escuchando a la terminal pero hay que pedirselo
	 * Despues hace otras cosas que se hacian siempre en conjunto, como el numero de intentos y demas
	 * Eventualmente hay que seguir una logica similar con la poantalla
	 */

	private void llamado() {
		
		
		String mensaje = this.empleado.recibirMensaje();
		System.out.println("ControladorEmpleado 86: " + mensaje);
		
		
		if (mensaje != null) {
		    	
	        vistaEmpleado.setProximoDni(mensaje);
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	    } 
	}
	
	
	/*
	 * IniciarSistema y finalizarTurno son IGUALES pero dejalas asi, seguro para la iteracion que viene son distintas
	 * Sino se cambia
	 */

	private void iniciarSistema() {
		sistemaIniciado = true;
		mostrarSigCliente();
	}
	
	private void mostrarSigCliente() {
		//dniActual_emp = null;
		System.out.println("ControladorEmpleado 112");
		this.llamado();
		vistaEmpleado.setProximoDni("-");
		vistaEmpleado.setIntentos(0);
		vistaEmpleado.activarBtnLlamar(false);
		vistaEmpleado.activarBtnIniciarTurno(false);
		vistaEmpleado.mostrarPantalla("Llamada");
		vistaEmpleado.mostrarMensaje("No hay clientes por atender. Aguarde.");
	}

	
	
	private void verSiEsAusente() {
		clienteAtendido = false;
		javax.swing.Timer timer = new javax.swing.Timer(3000, e -> { // NO SE SI NO SON MUCHOS SEGUNDOS O POCOS||| Son 0.6 segundos, realmente deberian ser mas
	        if (!clienteAtendido) {
	        	mostrarSigCliente(); //aca llama a llamado() y es AHI donde se reinician los intentos
	            vistaEmpleado.mostrarPantalla("Llamada");
	        }
	    });
	    timer.setRepeats(false);
	    timer.start();
	}
	
	private void rellamarCliente() {
		if (intentos > 1) {
			intentos--;
			vistaEmpleado.setIntentos(intentos);
			vistaEmpleado.activarBtnIniciarTurno(true);
		}
		else if (intentos == 1){
			intentos = 0;
			vistaEmpleado.setIntentos(intentos);
			vistaEmpleado.activarBtnIniciarTurno(true); //creo que tdv hay que dejarlo activo porque tiene oportunidad de presentarsee
			verSiEsAusente();
			
		}
	}

	private void iniciarTurno() {
        this.empleado.llamarCliente();
        dniActual_emp = this.empleado.getDniActual();
        //this.pantalla.escucharEmpleado();
        vistaEmpleado.setDniActual(dniActual_emp);
        vistaEmpleado.mostrarPantalla("Atencion");
        clienteAtendido = true;
	}


	private void finalizarTurno() {
	    vistaEmpleado.mostrarPantalla("Llamada");
	    vistaEmpleado.setDniActual("-");
	    mostrarSigCliente(); //actualizar el prox dni
	}

}
