package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;

public class Ventana_pantalla extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lbl_DNI2;
	private JLabel lbl_DNI1;
	private JLabel lbl_DNI3;
	private JLabel lbl_DNI4;
	private JLabel lbl_DNI5;
	private JPanel panel_turnos;
	private JLabel lbl_turnos;
	private JPanel panel_dni1;
	private JPanel panel_dni2;
	private JPanel panel_dni3;
	private JPanel panel_dni4;
	private JPanel panel_dni5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ventana_pantalla frame = new Ventana_pantalla();
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
	public Ventana_pantalla() {
		Color color = Color.decode("#2683BF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));
		
		this.panel = new JPanel();
		panel.setBackground(new Color(202, 243, 255));
		this.contentPane.add(this.panel, BorderLayout.CENTER);
		this.panel.setLayout(new GridLayout(5, 0, 0, 8));
		
		this.panel_dni1 = new JPanel();
		panel_dni1.setBackground(new Color(0, 160, 183));
		this.panel.add(this.panel_dni1);
		this.panel_dni1.setLayout(new GridLayout(0, 1, 10, 5));
		
		this.lbl_DNI1 = new JLabel("DNI:");
		lbl_DNI1.setBackground(new Color(38, 131, 191));
		lbl_DNI1.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_dni1.add(this.lbl_DNI1);
		this.lbl_DNI1.setFont(new Font("SansSerif", Font.BOLD, 36));
		this.lbl_DNI1.setForeground(Color.WHITE);
		
		this.panel_dni2 = new JPanel();
		panel_dni2.setBackground(new Color(255, 255, 255));
		this.panel.add(this.panel_dni2);
		this.panel_dni2.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.lbl_DNI2 = new JLabel("DNI:");
		lbl_DNI2.setBackground(new Color(255, 255, 255));
		lbl_DNI2.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_dni2.add(this.lbl_DNI2);
		this.lbl_DNI2.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.lbl_DNI2.setForeground(color);
		
		this.panel_dni3 = new JPanel();
		panel_dni3.setForeground(new Color(202, 243, 255));
		panel_dni3.setBackground(new Color(255, 255, 255));
		this.panel.add(this.panel_dni3);
		this.panel_dni3.setLayout(new GridLayout(0, 1, 0, 2));
		
		this.lbl_DNI3 = new JLabel("DNI:");
		lbl_DNI3.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_dni3.add(this.lbl_DNI3);
		this.lbl_DNI3.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.lbl_DNI3.setForeground(color);
		
		this.panel_dni4 = new JPanel();
		panel_dni4.setBackground(new Color(255, 255, 255));
		this.panel.add(this.panel_dni4);
		this.panel_dni4.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.lbl_DNI4 = new JLabel("DNI:");
		lbl_DNI4.setBackground(new Color(255, 255, 255));
		lbl_DNI4.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_dni4.add(this.lbl_DNI4);
		this.lbl_DNI4.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.lbl_DNI4.setForeground(color);
		
		this.panel_dni5 = new JPanel();
		panel_dni5.setForeground(new Color(202, 243, 255));
		panel_dni5.setBackground(new Color(255, 255, 255));
		this.panel.add(this.panel_dni5);
		this.panel_dni5.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.lbl_DNI5 = new JLabel("DNI:");
		lbl_DNI5.setBackground(new Color(202, 243, 255));
		lbl_DNI5.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_dni5.add(this.lbl_DNI5);
		this.lbl_DNI5.setFont(new Font("SansSerif", Font.PLAIN, 20));
		this.lbl_DNI5.setForeground(color);
		
		this.panel_turnos = new JPanel();
		panel_turnos.setBackground(new Color(206, 226, 235));
		this.panel_turnos.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.contentPane.add(this.panel_turnos, BorderLayout.NORTH);
		this.panel_turnos.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.lbl_turnos = new JLabel("HISTORIAL DE TURNOS LLAMADOS");
		lbl_turnos.setBackground(new Color(188, 221, 235));
		lbl_turnos.setHorizontalAlignment(SwingConstants.CENTER);
		this.lbl_turnos.setFont(new Font("SansSerif", Font.PLAIN, 18));
		this.panel_turnos.add(this.lbl_turnos);
		this.lbl_turnos.setForeground(new Color(49, 83, 102));

	}
	
	public void actualizarTurnos(LinkedList<String> turnos) {
		this.lbl_DNI1.setText("DNI: " + turnos.get(0));
		this.lbl_DNI2.setText("DNI: " + turnos.get(1));
		this.lbl_DNI3.setText("DNI: " + turnos.get(2));
		this.lbl_DNI4.setText("DNI: " + turnos.get(3));
		this.lbl_DNI5.setText("DNI: " + turnos.get(4));
	}

}
