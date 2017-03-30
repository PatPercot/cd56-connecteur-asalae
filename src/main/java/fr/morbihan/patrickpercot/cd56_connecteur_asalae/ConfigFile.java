package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
	private String username = null;
	private String password = null;
	private String UrlAsalae = null;
	
	/**
	 * @param configFile nom du fichier de configuration. Si le fichier n'est pas trouvé, loadFile essaie de l'ouvrir dans le répertoire parent
	 * @return true si tous les paramètres ont pu être chargés, faux sinon
	 */
	public boolean loadFile(String configFile) {
		boolean bOK = false;
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
					Pattern regex = Pattern.compile("^([-_a-zA-z0-9]+)\\s*=\\s*(.*)$");
					Matcher m;
					while ((line = br.readLine()) != null) {
						if (line.length() > 0) {
							if (line.startsWith("\uFEFF")) { // UTF8 BOM
								line = line.substring(1);
							}

							line = line.trim();
							if (line.length() > 0 && !line.startsWith("#")) {
								m = regex.matcher(line);
								if (m.matches()) {
									if (m.group(1).toLowerCase().equals("username")) {
										username = m.group(2);
									}
									if (m.group(1).toLowerCase().equals("password")) {
										password = m.group(2);
									}
									if (m.group(1).toLowerCase().equals("urlasalae")) {
										UrlAsalae = m.group(2);
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
		if (UrlAsalae != null && password != null && username != null)
			bOK = true;
		return bOK;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrlAsalae() {
		return UrlAsalae;
	}
	
}
