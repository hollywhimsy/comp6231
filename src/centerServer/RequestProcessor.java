package centerServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import common.Logger;
import record.Record;
import record.StudentRecord;
import record.TeacherRecord;

public class RequestProcessor
{
	private int groupIndex;
	private HashMap<String, Integer> myGroupPorts;
	private String cityAbbr;
	private Logger logger;
	private List<HashMap<String, Integer>> alives; // 0 -> dead, 1 -> alive
	private List<HashMap<String, Integer>> ports;
	private HashMap<Character, List<Record>> recordsMap; // The map which contains the records
	private HashMap<String, Record> indexPerId;
	private Integer lastTeacherId = 0; // Needs synchronization
	private Integer lastStudentId = 0; // Needs synchronization
	private FifoMulticast fifoMulticast;
	
	

	public RequestProcessor(int groupIndex, String cityAbbr, Logger logger, List<HashMap<String, Integer>> alives, List<HashMap<String, Integer>> ports,
			HashMap<Character, List<Record>> recordsMap, HashMap<String, Record> indexPerId)
	{
		super();
		this.groupIndex = groupIndex;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.alives = alives;
		this.ports = ports;
		this.recordsMap = recordsMap;
		this.indexPerId = indexPerId;
		
		myGroupPorts = this.ports.get(this.groupIndex);
		
		fifoMulticast = new FifoMulticast(groupIndex, alives, ports, cityAbbr, logger);
	}

	/*
	 * request can be: 
	 * HeartBit: server returns ACK to show it's alive
	 * createTRecord~[String]: the [String] gives the parameters and server creates a teacher record and returns a boolean 
	 * createSRecord~[String]: the [String] gives the parameters and server creates a student record and returns a boolean 
	 * getRecordsCount~[String]: the [String] gives the parameters and server returns the records count 
	 * editRecord~[String]: the [String] gives the parameters and server edits the record and returns a boolean
	 * recordExist~[String]: the [String] gives the parameters and server returns true/false 
	 * transferRecord~[String]: the [String] gives the parameters and server transfers the record and returns a boolean
	 */
	public String[] processRequest(String request)
	{
		String[] response = new String[2];
		
		if (request.trim().toLowerCase().contains("HeartBit".toLowerCase()))
		{
			response[0] = "ACK";
			response[1] = "";
 			return response;
		}

		if (request.toLowerCase().contains("createtrecord") || request.toLowerCase().contains("createmytrecord"))
		{
			// [String]: firstName~lastName~address~phoneNumber~specialization~location~managerId
			String[] parts = isRequestValid(request, 8);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			if (request.toLowerCase().contains("createtrecord"))
			{
				String msg = "createMyTRecord";
				for (int j = 1; j < 8; j++)
				{
					msg = msg + "~" + parts[j];
				}
				
				fifoMulticast.broadcast(msg);
			}

			if (createTRecord(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]))
			{
				response[0] = "ACK"; // Valid request + the process is done
				response[1] = "";
	 			return response;
			} else
			{
				// fail to do
				response[0] = "ERR"; // Valid request + Error in processing the request
				response[1] = "";
	 			return response;
			}
		}
		
