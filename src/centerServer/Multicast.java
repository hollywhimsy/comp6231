package centerServer;

import java.util.HashMap;
import java.util.List;
import common.Logger;

public class Multicast extends Thread
{
	private int groupIndex;
	private List<HashMap<String, Integer>> activeServers;
	private List<HashMap<String, Integer>> ports;
	private String cityAbbr;
	private Logger logger;
	private List<String> brdcMsgQueue;

	public Multicast(int groupIndex, List<HashMap<String, Integer>> activeServers, List<HashMap<String, Integer>> ports, String cityAbbr, 
			Logger logger, List<String> brdcMsgQueue)
	{
		super();
		this.groupIndex = groupIndex;
		this.activeServers = activeServers;
		this.ports = ports;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.brdcMsgQueue = brdcMsgQueue;
	}

	public void run()
	{
		try
		{
			Thread.sleep(3000);
		} catch (InterruptedException e)
		{
			// e.printStackTrace();
		}

		while (true) // run in background
		{
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				// e.printStackTrace();
			}

			synchronized (brdcMsgQueue) // Lock the queue
			{
				for (int k = 0; k < brdcMsgQueue.size(); k++)
				{
					String msg = brdcMsgQueue.get(0); // Take the first message of the queue = head of the queue
					for (int i = 0; i < 3; i++) // for all the groups = 3
					{
						if (i != groupIndex) // if the group ID in not equal to mine
						{
							if (activeServers.get(i).get(cityAbbr.toUpperCase()) == 1) // if the with my name server is alive
							{
								// Reliable send the same request to the server
								RudpClient client = new RudpClient(ports.get(i).get(cityAbbr), cityAbbr, logger);
								String result = client.requestRemote(msg).trim();

								// if the server was down
								if (result.equals("DWN"))
								{
									logger.logToFile(cityAbbr + "[Multicast.send()]: the request is multicasted to " + cityAbbr + " listening on "
											+ ports.get(i).get(cityAbbr) + ". This server was DEAD recently");
								} else
								{
									logger.logToFile(cityAbbr + "[Multicast.send()]: the request is multicasted to " + cityAbbr + " listening on "
											+ ports.get(i).get(cityAbbr));
								}
							}
						}
					}

					// remove the message from the queue head
					brdcMsgQueue.remove(0);
				}
			}

		}
	}
}
