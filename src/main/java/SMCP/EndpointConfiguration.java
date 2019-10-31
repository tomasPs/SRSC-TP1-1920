package SMCP;

public class EndpointConfiguration {
    private String sid;
    private String sea;
    private int seaks;
    private String mode;
    private String padding;
    private String intHash;
    private String mac;
    private int makks;
    private String ipPort;

    public EndpointConfiguration(
        String sid,
        String sea,
        int seaks,
        String mode,
        String padding,
        String intHash,
        String mac,
        int makks,
        String ipPort
    ) {
        this.sea = sea;
        this.sid = sid;
        this.seaks = seaks;
        this.mode = mode;
        this.padding = padding;
        this.intHash = intHash;
        this.mac = mac;
        this.makks = makks;
        this.ipPort = ipPort;
    }

    public String getSid() {
        return sid;
    }

    public String getSea() {
        return sea;
    }

    public int getSeaks() {
        return seaks;
    }

    public String getMode() {
        return mode;
    }

    public String getPadding() {
        return padding;
    }

    public String getIntHash() {
        return intHash;
    }

    public String getMac() {
        return mac;
    }

    public int getMakks() {
        return makks;
    }

    public String getIpPort() {
        return ipPort;
    }
}
