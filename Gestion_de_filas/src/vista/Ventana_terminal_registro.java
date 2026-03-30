package vista;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana_terminal_registro frame = new Ventana_terminal_registro();
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
	public Ventana_terminal_registro() {
		setTitle("Terminal de Registro");
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 584, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		
		this.panel = new JPanel();
		this.panel.setBounds(10, 10, 550, 67);
		this.contentPane.add(this.panel);
		this.panel.setLayout(null);
		
		this.lblIngreseSuDni = new JLabel("Ingrese su DNI: ");
		this.lblIngreseSuDni.setFont(new Font("Tahoma", Font.PLAIN, 14));
		this.lblIngreseSuDni.setBounds(157, 10, 127, 47);
		this.panel.add(this.lblIngreseSuDni);
		
		this.textFieldDNI = new JTextField();
		this.textFieldDNI.setBounds(312, 26, 112, 18);
		this.panel.add(this.textFieldDNI);
		this.textFieldDNI.setColumns(10);
		this.textFieldDNI.setEditable(false);
		
		this.panel_1 = new JPanel();
		this.panel_1.setBounds(167, 87, 196, 149);
		this.contentPane.add(this.panel_1);
		this.panel_1.setLayout(new GridLayout(4, 3, 0, 0));
		
		this.Button_1 = new JButton("1");
		this.panel_1.add(this.Button_1);
		
		this.Button_2 = new JButton("2");
		this.panel_1.add(this.Button_2);
		
		this.Button_3 = new JButton("3");
		this.panel_1.add(this.Button_3);
		
		this.Button_4 = new JButton("4");
		this.panel_1.add(this.Button_4);
		
		this.Button_5 = new JButton("5");
		this.panel_1.add(this.Button_5);
		
		this.Button_6 = new JButton("6");
		this.panel_1.add(this.Button_6);
		
		this.Button_7 = new JButton("7");
		this.panel_1.add(this.Button_7);
		
		this.Button_8 = new JButton("8");
		this.panel_1.add(this.Button_8);
		
		this.Button_9 = new JButton("9");
		this.panel_1.add(this.Button_9);
		
		this.panel_2 = new JPanel();
		this.panel_1.add(this.panel_2);
		
		this.Button_0 = new JButton("0");
		this.panel_1.add(this.Button_0);
		
		this.Button_back = new JButton("←");
		this.panel_1.add(this.Button_back);
		
		this.Button_aceptar = new JButton("Aceptar");
		this.Button_aceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.Button_aceptar.setBounds(408, 142, 84, 20);
		this.Button_aceptar.setEnabled(false);
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
		//aca tuve que buscar como poner timer je, 
		//no me parecia poner los carteles donde se tiene que presionar aceptar para salir asique lo hice asi
		javax.swing.Timer timer = new javax.swing.Timer(2500, e -> {
	        lblMensaje.setVisible(false);
	    });
	    timer.setRepeats(false); //solo se muestra una vez
	    timer.start();
		
	}
}
