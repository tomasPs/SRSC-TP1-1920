import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class ConfigFile {
	
private String sid;
private String sea;
private int seaks;
private String mode;
private String padding;
private String hash;
private String mac;
private int makks;
private File xml;

public ConfigFile(File ficheiro) {
	xml=ficheiro;
}

  public void readFile( File ficheiro) throws ParserConfigurationException, SAXException, IOException {
	  File file = ficheiro;
	  DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	  Document document = documentBuilder.parse(file);
	  sid = document.getElementsByTagName("sid").item(0).getTextContent();
	  sea = document.getElementsByTagName("sea").item(0).getTextContent();
	  seaks = Integer.parseInt(document.getElementsByTagName("seaks").item(0).getTextContent());
	  mode = document.getElementsByTagName("mode").item(0).getTextContent();
	  padding = document.getElementsByTagName("padding").item(0).getTextContent();
	  hash = document.getElementsByTagName("inthash").item(0).getTextContent();
	  mac = document.getElementsByTagName("mac").item(0).getTextContent();
	  makks = Integer.parseInt(document.getElementsByTagName("makks").item(0).getTextContent());
  }
  
  public String getSid(){
	  return sid;
  }

  public int getSeaks(){
	  return seaks;
  }
  
  public String getMode(){
	  return mode;
  }
  
  public String getSea(){
	  return sea;
  }
  
  public String getPadding(){
	  return padding;
  }
  
  public String getHash(){
	  return hash;
  }
  
  public String getMac(){
	  return mac;
  }
  
  public int getMakks(){
	  return makks;
  }
}