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
		System.out.println(rudpClient.requestRemote("HeartBit"));
		// [String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
		System.out.println(rudpClient.requestRemote("createTRecord~Siamak~Azadi~Montreal~12345~Network,Programing~MTL~MTL0001"));
		// [String]: recordID~fieldName~newValue~managerId
		System.out.println(rudpClient.requestRemote("editRecord~TR00001~phoneNumber~5555555~MTL0001"));
		// [String]: recordId~managerId
		System.out.println(rudpClient.requestRemote("recordExist~TR00333~MTL0001"));
		// [String]: recordId~remoteCenterServerName~managerId
		System.out.println(rudpClient.requestRemote("transferRecord~TR00001~LVL~MTL0001"));
		System.out.println(rudpClient.requestRemote("getRecordsCount~MTL0001"));

		RudpClient rudpClient2 = new RudpClient(3720, "LVL", logger);
		// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
		Date date = new Date();
		System.out.println(rudpClient2.requestRemote("createSRecord~Alice~asd~Network,JavaL~true~" + date.toString() + "~LVL0001"));
		System.out.println(rudpClient2.requestRemote("getRecordsCount~LVL0001"));

		RudpClient rudpClient3 = new RudpClient(3730, "DDO", logger);
		System.out.println(rudpClient3.requestRemote("getRecordsCount~DDO0001"));
		
		RudpClient rudpClient4 = new RudpClient(4730, "DDO", logger);
		System.out.println(rudpClient4.requestRemote("getRecordsCount~DDO0001"));
		
		RudpClient rudpClient5 = new RudpClient(5730, "DDO", logger);
		System.out.println(rudpClient5.requestRemote("getRecordsCount~DDO0001"));

	}

}