		if (request.toLowerCase().contains("createsrecord") || request.toLowerCase().contains("createmysrecord"))
		{
			// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
			String[] parts = isRequestValid(request, 7);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			if (request.toLowerCase().contains("createsrecord"))
			{
				String msg = "createMySRecord";
				for (int j = 1; j < 7; j++)
				{
					msg = msg + "~" + parts[j];
				}
				
				fifoMulticast.broadcast(msg);
				
			}
			
			if (createSRecord(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]))
			{
				response[0] = "ACK"; // Valid request + the process is done
				response[1] = "";
	 			return response;
			} else
			{
				// fail to do
				response[0] = "ERR"; // Valid request + Error in processing the request
				response[1] = "";
	 			return response;
			}
		}
		
		if (request.toLowerCase().contains("editrecord") || request.toLowerCase().contains("editmyrecord"))
		{
			// [String]: recordID~fieldName~newValue~managerId
			String[] parts = isRequestValid(request, 5);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			if (request.toLowerCase().contains("editrecord"))
			{
				String msg = "editMyRecord";
				for (int j = 1; j < 5; j++)
				{
					msg = msg + "~" + parts[j];
				}
				
				fifoMulticast.broadcast(msg);
				
			}

			if (editRecord(parts[1], parts[2], parts[3], parts[4]))
			{
				response[0] = "ACK"; // Valid request + the process is done
				response[1] = "";
	 			return response;
			} else
			{
				// fail to do
				response[0] = "ERR"; // Valid request + Error in processing the request
				response[1] = "";
	 			return response;
			}
		}
		
		if (request.toLowerCase().contains("recordExist".toLowerCase()))
		{
			// [String]: recordId~managerId
			String[] parts = isRequestValid(request, 3);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			if (recordExist(parts[1], parts[2]))
			{
				response[0] = "ACK"; // Valid request + the process is done
				response[1] = "";
	 			return response;
			} else
			{
				// fail to do
				response[0] = "ERR"; // Valid request + Error in processing the request
				response[1] = "";
	 			return response;
			}
		}

		if (request.toLowerCase().contains("transferrecord") || request.toLowerCase().contains("transfermyrecord"))
		{
			// [String]: recordId~remoteCenterServerName~managerId
			String[] parts = isRequestValid(request, 4);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			if (request.toLowerCase().contains("transferrecord"))
			{
				String msg = "transferMyRecord";
				for (int j = 1; j < 4; j++)
				{
					msg = msg + "~" + parts[j];
				}
				
				fifoMulticast.broadcast(msg);
				
			}

			if (transferRecord(parts[1], parts[2], parts[3]))
			{
				response[0] = "ACK"; // Valid request + the process is done
				response[1] = "";
	 			return response;
			} else
			{
				// fail to do
				response[0] = "ERR"; // Valid request + Error in processing the request
				response[1] = "";
	 			return response;
			}
		}

		if (request.toLowerCase().contains("getrecordscount") || request.toLowerCase().contains("getmyrecordscount"))
		{
			// [String]: managerId
			String[] parts = isRequestValid(request, 2);
			if (parts == null)
			{
				response[0] = "INV"; // Invalid request
				response[1] = "";
	 			return response;
			}
			
			String result = getRecordsCount(parts[1]);

			if (request.toLowerCase().contains("getrecordscount"))
			{
				for (String srv : myGroupPorts.keySet())
				{
					if (!srv.toUpperCase().equals(cityAbbr.toUpperCase()))
					{
						RudpClient client = new RudpClient(myGroupPorts.get(srv), cityAbbr, logger);
						String tempStr = client.requestRemote("getMyRecordsCount~" + srv + "0001").trim();
	
						if (tempStr == null)
						{
							logger.logToFile(cityAbbr + "[RecordManagerImpl.getRecordsCount()]: UDP server did not respond on port:" + myGroupPorts.get(srv)
									+ " {CallerManagerID: " + parts[1] + "}");
						} else
						{
							if (tempStr.contains("ACK"))
							{
								result = result + ", " + tempStr.substring(3, tempStr.length());
							}
						}
					}
				}
			}

			response[0] = "ACK"; // Valid request + the process is done
			response[1] = result;
 			return response;
		}

		logger.logToFile(cityAbbr + "[CenterImpl.processRequest()]: Request Was Invalid!");

		response[0] = "INV"; // Invalid request
		response[1] = "";
		return response;
	}

	private String[] isRequestValid(String request, int argsNum)
	{
		String[] parts = request.trim().split("~");
		if (parts.length != argsNum)
		{
			return null;
		}
		
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i] == null)
			{
				return null;
			}
			if (parts[i].length() <= 0)
			{
				return null;
			}
		}
		
		return parts;
	}
	
