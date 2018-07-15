package rudp;

import java.awt.geom.GeneralPath;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import common.Logger;
import record.Record;

public class Tester
{
	public static void main(String[] args)
	{
		HashMap<Character, List<Record>> recordsMap = new HashMap<>();
		HashMap<String, Record> indexPerId = new HashMap<>();
		Logger logger = new Logger("SRV_" + "MTL" + ".log");
		// HashMap initialization
		for (Character ch = 'A'; ch <= 'Z'; ch++)
		{
			ArrayList<Record> recordList = new ArrayList<>();
			recordsMap.put(ch, recordList);
		}
				
		RUDPServer rudpServer = new RUDPServer(recordsMap, indexPerId, 1353, "MTL", logger);
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
		//[String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
		System.out.println(rudpClient.requestRemote("MTL", "createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
		System.out.println(rudpClient.requestRemote("MTL", "createTRecord~Bob~dsasd~Montreal~564564~Network,Programing~MTL~MTL0001"));
		System.out.println(rudpClient.requestRemote("MTL", "getRecordsCount"));
		
		System.out.println("End");
		System.out.println(generateChecksum("createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
	}
	
	static String generateChecksum(String str)
    {
		MessageDigest md;
		StringBuffer sb = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			sb = new StringBuffer();
			for (byte b : digest) 
			{
				sb.append(String.format("%02x", b & 0xff));
			}
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}	
		
        return sb.toString();
    }	
}
