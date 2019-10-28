package SMCP;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static Utils.ParsingUtils.*;

public class SMCPMessage {
    private byte vID;
    private String sID;
    private byte type;
    private byte[] sAttributesHash;
    private byte[] securePayload;
    private byte[] fastSecurePayloadCheck;

    private SMCPMessage() {
    }

    public byte getvID() {
        return vID;
    }

    public String getsID() {
        return sID;
    }

    public byte getType() {
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
        buffer.put(type);
        buffer.put(sAttributesHash);
        writeByteArray(buffer, securePayload);
        writeByteArray(buffer, fastSecurePayloadCheck);

        return buffer.array();
    }

    public int getByteArrayLength() {
        return 2 + Integer.BYTES + this.sID.getBytes().length
            + sAttributesHash.length + Integer.BYTES + securePayload.length + Integer.BYTES + fastSecurePayloadCheck.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMCPMessage message = (SMCPMessage) o;
        return vID == message.vID &&
            type == message.type &&
            sID.equals(message.sID) &&
            Arrays.equals(sAttributesHash, message.sAttributesHash) &&
            Arrays.equals(securePayload, message.securePayload) &&
            Arrays.equals(fastSecurePayloadCheck, message.fastSecurePayloadCheck);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(vID, sID, type);
        result = 31 * result + Arrays.hashCode(sAttributesHash);
        result = 31 * result + Arrays.hashCode(securePayload);
        result = 31 * result + Arrays.hashCode(fastSecurePayloadCheck);
        return result;
    }

    public static SMCPMessage parse(byte[] input) {
        SMCPMessage message = new SMCPMessage();

        ByteBuffer buffer = ByteBuffer.wrap(input);
        message.vID = buffer.get();
        message.sID = readString(buffer);
        message.type = buffer.get();
        message.sAttributesHash = readByteArray(buffer, 32);
        int sizeOfPayload = buffer.getInt();
        message.securePayload = readByteArray(buffer, sizeOfPayload);
        int sizeOfPayloadCheck = buffer.getInt();
        message.fastSecurePayloadCheck = readByteArray(buffer, sizeOfPayloadCheck);

        return message;
    }

    public static class Builder {
        private byte vID;
        private String sID;
        private byte type;
        private byte[] sAttributesHash;
        private byte[] securePayload;
        private byte[] fastSecurePayloadCheck;

        public Builder(byte vID) {
            this.vID = vID;
        }

        public Builder identifiedBy(String sID) {
            this.sID = sID;
            return this;
        }

        public Builder ofMessageType(byte type) {
            this.type = type;
            return this;
        }

        public Builder integrityCheckedBy(byte[] sAttributesHash) {
            this.sAttributesHash = sAttributesHash;
            return this;
        }

        public Builder withPayload(byte[] securePayload) {
            this.securePayload = securePayload;
            return this;
        }

        public Builder payloadCheckedBy(byte[] fastSecurePayloadCheck) {
            this.fastSecurePayloadCheck = fastSecurePayloadCheck;
            return this;
        }

        public SMCPMessage build() {
            SMCPMessage message = new SMCPMessage();
            message.vID = this.vID;
            message.sID = this.sID;
            message.type = this.type;
            message.sAttributesHash = this.sAttributesHash;
            message.securePayload = this.securePayload;
            message.fastSecurePayloadCheck = this.fastSecurePayloadCheck;
            return message;
        }
    }
}
