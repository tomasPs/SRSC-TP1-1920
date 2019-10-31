package SMCP;

import Utils.EndpointReader;
import Utils.HashUtil;
import Utils.ParsingUtils;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import static SMCP.SMCPMessage.MessageType;
import static Utils.GeneratorUtils.generateSecureInt;

public class SMCPSocket extends MulticastSocket {

    private Logger logger = Logger.getLogger(SMCPSocket.class.getName());
    private EndpointConfiguration socketConfig;
    private Set<Integer> nonces;
    private MessageSequenceHandler sequenceHandler;

    public SMCPSocket() throws IOException {
        super();
    }

    public SMCPSocket(int port) throws IOException {
        super(port);
    }

    public SMCPSocket(SocketAddress bindaddr) throws IOException {
        super(bindaddr);
    }

    @Override
    public void joinGroup(InetAddress mcastaddr) throws IOException {
        super.joinGroup(mcastaddr);
        initialize(mcastaddr);
    }

    private void initialize(InetAddress mcastaddr) {
        setSocketConfig(mcastaddr);
        nonces = new HashSet<>();
        sequenceHandler = new MessageSequenceHandler();
    }

    @Override
    public void leaveGroup(InetAddress mcastaddr) throws IOException {
        super.leaveGroup(mcastaddr);
        this.socketConfig = null;
    }

    @Override
    public void send(DatagramPacket p) throws IOException {
        DataInputStream inputStream =
            new DataInputStream(new ByteArrayInputStream(
                p.getData(),
                p.getOffset(),
                p.getLength()
            ));

        long CHAT_MAGIC_NUMBER = inputStream.readLong();
        int MESSAGE = inputStream.readInt();
        String username = inputStream.readUTF();
        inputStream.close();

        //hash
        byte[] hash = new byte[0];
        try {
            MessageDigest msgDig = HashUtil.getInstance(socketConfig.getIntHash());
            hash = msgDig.digest(p.getData());
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Nonce
        int nonce = 0;
        try {
            nonce = generateSecureInt();
        } catch (NoSuchAlgorithmException e) {
            nonce = new Random().nextInt();
        }
        logger.info("Generated (" + nonce + ") nonce");
        int seq = sequenceHandler.useSequence();

        Payload payload = new Payload(username, seq, nonce, p.getData(), hash);
        logger.info("Sequence number of: " + seq);

        //sAttrbitues
        byte[] attributesHash = new byte[0];
        try {
            attributesHash = this.socketConfig.getHashValue();
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SMCPMessage msg = new SMCPMessage(
            (byte) 0,
            this.socketConfig.getSid(),
            attributesHash,
            payload.toByteArray(),
            new byte[0]
        );

        Protocol protocol = new Protocol(socketConfig);
        try {
            msg = protocol.encryptPayload(msg);

            //MAC
            try {
                msg.setFastSecurePayloadCheck(protocol.getMac(msg));
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }

            //Finalize packet and send
            byte[] data = msg.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, p.getAddress(),
                this.getLocalPort()
            );
            super.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void receive(DatagramPacket p) throws IOException {
        super.receive(p);

        ByteBuffer buffer = ByteBuffer.wrap(p.getData());

        byte version = buffer.get();
        String sID = ParsingUtils.readString(buffer);
        MessageType type = MessageType.fromTypeCode(buffer.get());

        SMCPMessage msg;
        switch (type) {
            case MessageWithIV:
                msg = SMCPMessageWithIV.parse(p.getData());
                break;
            default:
                msg = SMCPMessage.parse(p.getData());
                break;
        }

        //sAttributes
        try {
            byte[] attributesHash = this.socketConfig.getHashValue();
            if (!MessageDigest.isEqual(attributesHash, msg.getsAttributesHash())) {
                logger.warning("Attributes hash is not equal");
                return;
            } else
                logger.info("Attributes Hash: PASSES");
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Protocol protocol = new Protocol(socketConfig);

        //Check MAC
        try {
            if (!protocol.verifyMac(msg)) {
                logger.warning("Mac is not equal to the one received");
                return;
            } else
                logger.info("Mac: PASSES");
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | NoSuchProviderException | InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            msg = protocol.decryptPayload(msg);

            Payload payload = Payload.parse(msg.getSecurePayload());

            int nounce = payload.getRandomNonce();
            if (seenNonceBefore(nounce)) {
                return;
            }

            //Check sequence
            boolean validSeq = sequenceHandler.validateNewMsg(payload.getFromPeerID(), payload.getSeqNumber());
            if (!validSeq) {
                return;
            }

            //Check integrity
            MessageDigest digest = HashUtil.getInstance(socketConfig.getIntHash());
            byte[] hashValue = digest.digest(payload.getMessage());
            if (!MessageDigest.isEqual(hashValue,payload.getIntegrityControl()))
            {
                logger.warning("Integrity check on message FAILED");
                return;
            }

            p.setData(payload.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean seenNonceBefore(int nonce) {
        if (this.nonces.contains(nonce)) {
            logger.warning("Same nonce found - " + nonce);
            return true;
        }
        nonces.add(nonce);
        return false;
    }

    private void setSocketConfig(InetAddress mcastaddr) {
        try {
            InputStream in = getClass().getResourceAsStream("/security/SMCP.conf");

            socketConfig = EndpointReader.getInstance(in)
                .getEndpointConfig(mcastaddr.getHostAddress(), this.getLocalPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
