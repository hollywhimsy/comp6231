package centerServer;

import java.util.HashMap;
import java.util.List;
import common.Logger;

public class HealthChecker extends Thread
{
	private List<HashMap<String, Integer>> ports;
	private List<HashMap<String, Integer>> activeServers; // 0 -> dead, 1 -> alive
	private Logger logger;
	private int myGroupIndex;
	private String myCity;
	private HashMap<String, Integer> coordinator;
	
	public HealthChecker(List<HashMap<String, Integer>> ports, List<HashMap<String, Integer>> activeServers, Logger logger, String myCity, 
			int myGroupIndex, HashMap<String, Integer> coordinator)
	{
		super();
		this.ports = ports;
		this.activeServers = activeServers;
		this.logger = logger;
		this.myGroupIndex = myGroupIndex;
		this.myCity = myCity.trim().toUpperCase();
		this.coordinator = coordinator;		
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
				// e1.printStackTrace();
			}

			updateAlivesList();
		}
	}

	private void updateAlivesList()
	{
		for (int i = 0; i < 3; i++) // for all the groups
		{
//			for (String srv : activeServers.get(i).keySet()) // for all the servers inside each group
			{
				if ( (i == myGroupIndex)) // if this is myself   //(srv.toUpperCase().equals(myCity)) &&
				{
					continue; // do not check healthiness
				}

				if (activeServers.get(i).get(myCity) == 1) // if the server was alive based on the previous health check
				{
					// Heartbit check the server
					RudpClient client = new RudpClient(ports.get(i).get(myCity), myCity, logger);
					String result = client.requestRemote("HeartBit").trim();

					if (result.equals("DWN")) // if the server is down
					{
						activeServers.get(i).put(myCity, 0); // put this server is down
						
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: " + myCity + " listening on " + ports.get(i).get(myCity) 
								+ " is DEAD");
						
						if (coordinator.get("id") == i) // Coordinator is down. Run the Bully algorithm
						{		
							BullyElection bullyElection = new BullyElection(ports, activeServers);
							bullyElection.election(myCity, myGroupIndex, logger);
						}
					} else
					{
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: " + myCity + " listening on " + ports.get(i).get(myCity) 
								+ " is ALIVE");
					}
				}
			}
		}
	}
}
