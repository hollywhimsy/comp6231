package centerServer;

import common.Logger;
import record.Record;
import record.StudentRecord;
import record.TeacherRecord;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CenterServerCore extends Thread
{
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbr = new String(); // City abbreviation like "MTL"
	private HashMap<Character, List<Record>> recordsMap; // The map which contains the records
	private HashMap<String, Record> indexPerId;
	private Integer lastTeacherId = 0; // Needs synchronization
	private Integer lastStudentId = 0; // Needs synchronization
	private Logger logger;
	private HashMap<String, Integer> ports;

	// Constructor
	public CenterServerCore(HashMap<Character, List<Record>> recordsMap, HashMap<String, Record> indexPerId, int listenPort, String cityAbbr,
			Logger logger, HashMap<String, Integer> ports)
	{
		super();
		this.recordsMap = recordsMap;
		this.indexPerId = indexPerId;
		this.listenPort = listenPort;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.ports = ports;

		logger.logToFile(cityAbbr + "[RUDPServer Constructor]: UDPServer is initialized");
	}

	public void run()
	{
		DatagramSocket socket = null; // Socket declaration
		try
		{
			socket = new DatagramSocket(listenPort); // Socket initiation by given UDP port number
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: Listening on " + listenPort + " UDP Port");

			while (true) // Always receive the requests and response accordingly
			{
				byte[] buffer = new byte[1000]; // Buffer which receives the request
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				String req = new String(request.getData()); // Extract the request data
				logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Recieved a Request!");
				String[] parts = req.trim().split("#");
				if (parts.length != 2)
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (length)! NAK sent to the requester");
					continue;
				}
				if (!parts[1].trim().equals(generateChecksum(parts[0])))
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (checksum)! NAK sent to the requester");
					// corrupted request
					continue;
				}

				String result = processRequest(parts[0].trim());
				socket.send(prepareRespons(result, request)); // Send the reply

				logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
						+ request.getPort());
			}
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP Socket Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP IO Exception Error!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP Socket is Closed!");
		}
	}

	private String generateChecksum(String str)
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

	private DatagramPacket prepareRespons(String rep, DatagramPacket request)
	{
		String str = rep + "#" + generateChecksum(rep);
		byte[] replyBuffer = new byte[512];
		replyBuffer = str.getBytes(); // Convert String to Byte to send to the client
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, request.getAddress(), request.getPort());

		return reply;
	}

	/*
	 * request can be: HeartBit: server returns ACK to show it's alive
	 * createTRecord~[String]: the [String] gives the parameters and server creates
	 * a teacher record and returns a boolean createSRecord~[String]: the [String]
	 * gives the parameters and server creates a student record and returns a
	 * boolean getRecordsCount~[String]: the [String] gives the parameters and
	 * server returns the records count editRecord~[String]: the [String] gives the
	 * parameters and server edits the record and returns a boolean
	 * recordExist~[String]: the [String] gives the parameters and server returns
	 * true/false transferRecord~[String]: the [String] gives the parameters and
	 * server transfers the record and returns a boolean
	 */
	private String processRequest(String request)
	{
		if (request.trim().toLowerCase().contains("HeartBit".toLowerCase()))
		{
			return "ACK";
		}

		if (request.trim().toLowerCase().contains("createTRecord".toLowerCase()))
		{
			// [String]:
			// firstName~lastName~address~phoneNumber~specialization~location~managerId
			String[] parts = request.split("~");
			if (parts.length != 8)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			if (createTRecord(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]))
			{
				return "ACK";
			} else
			{
				// fail to create
				return "NAK3";
			}
		}

		if (request.trim().toLowerCase().contains("createSRecord".toLowerCase()))
		{
			// [String]: firstName~lastName~coursesRegistred~status~statusDate~managerId
			String[] parts = request.split("~");
			if (parts.length != 7)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			if (createSRecord(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]))
			{
				return "ACK";
			} else
			{
				// fail to create
				return "NAK3";
			}
		}

		if (request.trim().toLowerCase().contains("editRecord".toLowerCase()))
		{
			// [String]: recordID~fieldName~newValue~managerId
			String[] parts = request.split("~");
			if (parts.length != 5)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			if (editRecord(parts[1], parts[2], parts[3], parts[4]))
			{
				return "ACK";
			} else
			{
				// fail to create
				return "NAK3";
			}
		}

		if (request.trim().toLowerCase().contains("recordExist".toLowerCase()))
		{
			// [String]: recordId~managerId
			String[] parts = request.split("~");
			if (parts.length != 3)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			if (recordExist(parts[1], parts[2]))
			{
				return "ACK";
			} else
			{
				// fail to create
				return "NAK3";
			}
		}

		if (request.trim().toLowerCase().contains("transferRecord".toLowerCase()))
		{
			// [String]: recordId~remoteCenterServerName~managerId
			String[] parts = request.split("~");
			if (parts.length != 4)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			if (transferRecord(parts[1], parts[2], parts[3]))
			{
				return "ACK";
			} else
			{
				// fail to create
				return "NAK3";
			}
		}

		if (request.trim().toLowerCase().contains("getRecordsCount".toLowerCase()))
		{
			// [String]: managerId
			String[] parts = request.split("~");
			if (parts.length != 2)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			String result = getRecordsCount(parts[1]);

			for (String srv : ports.keySet())
			{
				if (!srv.toUpperCase().equals(cityAbbr.toUpperCase()))
				{
					RudpClient client = new RudpClient(ports.get(srv), cityAbbr, logger);// create a UDPClient by itself, connect to the UDPServer by
																							// udpPort
					String tempStr = client.requestRemote("getMyRecordsCount~" + parts[1]).trim();

					if (tempStr == null)
					{
						logger.logToFile(cityAbbr + "[RecordManagerImpl.getRecordsCount()]: UDP server did not respond on port:" + ports.get(srv)
								+ " {CallerManagerID: " + parts[1] + "}");
					} else
					{
						result = result + ", " + tempStr;
					}
				}
			}

			return result;
		}

		if (request.trim().toLowerCase().contains("getMyRecordsCount".toLowerCase()))
		{
			// [String]: managerId
			String[] parts = request.split("~");
			if (parts.length != 2)
				return "NAK1";
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i] == null)
					return "NAK2";
			}

			String result = getRecordsCount(parts[1]);

			return result;
		}

		logger.logToFile(cityAbbr + "[CenterImpl.processRequest()]: Request Was Invalid!");

		return "NAK4";
	}

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

			logger.logToFile(cityAbbr + "[MtlCenter.createSRecord()]: createSRecord is successfully done (ID: " + id + ")"
					+ " {CallerManagerID: " + managerId + "}");
			return true;
		}
		return false;
	}

	private String getRecordsCount(String managerId)
	{
		int count = 0;
		synchronized (recordsMap)
		{
			synchronized (indexPerId)
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

		if (!(isIdFormatCorrect(recordID)))
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
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
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
		if (!(isIdFormatCorrect(recordId)))
		{
			logger.logToFile(cityAbbr + "[RecordManagerImpl.recordExist()]: editRecord failed (recordId format is incorrect)"
					+ " {CallerManagerID: " + managerId + "}");
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

	private boolean transferRecord(String recordId, String remoteCenterServerName, String managerId)
	{
		if (!(isIdFormatCorrect(recordId)))
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
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
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
			RudpClient rudpClient = new RudpClient(ports.get(city), cityAbbr, logger);
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
			synchronized (recordsMap)
			{
				synchronized (indexPerId)
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
			RudpClient rudpClient = new RudpClient(ports.get(city), cityAbbr, logger);
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
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
}
