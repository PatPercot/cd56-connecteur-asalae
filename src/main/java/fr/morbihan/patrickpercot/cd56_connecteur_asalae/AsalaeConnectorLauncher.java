package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
// import com.jayway.jsonpath.PathNotFoundException;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Lanceur de la connexion à Asalaé en mode ligne de commande
 *  
 */
public class AsalaeConnectorLauncher {
	protected String serveur;
	protected boolean bVerbose = false;
	protected boolean bVeryVerbose = false;
	protected boolean bHasRun = false;
	protected ConfigFile param;
	protected String[] args;
	protected AsalaeConnector http;

	protected String commandeTester = "tester-serveur";
	protected String commandeConnexion = "connecter-serveur";
	protected String commandeTransfert = "transferer-archive";
	protected String commandeGetAcknowledge = "get-acknowledge";
	protected String commandeGetAcceptance = "get-acceptance";
	


	public static void main(String[] args) {
		AsalaeConnectorLauncher pmag = new AsalaeConnectorLauncher();
		pmag.execMain(args);
	}
	
	protected String[] analyserOptions(String[] args) {
		if (args.length > 1 && ("--verbose".equals(args[0]) || "-v".equals(args[0]) 
				|| "--veryverbose".equals(args[0]) || "-vv".equals(args[0])) ) {
			if ("--veryverbose".equals(args[0]) || "-vv".equals(args[0])) {
				bVeryVerbose = true;
				bVerbose = true;
			} else
				bVerbose = true;
			int len = args.length -1;
			String[] newArgs = new String[len];
			for (int i = 0; i < len; ++i) {
				newArgs[i] = args[i + 1];
			}
			// On remplace les arguments initiaux par la copie sans --verbose
			args = newArgs;
		}
		if (args.length > 2 && ("--serveur".equals(args[0]) || "-s".equals(args[0]) )) {
			serveur = args[1];
			int len = args.length -2;
			String[] newArgs = new String[len];
			for (int i = 0; i < len; ++i) {
				newArgs[i] = args[i + 2];
			}
			// On remplace les arguments initiaux par la copie sans --verbose
			args = newArgs;
		} else
			serveur = "";
		
		// On retourne les anciens arguments ou les nouveaux purgés des options 
		return args;
	}

	protected void execMain( String[] args )  {
		this.args = args;
		args = analyserOptions(args);
		
		if (bVerbose) {
			System.out.print("Exécution avec les paramètres suivants : ");
			for (String str : args) {
				System.out.print(" '" + str + "'");
			}
			System.out.println("");			
		}
		
        if (bVerbose)
        	System.out.println("Chargement de la configuration");
        param = new ConfigFile();
        if (! param.loadFile("param.config")) {
        	System.err.println("ERROR: Le fichier de configuration est introuvable ou incomplet");
        	bHasRun = true;
        } else {
            // On sélectionne le serveur demandé -si vide, c'est le serveur par défaut qui est sélectionné)
        	param.selectionServeur(serveur);
            
            http = new AsalaeConnector(param);
        	http.setVerbose(bVerbose);
        	http.setVeryVerbose(bVeryVerbose);
            
			if (	(args.length == 1 && commandeTester.equals(args[0].toLowerCase()))
				 || (args.length == 1 && commandeConnexion.equals(args[0].toLowerCase()))
				 || (args.length == 3 && commandeTransfert.equals(args[0].toLowerCase()))
				 || (args.length == 3 && commandeGetAcknowledge.equals(args[0].toLowerCase()))
				 || (args.length == 3 && commandeGetAcceptance.equals(args[0].toLowerCase()))
				 )  {
				bHasRun = true;

				if (commandeTester.equals(args[0].toLowerCase())) 
					tester();

				if (commandeConnexion.equals(args[0].toLowerCase())) 
					connexion();

				if (commandeTransfert.equals(args[0].toLowerCase())) 
					transfert(args[1], args[2]);

				if (commandeGetAcknowledge.equals(args[0].toLowerCase())) 
					getacknowledge(args[1], args[2]);

				if (commandeGetAcceptance.equals(args[0].toLowerCase())) 
					getacceptance(args[1], args[2]);
			}
        }
				
				
		if (! bHasRun)
			usage();
    }

	  public void tester() {
        	AsalaeConnector http = new AsalaeConnector(param);
        	http.setVerbose(bVerbose);
        	http.setVeryVerbose(bVeryVerbose);

        	if (bVerbose) {
        		System.out.println("Test de présence du serveur As@lae '" + param.getUrlAsalae() + "'");
        	}
    		try {
    			AsalaeReturn response = http.TestPingAsalae();
    			if (bVeryVerbose) {
	    			System.out.println("Message = " + response.getMessage());
	    			System.out.println("Code = " + response.getStatusCode());
    			}
    			if (response.statusCode == 200) {
					if (bVerbose)
						System.out.println("La version d'As@laé est la version '" + response.message + "'");
					else
						System.out.println("OK: '" + response.message + "'");
    			} else {
					if (bVerbose)
						System.out.println("Erreur lors de la demande de version : '" + response.message + "'");
					else
						System.out.println("ERROR: (tester-serveur) '" + response.message + "'");
    			}

    		}
			catch (Exception e) {
				System.err.println("ERROR: (tester-serveur) une exception de type " + e.getClass() + " s'est produite " + e.getMessage());
			}
	        
	  }

