package centerServer;

import java.util.HashMap;
import java.util.List;
import common.Logger;

public class BullyElection
{
	private List<HashMap<String, Integer>> ports;
	private List<HashMap<String, Integer>> activeServers; // 0 -> dead, 1 -> alive

	public BullyElection(List<HashMap<String, Integer>> ports, List<HashMap<String, Integer>> activeServers)
	{
		super();
		this.ports = ports;
		this.activeServers = activeServers;
	}

	public void election(String cityName, int MyIndex, Logger logger)
	{
		int maxId = MyIndex;

		for (int i = 0; i < 3; i++) // for all the groups
		{
			if ((i == MyIndex)) // if this is myself
			{
				continue; // do not send
			}

			if (activeServers.get(i).get(cityName) == 1) // if the server was alive based on the previous health check
			{
				// send Coordination message
				RudpClient client = new RudpClient(ports.get(i).get(cityName), cityName, logger);
				String result = client.requestRemote("Election~" + MyIndex).trim();

				if (!result.equals("DWN")) // if the server is not down
				{
					int temp = Integer.parseInt(result.substring(3, result.length()));
					if (temp > maxId)
					{
						maxId = temp;
					}
				}
			}
		}

		if (maxId == MyIndex)
		{
			for (int i = 0; i < 3; i++) // for all the groups
			{
				if (activeServers.get(i).get(cityName) == 1) // if the server was alive based on the previous health check
				{
					RudpClient client = new RudpClient(ports.get(i).get(cityName), cityName, logger);
					client.requestRemote("Coordinator~" + MyIndex).trim(); // I am the coordinator
				}
			}
		}
	}
}
