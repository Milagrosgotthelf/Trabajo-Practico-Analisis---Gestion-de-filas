package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.border.LineBorder;

import controlador.Controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class Ventana_empleado extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_inicial;
	private JPanel panel_llamada;
	private JPanel panel_atencion;
	private JPanel panel_izq;
	private JPanel panel_central;
	private JPanel panel_der;
	private JLabel lblNewLabel_1;
	private JPanel panel_5;
	private JPanel panel_7;
	private JPanel panel_8;
	private JLabel lbl_SISTEMA;
	private JPanel panel_6;
	private JButton btn_iniciar;
	private JPanel panel_sup;
	private JPanel panel_inf;
	private JButton btn_llamar;
	private JButton btn_iniciarturno;
	private JPanel panel_llamar;
	private JPanel panel_iniciar;
	private JLabel lbl_prox;
	private JLabel lbl_intentos;
	private JPanel panel_prox;
	private JPanel panel_int;
	private JLabel lbl_dniact;
	private JButton btn_fin;
	private JPanel panel_dniact;
	private JPanel panel_fin;
	private JLabel lblMensaje;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana_empleado frame = new Ventana_empleado();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ventana_empleado() {
		setTitle("Gestion de fila");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(550,300));
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new CardLayout(0, 0));
		
		this.panel_inicial = new JPanel();
		this.contentPane.add(this.panel_inicial, "Inicio");
		this.panel_inicial.setLayout(new GridLayout(0, 3, 0, 0));
		
		this.panel_izq = new JPanel();
		this.panel_izq.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.panel_inicial.add(this.panel_izq);
		
		this.panel_central = new JPanel();
		this.panel_central.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.panel_inicial.add(this.panel_central);
		this.panel_central.setLayout(new GridLayout(5, 3, 0, 0));
		
		this.panel_5 = new JPanel();
		this.panel_central.add(this.panel_5);
		
		this.panel_7 = new JPanel();
		this.panel_central.add(this.panel_7);
		this.panel_7.setLayout(new GridLayout(2, 2, 0, 0));
		
		this.lbl_SISTEMA = new JLabel("SISTEMA DE");
		this.lbl_SISTEMA.setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.panel_7.add(this.lbl_SISTEMA);
		
		this.lblNewLabel_1 = new JLabel("GESTIÓN DE FILA");
		this.panel_7.add(this.lblNewLabel_1);
		this.lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		this.panel_8 = new JPanel();
		this.panel_central.add(this.panel_8);
		
		this.panel_6 = new JPanel();
		this.panel_central.add(this.panel_6);
		
		this.btn_iniciar = new JButton("INICIAR");
		this.btn_iniciar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		this.panel_6.add(this.btn_iniciar);
		
		
		this.panel_der = new JPanel();
		this.panel_der.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.panel_inicial.add(this.panel_der);
		
		this.panel_llamada = new JPanel();
		this.contentPane.add(this.panel_llamada, "Llamada");
		this.panel_llamada.setLayout(new GridLayout(2, 0, 0, 0));
		
		this.panel_sup = new JPanel();
		this.panel_llamada.add(this.panel_sup);
		this.panel_sup.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.panel_prox = new JPanel();
		this.panel_sup.add(this.panel_prox);
		
		this.lbl_prox = new JLabel("Próximo DNI:");
		this.lbl_prox.setVerticalAlignment(SwingConstants.BOTTOM);
		this.panel_prox.add(this.lbl_prox);
		this.lbl_prox.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		this.panel_int = new JPanel();
		this.panel_sup.add(this.panel_int);
		
		this.lbl_intentos = new JLabel("Intentos pendientes: ");
		this.panel_int.add(this.lbl_intentos);
		this.lbl_intentos.setFont(new Font("Tahoma", Font.PLAIN, 25));
		
		this.panel_inf = new JPanel();
		this.panel_llamada.add(this.panel_inf);
		this.panel_inf.setLayout(new GridLayout(0, 2, 0, 0));
		
		this.panel_llamar = new JPanel();
		this.panel_inf.add(this.panel_llamar);
		this.panel_llamar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		this.btn_llamar = new JButton("Llamar");
		this.btn_llamar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.panel_llamar.add(this.btn_llamar);
		
		this.panel_iniciar = new JPanel();
		this.panel_inf.add(this.panel_iniciar);
		
		this.btn_iniciarturno = new JButton("Iniciar turno");
		this.btn_iniciarturno.setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.panel_iniciar.add(this.btn_iniciarturno);
		this.btn_iniciarturno.setEnabled(false);
		
		this.panel_atencion = new JPanel();
		this.contentPane.add(this.panel_atencion, "Atencion");
		this.panel_atencion.setLayout(new GridLayout(2, 0, 0, 0));
		
		this.panel_dniact = new JPanel();
		this.panel_atencion.add(this.panel_dniact);
		this.panel_dniact.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 50));
		
		this.lbl_dniact = new JLabel("DNI actual: ");
		this.panel_dniact.add(this.lbl_dniact);
		this.lbl_dniact.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		this.panel_fin = new JPanel();
		this.panel_atencion.add(this.panel_fin);
		
		this.btn_fin = new JButton("Finalizar turno");
		this.btn_fin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		this.panel_fin.add(this.btn_fin);
		
		//pongo solo uno asi lo podemos reutilizar unicamente cambiandole el mensaje
		this.lblMensaje = new JLabel("");
		this.lblMensaje.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.lblMensaje.setOpaque(true);
		this.lblMensaje.setBackground(Color.GRAY); //fondo oscuro
		this.lblMensaje.setForeground(Color.WHITE);
		this.lblMensaje.setVisible(false);
		this.contentPane.add(this.lblMensaje);
		this.contentPane.setComponentZOrder(lblMensaje, 0); //asi aparece sobre el teclado 
				

	}

	public void setActionListener(ActionListener actionListener) {
		this.btn_iniciar.addActionListener(actionListener);
		this.btn_llamar.addActionListener(actionListener);
		this.btn_iniciarturno.addActionListener(actionListener);
		this.btn_fin.addActionListener(actionListener);
		
	}
	public void mostrarPantalla (String nombre) {
		CardLayout cl = (CardLayout) contentPane.getLayout();
		cl.show(contentPane, nombre);
	}
	public void setIntentos(int intentos) {
		lbl_intentos.setText("Intentos pendientes: "+intentos);
	}
	public void setProximoDni(String dni) {
		lbl_prox.setText("Próximo DNI: "+dni);
	}
	public void setDniActual(String dni) {
		lbl_dniact.setText("DNI actual: "+dni);
	}

	public String getDniActual() {
		return lbl_dniact.getText();
	}
	
	public void mostrarMensaje(String mensaje) {
	    javax.swing.JOptionPane.showMessageDialog(
	            this,
	            mensaje,
	            "Información",
	            javax.swing.JOptionPane.INFORMATION_MESSAGE
	        );
	}

	public void activarBtnIniciarTurno() {
		this.btn_iniciarturno.setEnabled(true);
	}
}
