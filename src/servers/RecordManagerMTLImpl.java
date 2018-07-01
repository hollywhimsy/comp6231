package servers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import common.Infrastucture;
import common.Logger;
import ddoStub.RecordManagerDDOImplService;
import lvlStub.RecordManagerLVLImplService;
import mtlStub.RecordManagerMTLImplService;
import records.Record;
import records.StudentRecord;
import records.TeacherRecord;
import udp.UDPClient;
import udp.UDPServer;

public class RecordManagerMTLImpl implements RecordManagerInterface
{
	private static HashMap<Character, List<Record>> recordsMap = new HashMap<>(); // Needs synchronization
	private static HashMap<String, Record> indexPerId = new HashMap<>(); // Needs synchronization
	private static Integer lastTeacherId = 0; // Needs synchronization
	private static Integer lastStudentId = 0; // Needs synchronization
	private static String cityAbbr = "MTL";
	private static List<Integer> otherServersUDPPorts;
	private static Logger logger = new Logger("SRV_" + cityAbbr.toUpperCase().trim() + ".log");
	private static boolean isInitialized = false;

	// Constructor
	public RecordManagerMTLImpl()
	{
		if (!isInitialized)
		{	
			otherServersUDPPorts = Infrastucture.getOtherServersUDPPorts(cityAbbr);
			
			// Start UDP CenterServer as a separate thread
			Integer udpPort = Infrastucture.getServerPortUDP(cityAbbr);
			UDPServer srv = new UDPServer(indexPerId, udpPort, cityAbbr, logger);
			srv.start();
			
			logger.logToFile(cityAbbr + " City has been started!");
					
			isInitialized = true;
		}		
	}

	@Override
	public String createTRecord(String firstName, String lastName, String address, Integer phoneNumber, String specialization, String location, 
			String managerId)
	{
		if ((firstName == null) || (lastName == null) || (address == null) || (phoneNumber == null)
				|| (specialization == null) || (location == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createTRecord()]: Error! createTRecord failed (at least one property was NULL)" + 
					" {CallerManagerID: " + managerId + "}");
			return "Error! createTRecord failed (at least one property was NULL)";
		}

