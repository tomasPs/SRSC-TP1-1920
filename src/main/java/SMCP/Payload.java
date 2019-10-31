package SMCP;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static Utils.ParsingUtils.*;
import static Utils.ParsingUtils.writeByteArray;

public class Payload implements Comparable<Payload> {
    private String fromPeerID;
    private int seqNumber;
    private int randomNonce;
    private byte[] message;
    private byte[] integrityControl;

    private Payload() {
    }

    public Payload(String fromPeerID, int seqNumber, int randomNonce, byte[] message, byte[] integrityControl) {
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
        writeByteArray(buffer, message);
        writeByteArray(buffer, integrityControl);
        return buffer.array();
    }

    public String getFromPeerID() {
        return fromPeerID;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public int getRandomNonce() {
        return randomNonce;
    }

    public byte[] getMessage() {
        return message;
    }

    public byte[] getIntegrityControl() {
        return integrityControl;
    }

    private int getByteArrayLength() {
        return Integer.BYTES + fromPeerID.getBytes().length + Integer.BYTES + Integer.BYTES + Integer.BYTES + message
            .length + Integer.BYTES + integrityControl.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return seqNumber == payload.seqNumber &&
            randomNonce == payload.randomNonce &&
            fromPeerID.equals(payload.fromPeerID) &&
            Arrays.equals(message, payload.message) &&
            Arrays.equals(integrityControl, payload.integrityControl);
    }

    @Override
    public int compareTo(Payload msg) {
        return Integer.compare(getSeqNumber(), msg.getSeqNumber());
    }

    public static Payload parse(byte[] input) {
        Payload payload = new Payload();

        ByteBuffer buffer = ByteBuffer.wrap(input);
        payload.fromPeerID = readString(buffer);
        payload.seqNumber = buffer.getInt();
        payload.randomNonce = buffer.getInt();
        payload.message = readByteArray(buffer);
        payload.integrityControl = readByteArray(buffer);

        return payload;
    }

}
