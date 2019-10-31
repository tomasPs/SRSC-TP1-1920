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

    public static EndpointConfiguration readFile(
        File ficheiro,
        String ip
    ) throws ParserConfigurationException, SAXException, IOException {
        EndpointConfiguration config;

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
        String ip
    ) throws ParserConfigurationException, SAXException, IOException {      
    	
		EndpointConfiguration config;
		
		String sid;
		String sea;
		int seaks;
		String mode;
		String padding; 
		String hash;
		String mac;
		int makks;
		
		JSONParser parser = new JSONParser();
		
		JSONObject jsonobject = parser.parse(new FileReader("SMCP.json"));
		
		sid= jsonobject.getJSONObject(ip).getString("sid");
		
		sea= jsonobject.getJSONObject(ip).getString("sea");
		
		seaks= Integer.parseInt(jsonobject.getJSONObject(ip).getString("sid"));
		
		mode= jsonobject.getJSONObject(ip).getString("mode");
		
		padding= jsonobject.getJSONObject(ip).getString("padding");
		
		hash= jsonobject.getJSONObject(ip).getString("hash");
		
		mac= jsonobject.getJSONObject(ip).getString("mac");
		
		makks= Integer.parseInt(jsonobject.getJSONObject(ip).getString("makks"));
        

        config = new EndpointConfiguration(ip, sid, sea, seaks, mode, padding, hash, mac, makks);
        return config;
    }
}

    }

    public static EndpointConfiguration getTestConfig(
        String ip
    ) throws Exception {
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
