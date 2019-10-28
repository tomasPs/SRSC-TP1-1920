import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;  
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class EndpointConfiguration {

	private Element file;
	private String ip;
	private String sea;
	private String sid;
	private int seaks;
	private String mode;
	private String padding;
	private String hash;
	private String mac;
	private int makks;
	
	public EndpointConfiguration(Node input) {
		file=(Element) input;
		
		ip = file.getElementsByTagName("ip").item(0).getTextContent();
		sid = file.getElementsByTagName("sid").item(0).getTextContent();
		sea = file.getElementsByTagName("sea").item(0).getTextContent();
		seaks = Integer.parseInt(file.getElementsByTagName("seaks").item(0).getTextContent());
		mode = file.getElementsByTagName("mode").item(0).getTextContent();
		padding = file.getElementsByTagName("padding").item(0).getTextContent();
		hash = file.getElementsByTagName("inthash").item(0).getTextContent();
		mac = file.getElementsByTagName("mac").item(0).getTextContent();
		makks = Integer.parseInt(file.getElementsByTagName("makks").item(0).getTextContent());
		
	}
	
	public String getIp(){
		return ip;
	}
	
	public String getSid(){
		return sid;
	}
	
	public String getSea(){
		return sea;
	}
	
	public int getSeaks(){
		return seaks;
	}
	
	public String getMode(){
		return mode;
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