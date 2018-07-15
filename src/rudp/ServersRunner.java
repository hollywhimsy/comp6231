package rudp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Logger;
import record.Record;

public class ServersRunner
{
	static HashMap<Character, List<Record>> recordsMap1;
	static HashMap<String, Record> indexPerId1;
	static Logger logger1;
	static List<Integer> othersPort1;
	static RUDPServer rudpServer1;
	static RUDPServer rudpServer2;
	static RUDPServer rudpServer3;
	
	public static void main(String[] args)
	{
		recordsMap1 = new HashMap<>();
		indexPerId1 = new HashMap<>();
		logger1 = new Logger("SRV_" + "MTL" + ".log");
		othersPort1 = new ArrayList<>();
		othersPort1.add(3720);
		othersPort1.add(3730);
		rudpServer1 = new RUDPServer(recordsMap1, indexPerId1, 3710, "MTL", logger1, othersPort1);
		rudpServer1.run();
		
		HashMap<Character, List<Record>> recordsMap2 = new HashMap<>();
		HashMap<String, Record> indexPerId2 = new HashMap<>();
		Logger logger2 = new Logger("SRV_" + "LVL" + ".log");
		List<Integer> othersPort2 = new ArrayList<>();
		othersPort2.add(3710);
		othersPort2.add(3730);
		rudpServer2 = new RUDPServer(recordsMap2, indexPerId2, 3720, "LVL", logger2, othersPort2);
		rudpServer2.run();
		
		HashMap<Character, List<Record>> recordsMap3 = new HashMap<>();
		HashMap<String, Record> indexPerId3 = new HashMap<>();
		Logger logger3 = new Logger("SRV_" + "DDO" + ".log");
		List<Integer> othersPort3 = new ArrayList<>();
		othersPort3.add(3710);
		othersPort3.add(3720);
		rudpServer3 = new RUDPServer(recordsMap3, indexPerId3, 3730, "DDO", logger3, othersPort3);
		rudpServer3.run();
	}	
}