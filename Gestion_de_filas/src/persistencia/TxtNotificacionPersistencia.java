package persistencia;

import java.util.HashMap;
import java.util.Map;

import persistencia.IPersistencia.NotificacionPersistencia;

public class TxtNotificacionPersistencia extends TPersistencia<Map<String, Integer>> implements NotificacionPersistencia {

	public TxtNotificacionPersistencia() {
        super("datos/re_notificaciones.txt");
    }

    @Override
    public void guardarIntentos(Map<String, Integer> intentos) {
        guardar(intentos); 
    }

    @Override
    public Map<String, Integer> recuperarIntentos() {
        return recuperar(); 
    }

	@Override
	protected String formatearDatos(Map<String, Integer> datos) {
		String txt="";
        
        for (Map.Entry<String, Integer> entry : datos.entrySet()) 
            txt.concat(entry.getKey() + ":" + entry.getValue() + "\n");
        return txt.toString();
	}

	@Override
	protected Map<String, Integer> parsearDatos(String contenido) {
		String[] spliteado = contenido.split("\n");
		Map<String, Integer> mapa = new HashMap<String, Integer>();
		
		for(int i=0;i < spliteado.length;i++)
			mapa.put(spliteado[i].split(":")[0], Integer.parseInt(spliteado[i].split(":")[1]));
		return mapa;
	}

	@Override
	protected Map<String, Integer> obtenerObjetoVacio() {
		return new HashMap<>();
	}

}