	  public void connexion() {
      	AsalaeConnector http = new AsalaeConnector(param);
    	http.setVerbose(bVerbose);
    	http.setVeryVerbose(bVeryVerbose);

      	if (bVerbose) {
      		System.out.println("Test de connexion à As@lae '" + param.getUrlAsalae() + "'");
      	}
  		try {
  			AsalaeReturn response = http.TestVersionAsalae();
  			
  			if (bVeryVerbose) {
	    			System.out.println("Message = " + response.getMessage());
	    			System.out.println("Code = " + response.getStatusCode());
  			}
  			if (response.statusCode == 200) {
	    			Object document = Configuration.defaultConfiguration().jsonProvider().parse(response.message);
	    			String version = null;
	    			version = JsonPath.read(document, "$.version");
					if (bVerbose)
						System.out.println("La version d'As@laé est la version '" + version + "'");
					else
						System.out.println("OK: '" + version + "'");
  			} else {
					if (bVerbose)
						System.out.println("Erreur lors de la demande de version : '" + response.message + "'");
					else
						System.out.println("ERROR: (connecter-serveur) '" + response.message + "'");
  			}

  		}
			catch (Exception e) {
				System.err.println("ERROR: (connecter-serveur) une exception de type " + e.getClass() + " s'est produite " + e.getMessage());
			}
	        
	  }

		  public void transfert(String bordereau, String zip) {
	        	AsalaeConnector http = new AsalaeConnector(param);
	        	http.setVerbose(bVerbose);
	        	http.setVeryVerbose(bVeryVerbose);

	        	if (bVerbose) {
		    		System.out.println("Transfert vers '" + param.getUrlAsalae() + "' de '" +
		    				bordereau + "' et de '" + zip + "'");
	        	}
	    		try {
	    			AsalaeReturn retourTransfert = http.callPostDocuments(bordereau, zip);
	    			if (bVeryVerbose) {
		    			System.out.println("Message = " + retourTransfert.getMessage());
		    			System.out.println("Code = " + retourTransfert.getStatusCode());
	    			}
	    			if (retourTransfert.statusCode == 200) {
		    			Object document = Configuration.defaultConfiguration().jsonProvider().parse(retourTransfert.message);
		    			String date_du_depot = null;
	    				date_du_depot = JsonPath.read(document, "$.date_du_depot");
    					if (bVerbose)
    						System.out.println("Le processus de transfert s'est achevé avec succès à : '" + date_du_depot + "'");
    					else
    						System.out.println("OK: '" + date_du_depot + "'");
	    			} else {
    					if (bVerbose)
    						System.out.println("Erreur lors du processus de transfert : '" + retourTransfert.message + "'");
    					else
    						System.out.println("ERROR: (transferer-archive) '" + retourTransfert.message + "'");
	    			}

	    		}
    			catch (Exception e) {
    				System.err.println("ERROR: (transferer-archive) une exception de type " + e.getClass() + " s'est produite " + e.getMessage());
    			}
		  }

		  /*
		   * @param transferIdentifier identifiant du transfert, exemple DEP56_PESV2_WSJAVA_0000000002
		   */
		  public void getacknowledge(String transferIdentifier, String transferringAgency) {
	        	AsalaeConnector http = new AsalaeConnector(param);
	        	http.setVerbose(bVerbose);
	        	http.setVeryVerbose(bVeryVerbose);

	        	if (bVerbose) {
	        		System.out.println("Récupération d'un acknowledge As@lae '" + param.getUrlAsalae() + "' pour '" 
	        				+ transferIdentifier + "' transféré par '" + transferringAgency + "'");
	        	}
	    		try {
	    			AsalaeReturn response = http.getAcknowledge(transferIdentifier, transferringAgency);
	    			
	    			if (bVeryVerbose) {
		    			System.out.println("Message = " + response.getMessage());
		    			System.out.println("Code = " + response.getStatusCode());
	    			}
	    			if (response.statusCode == 200) {
		    			String accuseReception = response.Acknowledgement;
						if (bVerbose)
							System.out.println("L'accusé de réception du transfert vaut '" + accuseReception + "'");
						else
							System.out.println("OK: '" + accuseReception + "'");
	    			} else {
  					if (bVerbose)
  						System.out.println("Erreur lors de la demande d'accusé de réception du transfert : '" + response.message + "'");
  					else
  						System.out.println("ERROR: (get-acknowledge) '" + response.message + "'");
	    			}

	    		}
  			catch (Exception e) {
  				System.err.println("ERROR: (get-acknowledge) une exception de type " + e.getClass() + " s'est produite " + e.getMessage());
  			}
		        
		  }

