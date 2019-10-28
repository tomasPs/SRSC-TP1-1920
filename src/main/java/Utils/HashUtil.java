package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static void main(String[] args) {

    }

    public byte[] hashWithSHA256(byte[] input) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA256");

            hash.update(input);

            return hash.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
