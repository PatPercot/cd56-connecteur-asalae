package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import fr.morbihan.patrickpercot.cd56_connecteur_asalae.ConfigFile;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 *
 */
/**
 * Unit test for cd56-connecteur-asalae
 */
public class CD56ConnecteurAsalaeTest 
extends TestCase
{

	public ConfigFile param;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public CD56ConnecteurAsalaeTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		TestSuite test = new TestSuite();
		test.addTest(new CD56ConnecteurAsalaeTest("testConfigFile"));

		test.addTest(new CD56ConnecteurAsalaeTest("testUrlServeurDefaut"));
		test.addTest(new CD56ConnecteurAsalaeTest("testUzerNameDefaut"));
		test.addTest(new CD56ConnecteurAsalaeTest("testPasswordDefaut"));

		test.addTest(new CD56ConnecteurAsalaeTest("testUrlServeurSaeInt"));
		test.addTest(new CD56ConnecteurAsalaeTest("testUzerNameSaeInt"));
		test.addTest(new CD56ConnecteurAsalaeTest("testPasswordSaeInt"));

		test.addTest(new CD56ConnecteurAsalaeTest("testPingAsalaeParDefaut"));
		test.addTest(new CD56ConnecteurAsalaeTest("testPingAsalaeSaeInt"));

		test.addTest(new CD56ConnecteurAsalaeTest("testVersionAsalaeParDefaut"));
		test.addTest(new CD56ConnecteurAsalaeTest("testVersionAsalaeSaeInt"));

		test.addTest(new CD56ConnecteurAsalaeTest("testConnecteurParDefaut"));
		test.addTest(new CD56ConnecteurAsalaeTest("testConnecteurSaeInt"));

		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcknowledgeOk"));
		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcceptanceOk"));

		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcknowledgeRejete"));
		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcceptanceRejete"));

		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcknowledgeNotOk"));
		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcceptanceNotOk"));

		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcknowledgeEnAttente"));
		test.addTest(new CD56ConnecteurAsalaeTest("testGetAcceptanceEnAttente"));

		/*
        test.addTest(new CD56ConnecteurAsalaeTest("testPOSTAsalae"));
        test.addTest(new CD56ConnecteurAsalaeTest("testPOSTAsalaeErreur"));
		 */      
		return test;
	}

	/**
	 * Rigourous Test :-)
	 */
	/*
    public void testApp()
    {
        assertTrue( true );
    }
	 */

	/**
	 * 
	 * Fonction utilitaire
	 */
	protected boolean initConfigFile() {
		param = new ConfigFile();
		return param.loadFile("param.config.test");
	}

	/**
	 * Test pour la vérification du chargement du fichier de configuration 
	 * 
	 */
	public void testConfigFile() {
		System.out.println("\n\nTesting Chargement fichier de configuration");
		if (initConfigFile()) {
		} else {
			assertFalse("Chargement du fichier dans un emplacement connu", false);
		}
	}

	public void testUrlServeurDefaut() {
		if (initConfigFile()) {
			System.out.println("Testing testUrlServeur par défaut");
			assertEquals("http://sae.morbihan.fr", param.getUrlAsalae());
		}
	}

	public void testUzerNameDefaut() {
		if (initConfigFile()) {
			System.out.println("Testing testUzerName par défaut");
			assertEquals("web-service", param.getUsername());
		}
	}

	public void testPasswordDefaut() {
		if (initConfigFile()) {
			System.out.println("Testing testPassword par défaut");
			String passwd = param.getPassword();
			String message = "Le mot de passe ne doit pas être vide";
			assertNotNull(message, passwd);
			assertFalse(message, passwd.isEmpty());
		}
	}

	public void testUrlServeurSaeInt() {
		if (initConfigFile()) {
			param.selectionServeur("SAE-INT");
			System.out.println("Testing testUrlServeur SAE-INT");
			assertEquals("http://sae-int.morbihan.fr", param.getUrlAsalae());
		}
	}

	public void testUzerNameSaeInt() {
		if (initConfigFile()) {
			param.selectionServeur("SAE-INT");
			System.out.println("Testing testUzerName SAE-INT");
			assertEquals("web-service", param.getUsername());
		}
	}

	public void testPasswordSaeInt() {
		if (initConfigFile()) {
			param.selectionServeur("SAE-INT");
			System.out.println("Testing testPassword SAE-INT");
			String passwd = param.getPassword();
			String message = "Le mot de passe ne doit pas être vide";
			assertNotNull(message, passwd);
			assertFalse(message, passwd.isEmpty());
			assertFalse(passwd.startsWith("78b1b1a44976"));
		}
	}

	
	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testPingAsalaeParDefaut() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);

			System.out.println("Testing Appel TestPingAsalae");
			try {
				AsalaeReturn response = http.TestPingAsalae();
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().startsWith("\"webservices as@lae accessibles\""));
				Assert.assertEquals("Code de retour de Ping", response.getStatusCode(), 200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}


	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testPingAsalaeSaeInt() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			param.selectionServeur("SAE-INT");
			AsalaeConnector http = new AsalaeConnector(param);

			System.out.println("Testing Appel TestPingAsalae");
			try {
				AsalaeReturn response = http.TestPingAsalae();
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().startsWith("\"webservices as@lae accessibles\""));
				Assert.assertEquals("Code de retour de Ping", response.getStatusCode(), 200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	
	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testVersionAsalaeParDefaut() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);

			System.out.println("Testing Appel testVersionAsalae");
			try {
				AsalaeReturn response = http.TestVersionAsalae();
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().startsWith("{\"application\":\"as@lae\",\"denomination\":\"\",\"version\":\"V1.6.6\"}"));
				Assert.assertEquals("Code de retour de version", response.getStatusCode(), 200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testVersionAsalaeSaeInt() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			param.selectionServeur("SAE-INT");
			AsalaeConnector http = new AsalaeConnector(param);

			System.out.println("Testing Appel testVersionAsalae");
			try {
				AsalaeReturn response = http.TestVersionAsalae();
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().startsWith("{\"application\":\"as@lae\",\"denomination\":\"\",\"version\":\"V1.6.6\"}"));
				Assert.assertEquals("Code de retour de version", response.getStatusCode(), 200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	
	
	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testConnecteurParDefaut() {
		AsalaeConnectorLauncher pmag = new AsalaeConnectorLauncher();
		String[] args = new String[1];
		args[0] = "connecter-serveur";
		pmag.execMain(args);
	}


	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testConnecteurSaeInt() {
		AsalaeConnectorLauncher pmag = new AsalaeConnectorLauncher();
		String[] args = new String[3];
		args[0] = "--serveur";
		args[1] = "SAE-INT";
		args[2] = "connecter-serveur";
		pmag.execMain(args);
	}
	
	
	/*
	 * Test pour vérification du bon fonctionnement des HTTPPOST
	 * 
	 */
	public void testPOSTAsalae() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel TestPOSTAsalae");
			System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
			try {
				AsalaeReturn response = http.callPostDocuments("data/bordereau-PESV2_DEP56-1.xml"
						, "data/mandat-et-ar-1.zip");
				response.trace();
				assert(response.getMessage().startsWith("{\"date_du_depot\":\""));
				Assert.assertEquals("Code de retour de POST", response.getStatusCode(), 200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification d'un retour en erreur des HTTPPOST
	 * 
	 */
	public void testPOSTAsalaeErreur() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);

			System.out.println("\n\nTesting Appel TestPOSTAsalae");
			System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
			try {
				AsalaeReturn response = http.callPostDocuments("data/bordereau-PESV2_DEP56-erreur.xml"
						, "data/mandat-et-ar-erreur.zip");
				response.trace();
				assert(response.getMessage().equals("Erreur de donnée du formulaire : la taille du fichier transmis \"bordereau-PESV2_DEP56-erreur.xml\" est nulle"));
				Assert.assertEquals("Code de retour de POST", response.getStatusCode(), 400);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}


	/*
	 * Test pour vérification du bon fonctionnement de getAck
	 * 
	 */
	public void testGetAcknowledgeOk() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel testGetAck");
			try {
				// AsalaeReturn response = http.getAcknowledge("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcknowledge("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcknowledge("CD56_PES_MANUEL6c07694fe0d6f1c091582d95d7d6858df48d25e880ac96eaa7f4b7968da93c9b@2017-04-10T11:48:37.0000568Z", "225600014_122");
				
				response.trace();
				assert(response.getStatusCode() == 200);
				assert(response.getAcknowledgement().equals("ATR_161"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de getATR
	 * 
	 */
	public void testGetAcceptanceOk() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("\n\nTesting Appel testGetAcceptance OK");
			try {
				// AsalaeReturn response = http.getATR("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getATR("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				// AsalaeReturn response = http.getATR("CD56_PES_MANUEL6c07694fe0d6f1c091582d95d7d6858df48d25e880ac96eaa7f4b7968da93c9b@2017-04-10T11:48:37.0000568Z", "225600014_122");
				// AsalaeReturn response = http.getATR("CD56_SONORE_9e7cb501574f5f55f18c045471cb513764983581df41a0356e76a7201f408bb5@2017-12-01T15:49:53.0000176Z", "FRCOL_632");
				// AsalaeReturn response = http.getATR("MATRICES_CADASTRALES_c2caf023c5aab2db89ecf0cdd287077e6d080708bbb266b351b9e9450b9c66aa@2018-0926-18T210:50:51.0000791Z", "FRCOL_1141681");
				AsalaeReturn response = http.getAcceptance("INT_DEPT56_DRH-SFT439dd30feb232d19b8eab48e4b99a00dd66057a710009bb76711c411d7ee13e3@2019-11-15T12:53:22.0000194Z", "FRCOL_1786450");

				response.trace();
				assert(response.getStatusCode() == 200);
				// assert(response.getMessage().equals("ATA_55"));
				// assert(response.getMessage().equals("ATA_55"));
				// assert(response.getMessage().equals("ATA_55"));
				// assert(response.getMessage().equals("ATA_295"));
				assert(response.getArchiveTransferAcceptance().equals("ATA_332"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	// Rejeté
	// CD56_PES_MANUEL342e73e2d9118479d88fd54857cb49910f3ff17e08ced0977f5d66a1018a68f6@2017-04-05T18:15:04.0000945Z
	
	/*
	 * Test pour vérification du bon fonctionnement de getAck
	 * 
	 */
	public void testGetAcknowledgeRejete() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("\n\nTesting Appel testGetAcknowledge REJET");
			try {
				// AsalaeReturn response = http.getAcknowledge("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcknowledge("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcknowledge("CD56_PES_MANUEL342e73e2d9118479d88fd54857cb49910f3ff17e08ced0977f5d66a1018a68f6@2017-04-05T18:15:04.0000945Z", "225600014_122");
				

				response.trace();
				assert(response.getStatusCode() == 200);
				assert(response.getAcknowledgement().equals("ATR_151"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de getATR
	 * 
	 */
	public void testGetAcceptanceRejete() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("\n\nTesting Appel testGetAcceptance REJET");
			try {
				// AsalaeReturn response = http.getAcceptance("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcceptance("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcceptance("CD56_PES_MANUEL342e73e2d9118479d88fd54857cb49910f3ff17e08ced0977f5d66a1018a68f6@2017-04-05T18:15:04.0000945Z", "225600014_122");

				response.trace();
				assert(response.getStatusCode() == 200);
				// assert(response.getMessage().equals("ATA_55"));
				assert(response.getArchiveTransferAcceptance().equals("ATR_162"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de getAck
	 * 
	 */
	public void testGetAcknowledgeNotOk() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel testGetAcknowledge NOT OK");
			try {
				// AsalaeReturn response = http.getAcknowledge("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcknowledge("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcknowledge("XXXCD56_PES_MANUEL68a5755aecfd02a33c88d6ff26e5ae48598ff3724e3fea3bc741c2d6c7cf6d07@2017-04-06T17:52:52.0000977Z", "225600014_122");
				

				response.trace();
				assert(response.getStatusCode() == 500);
				assert(response.getMessage().contains("Transfert d'archive à l'origine de la séquence non trouvé : XXXCD56_PES_MANUEL68a5755aecfd02a33c88d6ff26e5ae48598ff3724e3fea3bc741c2d6c7cf6d07@2017-04-06T17:52:52.0000977Z"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de getATR
	 * 
	 */
	public void testGetAcceptanceNotOk() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel testGetAcceptance NOT OK");
			try {
				// AsalaeReturn response = http.getAcceptance("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcceptance("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcceptance("CD56_PES_MANUEL68a5755aecfd02a33c88d6ff26e5ae48598ff3724e3fea3bc741c2d6c7cf6d07@2017-04-06T17:52:52.0000977Z", "225600014_122");

				response.trace();
				assert(response.getStatusCode() == 500);
				assert(response.getMessage().contains("Transfert en cours de traitement"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	
	/*
	 * Test pour vérification du bon fonctionnement de getACK
	 * 
	 */
	public void testGetAcknowledgeEnAttente() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel testGetAcknowledge en attente");
			try {
				// AsalaeReturn response = http.getAcknowledge("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcknowledge("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				// AsalaeReturn response = http.getAcknowledge("CD56_PES_MANUELe2f3c4171a69998925f93fce0d50a2de8a40711ab68d2e77668519dd82c42167@2017-04-05T17:09:16.0000688Z", "225600014_122");
				AsalaeReturn response = http.getAcknowledge("CD56_SONORE_9e7cb501574f5f55f18c045471cb513764983581df41a0356e76a7201f408bb5@2020-01-13T10:00:03.0000506Z", "FRCOL_632");

				response.trace();
				assert(response.getStatusCode() == 200);
				// assert(response.getMessage().contains("ATR_145"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	/*
	 * Test pour vérification du bon fonctionnement de getATR
	 * 
	 */
	public void testGetAcceptanceEnAttente() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(false);

			System.out.println("\n\nTesting Appel testGetAccpetance en attente");
			try {
				// AsalaeReturn response = http.getAcceptance("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				// AsalaeReturn response = http.getAcceptance("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");
				AsalaeReturn response = http.getAcceptance("CD56_PES_MANUELe2f3c4171a69998925f93fce0d50a2de8a40711ab68d2e77668519dd82c42167@2017-04-05T17:09:16.0000688Z", "225600014_122");

				response.trace();
				assert(response.getStatusCode() == 500);
				assert(response.getMessage().contains("Transfert en cours de traitement"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assertFalse(false);
			}

		} else {
			System.out.println("Le fichier de configuration est introuvable ou incomplet");
			assertFalse(false);
		}

	}

	
}
