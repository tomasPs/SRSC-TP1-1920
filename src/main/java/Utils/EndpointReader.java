import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
		
		Element selectednode=(Element) tempdoc.item(0);
		
		String ipport = selectednode.getElementsByTagName("ip").item(0).getTextContent();
		
		String sid = selectednode.getElementsByTagName("sid").item(0).getTextContent();
		
		String sea = selectednode.getElementsByTagName("sea").item(0).getTextContent();
		
		int seaks = Integer.parseInt(selectednode.getElementsByTagName("seaks").item(0).getTextContent());
		
		String mode = selectednode.getElementsByTagName("mode").item(0).getTextContent();
		
		String padding = selectednode.getElementsByTagName("padding").item(0).getTextContent();
		
		String hash = selectednode.getElementsByTagName("inthash").item(0).getTextContent();
		
		String mac = selectednode.getElementsByTagName("mac").item(0).getTextContent();
		
		int makks = Integer.parseInt(selectednode.getElementsByTagName("makks").item(0).getTextContent());

		config= new EndpointConfiguration(ipport, sid, sea, seaks, mode, padding, hash, mac, makks);
		return config;
	}
}