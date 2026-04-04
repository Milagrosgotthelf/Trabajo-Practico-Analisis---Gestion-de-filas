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
		this.iniciarEscuchaClientes();
		
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
			if (intentos == 3 && this.vistaEmpleado.getProximoDni() != null && this.vistaEmpleado.getProximoDni().equals("-"))
				mostrarSigCliente();
			else if (intentos>0) 
				rellamarCliente();
		}
		else if (comando.equals("Iniciar turno")) {
			iniciarTurno();
		}
		else if (comando.equals("Finalizar turno")) {
			finalizarTurno();
		}
	}
	
	/*
	 * Llamado cliente tiene como unica tarea reducir el numero de intentos
	 */
	private void llamadoCliente() {
	    
	    this.intentos--;
	    this.vistaEmpleado.setIntentos(intentos);
	    this.vistaEmpleado.activarBtnIniciarTurno(true);
	}

	//COMUNICACION
	
	/*
	 * Invoca a empleado.llamarCliente() el cual envia un mensaje de confirmacion y devuelve el mensaje recibido
	 * MostrarSigclinte tiene la responsabilidad de avisarle a la terminal que envie al siguiente cliente
	 * Primero avisa y despues extrae al cliente, en el primer cliente va a haber un espacio nulo puesto que nunca se le dijo que lo envie
	 * mostrarSigCliente debe ser llamada unicamente cuando el cliente actual fue atendido o cuando se inicia el sistema
	 */
	private void mostrarSigCliente() {
		String mensaje = this.empleado.llamarCliente();
		System.out.println("ControladorEmpleado 83: " + mensaje);
		
		if (mensaje != null) {
		    
			this.proxdni = mensaje;
	        vistaEmpleado.setProximoDni(this.proxdni);
	        intentos = 3;
	        vistaEmpleado.setIntentos(intentos);
	        vistaEmpleado.activarBtnLlamar(true);
	        vistaEmpleado.activarBtnIniciarTurno(false);
	        vistaEmpleado.mostrarPantalla("Llamada");
	    } 
		else {
			this.proxdni = "-";
			vistaEmpleado.setProximoDni(this.proxdni);
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
	
	/*
	 * Rellamar tiene las mismas respponsabilidades que llamarCliente(), llamarCliente no es mas usada por ese motivo
	 * 
	 */
	private void rellamarCliente() {
		intentos--;
		vistaEmpleado.setIntentos(intentos);
		vistaEmpleado.activarBtnIniciarTurno(true); 
		if (intentos == 0)
			verSiEsAusente();
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
	    mostrarSigCliente(); 
	}

	
	private void iniciarEscuchaClientes() {
        Thread hiloEscucha = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                	dniActual_emp = empleado.llamarCliente();  
                    
                    if (dniActual_emp != null) {
                        System.out.println("ControladorEmpleado: Cliente recibido -> " + dniActual_emp);
                    }
                }
            }
        });
        hiloEscucha.setDaemon(true); // Para que se cierre si cierras la ventana
        hiloEscucha.start();
    }
}
