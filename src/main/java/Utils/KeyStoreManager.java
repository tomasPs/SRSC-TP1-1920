package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Objects;

public class KeyStoreManager {
    private KeyStore keyStore;

    public KeyStoreManager() {
        this("JCEKS");
    }

    public KeyStoreManager(String type) {
        try {
            keyStore = KeyStore.getInstance(type);
            InputStream keyStoreStream = getClass().getClassLoader().getResourceAsStream("security/SMCPKeystore.jecks");
            keyStore.load(keyStoreStream, "g11srsc".toCharArray());
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public Key getSymmetricKey(
        String alias,
        String password
    ) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        return keyStore.getKey(alias+"-se", password.toCharArray());
    }

    public Key getMac(
        String alias,
        String password
    ) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        return keyStore.getKey(alias + "-mac", password.toCharArray());
    }
}
