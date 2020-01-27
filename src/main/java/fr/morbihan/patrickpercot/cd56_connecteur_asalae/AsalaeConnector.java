package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Base64;


/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Connecteur Asalaé pour le transfert d'un bordereau de versment et du ZIP des documents
 * 
 */
public class AsalaeConnector {
	/*
	 * Explications sur la récupération de l'état d'un transfert
	 * Le message pour faire la demande d'accusé de réception est : Acknowledgement
	 * Le message pour faire la demande d'état d'un tansfert accepté est : ArchiveTransferReply
	 * 
	 * En SEDA V0.2
	 *	Acknowledgement : l'accusé de réception est un ArchiveTransferReply
	 * 
	 *	ArchiveTransferReply : la réponse du SAE pour un transfert accepté est un ArchiveTransferAcceptance
	 *	ArchiveTransferReply : la réponse du SAE pour un transfert rejeté est un ArchiveTransferReply
	 *	ArchiveTransferReply : la réponse du SAE pour un transfert en attente est HTTP 500
	 * 
	 * En SEDA V1.0
	 *  Acknowledgement : l'accusé de réception est un Acknowledgement
	 *  ArchiveTransferReply : la réponse du SAE pour un transfert accepté est un ArchiveTransferReply
	 *  ArchiveTransferReply : la réponse du SAE pour un transfert rejeté est un ArchiveTransferReply
	 *	ArchiveTransferReply : la réponse du SAE pour un transfert en attente est HTTP 500
	 *
	 */
	final String responseARv02 = "ArchiveTransferReply";
	final String responseAcceptv02 = "ArchiveTransferAcceptance";
	final String tr_identifier = "TransferReplyIdentifier";
	final String dateTransfert = "Date";
	final String request = "ArchiveTransferReply";
	final String ta_identifier = "TransferAcceptanceIdentifier";

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

    public AsalaeReturn getACK(String transferIdentifier, String tranferringAgency) {
    	HashMap<String, String> namedParameters = new  HashMap<String, String>();
    	
    	namedParameters.putIfAbsent("sequence", "ArchiveTransfer");
    	namedParameters.putIfAbsent("message", "Acknowledgement");
    	namedParameters.putIfAbsent("originMessageIdentifier", transferIdentifier);
    	namedParameters.putIfAbsent("originOrganizationIdentification", tranferringAgency);
    	AsalaeReturn response = callGetMethod("/restservices/sedaMessages", "application/xml", namedParameters);
    	// Extraction de ArchiveTransferReply/TransferReplyIdentifier
    	// La date de transfert est dans ArchiveTransferReply/Date
    	if (bVeryVerbose)
    		System.out.println(response.message);
    	if (response.statusCode == 200) {
    		SaxExtractor saxExtractor= new SaxExtractor();
    		saxExtractor.addKeyToExtract(responseARv02);
    		saxExtractor.addKeyToExtract(responseAcceptv02);
    		saxExtractor.addKeyToExtract(tr_identifier);
    		saxExtractor.addKeyToExtract(dateTransfert);
    		saxExtractor.demarrerExtraction(response.message);
    		response.message = saxExtractor.getExtractedValue(tr_identifier);
    		response.dateTransfert = saxExtractor.getExtractedValue(dateTransfert);
    		response.ArchiveTransferReply = saxExtractor.getExtractedValue(responseARv02);
    		response.ArchiveTransferAcceptance = saxExtractor.getExtractedValue(responseAcceptv02);
    	}
        return response;
    }

