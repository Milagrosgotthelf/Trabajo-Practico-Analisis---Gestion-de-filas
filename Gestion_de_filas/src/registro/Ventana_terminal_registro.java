package registro;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ventana_terminal_registro extends JFrame  {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblIngreseSuDni;
	private JTextField textFieldDNI;
	private JPanel panel_1;
	private JButton Button_1;
	private JButton Button_2;
	private JButton Button_5;
	private JButton Button_3;
	private JButton Button_4;
	private JButton Button_6;
	private JButton Button_7;
	private JButton Button_8;
	private JButton Button_9;
	private JPanel panel_2;
	private JButton Button_0;
	private JButton Button_back;
	private JButton Button_aceptar;
	private ActionListener actionListener;
	private JLabel lblMensaje; 
	private int numTerminal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana_terminal_registro frame = new Ventana_terminal_registro(1);
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
	public Ventana_terminal_registro(int id) {
		this.numTerminal = id;
		Color colorAtras;
		Color colorTeclas;
		setTitle("Terminal de Registro");
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 584, 350);
		this.contentPane = new JPanel();
		contentPane.setBackground(new Color(202, 243, 255));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		
		this.panel = new JPanel();
		panel.setBackground(new Color(202, 243, 255));
		this.panel.setBounds(10, 10, 550, 57);
		this.contentPane.add(this.panel);
		this.panel.setLayout(null);
		
		this.lblIngreseSuDni = new JLabel("Ingrese su DNI: ");
		this.lblIngreseSuDni.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.lblIngreseSuDni.setBounds(151, 8, 151, 47);
		this.panel.add(this.lblIngreseSuDni);
		
		this.textFieldDNI = new JTextField();
		textFieldDNI.setForeground(new Color(0, 160, 183));
		textFieldDNI.setBackground(new Color(202, 243, 255));
		textFieldDNI.setBorder(null);
		textFieldDNI.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.textFieldDNI.setBounds(312, 13, 184, 36);
		this.panel.add(this.textFieldDNI);
		this.textFieldDNI.setColumns(10);
		this.textFieldDNI.setEditable(false);
		
		this.panel_1 = new JPanel();
		panel_1.setBackground(new Color(225, 249, 255));
		this.panel_1.setBounds(188, 77, 196, 149);
		this.contentPane.add(this.panel_1);
		this.panel_1.setLayout(new GridLayout(4, 3, 0, 0));
		this.panel_1.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		colorTeclas = new Color(0, 216, 247);
		this.Button_1 = new JButton("1");
		Button_1.setBackground(new Color(0, 216, 247));
		Button_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_1.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		this.panel_1.add(this.Button_1);
		
		this.Button_2 = new JButton("2");
		this.panel_1.add(this.Button_2);
		Button_2.setBackground(colorTeclas);
		Button_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_2.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_3 = new JButton("3");
		this.panel_1.add(this.Button_3);
		Button_3.setBackground(colorTeclas);
		Button_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_3.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_4 = new JButton("4");
		this.panel_1.add(this.Button_4);
		Button_4.setBackground(colorTeclas);
		Button_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_4.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_5 = new JButton("5");
		this.panel_1.add(this.Button_5);
		Button_5.setBackground(colorTeclas);
		Button_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_5.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_6 = new JButton("6");
		this.panel_1.add(this.Button_6);
		Button_6.setBackground(colorTeclas);
		Button_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_6.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_7 = new JButton("7");
		this.panel_1.add(this.Button_7);
		Button_7.setBackground(colorTeclas);
		Button_7.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_7.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_8 = new JButton("8");
		this.panel_1.add(this.Button_8);
		Button_8.setBackground(colorTeclas);
		Button_8.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_8.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_9 = new JButton("9");
		this.panel_1.add(this.Button_9);
		Button_9.setBackground(colorTeclas);
		Button_9.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_9.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.panel_2 = new JPanel();
		panel_2.setBackground(new Color(225, 249, 255));
		this.panel_1.add(this.panel_2);
		
		this.Button_0 = new JButton("0");
		this.panel_1.add(this.Button_0);
		Button_0.setBackground(colorTeclas);
		Button_0.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_0.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_back = new JButton("←");
		Button_back.setBackground(new Color(221, 221, 221));
		this.panel_1.add(this.Button_back);
		colorAtras = Color.decode("#E3E8E8");
		this.Button_back.setBackground(colorAtras);
		Button_back.setFont(new Font("Tahoma", Font.BOLD, 14));
		this.Button_back.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.Button_aceptar = new JButton("Aceptar");
		Button_aceptar.setBackground(colorTeclas);
		this.Button_aceptar.setFont(new Font("SansSerif", Font.BOLD, 12));
		this.Button_aceptar.setBounds(242, 236, 84, 20);
		this.Button_aceptar.setEnabled(false);
		this.Button_aceptar.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		this.contentPane.add(this.Button_aceptar);
		
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
		this.actionListener = actionListener;
		this.Button_0.addActionListener(actionListener);
		this.Button_1.addActionListener(actionListener);
		this.Button_2.addActionListener(actionListener);
		this.Button_3.addActionListener(actionListener);
		this.Button_4.addActionListener(actionListener);
		this.Button_5.addActionListener(actionListener);
		this.Button_6.addActionListener(actionListener);
		this.Button_7.addActionListener(actionListener);
		this.Button_8.addActionListener(actionListener);
		this.Button_9.addActionListener(actionListener);
		this.Button_back.addActionListener(actionListener);
		this.Button_aceptar.addActionListener(actionListener);
	}
	
	public void habilitarAceptar (boolean habilitado) {
		this.Button_aceptar.setEnabled(habilitado);
	}

	public String getDni() {
		
		return textFieldDNI.getText();
	}

	public void setDni(String dniActual) {
		this.textFieldDNI.setText(dniActual);
	}

	public void mostrarMensajeTemporal(String mensaje, int x, int y, int ancho, int alto) {
		this.lblMensaje.setText(mensaje);
		this.lblMensaje.setVisible(true);
		this.lblMensaje.setBounds(x,y,ancho,alto);
		this.lblMensaje.setForeground(Color.WHITE);
		this.lblMensaje.setBackground(new Color(0,48,71));
		javax.swing.Timer timer = new javax.swing.Timer(2500, e -> {
	        lblMensaje.setVisible(false);
	    });
	    timer.setRepeats(false); //solo se muestra una vez
	    timer.start();
		
	}

	public void habilitarTeclado(boolean hab) {
		Button_0.setEnabled(hab);
		Button_1.setEnabled(hab);
		Button_2.setEnabled(hab);
		Button_3.setEnabled(hab);
		Button_4.setEnabled(hab);
		Button_5.setEnabled(hab);
		Button_6.setEnabled(hab);
		Button_7.setEnabled(hab);
		Button_8.setEnabled(hab);
		Button_9.setEnabled(hab);
	}
	
	public void validarLongitud() {
		int longitud = this.getDni().length();
		if (longitud >=7 && longitud<=8) {
			habilitarTeclado(true);
			this.habilitarAceptar(true);
			
			if (longitud == 8) {
				this.textFieldDNI.setEditable(false);
				habilitarTeclado(false);
			}
			
		} else{
			this.habilitarAceptar(false);
		}
		
	}

	public int confirmarDNI(String dniActual) {
		return JOptionPane.showConfirmDialog(
				this,
		        "¿El DNI ingresado " + dniActual + " es correcto?",
		        "Confirmación de DNI",
		        JOptionPane.YES_NO_OPTION
		    );
	}

	public void resetearPantalla() {
		habilitarTeclado(true);
		textFieldDNI.setEditable(true);
		
	}
}
