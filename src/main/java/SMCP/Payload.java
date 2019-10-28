package SMCP;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static Utils.ParsingUtils.*;
import static Utils.ParsingUtils.writeByteArray;

public class Payload {
    private String fromPeerID;
    private int seqNumber;
    private int randomNonce;
    private String message;
    private byte[] integrityControl;

    private Payload() {
    }

    public Payload(String fromPeerID, int seqNumber, int randomNonce, String message, byte[] integrityControl) {
        this.fromPeerID = fromPeerID;
        this.seqNumber = seqNumber;
        this.randomNonce = randomNonce;
        this.message = message;
        this.integrityControl = integrityControl;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteArrayLength());
        writeString(buffer, fromPeerID);
        buffer.putInt(seqNumber);
        buffer.putInt(randomNonce);
        writeString(buffer, message);
        writeByteArray(buffer, integrityControl);
        return buffer.array();
    }

    private int getByteArrayLength() {
        return Integer.BYTES + fromPeerID.getBytes().length + Integer.BYTES + Integer.BYTES + Integer.BYTES + message
            .getBytes().length + Integer.BYTES + integrityControl.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return seqNumber == payload.seqNumber &&
            randomNonce == payload.randomNonce &&
            fromPeerID.equals(payload.fromPeerID) &&
            message.equals(payload.message) &&
            Arrays.equals(integrityControl, payload.integrityControl);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fromPeerID, seqNumber, randomNonce, message);
        result = 31 * result + Arrays.hashCode(integrityControl);
        return result;
    }

    public static Payload parse(byte[] input) {
        Payload payload = new Payload();

        ByteBuffer buffer = ByteBuffer.wrap(input);
        payload.fromPeerID = readString(buffer);
        payload.seqNumber = buffer.getInt();
        payload.randomNonce = buffer.getInt();
        payload.message = readString(buffer);
        int hashLength = buffer.getInt();
        payload.integrityControl = readByteArray(buffer, hashLength);

        return payload;
    }
}
