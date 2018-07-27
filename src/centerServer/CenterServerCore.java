package centerServer;

import common.Logger;
import record.Record;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CenterServerCore extends Thread
{
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbr;// = new String(); // City abbreviation like "MTL"
	private HashMap<Character, List<Record>> recordsMap = new HashMap<>(); // The map which contains the records
	private HashMap<String, Record> indexPerId = new HashMap<>();
	private Logger logger;
	private List<HashMap<String, Integer>> alives = new ArrayList<>(); // 0 -> dead, 1 -> alive
	private HashMap<String, String[]> responses = new HashMap<>();
	private Operations operations;
	private int groupIndex;
	private List<String> brdcMsgQueue = new LinkedList<>();	

	// Constructor
	public CenterServerCore(String cityAbbr, Logger logger, List<HashMap<String, Integer>> ports, int groupIndex)
	{
		super();
		this.groupIndex = groupIndex;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.listenPort = ports.get(groupIndex).get(cityAbbr);
		
		String[] cities = {"MTL", "LVL", "DDO"};
		for (int i = 0; i < 3; i++)
		{		
			HashMap<String, Integer> aliveGroup = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{			
				aliveGroup.put(cities[j], 1);				
			}
			alives.add(aliveGroup);
		}
		
		HealthChecker healthChecker = new HealthChecker(ports, alives, logger, cityAbbr, groupIndex);
		healthChecker.start();
		
		Multicast multicast = new Multicast(groupIndex, alives, ports, cityAbbr, logger, brdcMsgQueue);
		multicast.start();
		
		operations = new Operations(groupIndex, cityAbbr, logger, alives, ports, recordsMap, indexPerId, brdcMsgQueue);

		logger.logToFile(cityAbbr + "[CenterServerCore Constructor]: CenterServerCore is initialized");
	}

	public void run()
	{
		DatagramSocket socket = null; // Socket declaration
		try
		{
			socket = new DatagramSocket(listenPort); // Socket initiation by given UDP port number
			logger.logToFile(cityAbbr + "[CenterServerCore.run()]: Listening on " + listenPort + " UDP Port");

			int count = 0;
			
			while (true) // Always receive the requests and response accordingly
			{
				count ++;
				
				byte[] buffer = new byte[1024]; // Buffer which receives the request
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				RequestManager requestManager = new RequestManager(request, socket, cityAbbr, logger, responses, operations);
				requestManager.start();
				
//				if (count == 10)
//				{
//					if ((groupIndex == 1) && (cityAbbr.equals("LVL")))
//					{	
//						try
//						{
//							Thread.sleep(1000);
//						} catch (InterruptedException e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						return;
//					}
//				}
			}
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbr + "[CenterServerCore.run()]: ERROR! UDP Socket Exception!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbr + "[CenterServerCore.run()]: ERROR! UDP IO Exception!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbr + "[CenterServerCore.run()]: ERROR! UDP Socket is Closed!");
		}
	}
}
