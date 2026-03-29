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
	public void agregarCliente(String dni) {
		if (this.validacionDNI(dni)) {
			this.terminal.agregarCliente(new Cliente(dni));			
		}
		else {
			System.out.println("DNI no valido");
			
		}
		TerminalRegistro.getInstance().agregarCliente(new Cliente(dni));
	}

	public boolean validacionDNI(String dni) {
		return this.dniNoRepetido(dni);
	}
	public boolean dniNoRepetido(String dni) {
		for (Cliente cliente : this.terminal.getClientes()) {
			if (cliente.getDni().equals(dni)) {
				return false; // El DNI ya existe en la lista de clientes
			}
		}
		return true; // El DNI no está repetido
	}
	public void llamarCliente() {
		this.empleado.llamarCliente();
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
			if (validacionDNI(dniActual)) {
				agregarCliente(dniActual);
				registro.setDni("");
			} else {
				System.out.println("DNI repetido");
			}
		}
		this.validarLongitud();
		
	}
	
	private void validarLongitud() {
		int longitud = this.registro.getDni().length();
		if (longitud >=7 && longitud<=8) {
			this.registro.habilitarAceptar(true);
		} else {
			this.registro.habilitarAceptar(false);
		}
	}
	
	private boolean esEmpleado(String comando) {
		return comando.equals("INICIAR")||comando.equals("Llamar")||
				comando.equals("Iniciar turno")||comando.equals("Finalizar turno");
	}
	private void manejarEmpleado(String comando) {
		if (comando.equals("INICIAR")){
			iniciarSistema();
		}
		else if (comando.equals("Llamar")) {
			llamarSiguiente();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}

	private void iniciarSistema() {
		intentos=3;
		if (!terminal.getClientes().isEmpty()) {
			dniActual_emp = terminal.getClientes().get(0).getDni();
			vistaEmpleado.setProximoDni(dniActual_emp);
			vistaEmpleado.setIntentos(intentos);
			vistaEmpleado.mostrarPantalla("Llamada");
		}
	}

	private void llamarSiguiente() {
		intentos--;
		vistaEmpleado.setIntentos(intentos);
		if (intentos==0) {
			llamarCliente();
			intentos=3;
			if (!terminal.getClientes().isEmpty()) {
				dniActual_emp = terminal.getClientes().get(0).getDni();
				vistaEmpleado.setProximoDni(dniActual_emp);
				vistaEmpleado.setIntentos(intentos);
			}
		}
		
	}

	private void iniciarTurno() {
		vistaEmpleado.setDniActual(dniActual_emp);
		vistaEmpleado.mostrarPantalla("Atencion");
	}

	private void finalizarTurno() {
		llamarCliente();
		intentos=3;
		if (!terminal.getClientes().isEmpty()) {
			dniActual_emp = terminal.getClientes().get(0).getDni();
			vistaEmpleado.setProximoDni(dniActual_emp);
			vistaEmpleado.setIntentos(intentos);
		}
		vistaEmpleado.mostrarPantalla("Llamada");
	}
}
