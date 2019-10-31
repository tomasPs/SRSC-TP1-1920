package SMCP;

import Utils.HashUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

    private byte[] hashValue;
    private Logger logger = Logger.getLogger(EndpointConfiguration.class.getName());

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
        this.sid = sid;
        this.sea = sea;
        this.seaks = seaks;
        this.mode = mode;
        this.padding = padding;
        this.intHash = intHash;
        this.mac = mac;
        this.makks = makks;
        this.ipPort = ipPort;

        hashValue = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointConfiguration that = (EndpointConfiguration) o;
        return seaks == that.seaks &&
            makks == that.makks &&
            sid.equals(that.sid) &&
            sea.equals(that.sea) &&
            mode.equals(that.mode) &&
            padding.equals(that.padding) &&
            intHash.equals(that.intHash) &&
            mac.equals(that.mac) &&
            ipPort.equals(that.ipPort);
    }

    public byte[] getHashValue() throws NoSuchProviderException, NoSuchAlgorithmException {
        if (hashValue != null)
            return hashValue;

        List<String> attributeList = new ArrayList<>(10);
        attributeList.add(sid);
        attributeList.add(sea);
        attributeList.add(Integer.toString(seaks));
        attributeList.add(mode);
        attributeList.add(padding);
        attributeList.add(intHash);
        attributeList.add(mac);
        attributeList.add(Integer.toString(makks));
        attributeList.add(ipPort);

        MessageDigest hash = HashUtil.getInstance(this.intHash);
        hashValue = hash.digest(attributeList.toString().getBytes());
        logger.info("Generated hash value for config");
        return hashValue;
    }
}
