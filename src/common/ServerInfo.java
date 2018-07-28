package common;

public class ServerInfo implements Comparable {

	private String location;
	private Integer udpPort;
	private String host;

	private Boolean isAlive;
	private Integer stackId;
	private Integer serverId;

	public Integer getStackId() {
		return stackId;
	}

	public void setStackId(Integer stackId) {
		this.stackId = stackId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public ServerInfo(String location, String host, Integer udpPort, Integer serverId, Integer stackId) {
		this.location = location;
		this.host = host;
		this.udpPort = udpPort;
		this.serverId = serverId;
		this.stackId = stackId;
		this.isAlive = true;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(Integer udpPort) {
		this.udpPort = udpPort;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public Boolean isAlive() {
		return isAlive;
	}
	
	public void markDead() {
		isAlive = false;
	}
	
	public void markAlive() {
		isAlive = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerInfo other = (ServerInfo) obj;
		if ((other.getHost() != this.getHost()) || (other.getLocation() != this.getLocation())
				|| (other.getUdpPort() != this.getUdpPort()))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "  Location: " + location + "  host: " + host + "  port: " + udpPort.toString();
	}
	


	@Override
	public int compareTo(Object o) {
		Integer srvId=((ServerInfo)o).getServerId();
        /* For Ascending order*/
        return this.getServerId()-srvId;
	}

}
