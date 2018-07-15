package runner;

import java.util.HashMap;
import java.util.List;
import centerServer.CenterServerCore;
import common.Logger;
import record.Record;

public class ServersRunner
{	
	public static void main(String[] args)
	{		
		HashMap<String, Integer> ports1 = new HashMap<>();
		ports1.put("MTL", 3710);
		ports1.put("LVL", 3720);
		ports1.put("DDO", 3730);
				
		HashMap<Character, List<Record>> recordsMap1 = new HashMap<>();
		HashMap<String, Record> indexPerId1 = new HashMap<>();
		Logger logger1 = new Logger("SRV_" + "MTL" + ".log");
		CenterServerCore rudpServer1 = new CenterServerCore(recordsMap1, indexPerId1, 3710, "MTL", logger1, ports1);
		rudpServer1.start();
		
		HashMap<Character, List<Record>> recordsMap2 = new HashMap<>();
		HashMap<String, Record> indexPerId2 = new HashMap<>();
		Logger logger2 = new Logger("SRV_" + "LVL" + ".log");
		CenterServerCore rudpServer2 = new CenterServerCore(recordsMap2, indexPerId2, 3720, "LVL", logger2, ports1);
		rudpServer2.start();
		
		HashMap<Character, List<Record>> recordsMap3 = new HashMap<>();
		HashMap<String, Record> indexPerId3 = new HashMap<>();
		Logger logger3 = new Logger("SRV_" + "DDO" + ".log");
		CenterServerCore rudpServer3 = new CenterServerCore(recordsMap3, indexPerId3, 3730, "DDO", logger3, ports1);
		rudpServer3.start();
		
		HashMap<String, Integer> ports2 = new HashMap<>();
		ports2.put("MTL", 4710);
		ports2.put("LVL", 4720);
		ports2.put("DDO", 4730);
		
		HashMap<String, Integer> ports3 = new HashMap<>();
		ports3.put("MTL", 5710);
		ports3.put("LVL", 5720);
		ports3.put("DDO", 5730);
	}	
}