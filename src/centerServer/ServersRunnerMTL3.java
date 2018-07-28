package centerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Logger;

public class ServersRunnerMTL3
{
	public static void main(String[] args)
	{
		String[] cities = { "MTL", "LVL", "DDO" };
		List<HashMap<String, Integer>> ports = new ArrayList<>();

		for (int i = 0; i < 3; i++)
		{
			HashMap<String, Integer> groupPorts = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{
				groupPorts.put(cities[j], 3710 + i * 1000 + j * 10);
			}
			ports.add(groupPorts);
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				// MTL 3 server
				if ((cities[j] == "MTL") && i == 2)
				{
					Logger logger = new Logger("SRV_" + cities[j] + i + ".log");
					CenterServerCore centerServerCore = new CenterServerCore(cities[j], logger, ports, i);
					centerServerCore.start();
				}
			}
		}

	}
}