		  public void getacceptance(String transferIdentifier, String transferringAgency) {
	        	AsalaeConnector http = new AsalaeConnector(param);
	        	http.setVerbose(bVerbose);
	        	http.setVeryVerbose(bVeryVerbose);

	        	if (bVerbose) {
	        		System.out.println("Récupération d'un ATR As@lae '" + param.getUrlAsalae() + "' pour '" 
	        				+ transferIdentifier + "' transféré par '" + transferringAgency + "'");
	        	}
	    		try {
	    			AsalaeReturn response = http.getAcceptance(transferIdentifier, transferringAgency);
	    			
	    			if (bVeryVerbose) {
		    			System.out.println("Message = " + response.getMessage());
		    			System.out.println("Code = " + response.getStatusCode());
	    			}
	    			if (response.statusCode == 200) {
		    			String archiveTransferReply = response.ArchiveTransferAcceptance;
						if (bVerbose)
							System.out.println("L'identifiant de l'accusé d'acceptation vaut '" + archiveTransferReply + "'");
						else
							System.out.println("OK: '" + archiveTransferReply + "'");
	    			} else {
  					if (bVerbose)
  						System.out.println("Erreur lors de la demande d'accusé d'acceptation d'archive : '" + response.message + "'");
  					else
  						System.out.println("ERROR: (get-acceptance) '" + response.message + "'");
	    			}

	    		}
  			catch (Exception e) {
  				System.err.println("ERROR: (get-acceptance) une exception de type " + e.getClass() + " s'est produite " + e.getMessage());
  			}
		        
		  }

		  protected void usage() {
			System.out.println("Le connecteur As@laé du département du Morbihan peut être utilisé pour transmettre");
			System.out.println("une archive à un SAE As@laé, obtenir son ACK et son ATR");
			System.out.println("");
			System.out.println("Options :");
			System.out.println("Identifier le serveur avec --serveur ID_SERVEUR (voir param.config)");
			System.out.println("Obtenir plus d'informations avec --verbose ou --veryverbose");
			System.out.println("");
			System.out.println("Référez-vous à la documentatioon pour paramétrer le logiciel");
			System.out.println("");
			
			System.out.println("Usage de la commande de test d'accès au serveur");
			System.out.println("\t\t-nom de la commande : tester-serveur");
			System.out.println("");
			System.out.println("\tLe programme retourne l'information donnée par le serveur ou un message d'erreur");
			System.out.println("\n");
			
			System.out.println("Usage de la commande de test de connexion");
			System.out.println("\t\t-nom de la commande : connecter-serveur");
			System.out.println("");
			System.out.println("\tLe programme retourne le numéro de version du serveur ou un message d'erreur");
			System.out.println("\n");
			
			System.out.println("Usage de la commande de transfert : 2 (deux) paramètres sont nécessaires");
			System.out.println("\t\t-nom de la commande : transferer-archive");
			System.out.println("\t\t-fichier de bordereau SEDA : \"nom-fichier.xml\"");
			System.out.println("\t\t-fichier des archives (ZIP ou TGZ) : \"nom-fichier.zip\"");
			System.out.println("");
			System.out.println("\tLe programme indique si le transfert s'est déroulé correctement");
			System.out.println("\n");
						
			System.out.println("Usage de la commande de récupération de l'identifiant d'accusé de transmission : 2 (deux) paramètres sont nécessaires");
			System.out.println("\t\t-nom de la commande : get-acknowledge");
			System.out.println("\t\t-identifiant du transfert (bordereau SEDA) : \"ID_ALPHANUM_TRANSFERT\"");
			System.out.println("\t\t-identifiant du service versant (bordereau SEDA) : \"ID_ALPHANUM_ORIGINATING_AGENCY\"");
			System.out.println("");
			System.out.println("\tLe programme retourne l'ACK dur transfert ou un message d'erreur");
			System.out.println("\n");
						
			System.out.println("Usage de la commande de récupération de l'identifiant d'azcceptation : 2 (deux) paramètres sont nécessaires");
			System.out.println("\t\t-nom de la commande : get-acceptance");
			System.out.println("\t\t-identifiant du transfert (bordereau SEDA) : \"ID_ALPHANUM_TRANSFERT\"");
			System.out.println("\t\t-identifiant du service versant (bordereau SEDA) : \"ID_ALPHANUM_ORIGINATING_AGENCY\"");
			System.out.println("");
			System.out.println("\tLe programme retourne l'identifant d'acceptation de l'archive ou un message d'erreur");
			System.out.println("\n");
						
		}

}
