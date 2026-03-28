
package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Ventana_terminal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Ventana_terminal frame = new Ventana_terminal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Ventana_terminal() {
        setTitle("Terminal de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 400));
        // Quitamos el setBounds fijo y usamos pack() al final o un tamaño inicial
        setSize(600, 400); 
        setLocationRelativeTo(null); // Centra la ventana en la pantalla

        // 1. ContentPane principal con BorderLayout
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // 2. Panel Superior (Ingreso de DNI)
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel lblIngreseSuDni = new JLabel("Ingrese su DNI: ");
        lblIngreseSuDni.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        textField = new JTextField();
        textField.setColumns(15);
        
        panelSuperior.add(lblIngreseSuDni);
        panelSuperior.add(textField);
        contentPane.add(panelSuperior, BorderLayout.NORTH);

        // 3. Panel Central (El Teclado Numérico)
        // Usamos un panel intermedio para que el teclado no ocupe TODA la pantalla
        JPanel contenedorTeclado = new JPanel(new GridBagLayout()); 
        JPanel panelTeclado = new JPanel(new GridLayout(4, 3, 5, 5));
        
        // Creamos los botones del 1 al 9
        for (int i = 1; i <= 9; i++) {
            panelTeclado.add(new JButton(String.valueOf(i)));
        }
        
        // Fila inferior
        panelTeclado.add(new JPanel()); // Espacio vacío
        panelTeclado.add(new JButton("0"));
        panelTeclado.add(new JButton("←"));

        contenedorTeclado.add(panelTeclado); // El GridBagLayout mantendrá el teclado al centro
        contentPane.add(contenedorTeclado, BorderLayout.CENTER);

        // 4. Panel Inferior (Botón Aceptar)
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setPreferredSize(new Dimension(120, 40));
        btnAceptar.setFont(new Font("Tahoma", Font.PLAIN, 12));
        
        panelInferior.add(btnAceptar);
        contentPane.add(panelInferior, BorderLayout.SOUTH);
    }
}