    public AsalaeReturn getATR(String transferIdentifier, String tranferringAgency) {
    	HashMap<String, String> namedParameters = new  HashMap<String, String>();
    	
    	namedParameters.putIfAbsent("sequence", "ArchiveTransfer");
    	namedParameters.putIfAbsent("message", "ArchiveTransferReply");
    	namedParameters.putIfAbsent("originMessageIdentifier", transferIdentifier);
    	namedParameters.putIfAbsent("originOrganizationIdentification", tranferringAgency);
    	AsalaeReturn response = callGetMethod("/restservices/sedaMessages", "application/xml", namedParameters);
    	// Extraction de ArchiveTransferAcceptance/TransferAcceptanceIdentifier
    	if (bVeryVerbose)
    		System.out.println(response.message);
    	if (response.statusCode == 200) {
    		SaxExtractor saxExtractor= new SaxExtractor();
    		saxExtractor.addKeyToExtract(responseARv02);
    		saxExtractor.addKeyToExtract(responseAcceptv02);
    		saxExtractor.addKeyToExtract(ta_identifier);
    		saxExtractor.addKeyToExtract(tr_identifier);
    		saxExtractor.addKeyToExtract(dateTransfert);
    		saxExtractor.demarrerExtraction(response.message);
    		response.message = saxExtractor.getExtractedValue(ta_identifier);
    		if (response.message == null) // Version SEDA 1.0
    			response.message = saxExtractor.getExtractedValue(tr_identifier);
    		response.dateTransfert = saxExtractor.getExtractedValue(dateTransfert);
    		response.ArchiveTransferReply = saxExtractor.getExtractedValue(responseARv02);
    		response.ArchiveTransferAcceptance = saxExtractor.getExtractedValue(responseAcceptv02);
    	}
    	return response;
    }

    public AsalaeReturn getATAandCheckAlert(String transferIdentifier, String tranferringAgency) {
    	AsalaeReturn response = getATR(transferIdentifier, tranferringAgency);
    	// On a une archive qui n'a pas encore été traitée
    	// Vérification du temps depuis lequel elle est en attente
    	if (response.statusCode == 500 && response.message.equals("Transfert en cours de traitement")) {
    		response = getACK(transferIdentifier, tranferringAgency);
    		if (response.getStatusCode() == 200) {
    			Date dateTransfert = Utils.getDateTime(response.getDateTransfert());
    			long elapsedTime = Utils.getDiffToNow(dateTransfert) / 60000L;
    			long lDelai = new java.math.BigInteger(param.getDelaiAlerteSae(), 10).longValue();
    			long lDelaiMax = new java.math.BigInteger(param.getDureeEmissionAlerte(), 10).longValue() + lDelai;
    			if (elapsedTime > lDelai && (lDelaiMax == 0 || elapsedTime < lDelaiMax)) {
        			long lDepassement = elapsedTime  - lDelai;
    				response.avertissement = "ALERTE: l'archive n'a pas été intégrée. Délai dépassé de " + lDepassement + " minutes";
    			}
    		}
    	}
    	return response;
    }

    public AsalaeReturn getATAandCheckError(String transferIdentifier, String tranferringAgency) {
    	AsalaeReturn response = getATR(transferIdentifier, tranferringAgency);
    	// On a une archive qui n'a pas encore été traitée
    	// Vérification du temps depuis lequel elle est en attente
    	if (response.statusCode == 500 && response.message.equals("Transfert en cours de traitement")) {
    		response = getACK(transferIdentifier, tranferringAgency);
    		if (response.getStatusCode() == 200) {
    			Date dateTransfert = Utils.getDateTime(response.getDateTransfert());
    			long elapsedTime = Utils.getDiffToNow(dateTransfert);
    			long lDelai = new java.math.BigInteger(param.getDelaiErreurSae(), 10).longValue();
    			long lDelaiMax = new java.math.BigInteger(param.getDureeEmissionErreur(), 10).longValue() + lDelai;
    			if (elapsedTime > lDelai && (lDelaiMax == 0 || elapsedTime < lDelaiMax)) {
        			long lDepassement = elapsedTime  - lDelai;
    				response.avertissement = "ERREUR: l'archive n'a pas été intégrée. Délai dépassé de " + lDepassement + " minutes";
    			}
    		}
    	}
    	return response;
    }

