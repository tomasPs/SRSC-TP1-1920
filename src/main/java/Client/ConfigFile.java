import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class EndpointReader {


	public EndpointConfiguration readFile( File ficheiro, String ip) throws ParserConfigurationException, SAXException, IOException {
		EndpointConfiguration config;
		
		File file = ficheiro;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		NodeList tempdoc= document.getElementsByTagName(ip);

		config= new EndpointConfiguration(tempdoc.item(0));
		return config;
	}
}