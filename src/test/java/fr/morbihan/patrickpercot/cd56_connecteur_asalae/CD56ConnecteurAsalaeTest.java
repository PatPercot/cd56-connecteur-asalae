package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
		test.addTest(new CD56ConnecteurAsalaeTest("testUrlServeur"));
		test.addTest(new CD56ConnecteurAsalaeTest("testUzerName"));
		test.addTest(new CD56ConnecteurAsalaeTest("testPassword"));

		test.addTest(new CD56ConnecteurAsalaeTest("testPingAsalae"));
		test.addTest(new CD56ConnecteurAsalaeTest("testVersionAsalae"));

		test.addTest(new CD56ConnecteurAsalaeTest("testGetAck"));
		test.addTest(new CD56ConnecteurAsalaeTest("testGetATR"));


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
		System.out.println("Testing Chargement fichier de configuration");
		if (initConfigFile()) {
		} else {
			assertFalse("Chargement du fichier dans un emplacement connu", false);
		}
	}

	public void testUrlServeur() {
		if (initConfigFile()) {
			System.out.println("Testing testUrlServeur");
			assertEquals("http://sae.morbihan.fr", param.getUrlAsalae());
		}
	}

	public void testUzerName() {
		if (initConfigFile()) {
			System.out.println("Testing testUzerName");
			assertEquals("web-service", param.getUsername());
		}
	}

	public void testPassword() {
		if (initConfigFile()) {
			System.out.println("Testing testPassword");
			String passwd = param.getPassword();
			String message = "Le mot de passe ne doit pas être vide";
			assertNotNull(message, passwd);
			assertFalse(message, passwd.isEmpty());
		}
	}




	/*
	 * Test pour vérification du bon fonctionnement de la connexion 
	 * 
	 */
	public void testPingAsalae() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

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
	public void testVersionAsalae() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("Testing Appel testVersionAsalae");
			try {
				AsalaeReturn response = http.TestVersionAsalae();
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().startsWith("{\"application\":\"as@lae\",\"denomination\":\"\",\"version\":\"V1.6.2\"}"));
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
	 * Test pour vérification du bon fonctionnement de getAck
	 * 
	 */
	public void testGetAck() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("Testing Appel testGetAck");
			try {
				// AsalaeReturn response = http.getACK("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				AsalaeReturn response = http.getACK("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");

				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().contains("<Acknowledgement xmlns=\"fr:gouv:culture:archivesdefrance:seda:v1.0\">"));

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
	public void testGetATR() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("Testing Appel testGetATR");
			try {
				// AsalaeReturn response = http.getATR("DEP56_PESV2_WSJAVA_0000000002", "225600014_122");
				AsalaeReturn response = http.getATR("MATRICES_CADASTRALES_60679b01e66ed25c03713c4a43056174fc6c25da1538c28514840f88d9fafbc3@2017-03-01T17:31:11.0000048Z", "FRCOL_840933");

				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
				assert(response.getMessage().contains("<ArchiveTransferReply xmlns=\"fr:gouv:culture:archivesdefrance:seda:v1.0\">"));

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
	 * Test pour vérification du bon fonctionnement des HTTPPOST
	 * 
	 */
	public void testPOSTAsalae() {
		ConfigFile param = new ConfigFile();
		if (param.loadFile("param.config.test")) {
			AsalaeConnector http = new AsalaeConnector(param);
			http.setVeryVerbose(true);

			System.out.println("Testing Appel TestPOSTAsalae");
			System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
			try {
				AsalaeReturn response = http.callPostDocuments("data/bordereau-PESV2_DEP56-1.xml"
						, "data/mandat-et-ar-1.zip");
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
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

			System.out.println("Testing Appel TestPOSTAsalae");
			System.out.println("Working Directory = " +
					System.getProperty("user.dir"));
			try {
				AsalaeReturn response = http.callPostDocuments("data/bordereau-PESV2_DEP56-erreur.xml"
						, "data/mandat-et-ar-erreur.zip");
				//print result
				System.out.println("Message = " + response.getMessage());
				System.out.println("Code = " + response.getStatusCode());
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

}
