package SMCP;

import Utils.ParsingUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static Utils.ParsingUtils.*;

public class SMCPMessageWithIV extends SMCPMessage {
    private byte[] IV;

    public SMCPMessageWithIV(
        byte vID,
        String sID,
        byte[] sAttributesHash,
        byte[] securePayload,
        byte[] fastSecurePayloadCheck,
        byte[] IV
    ) {
        super(vID, sID, MessageType.MessageWithIV, sAttributesHash, securePayload, fastSecurePayloadCheck);
        this.IV = IV;
    }

    public byte[] getIV() {
        return IV;
    }

    public byte[] toByteArray() {
        byte[] message = super.toByteArray();
        byte[] msgWithIV = new byte[message.length + IV.length + Integer.BYTES];
        ByteBuffer buffer = ByteBuffer.wrap(msgWithIV);

        buffer.put(message);
        writeByteArray(buffer, IV);
        return buffer.array();
    }

    public static SMCPMessageWithIV parse(byte[] input) {
        ByteBuffer buffer = ByteBuffer.wrap(input);
        byte vId = buffer.get();
        String sID = readString(buffer);
        MessageType type = MessageType.fromTypeCode(buffer.get());
        byte[] sAttributesHash = readByteArray(buffer);
        byte[] securePayload = readByteArray(buffer);
        byte[] fastSecurePayloadCheck = readByteArray(buffer);
        byte[] IV = readByteArray(buffer);
        return new SMCPMessageWithIV(vId, sID, sAttributesHash, securePayload, fastSecurePayloadCheck, IV);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SMCPMessageWithIV that = (SMCPMessageWithIV) o;
        return Arrays.equals(IV, that.IV);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(IV);
        return result;
    }
}
