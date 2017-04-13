package fr.morbihan.patrickpercot.cd56_connecteur_asalae;

import java.io.IOException;
import java.io.StringReader;

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
	protected String keyToExtract;
	protected String valueOfKey;
	
	/**
	 * @return the valueOfKey
	 */
	public String getExtractedValue() {
		return valueOfKey;
	}

	protected boolean keyFound;
	protected String currentKey;
	
	public SaxExtractor() {
		this.keyToExtract = "";
		this.valueOfKey = "";
		this.keyFound = false;
		currentKey = "";
	}
	
	public void addKeyToExtract(String keyToExtract) {
		this.keyToExtract = keyToExtract;
		this.valueOfKey = "";
		this.keyFound = false;
		currentKey = "";
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
		if (localName.equals(keyToExtract)) {
			keyFound = true;
		}
	}
	
	public void endElement(String nameSpace, String localName, 
		    String qName) throws SAXException {
		currentKey = "";
	}
	
	public void characters(char[] caracteres, int debut, int longueur) {
		if (currentKey.equals(keyToExtract)) {
			valueOfKey = new String(caracteres, debut, longueur);		}
	}		

}
