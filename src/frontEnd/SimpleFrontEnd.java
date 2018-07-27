package frontEnd;

import java.util.Date;
import centerServer.RudpClient;
import common.Logger;

public class SimpleFrontEnd
{
	public static void main(String[] args)
	{
		Logger logger = new Logger("SRV_" + "FE" + ".log");
		RudpClient rudpClient = new RudpClient(3710, "MTL", logger);
		
		System.out.println("1 : " + rudpClient.requestRemote("HeartBit"));
		System.out.println("2 : " + rudpClient.requestRemote("createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
		System.out.println("3 : " + rudpClient.requestRemote("editRecord~TR00001~phoneNumber~5555555~MTL0001"));
		System.out.println("4 : " + rudpClient.requestRemote("transferRecord~TR00002~LVL~MTL0001"));
		System.out.println("5 : " + rudpClient.requestRemote("recordExist~TR00003~MTL0001"));
		System.out.println("6 : " + rudpClient.requestRemote("getRecordsCount~MTL0001"));

		RudpClient rudpClient2 = new RudpClient(3730, "LVL", logger);
		Date date = new Date();
		System.out.println("7 : " + rudpClient2.requestRemote("createSRecord~Alice~asd~Network,JavaL~true~" + date.toString() + "~LVL0001"));
		System.out.println("8 : " + rudpClient2.requestRemote("getRecordsCount~LVL0001"));

		RudpClient rudpClient3 = new RudpClient(3730, "DDO", logger);
		System.out.println("9 : " + rudpClient3.requestRemote("getRecordsCount~DDO0001"));
		
		RudpClient rudpClient4 = new RudpClient(4730, "DDO", logger);
		System.out.println("10: " + rudpClient4.requestRemote("getRecordsCount~DDO0001"));
		
		RudpClient rudpClient5 = new RudpClient(5730, "DDO", logger);
		System.out.println("11: " + rudpClient5.requestRemote("getRecordsCount~DDO0001"));

	}

}
