package Utils;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import SMCP.EndpointConfiguration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EndpointReader {
    private Document document;

    private EndpointReader(InputStream configFile) {
        load(configFile);
    }

    public static EndpointReader getInstance(InputStream configFile) {
        return new EndpointReader(configFile);
    }

    private void load(InputStream configFile) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(configFile);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EndpointConfiguration getEndpointConfig(String ip, int port) {
        return getEndpointConfig(ip, Integer.toString(port));
    }

    public EndpointConfiguration getEndpointConfig(
        String ip,
        String port
    ) {
        NodeList endpointList = document.getElementsByTagName("endpoint");
        Element selectednode = null;

        for (int i = 0; i < endpointList.getLength(); i++) {
            Element temp = (Element) endpointList.item(i);
            if (temp.getAttribute("ip").equals(ip) && temp.getAttribute("port").equals(port)) {
                selectednode = temp;
                break;
            }
        }

        String sid = selectednode.getElementsByTagName("SID").item(0).getTextContent();

        String sea = selectednode.getElementsByTagName("SEA").item(0).getTextContent();

        int seaks = Integer.parseInt(selectednode.getElementsByTagName("SEAKS").item(0).getTextContent());

        String mode = selectednode.getElementsByTagName("MODE").item(0).getTextContent();

        String padding = selectednode.getElementsByTagName("PADDING").item(0).getTextContent();

        String hash = selectednode.getElementsByTagName("INTHASH").item(0).getTextContent();

        String mac = selectednode.getElementsByTagName("MAC").item(0).getTextContent();

        int makks = Integer.parseInt(selectednode.getElementsByTagName("MAKKS").item(0).getTextContent());

        String ipPort = ip + ":" + port;

        return new EndpointConfiguration(sid, sea, seaks, mode, padding, hash, mac, makks, ipPort);
    }

    public static EndpointConfiguration getTestConfig(String ip) throws Exception {
        if (ip.equals("224.5.6.7:9000")) {
            return new EndpointConfiguration(
                "Chat of Secret Oriental Culinary",
                "AES",
                256,
                "GCM",
                "PKCS5Padding",
                "SHA256",
                "HMacSHA256",
                256,
                "224.5.6.7:9000"
            );
        } else
            throw new Exception("Configuration not found");
    }
}
