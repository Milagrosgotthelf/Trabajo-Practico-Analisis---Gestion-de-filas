package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.border.LineBorder;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

public class Ventana_empleado extends JFrame implements KeyListener {

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
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblNumProxDNI;
	private JLabel lblNumIntentosPend;
	private JLabel lblNumDNIActual;
	private int numPuesto;
	private JLabel lblingrese;
	private JTextField textField_numeroPuesto;
	private JPanel panel_4;
	private JLabel lblMensajeEstado;


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
		Color color;
		setTitle("TERMINAL DE ATENCIÓN");
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
		panel_izq.setBackground(new Color(202, 243, 255));
		panel_izq.setForeground(new Color(0, 0, 0));
		this.panel_inicial.add(this.panel_izq);
		
		this.panel_central = new JPanel();
		panel_central.setBackground(new Color(202, 243, 255));
		this.panel_inicial.add(this.panel_central);
		this.panel_central.setLayout(new GridLayout(5, 3, 0, 0));
		
		this.panel_5 = new JPanel();
		panel_5.setBackground(new Color(202, 243, 255));
		this.panel_central.add(this.panel_5);
		
		this.panel_7 = new JPanel();
		panel_7.setBackground(new Color(229, 245, 255));
		this.panel_central.add(this.panel_7);
		this.panel_7.setLayout(new GridLayout(2, 2, 0, 0));
		
		this.lbl_SISTEMA = new JLabel("SISTEMA DE");
		lbl_SISTEMA.setBackground(new Color(201, 234, 255));
		lbl_SISTEMA.setHorizontalAlignment(SwingConstants.CENTER);
		this.lbl_SISTEMA.setFont(new Font("SansSerif", Font.BOLD, 20));
		color = Color.decode("#2683BF");
		this.lbl_SISTEMA.setForeground(new Color(0, 160, 183));
		this.panel_7.add(this.lbl_SISTEMA);
		
		this.lblNewLabel_1 = new JLabel("GESTIÓN DE FILA");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_7.add(this.lblNewLabel_1);
		this.lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.lblNewLabel_1.setForeground(new Color(0, 160, 183));
		
		this.panel_8 = new JPanel();
		panel_8.setBackground(new Color(229, 245, 255));
		this.panel_central.add(this.panel_8);
		this.panel_8.setLayout(new GridLayout(2, 1, 0, 0));
		
		this.lblingrese = new JLabel("Ingrese su número de puesto");
		this.lblingrese.setForeground(new Color(0, 64, 128));
		this.lblingrese.setFont(new Font("SansSerif", Font.BOLD, 12));
		this.lblingrese.setHorizontalAlignment(SwingConstants.CENTER);
		this.panel_8.add(this.lblingrese);
		
		this.panel_4 = new JPanel();
		this.panel_4.setBackground(new Color(229, 245, 255));
		this.panel_8.add(this.panel_4);
		
