import SMCP.EndpointConfiguration;
import Utils.EndpointReader;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ConfigParserTests {

    @Test
    void test_parser() throws IOException, SAXException, ParserConfigurationException {
        File configFile = new File(getClass().getClassLoader().getResource("security/SMCP.conf").getFile());
        EndpointConfiguration config = EndpointReader.readFile(configFile,"224.5.6.7:9000");

        System.out.println(config);
    }
}
