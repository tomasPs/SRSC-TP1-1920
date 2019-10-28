package SMCP;

public class SMCPMessage {
    private byte vID;
    private String sID;
    private byte type;
    private String sAttributes;
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

    public String getsAttributes() {
        return sAttributes;
    }

    public int getSizeOfPayload() {
        return sizeOfPayload;
    }

    public byte[] getSecurePayload() {
        return securePayload;
    }

    public static class Builder {
        private byte vID;
        private String sID;
        private byte type;
        private String sAttributes;
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

        public Builder attributesHash(String sAttributes) {
            this.sAttributes = sAttributes;
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

        public SMCPMessage build(){
            SMCPMessage message = new SMCPMessage();
            message.vID = this.vID;
            message.sID = this.sID;
            message.type = this.type;
            message.sAttributes = this.sAttributes;
            message.sizeOfPayload = this.sizeOfPayload;
            message.securePayload = this.securePayload;
            return message;
        }
    }
}
