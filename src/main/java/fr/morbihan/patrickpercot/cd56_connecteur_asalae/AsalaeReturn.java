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
	protected String dateTransfert;
	// Message d'avertissement si l'archive n'est pas transformée en entrée 
	// au-delà des délais
	// ALERTE: si délai court est dépassé
	// ERREUR: si délai long est dépassé
	protected String avertissement;
	
	protected String Acknowledgement;
	protected String ArchiveTransferAcceptance;
	
	
	AsalaeReturn() {
		statusCode = 0;
		message = "";
		dateTransfert = "";
		avertissement = "";
		Acknowledgement = "";
		ArchiveTransferAcceptance = "";
	}
	/**
	 * @return the Acknowledgement
	 */
	public String getAcknowledgement() {
		return Acknowledgement;
	}
	/**
	 * @return the archiveTransferReply
	 */
	public String getArchiveTransferAcceptance() {
		return ArchiveTransferAcceptance;
	}
	/**
	 * @return the avertissement
	 */
	public String getAvertissement() {
		return avertissement;
	}
	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the dateTransfert
	 */
	public String getDateTransfert() {
		return dateTransfert;
	}
	/**
	 * @param dateTransfert the dateTransfert to set
	 */
	public void setDateTransfert(String dateTransfert) {
		this.dateTransfert = dateTransfert;
	}
	

	/*
	 * 
	 */
	public void trace() {
		//print result
		System.out.println("Message = " + getMessage());
		System.out.println("Code = " + getStatusCode());
		System.out.println("Date = " + getDateTransfert());
		System.out.println("ArchiveTransferAcceptance = '" + getAcknowledgement() + "'");
		System.out.println("ArchiveTransferReply = '" + getArchiveTransferAcceptance() + "'");
	}
}
