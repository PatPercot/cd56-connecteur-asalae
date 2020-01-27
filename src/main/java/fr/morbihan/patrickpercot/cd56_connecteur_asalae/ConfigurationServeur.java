/**
 * 
 */
package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

/**
 * @author percot-p
 *
 */
public class ConfigurationServeur {
	private String username = null;
	private String password = null;
	private String UrlAsalae = null;
	
	// Tous les délais et durées sont en minutes
	
	// Délai avant émission d'une alerte pour les archives récemment transférées
	// Indication de risque de bordereau non conforme 
	private String delaiAlerteSae = null;
	// Temps durant lequel l'alerte pourra être émise (0 pour désactiver)
	// T1 : Transfert de l'archive ..... T2 = T1 +  delaiAlerteSae ..... T3 = T1 +  delaiAlerteSae + dureeEmissionAlerte
	//                                   Début alerte                    Fin alerte
	private String dureeEmissionAlerte = null;
	// Délai avant émission d'une erreur
	// Indication de risque de traitement manuel attendu 
	private String delaiErreurSae = null;
	// Temps durant lequel l'erreur pourra être émise (0 pour désactiver)
	// T1 : Transfert de l'archive ..... T4 = T1 +  delaiErreurSae ..... T5 = T1 +  delaiErreurSae + dureeEmissionErreur
	//                                   Début erreur                    Fin alerte
	private String dureeEmissionErreur = null;
	
	/**
	 * Vérifie que la configuration est complète 
	 * 
	 * @return true si tous les paramètres ont été instanciés
	 * @return false si un des paramètres est null
	 */
	public boolean checkConfiguration() {
		return (UrlAsalae != null && password != null && username != null);
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the urlAsalae
	 */
	public String getUrlAsalae() {
		return UrlAsalae;
	}
	/**
	 * @param urlAsalae the urlAsalae to set
	 */
	public void setUrlAsalae(String urlAsalae) {
		UrlAsalae = urlAsalae;
	}

	public String getDureeEmissionErreur() {
		return dureeEmissionErreur;
	}

	public void setDureeEmissionErreur(String dureeEmissionErreur) {
		this.dureeEmissionErreur = dureeEmissionErreur;
	}

	public String getDelaiErreurSae() {
		return delaiErreurSae;
	}

	public void setDelaiErreurSae(String delaiErreurSae) {
		this.delaiErreurSae = delaiErreurSae;
	}

	public String getDureeEmissionAlerte() {
		return dureeEmissionAlerte;
	}

	public void setDureeEmissionAlerte(String dureeEmissionAlerte) {
		this.dureeEmissionAlerte = dureeEmissionAlerte;
	}

	public String getDelaiAlerteSae() {
		return delaiAlerteSae;
	}

	public void setDelaiAlerteSae(String delaiAlerteSae) {
		this.delaiAlerteSae = delaiAlerteSae;
	}

}
