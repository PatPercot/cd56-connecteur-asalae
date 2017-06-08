package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Lecture du fichier de configuration des web services Asalaé pour le transfert
 * des archives (bordereau et ZIP)
 * 
 */
public class ConfigFile {
	private HashMap<String, ConfigurationServeur> serveurs;
	private String serveurCourant;
	private String serveurParDefaut;
	
	/**
	 * Sélectionne un serveur
	 * @param serveur le serveur à sélectionner s'il existe
	 * @return le serveur demandé ou le serveur par défaut si le premier n'a pas été trouvé
	 */
	public boolean selectionServeur(String serveur) {
		boolean bFound = false;
		// System.out.println("Cherche serveur : '" + serveur + "'");
		if (serveurs.containsKey(serveur)) {
			serveurCourant = serveur;
			// System.out.println("serveurCourant trouvé : '" + serveur + "'");
			bFound = true;
		} else
			serveurCourant = serveurParDefaut;
		
		return bFound;
	}
	
	/**
	 * @param configFile nom du fichier de configuration. Si le fichier n'est pas trouvé, loadFile essaie de l'ouvrir dans le répertoire parent
	 * @return true si tous les paramètres ont pu être chargés, faux sinon
	 */
	public boolean loadFile(String configFile) {
		serveurs = new HashMap<String, ConfigurationServeur>();
		serveurCourant = "";
		ConfigurationServeur confServeur = null;
		String serveurTraite = ""; // Le serveur en cours de lecture
		
		boolean bConfigurationComplete = false;
		File f = new File(configFile);
		// System.out.println(f.getAbsolutePath());
		if (!f.exists()) {
			f = new File("../" + configFile);
			// System.out.println(f.getAbsolutePath());
		}
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			InputStreamReader isr;
			try {
				isr = new InputStreamReader(fis, "UTF8");
				BufferedReader br = new BufferedReader(isr);
				String line;
				try {
					// On fait un line.trim(), il n'y a donc pas d'esapces en début ou fin de ligne
					Pattern regexServeur = Pattern.compile("^\\[([-_A-Za-z0-9]+)\\]$");
					Pattern regexEntree = Pattern.compile("^([-_a-zA-z0-9]+)\\s*=\\s*(.*)$");
					Matcher m;
					
					// On rentre en phase lecture, on renvoie true par défaut, sauf si une des configurations 
					// de serveur est incomplète
					bConfigurationComplete = true;
					
					while ((line = br.readLine()) != null) {
						if (line.length() > 0) {
							if (line.startsWith("\uFEFF")) { // UTF8 BOM
								line = line.substring(1);
							}

							line = line.trim();
							if (line.length() > 0 && !line.startsWith("#")) {
								
								m = regexServeur.matcher(line);
																
								if (m.matches()) {
									if (confServeur != null) {
										if (confServeur.checkConfiguration() == false)
											bConfigurationComplete = false;
										serveurs.put(serveurTraite, confServeur);
									}
									serveurTraite = m.group(1);
									// Si le serveur par défaut n'a pas été déjà sélectionné, on prend le premier serveur rencontré 
									if (serveurCourant.equals("")) {
										serveurParDefaut = serveurTraite;
										serveurCourant = serveurTraite;
									}
									
									confServeur = new ConfigurationServeur();
								} else {
									m = regexEntree.matcher(line);
									if (m.matches()) {
										if (confServeur == null) {
											System.err.println("Il n'y a pas de section serveur dans le fichier de configuration");
										} else {
											if (m.group(1).toLowerCase().equals("username")) {
												confServeur.setUsername(m.group(2));
											}
											if (m.group(1).toLowerCase().equals("password")) {
												confServeur.setPassword(m.group(2));
											}
											if (m.group(1).toLowerCase().equals("urlasalae")) {
												confServeur.setUrlAsalae(m.group(2));
											}
										}
									}
								}
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Si on n'a pas rencontré de serveur par défaut, la configuration est incomplète
		if (serveurCourant.equals(""))
			bConfigurationComplete = false;

		// Le dernier serveur traité est ajouté
		if (confServeur != null) {
			if (confServeur.checkConfiguration() == false)
				bConfigurationComplete = false;
			serveurs.put(serveurTraite, confServeur);
		}
		
		return bConfigurationComplete;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		if (serveurCourant.equals(""))
			return "";
		return serveurs.get(serveurCourant).getUsername();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		if (serveurCourant.equals(""))
			return "";
		return serveurs.get(serveurCourant).getPassword();
	}

	/**
	 * @return the urlAsalae
	 */
	public String getUrlAsalae() {
		if (serveurCourant.equals(""))
			return "";
		// System.out.println("Serveur courant : '" + serveurCourant);
		// System.out.println("URL : '" + serveurs.get(serveurCourant).getUrlAsalae());
		return serveurs.get(serveurCourant).getUrlAsalae();
	}
	
}
