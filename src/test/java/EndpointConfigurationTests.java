import SMCP.EndpointConfiguration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EndpointConfigurationTests {

    @Test
    void test_hash() {
        EndpointConfiguration config = new EndpointConfiguration("identified",
            "AES",
            256,
            "CBC",
            "NoPadding",
            "sha256",
            "shac256",
            128,
            "224.5.6.7:9000");
        System.out.println(config.toString());

        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("oi");
        list.add("how u do");
        System.out.println(list.toString());
    }
}
