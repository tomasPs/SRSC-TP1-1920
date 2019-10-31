package Utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.*;

import java.util.Map;

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
        String ip,
        String port
    ) throws ParserConfigurationException, SAXException, IOException {

        EndpointConfiguration config;

        File file = ficheiro;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        NodeList tempdoc= document.getElementsByTagName("endpoint");
        Element selectednode = null;

        for (int i = 0; i < tempdoc.getLength(); i++) {
            Element temp = (Element) tempdoc.item(i);
            if (temp.getAttribute("ip").equals(ip) && temp.getAttribute("port").equals(port)) {
                selectednode= (Element) tempdoc.item(i);
                break;
            }
        }

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

    public static EndpointConfiguration getTestConfig(String ip) throws Exception {
        if (ip.equals("224.5.6.7:9000")) {
            return new EndpointConfiguration(
                "224.5.6.7:9000",
                "Chat of Secret Oriental Culinary",
                "AES",
                256,
                "GCM",
                "PKCS5Padding",
                "SHA256",
                "HMacSHA256",
                256
            );
        } else
            throw new Exception("Configuration not found");
    }
}
