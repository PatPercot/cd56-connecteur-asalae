package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Patrick Percot - Direction des systèmes d'information - Conseil départemental du Morbihan 
 * 
 * Parser SAX pour l'interprétation des réponses du SAE ou extraction d'informations dans le bordereau de transfert
 * 
 */
public class SaxExtractor extends DefaultHandler {
	Hashtable<String, String> extractedTags;
	ArrayList<String> keysToExtract;
	protected String currentKey;
	protected boolean bVerbose = false;
	protected boolean bVeryVerbose = false;
	
	/**
	 * @return the valueOfKey
	 */
	public String getExtractedValue(String key) {
		return extractedTags.get(key);
	}

	
	public SaxExtractor() {
		currentKey = "";
		this.extractedTags = new Hashtable<String, String>();
		this.keysToExtract = new ArrayList<String>();
	}
	
	public SaxExtractor(boolean bVerbose, boolean bVeryVerbose) {
		currentKey = "";
		this.extractedTags = new Hashtable<String, String>();
		this.keysToExtract = new ArrayList<String>();
		
		this.bVerbose= bVerbose;
		this.bVeryVerbose = bVeryVerbose;
	}
	
	public void addKeyToExtract(String keyToExtract) {
		currentKey = "";
		this.keysToExtract.add(keyToExtract);
		if (bVeryVerbose)
			System.out.println("SAX: tracking key '" + keyToExtract + "'");
	}
	
	public void demarrerExtraction(String xmlMessage) {
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser;
		try {
			saxParser = spf.newSAXParser();
			XMLReader xmlReader;
			xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(new InputSource(new StringReader(xmlMessage)));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void startDocument() throws SAXException {
		
	}
	
	public void endDocument() throws SAXException {
		
	}
	
	public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
		currentKey = localName;
	}
	
	public void endElement(String nameSpace, String localName, 
		    String qName) throws SAXException {
		currentKey = "";
	}
	
	public void characters(char[] caracteres, int debut, int longueur) {
		if (keysToExtract.contains(currentKey)) {
			String valueOfKey = new String(caracteres, debut, longueur);
			if (extractedTags.contains(currentKey)) {
				String old = extractedTags.get(currentKey);
				valueOfKey = old + valueOfKey;
			}
			extractedTags.put(currentKey, valueOfKey);
			if (bVeryVerbose)
				System.out.println("SAX: '" + currentKey + "' vaut '" + valueOfKey + "'");
		}
	}		

}
