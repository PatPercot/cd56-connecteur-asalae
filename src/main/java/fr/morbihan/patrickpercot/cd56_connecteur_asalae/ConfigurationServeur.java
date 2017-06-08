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

}