//	private void broadcast(String msg)
//	{
//		for (int i = 0; i < 3; i++)
//		{
//			if (i != groupIndex)
//			{
//				if (alives.get(i).get(cityAbbr.toUpperCase()) == 1)
//				{
//					RudpClient client = new RudpClient(ports.get(i).get(cityAbbr), cityAbbr, logger);						
//					String result = client.requestRemote(msg).trim();
//					
//					if (result.equals("DWN"))
//					{
//						alives.get(i).put(cityAbbr, 0); // this server is down	
//						logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the request is broadcasted to " + cityAbbr + " listening on " 
//								+ ports.get(i).get(cityAbbr) + ". This server is Dead");
//					}
//					else
//					{
//						logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the request is broadcasted to " + cityAbbr + " listening on " 
//								+ ports.get(i).get(cityAbbr));
//					}
//				}
//				else
//				{
//					logger.logToFile(cityAbbr + "[CenterServerCore.broadcast()]: the " + cityAbbr + " listening on " + ports.get(i).get(cityAbbr) 
//							+ " is DOWN! => No broadcast to it!");
//				}
//			}
//		}
//	}
	
	private boolean createTRecord(String firstName, String lastName, String address, String phoneNumber, String specialization, String location,
			String managerId)
	{
		if ((firstName == null) || (lastName == null) || (address == null) || (phoneNumber == null) || (specialization == null) || (location == null))
		{
			logger.logToFile(cityAbbr + "[MtlCenter.createTRecord()]: createTRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		Integer phone = Integer.parseInt(phoneNumber);
		List<String> spec = new ArrayList<>();
		String[] parts = specialization.split(",");
		for (int i = 0; i < parts.length; i++)
			spec.add(parts[i]);
		String id = produceNewId("TR", managerId);
		if (id != null)
		{
			TeacherRecord teacher = new TeacherRecord(id, firstName, lastName, address, phone, spec, location);

			synchronized (indexPerId)
			{
				synchronized (recordsMap)
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

			logger.logToFile(cityAbbr + "[MtlCenter.createTRecord()]: createTRecord is successfully done (ID: " + id + ")"
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	private boolean createSRecord(String firstName, String lastName, String coursesRegistred, String status, String statusDate, String managerId)
	{
		if ((firstName == null) || (lastName == null) || (coursesRegistred == null) || (statusDate == null))
		{
			logger.logToFile(cityAbbr + "[MtlCenter.createSRecord()]: createSRecord failed (at least one property was NULL)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		Date date = new Date();
		date.parse(statusDate);
		List<String> courses = new ArrayList<>();
		String[] parts = coursesRegistred.split(",");
		for (int i = 0; i < parts.length; i++)
			courses.add(parts[i]);
		boolean stat = false;
		if (status.toLowerCase().equals("true"))
			stat = true;
		String id = produceNewId("SR", managerId);
		if (id != null)
		{
			StudentRecord student = new StudentRecord(id, firstName, lastName, courses, stat, date);

			synchronized (indexPerId)
			{
				synchronized (recordsMap)
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

			logger.logToFile(cityAbbr + "[MtlCenter.createSRecord()]: createSRecord is successfully done (ID: " + id + ")"
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		}
		return false;
	}

	private String getRecordsCount(String managerId)
	{
		int count = 0;
		synchronized (indexPerId)
		{
			synchronized (recordsMap)
			{
				count = indexPerId.size();
			}
		}

		String result = cityAbbr + " " + count;

		logger.logToFile(cityAbbr + "[RecordManagerImpl.getRecordCounts()]: getRecordsCount is successfully done" + " {CallerManagerID: "
				+ managerId + "}");
		return result;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private boolean editRecord(String recordID, String fieldName, String newValue, String managerId)
	{
		if ((recordID == null) || (fieldName == null) || (newValue == null))
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

		String oldValue = null;
		Character ch = recordID.toUpperCase().charAt(0);
		// If the record is Teacher type
		if (ch.equals('T'))
		{
			synchronized (indexPerId)
			{
				synchronized (recordsMap)
				{
					TeacherRecord teacher = (TeacherRecord) indexPerId.get(recordID);
					if (teacher == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)"
								+ " {CallerManagerID: " + managerId + "}");
						return false;
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
						teacher.setPhoneNumber(Integer.parseInt(newValue));
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: phoneNumber"
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "location":
						oldValue = teacher.getLocation();
						teacher.setLocation(newValue);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: location"
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					default:
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)"
								+ " {CallerManagerID: " + managerId + "}");
						return false;
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
					StudentRecord student = (StudentRecord) indexPerId.get(recordID);
					if (student == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)"
								+ " {CallerManagerID: " + managerId + "}");
						return false;
					}

					switch (fieldName)
					{
					case "coursesRegistred":
						List<String> courses = new ArrayList<>();
						String[] parts = newValue.split(",");
						for (int i = 0; i < parts.length; i++)
							courses.add(parts[i]);
						student.setCoursesRegistred(courses);// set the course status as well
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: coursesRegistred"
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "status":
						boolean status;
						if (newValue.toLowerCase().equals("true"))
							status = true;
						else
							status = false;
						oldValue = String.valueOf(student.getStatus());
						student.setStatus(status);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: status"
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					case "statusDate":
						Date date = new Date();
						date.parse(newValue);
						oldValue = student.getStatusDate().toString();
						student.setStatusDate(date);
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: statusDate"
								+ " oldValue: " + oldValue + " newValue: " + newValue + " {CallerManagerID: " + managerId + "}");
						break;
					default:
						logger.logToFile(cityAbbr + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)"
								+ " {CallerManagerID: " + managerId + "}");
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean recordExist(String recordId, String managerId)
	{
		if (!(isRecordIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.recordExist()]: editRecord failed (recordId format is incorrect)"
					+ " {CallerManagerID: " + managerId + "}");
			return false;
		}

		synchronized (indexPerId)
		{
			synchronized (recordsMap)
			{
				if (indexPerId.containsKey(recordId))
					return true;
				else
					return false;
			}
		}
	}

	private boolean transferRecord(String recordId, String remoteCenterServerName, String managerId)
	{
		if (!(isRecordIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! recordId format is incorrect" + " {CallerManagerID: "
					+ managerId + "}");
			return false;
		}

		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || remoteCenterServerName.toUpperCase().equals("LVL")
				|| remoteCenterServerName.toUpperCase().equals("DDO"))))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Error! remoteCenterServerName is invalid" + " {CallerManagerID: "
					+ managerId + "}");
			return false; // Given city name is incorrect
		}

		String city = remoteCenterServerName.toUpperCase().trim();

		if (recordId.toUpperCase().trim().charAt(0) == 'T') // Teacher record
		{
			TeacherRecord teacher;
			synchronized (indexPerId)
			{
				synchronized (recordsMap)
				{
					teacher = (TeacherRecord) indexPerId.get(recordId.toUpperCase().trim()); // Retrieve the record
					if (teacher == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
								+ " {CallerManagerID: " + managerId + "}");
						return false; // The given record dosen't exist in this server
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
			RudpClient rudpClient = new RudpClient(myGroupPorts.get(city), cityAbbr, logger);
			// [String]:
			// firstName~lastName~address~phoneNumber~specialization~location~managerId
			String reply = rudpClient.requestRemote("createTRecord~" + teacher.getFirstName() + "~" + teacher.getLastName() + "~"
					+ teacher.getAddress() + "~" + teacher.getPhone().toString() + "~" + spec + "~" + city + "~" + city + "0001");

			if (reply.toUpperCase().contains("NAK"))
			{
				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record is not added to thenew server"
						+ " {CallerManagerID: " + managerId + "}");
				return false; // The given record dosen't exist in this server
			}

			// recMng.shutdown();
		}

		if (recordId.toUpperCase().trim().charAt(0) == 'S') // Student record
		{
			StudentRecord student;
			synchronized (indexPerId)
			{
				synchronized (recordsMap)
				{
					student = (StudentRecord) indexPerId.get(recordId.toUpperCase().trim()); // Retrieve the record
					if (student == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
								+ " {CallerManagerID: " + managerId + "}");
						return false; // The given record dosen't exist in this server
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
			RudpClient rudpClient = new RudpClient(myGroupPorts.get(city), cityAbbr, logger);
			// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
			String reply = rudpClient.requestRemote("createSRecord~" + student.getFirstName() + "~" + student.getLastName() + "~" + courses
					+ "~" + student.getStatus() + "~" + student.getDate().toString() + "~" + city + "0001");

			if (reply.toUpperCase().contains("NAK"))
			{
				logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: Failed! The given record is not added to thenew server"
						+ " {CallerManagerID: " + managerId + "}");
				return false; // The given record dosen't exist in this server
			}
		}

		logger.logToFile(cityAbbr + "[RecordManagerImpl.transferRecord()]: The given record is transfered successfully to: " + city
				+ " {CallerManagerID: " + managerId + "}");

		return true;
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
}
