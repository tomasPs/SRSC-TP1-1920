package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
            File keyStoreFile = new File(Objects
                .requireNonNull(getClass().getClassLoader().getResource("security/keyStore.ks")).getFile());
            FileInputStream stream = new FileInputStream(keyStoreFile);
            keyStore.load(stream, "g11srsc".toCharArray());
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public Key getKey(
        String alias,
        String password
    ) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        return keyStore.getKey(alias, password.toCharArray());
    }
}
