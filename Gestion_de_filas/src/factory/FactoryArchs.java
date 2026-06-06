package factory;

import sfd.Utils;

public class FactoryArchs {

	public static IAbstractFactory getFormato() {
		if (Utils.Formato.toUpperCase().trim().equals("JSON"))
	       return new JsonFactory();
	    else if (Utils.Formato.toUpperCase().trim().equals("XML"))
	        return new XmlFactory();
	    else if (Utils.Formato.toUpperCase().trim().equals("TXT"))
	        return new TxtFactory();
	    else
	        throw new IllegalArgumentException("Formato no soportado: " + Utils.Formato);
	        

	}

}
