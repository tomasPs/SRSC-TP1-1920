import SMCP.SMCPMessage;
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
            new SMCPMessage.Builder((byte) 0x01)
                .identifiedBy("hello")
                .ofMessageType((byte) 0x02)
                .withPayload(payload)
                .integrityCheckedBy(hash.digest())
                .payloadCheckedBy(hash.digest()).build();

        byte[] messageBytes = message.toByteArray();
        SMCPMessage message2 = SMCPMessage.parse(messageBytes);

        assertEquals(message,message2);
    }
}
