package registro;

public class MainReg {

	public static void main(String[] args) {

		Ventana_terminal_registro vistasReg = new Ventana_terminal_registro(0);
		ControladorRegistro controladores = new ControladorRegistro(0, vistasReg);
		
		vistasReg.setVisible(true);
		
	}

}
