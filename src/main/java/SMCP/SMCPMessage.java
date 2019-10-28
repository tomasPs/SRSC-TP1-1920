package SMCP;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class SMCPMessage {
    private byte vID;
    private String sID;
    private byte type;
    private byte[] sAttributesHash;
    private int sizeOfPayload;
    private byte[] securePayload;

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

    public int getSizeOfPayload() {
        return sizeOfPayload;
    }

    public byte[] getSecurePayload() {
        return securePayload;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteArrayLength());
        buffer.put(vID);
        buffer.putInt(sID.getBytes().length);
        buffer.put(sID.getBytes());
        buffer.put(type);
        buffer.put(sAttributesHash);
        buffer.putInt(sizeOfPayload);
        buffer.put(securePayload);

        return buffer.array();
    }

    public int getByteArrayLength() {
        return 2 + Integer.BYTES + this.sID.getBytes().length
            + sAttributesHash.length + Integer.BYTES + sizeOfPayload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SMCPMessage message = (SMCPMessage) o;
        return vID == message.vID &&
            type == message.type &&
            sizeOfPayload == message.sizeOfPayload &&
            sID.equals(message.sID) &&
            Arrays.equals(sAttributesHash, message.sAttributesHash) &&
            Arrays.equals(securePayload, message.securePayload);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(vID, sID, type, sizeOfPayload);
        result = 31 * result + Arrays.hashCode(sAttributesHash);
        result = 31 * result + Arrays.hashCode(securePayload);
        return result;
    }

    public static SMCPMessage parse(byte[] input) {
        SMCPMessage message = new SMCPMessage();

        ByteBuffer buffer = ByteBuffer.wrap(input);
        message.vID = buffer.get();
        message.sID = readString(buffer);
        message.type = buffer.get();
        message.sAttributesHash = readByteArray(buffer, 32);
        message.sizeOfPayload = buffer.getInt();
        message.securePayload = readByteArray(buffer, message.sizeOfPayload);

        return message;
    }

    private static byte[] readByteArray(ByteBuffer buffer, int length) {
        byte[] arr = new byte[length];
        buffer.get(arr);
        return arr;
    }

    private static String readString(ByteBuffer buffer) {
        int length = buffer.getInt();
        return new String(readByteArray(buffer, length));
    }

    public static class Builder {
        private byte vID;
        private String sID;
        private byte type;
        private byte[] sAttributesHash;
        private int sizeOfPayload;
        private byte[] securePayload;

        public Builder(byte vID) {
            this.vID = vID;
        }

        public Builder identifiedBy(String sID) {
            this.sID = sID;
            return this;
        }

        public Builder messageType(byte type) {
            this.type = type;
            return this;
        }

        public Builder attributesHash(byte[] sAttributesHash) {
            this.sAttributesHash = sAttributesHash;
            return this;
        }

        public Builder withSize(int sizeOfPayload) {
            this.sizeOfPayload = sizeOfPayload;
            return this;
        }

        public Builder payload(byte[] securePayload) {
            this.securePayload = securePayload;
            return this;
        }

        public SMCPMessage build() {
            SMCPMessage message = new SMCPMessage();
            message.vID = this.vID;
            message.sID = this.sID;
            message.type = this.type;
            message.sAttributesHash = this.sAttributesHash;
            message.sizeOfPayload = this.sizeOfPayload;
            message.securePayload = this.securePayload;
            return message;
        }
    }
}
