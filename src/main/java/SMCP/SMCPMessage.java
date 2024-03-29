package SMCP;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static Utils.ParsingUtils.*;

public class SMCPMessage {
    private byte vID;
    private String sID;
    private MessageType type;
    private byte[] sAttributesHash;
    private byte[] securePayload;
    private byte[] fastSecurePayloadCheck;

    private SMCPMessage() {
    }

    protected SMCPMessage(
        byte vID,
        String sID,
        MessageType type,
        byte[] sAttributesHash,
        byte[] securePayload,
        byte[] fastSecurePayloadCheck
    ) {
        this.vID = vID;
        this.sID = sID;
        this.type = type;
        this.sAttributesHash = sAttributesHash;
        this.securePayload = securePayload;
        this.fastSecurePayloadCheck = fastSecurePayloadCheck;
    }

    public SMCPMessage(
        byte vID,
        String sID,
        byte[] sAttributesHash,
        byte[] securePayload,
        byte[] fastSecurePayloadCheck
    ) {
        this.vID = vID;
        this.sID = sID;
        this.type = MessageType.SMCPMessage;
        this.sAttributesHash = sAttributesHash;
        this.securePayload = securePayload;
        this.fastSecurePayloadCheck = fastSecurePayloadCheck;
    }

    public byte getvID() {
        return vID;
    }

    public String getsID() {
        return sID;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getsAttributesHash() {
        return sAttributesHash;
    }

    public byte[] getSecurePayload() {
        return securePayload;
    }

    public byte[] getFastSecurePayloadCheck() {
        return fastSecurePayloadCheck;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteArrayLength());
        buffer.put(vID);
        writeString(buffer, sID);
        buffer.put(type.getTypeCode());
        writeByteArray(buffer, sAttributesHash);
        writeByteArray(buffer, securePayload);
        writeByteArray(buffer, fastSecurePayloadCheck);

        return buffer.array();
    }

    private int getByteArrayLength() {
        return 2 + Integer.BYTES + this.sID.getBytes().length + Integer.BYTES
            + sAttributesHash.length + Integer.BYTES + securePayload.length + Integer.BYTES + fastSecurePayloadCheck.length;
    }

    public void setSecurePayload(byte[] securePayload) {
        this.securePayload = securePayload;
    }

    public void setFastSecurePayloadCheck(byte[] fastSecurePayloadCheck) {
        this.fastSecurePayloadCheck = fastSecurePayloadCheck;
    }

    public Payload getPayload() {
        return Payload.parse(this.securePayload);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMCPMessage message = (SMCPMessage) o;
        return vID == message.vID &&
            sID.equals(message.sID) &&
            type == message.type &&
            Arrays.equals(sAttributesHash, message.sAttributesHash) &&
            Arrays.equals(securePayload, message.securePayload) &&
            Arrays.equals(fastSecurePayloadCheck, message.fastSecurePayloadCheck);
    }

    public static SMCPMessage parse(byte[] input) {
        SMCPMessage message = new SMCPMessage();

        ByteBuffer buffer = ByteBuffer.wrap(input);
        message.vID = buffer.get();
        message.sID = readString(buffer);
        message.type = MessageType.fromTypeCode(buffer.get());
        message.sAttributesHash = readByteArray(buffer);
        message.securePayload = readByteArray(buffer);
        message.fastSecurePayloadCheck = readByteArray(buffer);

        return message;
    }

    public enum MessageType {
        SMCPMessage((byte) 0), MessageWithIV((byte) 1);

        private byte typeCode;

        MessageType(byte typeCode) {
            this.typeCode = typeCode;
        }

        public byte getTypeCode() {
            return typeCode;
        }

        public static MessageType fromTypeCode(byte type) {
            switch (type) {
                case 1:
                    return MessageWithIV;
                default:
                    return SMCPMessage;
            }
        }
    }
}
