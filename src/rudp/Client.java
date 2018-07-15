package rudp;

import common.Logger;

public class Client
{
	public static void main(String[] args)
	{
		Logger logger = new Logger("SRV_" + "FE" + ".log");
		RUDPClient rudpClient = new RUDPClient(3710, "MTL", logger);		
		System.out.println(rudpClient.requestRemote("MTL", "HeartBit"));
		//[String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
		System.out.println(rudpClient.requestRemote("MTL", "createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
		System.out.println(rudpClient.requestRemote("MTL", "createTRecord~Bob~dsasd~Montreal~564564~Network,Programing~MTL~MTL0001"));
		System.out.println(rudpClient.requestRemote("MTL", "getRecordsCount~MTL0001"));
		
		System.out.println("End");	
	}
	
}
