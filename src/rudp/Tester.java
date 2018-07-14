package rudp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import common.Logger;
import record.Record;

public class Tester
{
	public static void main(String[] args)
	{
		HashMap<Character, List<Record>> recordsMap = new HashMap<>();
		Logger logger = new Logger("SRV_" + "MTL" + ".log");
		// HashMap initialization
		for (Character ch = 'A'; ch <= 'Z'; ch++)
		{
			ArrayList<Record> recordList = new ArrayList<>();
			recordsMap.put(ch, recordList);
		}
				
		RUDPServer rudpServer = new RUDPServer(recordsMap, 1353, "MTL", logger);
		rudpServer.run();
		
//		try
//		{
//			TimeUnit.MILLISECONDS.sleep(3000);
//		} catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
		
		RUDPClient rudpClient = new RUDPClient(1353, "MTL", logger);
		System.out.println(rudpClient.requestRemote("MTL", "HeartBit"));
		System.out.println("End");
	}
}