		List<String> spec = new ArrayList<>();
		String[] parts = specialization.split(",");
		for (int i = 0; i < parts.length; i++)
			spec.add(parts[i]);
		String id = produceNewId("TR", managerId);
		if (id != null)
		{
			TeacherRecord teacher = new TeacherRecord(id, firstName, lastName, address, phoneNumber, spec, location);

			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					if (!recordsMap.containsKey(teacher.getLastName().toUpperCase().charAt(0)))
					{
						ArrayList<Record> recordList = new ArrayList<>();
						recordsMap.put(teacher.getLastName().toUpperCase().charAt(0), recordList);
					}
					recordsMap.get(teacher.getLastName().toUpperCase().charAt(0)).add(teacher);
					indexPerId.put(id, teacher);
				}
			}

			logger.logToFile(cityAbbr + "[RecordManagerImpl.createTRecord()]: createTRecord is successfully done (ID: " + id + ")"
					+ " {CallerManagerID: " + managerId + "}");
			return "createTRecord is successfully done (ID: " + id + ")";
		}
		return "Error! createTRecord failed (Unknown error!)";
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public String createSRecord(String firstName, String lastName, String coursesRegistred, boolean status, String statusDate, String managerId)
	{
		if ((firstName == null) || (lastName == null) || (coursesRegistred == null) || (statusDate == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.createSRecord()]: createSRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return "Error! createSRecord failed (at least one property was NULL)";
		}

		Date date = new Date();
		date.parse(statusDate);
		List<String> courses = new ArrayList<>();
		String[] parts = coursesRegistred.split(",");
		for (int i = 0; i < parts.length; i++)
			courses.add(parts[i]);
		String id = produceNewId("SR", managerId);
		if (id != null)
		{
			StudentRecord student = new StudentRecord(id, firstName, lastName, courses, status, date);

			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					if (!recordsMap.containsKey(student.getLastName().toUpperCase().charAt(0)))
					{
						ArrayList<Record> recordList = new ArrayList<>();
						recordsMap.put(student.getLastName().toUpperCase().charAt(0), recordList);
					}
					indexPerId.put(id, student);
					recordsMap.get(student.getLastName().toUpperCase().charAt(0)).add(student);
				}
			}

			logger.logToFile(cityAbbr + "[RecordManagerImpl.createSRecord()]: createSRecord is successfully done (ID: " + id + ")"
					+ " {CallerManagerID: " + managerId + "}");
			return "createSRecord is successfully done (ID: " + id + ")";
		}
		return "Error! createSRecord failed (Unknown error!)";
	}

	@Override
	public String getRecordsCount(String managerId)
	{
		int count;
		synchronized (recordsMap)
		{
			synchronized (indexPerId)
			{
				count = indexPerId.size();				
			}
		}

		String result = cityAbbr + " " + count;

		for (Integer udpPort : otherServersUDPPorts)
		{
			UDPClient client = new UDPClient(udpPort, logger);// create a UDPClient by itself, connect to the UDPServer by udpPort
			String tempStr = client.requestCount().trim();

			if (tempStr == null)
			{
				logger.logToFile(cityAbbr + "[RecordManagerImpl.getRecordCounts()]: UDP server did not respond on port:" + udpPort + 
						" {CallerManagerID: " + managerId + "}");
			} else
			{
				result = result + ", " + tempStr;
			}
		}

		logger.logToFile(cityAbbr + "[RecordManagerImpl.getRecordCounts()]: getRecordCounts is successfully done" + " {CallerManagerID: " + 
				managerId + "}");
		return result;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public String editRecord(String recordId, String fieldName, String newValue, String managerId)
	{
		if ((recordId == null) || (fieldName == null) || (newValue == null) || (managerId == null))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return "Error! editRecord failed (at least one property was NULL)";
		}

		if (!(isIdFormatCorrect(recordId)))// efficiency
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId format is incorrect)" + " {CallerManagerID: " 
					+ managerId + "}");
			return "Error! editRecord failed (recordId format is incorrect)";
		}

		if (!recordExist(recordId, managerId))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (There is no record with this ID)" + " {CallerManagerID: " 
					+ managerId + "}");
			return "Error! editRecord failed (There is no record with this ID)";
		}
		
		String oldValue = null;
		Character ch = recordId.toUpperCase().charAt(0);
		// If the record is Teacher type
		if (ch.equals('T'))
		{
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					TeacherRecord teacher = (TeacherRecord) indexPerId.get(recordId);
					if (teacher == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)" + 
								" {CallerManagerID: " + managerId + "}");
						return "Error! editRecord failed (Given ID doesn't exist)";
					}

					switch (fieldName)
					{
					case "address":
						oldValue = teacher.getAddress();
						teacher.setAddress(newValue);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: address" + " oldValue: " 
								+ oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "phoneNumber":
						oldValue = teacher.getPhone().toString();
						teacher.setPhone(Integer.parseInt(newValue));
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: phoneNumber" + 
								" oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "location":
						oldValue = teacher.getLocation();
						teacher.setLocation(newValue);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: location" + 
								" oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					default:
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)" + 
								" {CallerManagerID: " + managerId + "}");
						return "Error! editRecord failed (Given fieldName is invalid)";
					}
				}
			}
		}

		// If the record is Student type
		if (ch.equals('S'))
		{
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					StudentRecord student = (StudentRecord) indexPerId.get(recordId);
					if (student == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)" + 
								" {CallerManagerID: " + managerId + "}");
						return "Error! editRecord failed (Given ID doesn't exist)";
					}

					switch (fieldName)
					{
					case "coursesRegistred":
						ArrayList<String> courses = new ArrayList<>();
						String[] parts = newValue.split(",");
						for (int i = 0; i < parts.length; i++)
							courses.add(parts[i]);
						student.setCoursesRegistred(courses);// set the course status as well
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: coursesRegistred" 
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "status":
						boolean status;
						if (newValue.equals("true"))
							status = true;
						else
							status = false;
						oldValue = String.valueOf(student.getStatus());
						student.setStatus(status);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: status" + " oldValue: " 
								+ oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "statusDate":
						Date date = new Date();
						date.parse(newValue);
						oldValue = student.getStatusDate().toString();
						student.setStatusDate(date);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: statusDate" + 
								" oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					default:
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)" + 
								" {CallerManagerID: " + managerId + "}");
						return "Error! editRecord failed (Given fieldName is invalid)";
					}
				}
			}
		}

		return "Edit Record is successfully done (Field: " + fieldName + ", New Value: " + newValue + ")";
	}

	@Override
	public boolean recordExist(String recordId, String managerId)
	{
		if (!(isIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.recordExist()]: editRecord failed (recordId format is incorrect)" + 
					" {CallerManagerID: " + managerId + "}"); 
			return false;
		}

		synchronized (recordsMap)
		{
			synchronized (indexPerId)
			{
				if (indexPerId.containsKey(recordId))
					return true;
				else
					return false;
			}
		}
	}

	@Override
	public String transferRecord(String managerId, String recordId, String remoteCenterServerName)
	{
		if (!(isIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! recordId format is incorrect" + " {CallerManagerID: "
					+ managerId + "}");
			return "Error! recordId format is incorrect";
		}

		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || remoteCenterServerName.toUpperCase().equals("LVL")
				|| remoteCenterServerName.toUpperCase().equals("DDO"))))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! remoteCenterServerName is invalid" + " {CallerManagerID: "
					+ managerId + "}");
			return "Error! remoteCenterServerName is invalid";
		}

		if (recordId.toUpperCase().trim().charAt(0) == 'T') // Teacher record
		{
			TeacherRecord teacher;
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					teacher = (TeacherRecord) indexPerId.get(recordId.toUpperCase().trim()); // Retrieve the record
					if (teacher == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
								+ " {CallerManagerID: " + managerId + "}");
						return "Failed! The given record dosen't exist in this server"; // The given record dosen't exist in this server
					}
					recordsMap.get(teacher.getLastName().toUpperCase().charAt(0)).remove(teacher); // Delete the record from the Map
					indexPerId.remove(recordId.toUpperCase().trim(), teacher); // Delete the record from the Index
				}
			}

			String spec = "";
			String spliter = "";
			for (int i = 0; i < teacher.getSpecialization().size(); i++)
			{
				// form the acceptable format by remote CORBA server
				spec = spec + spliter + teacher.getSpecialization().get(i);
				spliter = ",";
			}
			
			// Call the remote server to add this record on that
			if (remoteCenterServerName.toUpperCase().equals("MTL"))
			{
				RecordManagerMTLImplService caller = new RecordManagerMTLImplService();
				String result = caller.getRecordManagerMTLImpl().createTRecord(teacher.getFirstName(), teacher.getLastName(), teacher.getAddress(),
						teacher.getPhone(), spec, remoteCenterServerName.toUpperCase(), remoteCenterServerName.toUpperCase() + "0001");

				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}
			if (remoteCenterServerName.toUpperCase().equals("LVL"))
			{
				RecordManagerLVLImplService caller = new RecordManagerLVLImplService();
				String result = caller.getRecordManagerLVLImpl().createTRecord(teacher.getFirstName(), teacher.getLastName(), teacher.getAddress(),
						teacher.getPhone(), spec, remoteCenterServerName.toUpperCase(), remoteCenterServerName.toUpperCase() + "0001");

				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}
			if (remoteCenterServerName.toUpperCase().equals("DDO"))
			{
				RecordManagerDDOImplService caller = new RecordManagerDDOImplService();
				String result = caller.getRecordManagerDDOImpl().createTRecord(teacher.getFirstName(), teacher.getLastName(), teacher.getAddress(),
						teacher.getPhone(), spec, remoteCenterServerName.toUpperCase(), remoteCenterServerName.toUpperCase() + "0001");

				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}
		}

		if (recordId.toUpperCase().trim().charAt(0) == 'S') // Student record
		{
			StudentRecord student;
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
				{
					student = (StudentRecord) indexPerId.get(recordId.toUpperCase().trim()); // Retrieve the record
					if (student == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
								+ " {CallerManagerID: " + managerId + "}");
						return "Failed! The given record dosen't exist in this server"; // The given record dosen't exist in this server
					}
					recordsMap.get(student.getLastName().toUpperCase().charAt(0)).remove(student); // Delete the record from the Map
					indexPerId.remove(recordId.toUpperCase().trim(), student); // Delete the record from the Index
				}
			}

			String courses = "";
			String spliter = "";
			for (int i = 0; i < student.getCoursesRegistred().size(); i++)
			{
				// form the acceptable format by remote CORBA server
				courses = courses + spliter + student.getCoursesRegistred().get(i);
				spliter = ",";
			}
			// Call the remote server to add this record on that
			if (remoteCenterServerName.toUpperCase().equals("MTL"))
			{
				RecordManagerMTLImplService caller = new RecordManagerMTLImplService();
				String result = caller.getRecordManagerMTLImpl().createSRecord(student.getFirstName(), student.getLastName(), courses,
						student.getStatus(), student.getStatusDate().toString(), remoteCenterServerName + "0001");
				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}
			if (remoteCenterServerName.toUpperCase().equals("LVL"))
			{
				RecordManagerLVLImplService caller = new RecordManagerLVLImplService();
				String result = caller.getRecordManagerLVLImpl().createSRecord(student.getFirstName(), student.getLastName(), courses,
						student.getStatus(), student.getStatusDate().toString(), remoteCenterServerName + "0001");
				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}
			if (remoteCenterServerName.toUpperCase().equals("DDO"))
			{
				RecordManagerDDOImplService caller = new RecordManagerDDOImplService();
				String result = caller.getRecordManagerDDOImpl().createSRecord(student.getFirstName(), student.getLastName(), courses,
						student.getStatus(), student.getStatusDate().toString(), remoteCenterServerName + "0001");
				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: " + result + " {CallerManagerID: " + managerId + "}");
				return "Record Transfered successfully (" + result + ")";
			}			
		}

		return null;
	}

	private String produceNewId(String prefix, String managerId)
	{
		if (prefix.toUpperCase().equals("TR"))
		{
			if (lastTeacherId >= 99999) // ID can have 5 digits only not more
			{
				logger.logToFile(cityAbbr + "[RecordManagerClass.produceNewId()]: produceNewId failed (Teachers record number reached 99999)" 
						+ " {CallerManagerID: " + managerId + "}");
				return null;
			}

			// Need to be locked & Synchronized
			String tId = null;
			synchronized (lastTeacherId)
			{
				lastTeacherId++;
				tId = lastTeacherId.toString();
			}

			int zeroNum = 5 - tId.length();
			for (int i = 1; i <= zeroNum; i++)
			{
				tId = "0" + tId;
			}
			tId = "TR" + tId;

			return tId;
		}

		if (prefix.toUpperCase().equals("SR"))
		{
			if (lastStudentId >= 99999) // ID can have 5 digits only not more
			{
				logger.logToFile(cityAbbr + "[RecordManagerClass.produceNewId()]: produceNewId failed (Students record number reached 99999)" 
						+ " {CallerManagerID: " + managerId + "}");
				return null;
			}

			// Need to be locked & Synchronized
			String sId = null;
			synchronized (lastStudentId)
			{
				lastStudentId++;
				sId = lastStudentId.toString();
			}

			int zeroNum = 5 - sId.length();
			for (int i = 1; i <= zeroNum; i++)
			{
				sId = "0" + sId;
			}
			sId = "SR" + sId;

			return sId;
		}

		return null;
	}

	private boolean isIdFormatCorrect(String id)
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

		if (!isNumeric(id.substring(2, 5)))
		{
			return false;
		}

		return true;
	}
	
	private boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
