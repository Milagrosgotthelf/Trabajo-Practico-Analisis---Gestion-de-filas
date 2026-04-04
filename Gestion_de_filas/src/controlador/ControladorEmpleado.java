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
			sistemaIniciado = true;
			mostrarSigCliente();
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

	//COMUNICACION
	private void mostrarSigCliente() {
		String mensaje = this.empleado.recibirMensaje();
		System.out.println("ControladorEmpleado 75: " + mensaje);
		
		if (mensaje != null) {
		    	
	        vistaEmpleado.setProximoDni(mensaje);
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	        vistaEmpleado.mostrarPantalla("Llamada");
	    } 
		else {
			vistaEmpleado.setProximoDni("-");
			vistaEmpleado.setIntentos(0);
			vistaEmpleado.activarBtnLlamar(false);
			vistaEmpleado.activarBtnIniciarTurno(false);
			vistaEmpleado.mostrarMensaje("No hay clientes por atender. Aguarde.");
			sistemaIniciado = false;
			vistaEmpleado.mostrarPantalla("Inicio");
		}
		
	}	
	
	private void verSiEsAusente() {
		clienteAtendido = false;
		javax.swing.Timer timer = new javax.swing.Timer(3000, e -> { // NO SE SI NO SON MUCHOS SEGUNDOS O POCOS||| Son 0.6 segundos, realmente deberian ser mas
	        if (!clienteAtendido) {
	        	mostrarSigCliente(); 
	            vistaEmpleado.mostrarPantalla("Llamada");
	        }
	    });
	    timer.setRepeats(false);
	    timer.start();
	}
	
	private void rellamarCliente() {
		intentos--;
		vistaEmpleado.setIntentos(intentos);
		vistaEmpleado.activarBtnIniciarTurno(true); //O: Sujeto a cambios
		if (intentos == 0)
			verSiEsAusente();
	}

	private void iniciarTurno() {
        this.empleado.llamarCliente();
        dniActual_emp = this.empleado.getDniActual();
        System.out.println("ControladorEmpleado 122: " + dniActual_emp);
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
