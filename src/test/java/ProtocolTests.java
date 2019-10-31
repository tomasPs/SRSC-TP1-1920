import SMCP.EndpointConfiguration;
import SMCP.Payload;
import SMCP.Protocol;
import SMCP.SMCPMessage;
import Utils.EndpointReader;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.File;
import java.io.InputStream;
import java.security.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProtocolTests {
    @Test
    void TestEncryption() {
        InputStream in = getClass().getResourceAsStream("/security/SMCP.conf");
        EndpointReader reader = EndpointReader.getInstance(in);
        EndpointConfiguration config = reader.getEndpointConfig("224.5.6.7", "9000");

        Protocol protocol = new Protocol(config);

        byte[] input = new byte[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        Payload payload = new Payload("user2", 2, 465376, input, input);

        SMCPMessage msg = new SMCPMessage((byte) 0, "identifier", input, payload.toByteArray(), input);

        try {
            String endpoint = "224.5.6.7:9000";
            protocol.encryptPayload(msg);
            byte[] cipherText = msg.getSecurePayload().clone();
            protocol.decryptPayload(msg);
            byte[] clearText = msg.getSecurePayload().clone();
            Payload payload1 = Payload.parse(clearText);

            assertEquals(payload, payload1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void Test_NeedsIV() throws BadPaddingException, ShortBufferException, IllegalBlockSizeException {
        InputStream in = getClass().getResourceAsStream("/security/SMCP.conf");
        EndpointReader reader = EndpointReader.getInstance(in);
        EndpointConfiguration config = reader.getEndpointConfig("224.5.6.7", "9000");

        Protocol protocol = new Protocol(config);

        boolean needs = false;
        try {
            needs = protocol.needsIV();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
            e.printStackTrace();
        }

        System.out.println(needs);
    }
}
