public class EndpointConfiguration {


	private String ip;
	private String sea;
	private String sid;
	private int seaks;
	private String mode;
	private String padding;
	private String hash;
	private String mac;
	private int makks;
	
	
	
	public EndpointConfiguration(String iip, String isid, String isea, int iseaks, String imode, String ipadding, String ihash, String imac, int imakks) {
		
		ip= iip;
		sea= isea;
		sid= isid;
		seaks= iseaks;
		mode= imode;
		padding= ipadding;
		hash= ihash;
		mac= imac;
		makks= imakks;
		
	}
	
	public String getIp(){
		return ip;
	}
	
	public String getSid(){
		return sid;
	}
	
	public String getSea(){
		return sea;
	}
	
	public int getSeaks(){
		return seaks;
	}
	
	public String getMode(){
		return mode;
	}
	public String getPadding(){
		return padding;
	}
	public String getHash(){
		return hash;
	}
	
	public String getMac(){
		return mac;
	}
	
	public int getMakks(){
		return makks;
	}

}