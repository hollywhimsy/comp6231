package centerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Logger;

public class HealthChecker extends Thread
{
	private List<HashMap<String, Integer>> ports = new ArrayList<>();
	private List<HashMap<String, Integer>> alives = new ArrayList<>(); // 0 -> dead, 1 -> alive
	private Logger logger;
	private int myGroupIndex;
	private String myCity;

	public HealthChecker(List<HashMap<String, Integer>> ports, List<HashMap<String, Integer>> alives, Logger logger, String myCity, int myGroupIndex)
	{
		super();
		this.ports = ports;
		this.alives = alives;
		this.logger = logger;
		this.myGroupIndex = myGroupIndex;
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
				// e1.printStackTrace();
			}

			updateAlivesList();
		}
	}

	private void updateAlivesList()
	{
		for (int i = 0; i < 3; i++) // for all the groups
		{
			for (String srv : alives.get(i).keySet()) // for all the servers inside each group
			{
				if ((srv.toUpperCase().equals(myCity)) && (i == myGroupIndex)) // if this is myself
				{
					continue; // do not check healthiness
				}

				if (alives.get(i).get(srv) == 1) // if the server was alive based on the previous health check
				{
					// Heartbit the server
					RudpClient client = new RudpClient(ports.get(i).get(srv), myCity, logger);
					String result = client.requestRemote("HeartBit").trim();

					if (result.equals("DWN")) // if the server is down
					{
						alives.get(i).put(srv, 0); // put this server is down
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: " + srv + " listening on " + ports.get(i).get(srv) + " is DEAD");
					} else
					{
						logger.logToFile(myCity + "[HealthCheker.updateAlivesList()]: " + srv + " listening on " + ports.get(i).get(srv) + " is ALIVE");
					}
				}
			}
		}
	}
}
