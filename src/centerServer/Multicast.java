package centerServer;

import java.util.HashMap;
import java.util.List;
import common.Logger;

public class Multicast extends Thread
{
	private int groupIndex;
	private List<HashMap<String, Integer>> alives;
	private List<HashMap<String, Integer>> ports;
	private String cityAbbr;
	private Logger logger;	
	private List<String> brdcMsgQueue;
	
	public Multicast(int groupIndex, List<HashMap<String, Integer>> alives, List<HashMap<String, Integer>> ports, String cityAbbr, Logger logger,
			List<String> brdcMsgQueue)
	{
		super();
		this.groupIndex = groupIndex;
		this.alives = alives;
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
			//e.printStackTrace();
		}
		
		while (true)
		{
			try
			{
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				//e.printStackTrace();
			}
			
			synchronized (brdcMsgQueue)
			{
				for (int k = 0; k < brdcMsgQueue.size(); k++)
				{
					String msg = brdcMsgQueue.get(0);
					for (int i = 0; i < 3; i++)
					{
						if (i != groupIndex)
						{
							if (alives.get(i).get(cityAbbr.toUpperCase()) == 1)
							{
								RudpClient client = new RudpClient(ports.get(i).get(cityAbbr), cityAbbr, logger);						
								String result = client.requestRemote(msg).trim();
								
								if (result.equals("DWN"))
								{
									logger.logToFile(cityAbbr + "[Multicast.send()]: the request is multicasted to " + cityAbbr + " listening on " 
											+ ports.get(i).get(cityAbbr) + ". This server was DEAD recently");
								}
								else
								{
									logger.logToFile(cityAbbr + "[Multicast.send()]: the request is multicasted to " + cityAbbr + " listening on " 
											+ ports.get(i).get(cityAbbr));
								}
							}			
						}
					}
					
					brdcMsgQueue.remove(0);
				}
			}
			
		}
	}
}
