package Utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import SMCP.EndpointConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EndpointReader {

    public EndpointConfiguration readFile(
        File ficheiro,
        String ip
    ) throws ParserConfigurationException, SAXException, IOException {
        EndpointConfiguration config;

        File file = ficheiro;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        NodeList tempdoc = document.getElementsByTagName(ip);

        Element selectedNode = (Element) tempdoc.item(0);

        String ipport = selectedNode.getElementsByTagName("ipmc:port").item(0).getTextContent();

        String sid = selectedNode.getElementsByTagName("sid").item(0).getTextContent();

        String sea = selectedNode.getElementsByTagName("sea").item(0).getTextContent();

        int seaks = Integer.parseInt(selectedNode.getElementsByTagName("seaks").item(0).getTextContent());

        String mode = selectedNode.getElementsByTagName("mode").item(0).getTextContent();

        String padding = selectedNode.getElementsByTagName("padding").item(0).getTextContent();

        String hash = selectedNode.getElementsByTagName("inthash").item(0).getTextContent();

        String mac = selectedNode.getElementsByTagName("mac").item(0).getTextContent();

        int makks = Integer.parseInt(selectedNode.getElementsByTagName("makks").item(0).getTextContent());

        config = new EndpointConfiguration(ipport, sid, sea, seaks, mode, padding, hash, mac, makks);
        return config;
    }
}
