package SMCP;

import Utils.EndpointReader;
import Utils.ParsingUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.*;

import static SMCP.SMCPMessage.MessageType;

public class SMCPSocket extends MulticastSocket {

    private EndpointConfiguration socketConfig;

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
        configure(mcastaddr);
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

        //TODO securiy stuff
        byte[] hash = new byte[32];
        Payload payload = new Payload(username, 0, 0, p.getData(), hash);

        SMCPMessage msg = new SMCPMessage(
            (byte) 0,
            this.socketConfig.getSid(),
            SMCPMessage.MessageType.SMCPMessage,
            hash,
            payload.toByteArray(),
            hash
        );

        Protocol protocol = new Protocol(socketConfig);
        try {
            msg = protocol.encryptPayload(msg);

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
        Protocol protocol = new Protocol(socketConfig);

        try {
            msg = protocol.decryptPayload(msg);

            Payload payload = Payload.parse(msg.getSecurePayload());
            p.setData(payload.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configure(InetAddress mcastaddr) {
        try {
//            File configFile =
//                new File(Objects.requireNonNull(getClass().getClassLoader().getResource("security/SMCP.conf"))
//                    .getFile());

            InputStream in = getClass().getResourceAsStream("/security/SMCP.conf");

            socketConfig = EndpointReader.getInstance(in)
                .getEndpointConfig(mcastaddr.getHostAddress(), this.getLocalPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
