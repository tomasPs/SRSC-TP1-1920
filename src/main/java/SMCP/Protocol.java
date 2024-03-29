package SMCP;

import Utils.KeyStoreManager;
import Utils.MacUtil;
import Utils.SymmetricEncryptionUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static Utils.GeneratorUtils.generateIV;

public class Protocol {
    private EndpointConfiguration configuration;
    private String params;
    private Map<String, Boolean> cachedIVCheck;

    private Logger logger = Logger.getLogger(Protocol.class.getName());
    KeyStoreManager manager;

    public Protocol(EndpointConfiguration configuration) {
        this.configuration = configuration;
        this.params = configuration.getSea() + "/" + configuration.getMode() + "/" + configuration.getPadding();
        this.cachedIVCheck = new HashMap<>();
        initializeCachedIVChecks();
        manager = new KeyStoreManager();
    }

    private void initializeCachedIVChecks() {
        this.cachedIVCheck.put("ECB", false);
        this.cachedIVCheck.put("CBC", true);
        this.cachedIVCheck.put("CFB", true);
        this.cachedIVCheck.put("GCM", true);
        this.cachedIVCheck.put("CTR", true);
        this.cachedIVCheck.put("OFB", true);
        this.cachedIVCheck.put("PCBC", true);
    }

    public SMCPMessage encryptPayload(
        SMCPMessage message
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException, InvalidAlgorithmParameterException {
        KeyStoreManager manager = new KeyStoreManager();

        Cipher cipher = SymmetricEncryptionUtil.getInstance(params);
        Key k = manager.getSymmetricKey(configuration.getIpPort(), configuration.getIpPort());

        if (needsIV()) {
            IvParameterSpec ivSpec = generateIV(cipher.getBlockSize());
            message = toMessageWithIV(message, ivSpec.getIV());
            cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, k);
        }

        byte[] payload = message.getSecurePayload();

        byte[] cipherText = new byte[cipher.getOutputSize(payload.length)];
        int ctLength = cipher.update(payload, 0, payload.length, cipherText, 0);
        cipher.doFinal(cipherText, ctLength);

        message.setSecurePayload(cipherText);

        return message;
    }

    private SMCPMessageWithIV toMessageWithIV(SMCPMessage message, byte[] IV) {
        return new SMCPMessageWithIV(
            message.getvID(),
            message.getsID(),
            message.getsAttributesHash(),
            message.getSecurePayload(),
            message.getFastSecurePayloadCheck(),
            IV
        );
    }

    public boolean needsIV() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        if (cachedIVCheck.containsKey(configuration.getMode()))
            return cachedIVCheck.get(configuration.getMode());

        logger.warning("needsIv(): Mode '" + configuration.getMode() + "' not found in cache");

        String p = configuration.getSea() + "/" + configuration.getMode() + "/" + configuration.getPadding();
        Cipher cipher = SymmetricEncryptionUtil.getInstance(p);

        byte[] keyBytes = new byte[configuration.getSeaks() / 8];
        SecretKeySpec key = new SecretKeySpec(keyBytes, configuration.getSea());

        byte[] input = new byte[keyBytes.length];

        try {
            //Encrypt
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLen = cipher.update(input, 0, input.length, cipherText, 0);
            cipher.doFinal(cipherText, ctLen);

            //Decrypt
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] clearText = new byte[cipher.getOutputSize(cipherText.length)];
            ctLen = cipher.update(cipherText, 0, cipherText.length, clearText, 0);
            cipher.doFinal(clearText, ctLen);
        } catch (InvalidKeyException e) {
            if (e.getMessage().equals("no IV set when one expected")) {
                this.cachedIVCheck.put(configuration.getMode(), true);
                return true;
            } else
                throw e;
        }
        this.cachedIVCheck.put(configuration.getMode(), false);
        return false;
    }

    public SMCPMessage decryptPayload(SMCPMessage message) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        KeyStoreManager manager = new KeyStoreManager();

        Cipher cipher = SymmetricEncryptionUtil.getInstance(params);
        Key k = manager.getSymmetricKey(configuration.getIpPort(), configuration.getIpPort());

        if (message instanceof SMCPMessageWithIV) {
            byte[] iv = ((SMCPMessageWithIV) message).getIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, k);
        }

        byte[] cPayload = message.getSecurePayload();

        byte[] plainText = new byte[cipher.getOutputSize(cPayload.length)];
        int ptLength = cipher.update(cPayload, 0, cPayload.length, plainText, 0);
        cipher.doFinal(plainText, ptLength);

        Payload clearPayload = Payload.parse(plainText);
        message.setSecurePayload(clearPayload.toByteArray());

        return message;
    }

    public byte[] getMac(SMCPMessage msg) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException {
        byte[] bytes = msg.toByteArray();
        Key k = manager.getMacKey(configuration.getIpPort(), configuration.getIpPort());
        Mac mac = MacUtil.getInstance(configuration.getMac());

        mac.init(k);
        mac.update(bytes);

        return mac.doFinal();
    }

    public boolean verifyMac(SMCPMessage msg) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException {
        Key k = manager.getMacKey(configuration.getIpPort(), configuration.getIpPort());
        Mac mac = MacUtil.getInstance(configuration.getMac());

        byte[] receivedMac = msg.getFastSecurePayloadCheck();

        msg.setFastSecurePayloadCheck(new byte[0]);

        byte[] newMac = getMac(msg);
        msg.setFastSecurePayloadCheck(receivedMac);

        return MessageDigest.isEqual(receivedMac,newMac);
    }
}
