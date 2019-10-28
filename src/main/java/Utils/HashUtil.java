package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class HashUtil {
    public MessageDigest getInstance(String algorithm) throws NoSuchProviderException, NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm, "BC");
    }

    public MessageDigest getSHA256Instance() throws NoSuchAlgorithmException, NoSuchProviderException {
        return MessageDigest.getInstance("SHA256", "BC");
    }
}
