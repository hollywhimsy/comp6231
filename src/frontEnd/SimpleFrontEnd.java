package frontEnd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import centerServer.RudpClient;
import common.Logger;

public class SimpleFrontEnd
{
	public static void main(String[] args)
	{
		Logger logger = new Logger("SRV_" + "FE" + ".log");
		
		String[] cities = {"MTL", "LVL", "DDO"};
		List<HashMap<String, Integer>> ports = new ArrayList<>();
		
		for (int i = 0; i < 3; i++)
		{		
			HashMap<String, Integer> groupPorts = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{			
				groupPorts.put(cities[j], 3710 + i*1000 + j*10);				
			}
			ports.add(groupPorts);
		}
		List<HashMap<String, Integer>> activeServers = new ArrayList<>();
		for (int i = 0; i < 3; i++)
		{
			HashMap<String, Integer> aliveGroup = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{
				aliveGroup.put(cities[j], 1);
			}
			activeServers.add(aliveGroup);
		}		
		HashMap<String, Integer> coordinators = new HashMap<>();
		coordinators.put("MTL", 2);
		coordinators.put("LVL", 2);
		coordinators.put("DDO", 2);
		
		HealthMonitor healthMonitor = new HealthMonitor(ports, activeServers, logger, coordinators);
		healthMonitor.start();
		
		while (true)
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("MTL: " + coordinators.get("MTL"));
			System.out.println("LVL: " + coordinators.get("LVL"));
			System.out.println("DDO: " + coordinators.get("DDO"));
		}
		
//		RudpClient rudpClient = new RudpClient(5710, "MTL", logger);		
//		System.out.println("1 : " + rudpClient.requestRemote("HeartBit"));
//		System.out.println("2 : " + rudpClient.requestRemote("createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
//		System.out.println("3 : " + rudpClient.requestRemote("editRecord~TR00001~phoneNumber~5555555~MTL0001"));
//		System.out.println("4 : " + rudpClient.requestRemote("transferRecord~TR00002~LVL~MTL0001"));
//		System.out.println("5 : " + rudpClient.requestRemote("recordExist~TR00003~MTL0001"));
//		System.out.println("6 : " + rudpClient.requestRemote("getRecordsCount~MTL0001"));
//
//		RudpClient rudpClient2 = new RudpClient(5730, "LVL", logger);
//		Date date = new Date();
//		System.out.println("7 : " + rudpClient2.requestRemote("createSRecord~Alice~asd~Network,JavaL~true~" + date.toString() + "~LVL0001"));
//		System.out.println("8 : " + rudpClient2.requestRemote("getRecordsCount~LVL0001"));
//
//		RudpClient rudpClient3 = new RudpClient(5730, "DDO", logger);
//		System.out.println("9 : " + rudpClient3.requestRemote("getRecordsCount~DDO0001"));
	}

}
