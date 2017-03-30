package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Retour des web services Asalaé 
 * 
 */
public class AsalaeReturn {
	// Code de retour de l'API asalaé
	protected int statusCode;
	protected String message;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