		this.textField_numeroPuesto = new JTextField();
		this.panel_4.add(this.textField_numeroPuesto);
		this.textField_numeroPuesto.setColumns(2);
		this.textField_numeroPuesto.addKeyListener(this);
		this.panel_6 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) this.panel_6.getLayout();
		flowLayout_4.setVgap(10);
		panel_6.setBackground(new Color(229, 245, 255));
		this.panel_central.add(this.panel_6);
		
		this.btn_iniciar = new JButton("INICIAR");
		this.btn_iniciar.setEnabled(false);
		//damos color
		this.btn_iniciar.setFont(new Font("SansSerif", Font.BOLD, 15));
		this.btn_iniciar.setBackground(new Color(0, 160, 183));
		this.btn_iniciar.setFocusPainted(false);
		this.btn_iniciar.setBackground(color);
		this.btn_iniciar.setForeground(Color.WHITE);
		this.btn_iniciar.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.panel_6.add(this.btn_iniciar);
		this.panel_der = new JPanel();
		panel_der.setBackground(new Color(202, 243, 255));
		this.panel_inicial.add(this.panel_der);
		
		this.panel_llamada = new JPanel();
		this.panel_llamada.setBorder(new LineBorder(new Color(202, 243, 255)));
		this.panel_llamada.setForeground(new Color(202, 243, 255));
		this.panel_llamada.setBackground(new Color(202, 243, 255));
		this.contentPane.add(this.panel_llamada, "Llamada");
		this.panel_llamada.setLayout(new GridLayout(2, 0, 0, 0));
		
		this.panel_sup = new JPanel();
		this.panel_llamada.add(this.panel_sup);
		this.panel_sup.setLayout(new GridLayout(0, 1, 0, 0));
		
		Color colorPaneles =  new Color(202, 243, 255);
		this.panel_prox = new JPanel();
		panel_prox.setBackground(colorPaneles);
		this.panel_sup.add(this.panel_prox);
		
		this.lbl_prox = new JLabel("Próximo DNI:");
		this.lbl_prox.setVerticalAlignment(SwingConstants.BOTTOM);
		this.panel_prox.add(this.lbl_prox);
		this.lbl_prox.setFont(new Font("SansSerif", Font.PLAIN, 25));
		this.lbl_prox.setForeground(new Color(0, 0, 0));
		
		lblNumProxDNI = new JLabel("New label");
		lblNumProxDNI.setForeground(new Color(0, 160, 183));
		lblNumProxDNI.setFont(new Font("SansSerif", Font.BOLD, 25));
		panel_prox.add(lblNumProxDNI);
		lblNumProxDNI.setVisible(false);
		
		lblMensajeEstado = new JLabel("New label");
		lblMensajeEstado.setForeground(new Color(0, 160, 183));
		lblMensajeEstado.setFont(new Font("SansSerif", Font.BOLD, 25));
		panel_prox.add(lblMensajeEstado);
		lblMensajeEstado.setVisible(true);
		
		this.panel_int = new JPanel();
		panel_int.setBackground(colorPaneles);
		this.panel_sup.add(this.panel_int);
		
		
		this.lbl_intentos = new JLabel("Intentos pendientes: ");
		this.panel_int.add(this.lbl_intentos);
		this.lbl_intentos.setFont(new Font("SansSerif", Font.PLAIN, 25));
		this.lbl_intentos.setForeground(new Color(0, 0, 0));
		
		lblNumIntentosPend = new JLabel("New label");
		lblNumIntentosPend.setForeground(new Color(0, 160, 183));
		lblNumIntentosPend.setFont(new Font("SansSerif", Font.BOLD, 25));
		panel_int.add(lblNumIntentosPend);
		lblNumIntentosPend.setVisible(false);
		this.panel_inf = new JPanel();
		this.panel_llamada.add(this.panel_inf);
		this.panel_inf.setLayout(new GridLayout(0, 2, 0, 0));
		
		this.panel_llamar = new JPanel();
		panel_llamar.setBackground(colorPaneles);
		this.panel_inf.add(this.panel_llamar);
		panel_llamar.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.setBackground(colorPaneles);
		panel_llamar.add(panel);
		
		panel_1 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_1.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		panel_1.setBackground(colorPaneles);
		panel_llamar.add(panel_1);
		
		this.btn_llamar = new JButton("Llamar");
		panel_1.add(btn_llamar);
		this.btn_llamar.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.btn_llamar.setBackground(new Color(0, 160, 183));
		this.btn_llamar.setForeground(Color.WHITE);
		this.btn_llamar.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.panel_iniciar = new JPanel();
		panel_iniciar.setBackground(colorPaneles);
		this.panel_inf.add(this.panel_iniciar);
		panel_iniciar.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setHgap(0);
		flowLayout_1.setVgap(0);
		panel_2.setBackground(colorPaneles);
		panel_iniciar.add(panel_2);
		
		panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel_3.setBackground(colorPaneles);
		panel_iniciar.add(panel_3);
		
		this.btn_iniciarturno = new JButton("Iniciar turno");
		panel_3.add(btn_iniciarturno);
		this.btn_iniciarturno.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.btn_iniciarturno.setEnabled(false);
		this.btn_iniciarturno.setBackground(new Color(0, 160, 183));
		this.btn_iniciarturno.setForeground(Color.WHITE);
		this.btn_iniciarturno.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
		
		this.panel_atencion = new JPanel();
		this.contentPane.add(this.panel_atencion, "Atencion");
		this.panel_atencion.setLayout(new GridLayout(2, 0, 0, 0));
		
		this.panel_dniact = new JPanel();
		panel_dniact.setBackground(new Color(202, 243, 255));
		panel_dniact.setForeground(new Color(202, 243, 255));
		this.panel_atencion.add(this.panel_dniact);
		this.panel_dniact.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
		
		this.lbl_dniact = new JLabel("DNI actual: ");
		this.panel_dniact.add(this.lbl_dniact);
		this.lbl_dniact.setFont(new Font("SansSerif", Font.PLAIN, 30));
		
		lblNumDNIActual = new JLabel("New label");
		lblNumDNIActual.setForeground(new Color(0, 160, 183));
		lblNumDNIActual.setFont(new Font("SansSerif", Font.BOLD, 30));
		panel_dniact.add(lblNumDNIActual);
		
		this.panel_fin = new JPanel();
		panel_fin.setForeground(new Color(202, 243, 255));
		panel_fin.setBackground(new Color(202, 243, 255));
		this.panel_atencion.add(this.panel_fin);
		
		this.btn_fin = new JButton("Finalizar turno");
		this.btn_fin.setFont(new Font("SansSerif", Font.BOLD, 20));
		this.btn_fin.setBackground(new Color(0, 160, 183));
		this.btn_fin.setForeground(Color.WHITE);
		this.btn_fin.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
			    BorderFactory.createEmptyBorder(5, 5, 5, 5)
			));
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

	public String getTextField_numeroPuesto() {
		return textField_numeroPuesto.getText();
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
		
		switch (nombre) {
        case "Inicio":
            setTitle("Inicio - Puesto "+ this.numPuesto);
            break;
        case "Llamada":
            setTitle("Llamado de clientes - Puesto "+ this.numPuesto);
            
            break;
        case "Atencion":
            setTitle("Atención al cliente - Puesto "+ this.numPuesto);
            break;
		}
	}
	public void setIntentos(int intentos) {
		lblNumIntentosPend.setText(Integer.toString(intentos));
	}
	public void setProximoDni(String dni) {
		lblNumProxDNI.setText(dni);
	}
	public void setDniActual(String dni) {
		lblNumDNIActual.setText(dni);
	}

	public String getDniActual() {
		return lbl_dniact.getText();
	}
	
	public String getProximoDni() {
		return lbl_prox.getText();
	}
	public void mostrarMensaje(String mensaje) {
	    javax.swing.JOptionPane.showMessageDialog(
	            this,
	            mensaje,
	            "Información",
	            javax.swing.JOptionPane.INFORMATION_MESSAGE
	        );
	}

	public void activarBtnIniciarTurno(boolean activar) {
		this.btn_iniciarturno.setEnabled(activar);
	}
	
	public void activarBtnLlamar(boolean activar) {
		this.btn_llamar.setEnabled(activar); //este para que cuando pasemos a la pantalla de llamados sin clientes no se pueda presionar el btn
	}
	

	public void notificarLlamada(int intento) {
		this.mostrarMensaje("Notificando: intento " + intento + " de 3");
        
	    try {
	    	java.net.URL url = getClass().getResource("/sonidos/freesound_community-elevator-ding-at-arenco-tower-dubai-38520.wav");
	    	//java.net.URL url = getClass().getResource("/sonidos/benkirb-electronic-doorbell-262895.wav");
	        if (url !=null) {
	            javax.sound.sampled.AudioInputStream audioStream = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
	            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
	            clip.open(audioStream);
	            //clip.start();
	        }
	    } catch (Exception e) {
	        System.err.println("No se pudo reproducir el sonido: " + e.getMessage());
	    }
	}

	public void activariniciar() {
		this.btn_iniciar.setEnabled(true);
	}

	

	public void actualizarEstadoEspera(boolean hayClientes) {
	    if (hayClientes) {
	        lblMensajeEstado.setText("Hay clientes esperando.");
	        lblMensajeEstado.setVisible(true);
	        this.btn_llamar.setEnabled(true);
	       
	    } else {
	        lblMensajeEstado.setText("No hay clientes esperando.");
	        lblMensajeEstado.setVisible(true);
	        btn_llamar.setEnabled(false);
	        setLabelsVisibles(false);
	    }
	}
	public void setLabelsVisibles(boolean visible) {
		
		lbl_prox.setVisible(visible);
	    lblNumProxDNI.setVisible(visible);
	    lbl_intentos.setVisible(visible);
	    lblNumIntentosPend.setVisible(visible);
	    if (visible) {
	    	lblMensajeEstado.setVisible(false);
	 	
	    }
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		
	}

	

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
	    
	    // Si no es un dígito, consumimos el evento (bloqueamos la entrada)
	    if (!Character.isDigit(c)) {
	        e.consume(); 
	    }
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		boolean valido = false;
		
		try {
				if(!this.textField_numeroPuesto.getText().isEmpty() && this.textField_numeroPuesto.getText().length() <7 ) {
					this.numPuesto = Integer.parseInt(this.textField_numeroPuesto.getText());
					valido = true;
				}
				else
					this.textField_numeroPuesto.setText("");
				
		} catch (NumberFormatException e) {
		}
		this.btn_iniciar.setEnabled(valido);
		}	
}

