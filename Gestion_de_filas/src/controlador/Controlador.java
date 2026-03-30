package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.*;
import vista.Ventana_empleado;
import vista.Ventana_terminal_registro;
public class Controlador implements ActionListener{
	
	private static Controlador instancia = null;
	private Empleado empleado = null;
	private TerminalRegistro terminal = null;
	private Pantalla pantalla = null;
	private Ventana_terminal_registro registro;
	private Ventana_empleado vistaEmpleado;
	private int intentos =3;
	private String dniActual_emp ="";
	private boolean clienteAtendido = false;
	private Controlador()  {
		
	}
	
	public static Controlador getInstance() {
		if(instancia == null) {
			instancia = new Controlador();
			instancia.terminal = TerminalRegistro.getInstance();
			instancia.empleado = new Empleado();
			instancia.pantalla = Pantalla.getInstance();
			instancia.empleado.escucharTerminal();
			instancia.pantalla.escucharEmpleado();
		}
		return instancia;	
	}
	

	public void setVistas(Ventana_terminal_registro reg, Ventana_empleado emp) {
	    this.registro = reg;
	    this.vistaEmpleado = emp;
	    this.registro.setActionListener(this);
	    this.vistaEmpleado.setActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		//Separo por ventanas
		if(esRegistro(comando)) {
			manejarRegistro(comando);
		}
		else if (esEmpleado(comando)) {
			manejarEmpleado(comando);
		}
		
	}
	
	
	
	
	//TERMINAL
	
	/*
	 * El controlador le manda a la terminal el dni para que lo agregue a su cola de clientes
	 */
	public void agregarCliente(String dni) {
		if (this.dniNoRepetido(dni)) {
			this.terminal.agregarCliente(new Cliente(dni));	
			this.registro.mostrarMensajeTemporal("   Usted ha sido registrado con exito.  ",155, 100, 240, 50);
		}
		else {
			System.out.println("DNI no valido");	
			this.registro.mostrarMensajeTemporal("   Usted ya se ha registrado.\nPor favor, aguarde pacientemente.  ", 85, 85, 400, 50);
		}
	}


	
	
	

	//REGISTRO
	private boolean esRegistro(String comando) {
		return comando.matches("\\d") || comando.equals("←") || comando.equals("Aceptar");
	}

	private void manejarRegistro(String comando) {
		String dniActual =registro.getDni();
		if (comando.matches("\\d")) { //busca si es igual a cualquier digito decimal
			dniActual += comando;
			registro.setDni(dniActual);
		}
		else if (comando.equals("←")) {
			if (dniActual.length()>0) {
				dniActual = dniActual.substring(0, dniActual.length()-1); //le saco el ultimo digito
				registro.setDni(dniActual);
			}
		}
		else if (comando.equals("Aceptar")) {
			agregarCliente(dniActual);
			registro.setDni("");
			
		}
		this.validarLongitud();
		
	}
	
	//Esto creo que es responsabilidad de la ventana
	private void validarLongitud() {
		int longitud = this.registro.getDni().length();
		if (longitud >=7 && longitud<=8) {
			this.registro.habilitarAceptar(true);
		} else {
			this.registro.habilitarAceptar(false);
		}
	}
	
	public boolean dniNoRepetido(String dni) {
		for (Cliente cliente : this.terminal.getClientes()) {
			if (cliente.getDni().equals(dni)) {
				return false; // El DNI ya existe en la lista de clientes
			}
		}
		return true; // El DNI no está repetido
	}

	
	
	
	
	//EMPLEADO
	private boolean esEmpleado(String comando) {
		return comando.equals("INICIAR")||comando.equals("Llamar")||
				comando.equals("Iniciar turno")||comando.equals("Finalizar turno");
	}
	
	private void manejarEmpleado(String comando) {
		if (comando.equals("INICIAR")){
			iniciarSistema();
		}
		else if (comando.equals("Llamar")) {
			rellamarCliente();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}
	
	/*COMUNICACION
	 * Es necesario que ambas funciones se ejecuten juntas
	 * Llamado le pide a la terminal que envie al cliente y le avisa al empleado que lo devuelva
	 * El empleado siempre esta escuchando a la terminal pero hay que pedirselo
	 * Despues hace otras cosas que se hacian siempre en conjunto, como el numero de intentos y demas
	 * Eventualmente hay que seguir una logica similar con la poantalla
	 */
	private void llamado() {
		intentos=3;
		this.terminal.enviarCliente();
		this.empleado.llamarCliente();
		//Al iniciar el sistema es necesario que la terminal envie el cliente para que el cliente la lea
		
		dniActual_emp = this.empleado.getDniActual();
		vistaEmpleado.setProximoDni(dniActual_emp);
		vistaEmpleado.setIntentos(intentos);
		
		vistaEmpleado.activarBtnIniciarTurno();
		
		/*if (terminal.getClientes().isEmpty()) {
			vistaEmpleado.mostrarMensajeTemporal("No hay mas clientes en la cola.", 110, 100, 120, 50);
		}*/
	}
	
	
	/*
	 * IniciarSistema y finalizarTurno son IGUALES pero dejalas asi, seguro para la iteracion que viene son distintas
	 * Sino se cambia
	 */
	private void iniciarSistema() {
		
		if (!terminal.getClientes().isEmpty()) {
			this.llamado();
			vistaEmpleado.mostrarPantalla("Llamada");
		}
		else
			System.out.println("No hay clientes en la cola");
	}

	private void llamarSiguiente() {
		
		if (!terminal.getClientes().isEmpty())
			this.llamado();
		else
			System.out.println("Cola de clientes vacia");
	}
	
	private void verSiEsAusente() {
		clienteAtendido = false;
		javax.swing.Timer timer = new javax.swing.Timer(6000, e -> { // NO SE SI NO SON MUCHOS SEGUNDOS O POCOS 
	        if (!clienteAtendido) {
	        	llamarSiguiente();
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
		}
		else{
			//llamarSiguiente();
			//vistaEmpleado.mostrarPantalla("Llamada");
			intentos--;
			vistaEmpleado.setIntentos(0);
			verSiEsAusente();
		}
	}

	private void iniciarTurno() {
		vistaEmpleado.setDniActual(dniActual_emp);
		vistaEmpleado.mostrarPantalla("Atencion");
		this.clienteAtendido = true;
	}

	
	private void finalizarTurno() {
		if (!terminal.getClientes().isEmpty()) {
			this.llamado();
			vistaEmpleado.mostrarPantalla("Llamada");
			this.clienteAtendido = false;
		}
		else
			System.out.println("No hay clientes en la cola");
	}
	/*private void finalizarTurno() {
		this.llamado();
		vistaEmpleado.mostrarPantalla("Llamada");
		this.clienteAtendido = false;
		if (terminal.getClientes().isEmpty()) {
			vistaEmpleado.mostrarMensajeTemporal("No hay mas clientes en la cola.", 110, 100, 120, 50);
		}
	}*/

}
