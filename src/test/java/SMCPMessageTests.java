import SMCP.SMCPMessage;
import SMCP.SMCPMessageWithIV;
import Utils.HashUtil;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SMCPMessageTests {

    @Test
    void Test_Parse() throws NoSuchProviderException, NoSuchAlgorithmException {
        byte[] payload = {0x00, 0x01, 0x02, 0x03};
        MessageDigest hash = HashUtil.getSHA256Instance();
        hash.update("stuff".getBytes());

        SMCPMessage message =
            new SMCPMessage(
                (byte) 0,
                "hello",
                hash.digest(),
                payload,
                hash.digest()
            );

        byte[] messageBytes = message.toByteArray();
        SMCPMessage message2 = SMCPMessage.parse(messageBytes);

        assertEquals(message, message2);
    }

    @Test
    void Test_Parse_WithIV() throws NoSuchProviderException, NoSuchAlgorithmException {
        byte[] payload = {0x00, 0x01, 0x02, 0x03};
        MessageDigest hash = HashUtil.getSHA256Instance();
        hash.update("stuff".getBytes());

        SMCPMessageWithIV message =
            new SMCPMessageWithIV(
                (byte) 0,
                "hello",
                hash.digest(),
                payload,
                hash.digest(),
                new byte[]{0, 5, 3, 2}
            );

        byte[] messageBytes = message.toByteArray();
        SMCPMessageWithIV message2 = SMCPMessageWithIV.parse(messageBytes);

        assertEquals(message, message2);
    }
}
