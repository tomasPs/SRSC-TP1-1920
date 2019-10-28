import SMCP.Payload;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayloadTests {

    @Test
    void Test_Payload()
    {
        byte[] check = {0x00, 0x01, 0x02, 0x03};
        String msg = "bla bla bla";

        Payload payload = new Payload("fromMe",5,43634,msg, check);

        Payload payload2 = Payload.parse(payload.toByteArray());

        assertEquals(payload,payload2);
    }
}
