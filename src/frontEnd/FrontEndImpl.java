package frontEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.omg.CORBA.ORB;
import centerServer.RudpClient;
import common.Infrastucture;
import common.Logger;

public class FrontEndImpl extends FrontEndPOA
{
	private ORB orb;
	private String cityAbbr = "FE";
	private Logger logger;	
	private List<HashMap<String, Integer>> ports = new ArrayList<>();
	private List<HashMap<String, Integer>> activeServers = new ArrayList<>();
	private HashMap<String, Integer> coordinators = new HashMap<>();	
	
	// Constructor
	public FrontEndImpl()
	{
		super();
		logger = new Logger("SRV_" + "FE" + ".log");
		
		String[] cities = {"MTL", "LVL", "DDO"};
				
		for (int i = 0; i < 3; i++)
		{		
			HashMap<String, Integer> groupPorts = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{			
				groupPorts.put(cities[j], 3710 + i*1000 + j*10);				
			}
			ports.add(groupPorts);
		}
		
		for (int i = 0; i < 3; i++)
		{
			HashMap<String, Integer> aliveGroup = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{
				aliveGroup.put(cities[j], 1);
			}
			activeServers.add(aliveGroup);
		}		
		
		coordinators.put("MTL", 2);
		coordinators.put("LVL", 2);
		coordinators.put("DDO", 2);
		
		HealthMonitor healthMonitor = new HealthMonitor(ports, activeServers, logger, coordinators);
		healthMonitor.start();

		logger.logToFile(cityAbbr + "[FrontEndImpl Constructor]: An instance of FrontEndImpl is created");
	}

	@Override
	public boolean createTRecord(String firstName, String lastName, String address, String phoneNumber, 
			String specialization, // the sign ',' is the separator
			String location, String managerId)
	{
		if ((firstName == null) || (lastName == null) || (address == null) || (phoneNumber == null)
				|| (specialization == null) || (location == null) || (managerId == null))
		{
			logger.logToFile(cityAbbr + "[FrontEndImpl.createTRecord()]: createTRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String result = sender("createTRecord~" + firstName + "~" + lastName + "~" + address + "~" + phoneNumber + "~" + specialization + "~" 
				+ location + "~" + managerId, managerId);
				
		if (result.contains("ACK"))
		{
			logger.logToFile(cityAbbr + "[FrontEndImpl.createTRecord()]: createTRecord is successfully done " + " {CallerManagerID: " 
					+ managerId + "}");
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
			logger.logToFile(cityAbbr + "[FrontEndImpl.createSRecord()]: createSRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String result = sender("createSRecord~" + firstName + "~" + lastName + "~" + coursesRegistred + "~" + status + "~" 
				+ statusDate + "~" + managerId, managerId);
						
		if (result.contains("ACK"))
		{
			logger.logToFile(cityAbbr + "[FrontEndImpl.createSRecord()]: createTRecord is successfully done " + " {CallerManagerID: " 
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
		
		String result = sender("getRecordsCount~"+ managerId, managerId);
		
		if (result.contains("ACK"))
		{
			return result.substring(3, result.length());
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
			logger.logToFile(cityAbbr + "[FrontEndImpl.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!(isRecordIdFormatCorrect(recordID)))
		{
			logger.logToFile(cityAbbr + "[FrontEndImpl.editRecord()]: editRecord failed (recordId format is incorrect)" + " {CallerManagerID: " 
					+ managerId + "}");
			return false;
		}
		
		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String result = sender("editRecord~" + recordID + "~" + fieldName + "~" + newValue + "~" + managerId, managerId);
		
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
					+ "[FrontEndImpl.recordExist()]: Error! recordId format is incorrect" + " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String result = sender("recordExist~" + recordId + "~" + managerId, managerId);		

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
			logger.logToFile(cityAbbr + "[FrontEndImpl.transferRecord()]: Error! recordId format is incorrect" + " {CallerManagerID: " 
					+ managerId + "}");
			return false;
		}

		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || remoteCenterServerName.toUpperCase().equals("LVL")
				|| remoteCenterServerName.toUpperCase().equals("DDO"))))
		{
			logger.logToFile(cityAbbr + "[FrontEndImpl.transferRecord()]: Error! remoteCenterServerName is invalid" + " {CallerManagerID: " 
					+ managerId	+ "}");
			return false; // Given city name is incorrect
		}

		if (!isManagerIdFormatCorrect(managerId))
		{
			//Manager ID incorrect
			return false;
		}
		
		String result = sender("transferRecord~" + recordId + "~" + remoteCenterServerName + "~" + managerId, managerId);		

		if (result.contains("ACK"))
		{
			return true;
		}
		else
		{
			return false;
		}				
	}
	
	private String sender(String msg, String managerId)
	{
		String city = managerId.substring(0,3).toUpperCase();
		String result = "";
		boolean check = true;
		while (check)
		{
			RudpClient rudpClient = new RudpClient(ports.get(coordinators.get(city)).get(city), cityAbbr, logger);
			// [String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
			result = rudpClient.requestRemote(msg);
			if (result.equals("DWN"))
			{
				logger.logToFile(cityAbbr + "[FrontEndImpl.createTRecord()]: Coordinator is down. Wait for election " + coordinators.get(city));
			}
			else
			{
				check = false;
			}
		}
		
		return result;
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

		if (!isNumeric(id.substring(2, 7)))
		{
			return false;
		}

		return true;
	}

	private boolean isNumeric(String str)
	{
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c))
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

		if (!isNumeric(id.substring(3, 7)))
		{
			return false;
		}

		return true;
	}
		
}
