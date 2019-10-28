package SMCP;
import java.io.IOException;
import java.net.MulticastSocket;

public class SMCPSocket extends MulticastSocket {

	EndpointConfig socketconfig;
	
    public SMCPSocket(EndpointConfig config) throws IOException {
    	super();
		socketconfig = config;
    	
	}

    public EndpointConfig getConfig {
    	return socketconfig;
    }
	
}
