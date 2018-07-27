package centerServer;

import java.util.HashMap;
import java.util.List;
import common.Logger;

public class FifoMulticast
{
	private int groupIndex;
	private List<HashMap<String, Integer>> alives;
	private List<HashMap<String, Integer>> ports;
	private String cityAbbr;
	private Logger logger;	
	
	public FifoMulticast(int groupIndex, List<HashMap<String, Integer>> alives, List<HashMap<String, Integer>> ports, String cityAbbr, Logger logger)
	{
		super();
		this.groupIndex = groupIndex;
		this.alives = alives;
		this.ports = ports;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
	}

	public void broadcast(String msg)
	{
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
						alives.get(i).put(cityAbbr, 0); // this server is down	
						logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the request is broadcasted to " + cityAbbr + " listening on " 
								+ ports.get(i).get(cityAbbr) + ". This server is Dead");
					}
					else
					{
						logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the request is broadcasted to " + cityAbbr + " listening on " 
								+ ports.get(i).get(cityAbbr));
					}
				}
				else
				{
					logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the " + cityAbbr + " listening on " + ports.get(i).get(cityAbbr) 
							+ " is DOWN! => No broadcast to it!");
				}
			}
		}
	}
}
