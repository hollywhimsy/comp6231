package frontEnd;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.omg.CORBA.ORB;
import centerServer.RudpClient;
import common.Infrastucture;
import common.Logger;
import common.ServerInfo;

public class RecordManagerImpl extends FrontEndPOA {
	private ORB orb;
	private String cityAbbr;
	private Logger logger;

	// to store requests in case of a server failure to send the request to a chosen
	// replica next
	private List<String> requests = new ArrayList<>();

	// RUDP ports for the 9 servers, each List entry for 3 servers
	private List<HashMap<String, Integer>> ports;

	// indicates active server of each city: {0, 1, 2}
	private HashMap<String, Integer> activeServers = new HashMap<>();

	// indicates master server of each city: {MTL, DDO, LVL}
	private ConcurrentHashMap<String, ServerInfo> masterServers = new ConcurrentHashMap<>();

	private ConcurrentHashMap<String, List<ServerInfo>> actServers = new ConcurrentHashMap<>();

	// Constructor
	public RecordManagerImpl(List<HashMap<String, Integer>> ports) {
		super();
		this.cityAbbr = "FE";
		this.ports = ports;
		activeServers.put("MTL", 0);
		activeServers.put("LVL", 0);
		activeServers.put("DDO", 0);

		this.logger = new Logger("SRV_" + "FE" + ".log");

		logger.logToFile(cityAbbr + "[RecordManagerImpl Constructor]: An instance of RecordManagerImpl is created");
	}

	// Constructor
	public RecordManagerImpl(ConcurrentHashMap<String, List<ServerInfo>> servers) {
		super();
		this.cityAbbr = "FE";

		this.actServers = servers;

		this.logger = new Logger("SRV_" + "FE" + ".log");

		logger.logToFile(cityAbbr + "[RecordManagerImpl Constructor]: An instance of RecordManagerImpl is created");

		// elect our master servers
		electNewLeadForCity("MTL", 0);
		electNewLeadForCity("LVL", 0);
		electNewLeadForCity("DDO", 0);

		HealthMonitor monitor = new HealthMonitor(this);
		monitor.start();

	}

	@Override
	public boolean createTRecord(String firstName, String lastName, String address, String phoneNumber,
			String specialization, // the sign ',' is the separator
			String location, String managerId) {
		if ((firstName == null) || (lastName == null) || (address == null) || (phoneNumber == null)
				|| (specialization == null) || (location == null) || (managerId == null)) {
			logger.logToFile(cityAbbr
					+ "[RecordManagerImpl.createTRecord()]: createTRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return false;
		}

		String city = managerId.substring(0, 3);

		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		// [String]:
		// firstName~lastName~address~phoneNumber~specialization~location~managerId
		String result = rudpClient.requestRemote("createTRecord~" + firstName + "~" + lastName + "~" + address + "~"
				+ phoneNumber + "~" + specialization + "~" + location + "~" + managerId);

		if (result.contains("ACK")) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createTRecord()]: createTRecord is successfully done "
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean createSRecord(String firstName, String lastName, String coursesRegistred, boolean status,
			String statusDate, String managerId) {
		if ((firstName == null) || (lastName == null) || (coursesRegistred == null) || (statusDate == null)
				|| (managerId == null)) {
			logger.logToFile(cityAbbr
					+ "[RecordManagerImpl.createSRecord()]: createSRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return false;
		}

		String city = managerId.substring(0, 3);

		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
		String result = rudpClient.requestRemote("createTRecord~" + firstName + "~" + lastName + "~" + coursesRegistred
				+ "~" + status + "~" + statusDate + "~" + managerId);

		if (result.contains("ACK")) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createSRecord()]: createTRecord is successfully done "
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getRecordCounts(String managerId) {
		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return null;
		}

		String city = managerId.substring(0, 3);
		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		String result = rudpClient.requestRemote("getRecordsCount~" + managerId);

		if (result.contains("ACK")) {
			return result;
		} else {
			return null;
		}
	}

	// Returns the UDP port of the master server instance

	private Integer getMasterPortUDPByCity(String city) {
		// TODO
		// refactor this when decide the appropriate data structure

		return ports.get(activeServers.get(city)).get(city);
	}

	@Override
	public boolean editRecord(String recordID, String fieldName, String newValue, String managerId) {
		if ((recordID == null) || (fieldName == null) || (newValue == null) || (managerId == null)) {
			logger.logToFile(cityAbbr
					+ "[RecordManagerImpl.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!(isRecordIdFormatCorrect(recordID))) {
			logger.logToFile(
					cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId format is incorrect)"
							+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return false;
		}

		String city = managerId.substring(0, 3);
		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		// [String]: recordID~fieldName~newValue~managerId
		String result = rudpClient
				.requestRemote("editRecord~" + recordID + "~" + fieldName + "~" + newValue + "~" + managerId);

		if (result.contains("ACK")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean recordExist(String recordId, String managerId) {
		if (!(isRecordIdFormatCorrect(recordId))) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.recordExist()]: Error! recordId format is incorrect"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return false;
		}

		String city = managerId.substring(0, 3);
		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		// [String]: recordId~managerId
		String result = rudpClient.requestRemote("recordExist~" + recordId + "~" + managerId);

		if (result.contains("ACK")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean transferRecord(String managerId, String recordId, String remoteCenterServerName) {
		if (!(isRecordIdFormatCorrect(recordId))) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! recordId format is incorrect"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || remoteCenterServerName.toUpperCase().equals("LVL")
				|| remoteCenterServerName.toUpperCase().equals("DDO")))) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! remoteCenterServerName is invalid"
					+ " {CallerManagerID: " + managerId + "}");
			return false; // Given city name is incorrect
		}

