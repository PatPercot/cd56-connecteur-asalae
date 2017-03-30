package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.File;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.commons.codec.binary.Base64;

import fr.morbihan.patrickpercot.cd56_connecteur_asalae.ConfigFile;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Connecteur Asalaé pour le transfert d'un bordereau de versment et du ZIP des documents
 * 
 */
public class AsalaeConnector {
	private ConfigFile param;

	private boolean bVerbose = false;
	private boolean bVeryVerbose = false;

	private String UrlSedaMessages;
	private String User;
	private String Pwd;
	private String BaseURI;

	public String getUrlSedaMessages() {
		return UrlSedaMessages;
	}
	public void setUrlSedaMessages(String urlSedaMessages) {
		UrlSedaMessages = urlSedaMessages;
	}


	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}


	public String getPwd() {
		return Pwd;
	}
	public void setPwd(String pwd) {
		Pwd = pwd;
	}


	public String getBaseURI() {
		return BaseURI;
	}
	public void setBaseURI(String baseURI) {
		BaseURI = baseURI;
	}

	public AsalaeConnector(ConfigFile param) {
		this.param = param;
	}
	
    public AsalaeConnector(String UrlSedaMessages, String User, String Pwd, String BaseURI) {
        this.UrlSedaMessages = UrlSedaMessages;
        this.User = User;
        this.Pwd = Pwd;
        this.BaseURI = BaseURI;
    }


	/**
	 * Pour autoriser ou interdire les traces dans la console
	 * @param bVerbose false = mode silencieux ou true = mode verbeux
	 */
	public void setVerbose(boolean bVerbose) {
		this.bVerbose = bVerbose;
	}
	

	/**
	 * @param bVeryVerbose the bVeryVerbose to set
	 */
	public void setVeryVerbose(boolean bVeryVerbose) {
		this.bVeryVerbose = bVeryVerbose;
		this.bVerbose = bVeryVerbose;
	}

    public AsalaeReturn TestPingAsalae() {
        return callGetMethod("/restservices/ping");
    }

    
    public AsalaeReturn TestVersionAsalae() {
        return callGetMethod("/restservices/versions");
    }

    
	/**
	 * Appel un web service à l'aide de la méthode GET
	 * 
	 * @param webService Nom du web service complété des paramètres éventuels
	 * @return Réponse du web service au format JSON 
	 */
	protected AsalaeReturn callGetMethod(String webService) {
		AsalaeReturn asalaeRet = new AsalaeReturn();
		
		String url = param.getUrlAsalae() + webService;
		/*
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
			new AuthScope(param.getAuthRealm(), AuthScope.ANY_PORT),
			//	new AuthScope(null, AuthScope.ANY_PORT),
		    new UsernamePasswordCredentials(param.getUsername(), param.getPassword()));
		CloseableHttpClient httpClient =  HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
                */
		CloseableHttpClient httpClient =  HttpClients.custom()
                .setDefaultCredentialsProvider(null)
                .build();
		HttpGet httpGet = new HttpGet(url);
		String auth = param.getUsername() + ":" + param.getPassword();
		String encoding = Base64.encodeBase64URLSafeString(auth.getBytes());
		httpGet.setHeader("Authorization", "Basic " + encoding);

		httpGet.addHeader("Accept", "application/json");
		
		CloseableHttpResponse response;
		String resp = "";
		try {
			if (bVeryVerbose) {
				System.out.println("Request : " + httpGet.toString());
				for (Header header : httpGet.getAllHeaders()) {
					System.out.println("\tHeader : " + header.toString());				
				}
				
			}

			response = httpClient.execute(httpGet);
	        if (bVeryVerbose) {
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + response);
	        }
	        asalaeRet.setStatusCode(response.getStatusLine().getStatusCode());
	        if (bVeryVerbose) {
	        	if (response.getStatusLine().getStatusCode() != 200) {
    				System.out.println("Failed : HTTP error code : "
                            + response.getStatusLine().getStatusCode());
    	        }
            }
			HttpEntity responseEntity = response.getEntity();
			asalaeRet.setMessage(EntityUtils.toString(responseEntity));
	        if (bVeryVerbose)
	        	System.out.println(resp);
		} catch (ClientProtocolException e) {
			System.err.println("ERROR: (callGetMethod) une exception de type ClientProtocolException s'est produite : '" + e.getMessage() + "'" );
		    return null;
		} catch (IOException e) {
			System.err.println("ERROR: (callGetMethod) une exception de type IOException s'est produite : '" + e.getMessage() + "'" );
		    return null;
		}

		return asalaeRet;
	}

	
	/**
	 * Appel du web service "restservices/sedaMessages"  
	 * 
	 * @param bordereauFile nom du fichier de bordereau de transfert
	 * @param zipFile nom du fichier d'archive (ZIP ou TGZ)
	 * @return Réponse du web service au format JSON 
	 */
	protected AsalaeReturn callPostDocuments(String bordereauFile, String zipFile) {
		AsalaeReturn asalaeRet = new AsalaeReturn();
		/*
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
			    new AuthScope(param.getAuthRealm(), AuthScope.ANY_PORT), 
			    new UsernamePasswordCredentials(param.getUsername(), param.getPassword()));
		CloseableHttpClient httpClient =  HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
                */
		CloseableHttpClient httpClient =  HttpClients.custom()
                .setDefaultCredentialsProvider(null)
                .build();
		HttpPost httpPost = new HttpPost(param.getUrlAsalae() + "/restservices/sedaMessages");
		String auth = param.getUsername() + ":" + param.getPassword();
		String encoding = Base64.encodeBase64URLSafeString(auth.getBytes());
		httpPost.setHeader("Authorization", "Basic " + encoding);

		httpPost.addHeader("Accept", "application/json");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("seda_message", new File(bordereauFile));
		builder.addBinaryBody("attachments", new File(zipFile));
		
		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);
		CloseableHttpResponse response;
		String resp = "";
		try {
			if (bVeryVerbose) {
				System.out.println("Request : " + httpPost.toString());
				for (Header header : httpPost.getAllHeaders()) {
					System.out.println("\tHeader : " + header.toString());				
				}
			}
			
			response = httpClient.execute(httpPost);
			asalaeRet.setStatusCode(response.getStatusLine().getStatusCode());
	        if (bVeryVerbose) {
	        	if (response.getStatusLine().getStatusCode() != 200) {
    				System.out.println("Failed : HTTP error code : "
                            + response.getStatusLine().getStatusCode());
    	        }
            }
			HttpEntity responseEntity = response.getEntity();
			asalaeRet.setMessage(EntityUtils.toString(responseEntity));
			if (bVeryVerbose)
	        	System.out.println(resp);
		} catch (ClientProtocolException e) {
			System.err.println("ERROR: (callPostDocuments) une exception de type ClientProtocolException s'est produite : '" + e.getMessage() + "'" );
		    return null;
		} catch (IOException e) {
			System.err.println("ERROR: (callPostDocuments) une exception de type IOException s'est produite : '" + e.getMessage() + "'" );
		    return null;
		}
	    return asalaeRet;
	}

}
