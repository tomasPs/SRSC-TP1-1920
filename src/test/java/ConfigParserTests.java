import SMCP.EndpointConfiguration;
import Utils.EndpointReader;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigParserTests {

    @Test
    void test_parser() throws IOException, SAXException, ParserConfigurationException {
        InputStream in = getClass().getResourceAsStream("/security/SMCP.conf");
        EndpointReader reader = EndpointReader.getInstance(in);
        EndpointConfiguration config = reader.getEndpointConfig("224.5.6.7","9000");

        assertEquals(config.getSid(),"Chat of Secret Oriental Culinary");
        assertEquals(config.getSea(),"AES");
        assertEquals(config.getSeaks(), 256);
//        assertEquals(config.getMode(), "CBC");
        assertEquals(config.getPadding(),"PKCS5Padding");
        assertEquals(config.getIntHash(),"SHA256");
        assertEquals(config.getMac(),"HMacSHA256");
        assertEquals(config.getMakks(), 256);
    }
}
