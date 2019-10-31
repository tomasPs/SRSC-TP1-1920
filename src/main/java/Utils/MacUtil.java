package Utils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class MacUtil {
    public static Mac getInstance(String alg) throws NoSuchAlgorithmException, NoSuchProviderException {
        return Mac.getInstance(alg, "BC");
    }
}
