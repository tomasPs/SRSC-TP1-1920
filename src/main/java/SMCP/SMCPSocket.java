package SMCP;

import Utils.EndpointReader;

import java.io.*;
import java.net.*;
import java.util.Objects;

public class SMCPSocket extends MulticastSocket {

    private EndpointConfiguration socketconfig;

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
        this.socketconfig = null;
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
            this.socketconfig.getSid(),
            SMCPMessage.MessageType.SMCPMessage,
            hash,
            payload.toByteArray(),
            hash
        );

        byte[] data = msg.toByteArray();

        DatagramPacket packet = new DatagramPacket(data, data.length, p.getAddress(),
            this.getLocalPort()
        );
        super.send(packet);
    }

    @Override
    public synchronized void receive(DatagramPacket p) throws IOException {
        super.receive(p);

        byte[] buffer = new byte[65508];

        SMCPMessage smcpMessage = SMCPMessage.parse(p.getData());
        Payload payload = Payload.parse(smcpMessage.getSecurePayload());
        System.out.println("TEST");

        p.setData(payload.getMessage());
    }

    private void configure(InetAddress mcastaddr) {
        try {
            File configFile =
                new File(Objects.requireNonNull(getClass().getClassLoader().getResource("security/SMCP.conf"))
                    .getFile());
            String ip = mcastaddr.getHostAddress() + ":" + this.getLocalPort();
//            socketconfig = EndpointReader.readFile(configFile, ip);
            this.socketconfig = EndpointReader.getTestConfig(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
