package frontEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.omg.CORBA.ORB;
import centerServer.RudpClient;
import common.Infrastucture;
import common.Logger;

public class RecordManagerImpl extends FrontEndPOA
{
	private ORB orb;
	private String cityAbbr;
	private Logger logger;
	private List<String> requests = new ArrayList<>(); // to store requests in case of a server failure to send the request to a chosen replica next
	private List<HashMap<String, Integer>> ports; // RUDP ports for the 9 servers, each List entry for 3 servers
	private HashMap<String, Integer> activeServers = new HashMap<>(); // indicates active server of each city: {0, 1, 2}
	
	// Constructor
	public RecordManagerImpl(List<HashMap<String, Integer>> ports)
	{
		super();
		this.cityAbbr = "FE";
		this.ports = ports;
		activeServers.put("MTL", 0);
		activeServers.put("LVL", 0);
		activeServers.put("DDO", 0);
		
		this.logger = new Logger("SRV_" + "FE" + ".log");

		logger.logToFile(cityAbbr + "[RecordManagerImpl Constructor]: An instance of RecordManagerImpl is created");
	}

	@Override
	public boolean createTRecord(String firstName, String lastName, String address, String phoneNumber, 
			String specialization, // the sign ',' is the separator
			String location, String managerId)
	{
		if ((firstName == null) || (lastName == null) || (address == null) || (phoneNumber == null)
				|| (specialization == null) || (location == null) || (managerId == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createTRecord()]: createTRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String city = managerId.substring(0,3);
		
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		// [String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
		String result = rudpClient.requestRemote("createTRecord~" + firstName + "~" + lastName + "~" + address + "~" + phoneNumber + "~" 
				+ specialization + "~" + location + "~" + managerId);
				
		if (result.contains("ACK"))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createTRecord()]: createTRecord is successfully done "
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		}
		else
		{
			return false;
		}		
	}

	@Override
	public boolean createSRecord(String firstName, String lastName, String coursesRegistred, boolean status, String statusDate, String managerId)
	{
		if ((firstName == null) || (lastName == null) || (coursesRegistred == null) || (statusDate == null) || (managerId == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createSRecord()]: createSRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String city = managerId.substring(0,3);
		
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
		String result = rudpClient.requestRemote("createTRecord~" + firstName + "~" + lastName + "~" + coursesRegistred + "~" + status + "~" 
				+ statusDate + "~" + managerId);
				
		if (result.contains("ACK"))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createSRecord()]: createTRecord is successfully done " + " {CallerManagerID: " 
					+ managerId + "}");
			return true;
		}
		else
		{
			return false;
		}				
	}

	@Override
	public String getRecordCounts(String managerId)
	{
		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return null;
		}
		
		String city = managerId.substring(0,3);
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		String result = rudpClient.requestRemote("getRecordsCount~"+ managerId);
		
		if (result.contains("ACK"))
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean editRecord(String recordID, String fieldName, String newValue, String managerId)
	{
		if ((recordID == null) || (fieldName == null) || (newValue == null) || (managerId == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!(isRecordIdFormatCorrect(recordID)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId format is incorrect)" + " {CallerManagerID: " 
					+ managerId + "}");
			return false;
		}
		
		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String city = managerId.substring(0,3);
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		// [String]: recordID~fieldName~newValue~managerId
		String result = rudpClient.requestRemote("editRecord~" + recordID + "~" + fieldName + "~" + newValue + "~" + managerId);

		if (result.contains("ACK"))
		{
			return true;
		}
		else
		{
			return false;
		}		
	}

	@Override
	public boolean recordExist(String recordId, String managerId)
	{
		if (!(isRecordIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr
					+ "[RecordManagerImpl.recordExist()]: Error! recordId format is incorrect" + " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String city = managerId.substring(0,3);
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		// [String]: recordId~managerId
		String result = rudpClient.requestRemote("recordExist~" + recordId + "~" + managerId);

		if (result.contains("ACK"))
		{
			return true;
		}
		else
		{
			return false;
		}		
	}

	@Override
	public boolean transferRecord(String managerId, String recordId, String remoteCenterServerName)
	{
		if (!(isRecordIdFormatCorrect(recordId)))
		{
			logger.logToFile(
					cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! recordId format is incorrect" + " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || remoteCenterServerName.toUpperCase().equals("LVL")
				|| remoteCenterServerName.toUpperCase().equals("DDO"))))
		{
			logger.logToFile(
					cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! remoteCenterServerName is invalid" + " {CallerManagerID: " + managerId
							+ "}");
			return false; // Given city name is incorrect
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String city = managerId.substring(0,3);
		RudpClient rudpClient = new RudpClient(ports.get(activeServers.get(city)).get(city), cityAbbr, logger);
		// [String]: recordId~remoteCenterServerName~managerId
		String result = rudpClient.requestRemote("transferRecord~" + recordId + "~" + remoteCenterServerName + "~" + managerId);

		if (result.contains("ACK"))
		{
			return true;
		}
		else
		{
			return false;
		}				
	}

	@Override
	public void shutdown()
	{
		orb.shutdown(false);
	}

	public void setOrb(ORB orb)
	{
		this.orb = orb;
	}
	
	private boolean isRecordIdFormatCorrect(String id)
	{
		if (id == null)
		{
			return false;
		}

		if (id.length() != 7)
		{
			return false;
		}

		Character ch = id.toUpperCase().charAt(0);
		if (!(ch.equals('T') || ch.equals('S')))
		{
			return false;
		}

		ch = id.toUpperCase().charAt(1);

		if (!(ch.equals('R')))
		{
			return false;
		}

		if (!(id.substring(2, 5).chars().allMatch(Character::isDigit)))
		{
			return false;
		}

		return true;
	}
	
	private boolean isManagerIdFormatCorrect(String id)
	{
		if (id == null)
		{
			return false;
		}

		if (id.length() != 7)
		{
			return false;
		}

		String srvName = id.substring(0, 3).toUpperCase();
		if (!Infrastucture.isSystemServerName(srvName))
		{
			return false;
		}

		if (!(id.substring(3, 4).chars().allMatch(Character::isDigit)))
		{
			return false;
		}

		return true;
	}
}
