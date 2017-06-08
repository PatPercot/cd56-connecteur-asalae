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
			assertTrue(passwd.startsWith("78b1b1a44976"));
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
