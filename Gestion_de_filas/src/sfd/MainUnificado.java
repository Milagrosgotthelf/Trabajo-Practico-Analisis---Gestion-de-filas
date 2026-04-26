package sfd;

import llamado.MainEmp;
import notificacion.MainPan;
import registro.MainReg;

public class MainUnificado {

	public static void main(String[] args) {

		MainServer.main(args);
		MainPan.main(args);
		MainReg.main(args);
		MainEmp.main(args);
		MainEmp.main(args);

	}

}
