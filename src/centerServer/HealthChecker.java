package centerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Logger;
import common.ServerInfo;

public class HealthChecker extends Thread
{
	private List<ServerInfo> myGroupServers = new ArrayList<>();
	
	private Logger logger;
	private int serverId;
	private String myCity;
	
	public HealthChecker(List<ServerInfo> myGroupServers, Logger logger, String myCity, Integer serverId)
	{
		super();
		this.myGroupServers = myGroupServers;
		this.logger = logger;
		this.serverId = serverId;
		this.myCity = myCity.trim().toUpperCase();
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(3000);
			} catch (InterruptedException e1)
			{
				//e1.printStackTrace();
			}
			
			updateAlivesList();	
		}
	}
	
	private void updateAlivesList()
	{
		for (ServerInfo srv : myGroupServers)
		{
			
				if (srv.getServerId() == serverId)
				{
					continue;
				}
				
				if(srv.isAlive())
				{
					RudpClient client = new RudpClient(srv.getUdpPort(), myCity, logger);
					String result = client.requestRemote("HeartBit").trim();
	
					if (result.equals("DWN"))
					{
						srv.markDead();; // this server is down	
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: Heartbit cheked for " + srv + " listening on " 
								+ srv.getUdpPort() + ". Result: Dead");
					}
					else
					{
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: Heartbit cheked for " + srv + " listening on " 
								+ srv.getUdpPort() + ". Result: Live");
					}
				}				
			}
				
	}
}