		if (!isManagerIdFormatCorrect(managerId)) {
			// Manager ID incorrect
			return false;
		}

		String city = managerId.substring(0, 3);
		RudpClient rudpClient = new RudpClient(getMasterPortUDPByCity(city), cityAbbr, logger);
		// [String]: recordId~remoteCenterServerName~managerId
		String result = rudpClient
				.requestRemote("transferRecord~" + recordId + "~" + remoteCenterServerName + "~" + managerId);

		if (result.contains("ACK")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void shutdown() {
		orb.shutdown(false);
	}

	public void setOrb(ORB orb) {
		this.orb = orb;
	}

	private boolean isRecordIdFormatCorrect(String id) {
		if (id == null) {
			return false;
		}

		if (id.length() != 7) {
			return false;
		}

		Character ch = id.toUpperCase().charAt(0);
		if (!(ch.equals('T') || ch.equals('S'))) {
			return false;
		}

		ch = id.toUpperCase().charAt(1);

		if (!(ch.equals('R'))) {
			return false;
		}

		if (!(id.substring(2, 5).chars().allMatch(Character::isDigit))) {
			return false;
		}

		return true;
	}

	private boolean isManagerIdFormatCorrect(String id) {
		if (id == null) {
			return false;
		}

		if (id.length() != 7) {
			return false;
		}

		String srvName = id.substring(0, 3).toUpperCase();
		if (!Infrastucture.isSystemServerName(srvName)) {
			return false;
		}

		if (!(id.substring(3, 4).chars().allMatch(Character::isDigit))) {
			return false;
		}

		return true;
	}

	// elects a new lead in the group for the passed city
	private  void electNewLeadForCity(String city, Integer crachedServerId) {
		List<ServerInfo> cityActiveSrvs = actServers.get(city);
		if (cityActiveSrvs.isEmpty()) {
			logger.logToFile(cityAbbr + "[RecordManagerImpl.electNewLeadForCity()]: " + city
					+ " can not elect leader no active servers");
		} else {
			
			ServerInfo beforeCurrentId = null;
			ServerInfo afterCurrentId = null;
			Collections.sort( cityActiveSrvs);
			ServerInfo si = null;
			for(ServerInfo s: cityActiveSrvs) {
				if ((beforeCurrentId == null) && s.getServerId() < crachedServerId)
					beforeCurrentId = s;
				
				if((afterCurrentId == null) && (s.getServerId() > crachedServerId))
				   afterCurrentId = s;
				
			}
			if (afterCurrentId !=null)
				si = afterCurrentId;
			else
				si = afterCurrentId;
					
			masterServers.put(city, si);

			// log election
			logger.logToFile(
					cityAbbr + "[RecordManagerImpl.electNewLeadForCity()]: " + city + " server :" + si.toString());
		}

	}

	// checks the server health statuses
	@SuppressWarnings("unchecked")
	public void checkServerStatuses() {
		synchronized (actServers) {
		for (java.util.Map.Entry<String, List<ServerInfo>> e : actServers.entrySet()) {
			String city = e.getKey();
			List<ServerInfo> cityServers = e.getValue();

			System.out.println("CHECKING city: " +city + " " + cityServers.size() );
			
			
			for (ServerInfo si : cityServers) {
				RudpClient client = new RudpClient(si.getUdpPort(), city, logger);
				String response = client.requestRemote("HeartBit");
				if (response == "ACK") {
					// log server alive
					logger.logToFile(
							cityAbbr + "[RecordManagerImpl.electNewLeadForCity()]: " + city + " server :" + si.toString());
//					System.out.println("alice city: " +city + " server" + si);
				} else if (response == "DWN") {

					// log server down
					cityServers.remove(si);
//					System.out.println("down city: " +city + " server" + si);
					if (isLeadServer(si)) {
//						System.out.println("Electing lead city: " +city );
						electNewLeadForCity(city, si.getServerId());
					}
				}

			}

		}
		}
	}

	// check if a server is a master server or not
	private boolean isLeadServer(ServerInfo si) {
		if (masterServers.containsValue(si))
			return true;
		else
			return false;
	}
}
