package Utils;

import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GeneratorUtils {
    public static IvParameterSpec generateIV(int size) throws NoSuchAlgorithmException {
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[size];
        randomSecureRandom.nextBytes(iv);

        return new IvParameterSpec(iv);
    }

    public static int generateSecureInt() throws NoSuchAlgorithmException {
        SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
        return randomSecureRandom.nextInt();
    }
}