    /*
    Message = Transfert en cours de traitement
    		Code = 500
    		*/
    
    
    public AsalaeReturn getArchiveIdentifiersBy(boolean duaTerminee, boolean sortConserver, String serviceVersant, String serviceArchive, String serviceProducteur) {
    	HashMap<String, String> namedParameters = new  HashMap<String, String>();
    	
    	// TODO paramètres éventuelement inconnus
    	namedParameters.putIfAbsent("DuaStatus", duaTerminee ? "terminee" : "en_cours");
    	namedParameters.putIfAbsent("AppraisalCode", sortConserver ? "conserver" : "detruire");
    	namedParameters.putIfAbsent("TransferringAgencyIdentification", serviceVersant);
    	namedParameters.putIfAbsent("ArchivalAgencyIdentification", serviceArchive);
    	namedParameters.putIfAbsent("OriginatingAgencyIdentification", serviceProducteur);
    	
    	// TODO termeiner le code
    	AsalaeReturn response = callGetMethod("/restservices/sedaMessages", "application/xml", namedParameters);
    	// Extraction de ArchiveTransferAcceptance/TransferAcceptanceIdentifier
    	if (bVeryVerbose)
    		System.out.println(response.message);
    	if (response.statusCode == 200) {
    		SaxExtractor saxExtractor= new SaxExtractor();
    		saxExtractor.addKeyToExtract(responseARv02);
    		saxExtractor.addKeyToExtract(responseAcceptv02);
    		saxExtractor.addKeyToExtract(ta_identifier);
    		saxExtractor.addKeyToExtract(tr_identifier);
    		saxExtractor.addKeyToExtract(dateTransfert);
    		saxExtractor.demarrerExtraction(response.message);
    		response.message = saxExtractor.getExtractedValue(ta_identifier);
    		if (response.message == null) // Version SEDA 1.0
    			response.message = saxExtractor.getExtractedValue(tr_identifier);
    		response.dateTransfert = saxExtractor.getExtractedValue(dateTransfert);
    		response.ArchiveTransferReply = saxExtractor.getExtractedValue(responseARv02);
    		response.ArchiveTransferAcceptance = saxExtractor.getExtractedValue(responseAcceptv02);
    	}
    	return response;
    }


	/**
	 * Appel un web service à l'aide de la méthode GET
	 * 
	 * @param webService Nom du web service complété des paramètres éventuels
	 * @return Réponse du web service au format JSON 
	 */
	protected AsalaeReturn callGetMethod(String webService) {
		return callGetMethod(webService, "application/json", null);
	}
	
	/**
	 * Appel un web service à l'aide de la méthode GET
	 * 
	 * @param webService Nom du web service complété des paramètres éventuels
	 * @return Réponse du web service au format JSON si responseFormat = "application/json"
	 * @return Réponse du web service au format XML si responseFormat = "application/xml"
	 */
	protected AsalaeReturn callGetMethod(String webService, String responseFormat
			, HashMap<String, String> namedParameters) {
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
		if ( namedParameters != null) {
			StringBuilder stUrl = new StringBuilder(url);
			boolean first = true;
			for ( String key : namedParameters.keySet())  {
				if (first) 
					stUrl.append("?");
				else
					stUrl.append("&");
				try {
					stUrl.append(key).append("=").append(URLEncoder.encode(namedParameters.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				first = false;
			}
			url = stUrl.toString();
		}

		HttpGet httpGet = new HttpGet(url);
		String auth = param.getUsername() + ":" + param.getPassword();
		String encoding = Base64.getUrlEncoder().encodeToString(auth.getBytes());
		httpGet.setHeader("Authorization", "Basic " + encoding);

		httpGet.addHeader("Accept", responseFormat);
		
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
		String encoding = Base64.getUrlEncoder().encodeToString(auth.getBytes());
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
