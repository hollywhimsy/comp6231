package frontEnd;

import java.util.HashMap;
import java.util.List;
import centerServer.RudpClient;
import common.Logger;

public class HealthMonitor extends Thread
{
	private List<HashMap<String, Integer>> ports;
	private List<HashMap<String, Integer>> activeServers; // 0 -> dead, 1 -> alive
	private Logger logger;
	// private int myGroupIndex;
	private String myCity = "FE";
	private HashMap<String, Integer> coordinators;

	public HealthMonitor(List<HashMap<String, Integer>> ports, List<HashMap<String, Integer>> activeServers, Logger logger,
			HashMap<String, Integer> coordinators)
	{
		super();
		this.ports = ports;
		this.activeServers = activeServers;
		this.logger = logger;
		// this.myGroupIndex = myGroupIndex;
		// this.myCity = myCity.trim().toUpperCase();
		this.coordinators = coordinators;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e1)
			{
				// e1.printStackTrace();
			}

			updateAlivesList();
		}
	}

	public void updateAlivesList()
	{
		for (String srv : coordinators.keySet()) // for all the servers inside each group
		{
			// Heartbit check the server
			RudpClient client = new RudpClient(ports.get(coordinators.get(srv)).get(srv), myCity, logger);
			String result = client.requestRemote("HeartBit").trim();

			if (result.contains("DWN")) // if the server is down
			{
				activeServers.get(coordinators.get(srv)).put(srv, 0); // put this server is down

				logger.logToFile(myCity + "[HealthMonitor.updateAlivesList()]: " + srv + " coordinator is DEAD");
				
				try
				{
					Thread.sleep(3000);
				} catch (InterruptedException e1)
				{
					// e1.printStackTrace();
				}

				for (int k = 0; k < 3; k++) // for all the groups
				{
					if (activeServers.get(k).get(srv) == 1) // if the server was alive based on the previous health check
					{
						RudpClient client2 = new RudpClient(ports.get(k).get(srv), myCity, logger);
						String result2 = client2.requestRemote("getMaster").trim();
						
						if (result2.contains("ACK"))
						{
							int temp = Integer.parseInt(result2.substring(3, result2.length()));
							coordinators.put(srv, temp);
						}
					}
				}						
			}
		}
		
		
		
//		for (int i = 0; i < 3; i++) // for all the groups
//		{
//			for (String srv : activeServers.get(i).keySet()) // for all the servers inside each group
//			{
//				if (activeServers.get(i).get(srv) == 1) // if the server was alive based on the previous health check
//				{
//					// Heartbit check the server
//					RudpClient client = new RudpClient(ports.get(i).get(srv), myCity, logger);
//					String result = client.requestRemote("HeartBit").trim();
//
//					if (result.contains("DWN")) // if the server is down
//					{
//						activeServers.get(i).put(srv, 0); // put this server is down
//
//						logger.logToFile(myCity + "[HealthMonitor.updateAlivesList()]: " + srv + " listening on " + ports.get(i).get(srv)
//								+ " is DEAD");
//
//						if (coordinators.get(srv) == i) // Coordinator is down //ask for the new coordinator and set it
//						{
//							try
//							{
//								Thread.sleep(3000);
//							} catch (InterruptedException e1)
//							{
//								// e1.printStackTrace();
//							}
//
//							for (int k = 0; k < 3; k++) // for all the groups
//							{
//								if (activeServers.get(k).get(srv) == 1) // if the server was alive based on the previous health check
//								{
//									RudpClient client2 = new RudpClient(ports.get(k).get(srv), myCity, logger);
//									String result2 = client2.requestRemote("getMaster").trim();
//									
//									if (result2.contains("ACK"))
//									{
//										int temp = Integer.parseInt(result2.substring(3, result2.length()));
//										coordinators.put(srv, temp);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
	}
}
