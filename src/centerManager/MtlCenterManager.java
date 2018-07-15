package centerManager;

import java.util.HashMap;
import java.util.List;
import common.Infrastucture;
import common.Logger;
import record.Record;
import udp.UDPServer;

public class MtlCenterManager extends Thread
{
	private String city;
	private Logger logger;
	private HashMap<Character, List<Record>> recordsMap = new HashMap<>();
	private HashMap<String, Record> indexPerId = new HashMap<>();
	
	public MtlCenterManager(String city)
	{
		super();
		this.city = city;
		
		logger = new Logger("SRV_" + city.toUpperCase().trim() + ".log");
		logger.logToFile(city + "[MtlCenterManager Constructor]: " + city + " Center server is created :)");
	}
	
	public void run()
	{
		// Start UDP CenterServer as a separate thread
		Integer udpPort = Infrastucture.getServerPortUDP(city);
		UDPServer srv = new UDPServer(recordsMap, udpPort, city, logger);
		srv.start();
	}
}
