package corba;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import common.Infrastucture;
import common.Logger;
import record.Record;
import record.StudentRecord;
import record.TeacherRecord;
import udp.UDPClient;

public class RecordManagerCORBAImpl extends RecordManagerCORBAPOA
{
	private ORB orb;
	private HashMap<Character, List<Record>> recordsMap; // Needs synchronization
    private HashMap<String, Record> indexPerId = new HashMap<>(); // Needs synchronization, acts as an index to find records by ID
    private Integer lastTeacherId = 0; // Needs synchronization
    private Integer lastStudentId = 0; // Needs synchronization
    private String cityAbbreviation;
    private List<Integer> otherServersUDPPorts;
    private Logger logger;
	
	public void setOrb(ORB orb) 
	{
		this.orb = orb;
	}
	
	//Constructor
	public RecordManagerCORBAImpl(HashMap<Character, List<Record>> recordsMap, String cityAbbreviation, Logger logger)
	{
		super();
		this.recordsMap = recordsMap;
		this.cityAbbreviation = cityAbbreviation;
		this.otherServersUDPPorts = Infrastucture.getOtherServersUDPPorts(cityAbbreviation);
		this.logger = logger;
		
		logger.logToFile(cityAbbreviation + "[RecordManagerImpl Constructor]: An instance of RecordManagerImpl is created");
	}

	@Override
	public boolean createTRecord(String firstName, 
			String lastName, 
			String address, 
			String phoneNumber,
			String specialization, // the sign ',' is the separator
			String location, 
			String callerId)
	{
		if ((firstName == null) ||
                (lastName == null) ||
                (address == null) ||
                (phoneNumber == null) ||
                (specialization == null) ||
                (location == null)) 
        {
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.createTRecord()]: createTRecord failed (at least one property was NULL)" +
                    " {CallerManagerID: " + callerId + "}");
            return false;
        }
		
		Integer phone = Integer.parseInt(phoneNumber);
		List<String> spec = new ArrayList<>();
		String[] parts = specialization.split(",");
		for (int i = 0; i < parts.length; i ++)
			spec.add(parts[i]);
        String id = produceNewId("TR", callerId);
        if (id != null) 
        {
            TeacherRecord teacher = new TeacherRecord(id, firstName, lastName, address, phone, spec, location);

            synchronized (recordsMap) 
            {
            	synchronized (indexPerId) 
                {
            		recordsMap.get(teacher.getLastName().toUpperCase().charAt(0)).add(teacher);
                    indexPerId.put(id, teacher);
                }                
            }            

            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.createTRecord()]: createTRecord is successfully done (ID: " + id + ")" +
                    " {CallerManagerID: " + callerId + "}");
            return true;
        }
		return false;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean createSRecord(String firstName, 
			String lastName, 
			String coursesRegistred, 
			boolean status,
			String statusDate, 
			String callerId)
	{
		if ((firstName == null) ||
                (lastName == null) ||
                (coursesRegistred == null) ||
                (statusDate == null)) 
		{
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.createSRecord()]: createSRecord failed (at least one property was NULL)" +
                    " {CallerManagerID: " + callerId + "}");
            return false;
        }

		Date date = new Date();
		date.parse(statusDate);
		List<String> courses = new ArrayList<>();
		String[] parts = coursesRegistred.split(",");
		for (int i = 0; i < parts.length; i ++)
			courses.add(parts[i]);
        String id = produceNewId("SR", callerId);
        if (id != null) 
        {
            StudentRecord student = new StudentRecord(id, firstName, lastName, courses, status, date);

            synchronized (recordsMap) 
            {
            	synchronized (indexPerId) 
                {
                    indexPerId.put(id, student);
                    recordsMap.get(student.getLastName().toUpperCase().charAt(0)).add(student);
                }                
            }
            

            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.createSRecord()]: createSRecord is successfully done (ID: " + id + ")" +
                    " {CallerManagerID: " + callerId + "}");
            return true;
        }
		return false;
	}

	@Override
	public String getRecordCounts(String callerId)
	{
		int count = 0;
        synchronized (recordsMap) 
        {
        	synchronized (indexPerId)
			{
	            for (Character ch = 'A'; ch <= 'Z'; ch++) 
	            {
	                count += recordsMap.get(ch).size();
	            }
			}
        }
		
        String result = cityAbbreviation + " " + count;

        for (Integer udpPort: otherServersUDPPorts) 
        {
            UDPClient client = new UDPClient(udpPort);
            String tempStr = client.requestCount().trim();

            if (tempStr == null) 
            {
                logger.logToFile(cityAbbreviation + "[RecordManagerImpl.getRecordCounts()]: UDP server did not respond on port:" +
                        udpPort + " {CallerManagerID: " + callerId + "}");
            } 
            else 
            {
                result = result + ", " + tempStr;
            }
        }

        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.getRecordCounts()]: getRecordCounts is successfully done" +
                " {CallerManagerID: " + callerId + "}" + result);
        return result;		
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public boolean editRecord(String recordID, String fieldName, String newValue, String callerId)
	{
		if ((recordID == null) || (fieldName == null) || (newValue == null)) 
		{
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
                    + " {CallerManagerID: " + callerId + "}");
            return false;
        }

        if (!(isIdFormatCorrect(recordID))) 
        {
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (recordId format is incorrect)"
                    + " {CallerManagerID: " + callerId + "}");
            return false;
        }

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
		                logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)" +
		                        " {CallerManagerID: " + callerId + "}");
		                return false;
		            }
            
				    switch (fieldName) 
	                {
	                    case "address":
	                        teacher.setAddress(newValue);
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: address" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    case "phoneNumber":
	                        teacher.setPhoneNumber(Integer.parseInt(newValue));
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: phoneNumber" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    case "location":
	                        teacher.setLocation(newValue);
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: phoneNumber" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    default:
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)" +
	                                " {CallerManagerID: " + callerId + "}");
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
		                logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (Given ID doesn't exist)" +
		                        " {CallerManagerID: " + callerId + "}");
		                return false;
		            }
            		            
	                switch (fieldName) 
	                {
	                    case "coursesRegistred":
	                    	List<String> courses = new ArrayList<>();
	                		String[] parts = newValue.split(",");
	                		for (int i = 0; i < parts.length; i ++)
	                			courses.add(parts[i]);
	                        student.setCoursesRegistred(courses);
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: coursesRegistred" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    case "status":
	                    	boolean status;
	                    	if (newValue.equals("true"))
	                    		status = true;
	                    	else
	                    		status = false;
	                        student.setStatus(status);
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: status" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    case "statusDate":
	                    	Date date = new Date();
	                		date.parse(newValue);
	                        student.setStatusDate(date);
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord is successfully done for: statusDate" +
	                                " {CallerManagerID: " + callerId + "}");
	                        break;
	                    default:
	                        logger.logToFile(cityAbbreviation + "[RecordManagerImpl.editRecord()]: editRecord failed (Given fieldName is invalid)" +
	                                " {CallerManagerID: " + callerId + "}");
	                        return false;
	                }		            
        		}
        	}
        }

        return true;
	}

	@Override
	public boolean recordExist(String recordId, String callerId)
	{
		if (!(isIdFormatCorrect(recordId))) 
        {
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.recordExist()]: editRecord failed (recordId format is incorrect)"
                    + " {CallerManagerID: " + callerId + "}");
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
	public boolean transferRecord(String managerId, String recordId, String remoteCenterServerName)
	{
		if (!(isIdFormatCorrect(recordId))) 
        {
            logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Error! recordId format is incorrect"
                    + " {CallerManagerID: " + managerId + "}");
            return false;
        }
		
		if (!((remoteCenterServerName.toUpperCase().equals("MTL") || 
				remoteCenterServerName.toUpperCase().equals("LVL") ||
				remoteCenterServerName.toUpperCase().equals("DDO"))))
		{
			logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Error! remoteCenterServerName is invalid"
                    + " {CallerManagerID: " + managerId + "}");
			return false; //Given city name is incorrect
		}
		
		RecordManagerCORBA recMng;
		String city = remoteCenterServerName.toUpperCase().trim();		
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
		
		try
		{
			// create and initialize the ORB
			ORB orb= ORB.init(configuration, null);
			
			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExtinstead of NamingContext. This is part of the Interoperable naming Service.
			NamingContextExt ncRef= NamingContextExtHelper.narrow(objRef);
			
			// resolve the Object Reference in Naming
			String name1 = "RecordManagerCORBA_" + city;
			recMng= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name1));
			
			if (recordId.toUpperCase().trim().charAt(0) == 'T') // Teacher record
			{		
				TeacherRecord teacher;
				synchronized (recordsMap) 
	            {
					synchronized (indexPerId)
					{					
						logger.logToFile(indexPerId.size() + "");
						teacher = (TeacherRecord) indexPerId.get(recordId.toUpperCase().trim()); // Retrieve the record
						if (teacher == null)
						{
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
				                    + " {CallerManagerID: " + managerId + "}");
							return false; //The given record dosen't exist in this server
						}
						if (recordsMap.get(teacher.getLastName().toUpperCase().charAt(0)).remove(teacher)) // Delete the record from the Map
						{
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Record removed from HashMap on this server"
				                    + " {CallerManagerID: " + managerId + "}");
						}
						if (indexPerId.remove(recordId.toUpperCase().trim(), teacher)) // Delete the record from the Index		
						{
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Record removed from Index on this server"
				                    + " {CallerManagerID: " + managerId + "}");
						}
						logger.logToFile(indexPerId.size() + "");
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
				// By calling createTRecord on the remote server, new record ID will be assigned to the record
				if (!(recMng.createTRecord(teacher.getFirstName(), 
						teacher.getLastName(), 
						teacher.getAddress(), 
						teacher.getPhone().toString(), 
						spec, 
						city, 
						city + "0001")))
				{
					logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Failed! The given record is not added to thenew server"
		                    + " {CallerManagerID: " + managerId + "}");
					return false; //The given record dosen't exist in this server
				}
				
				//recMng.shutdown();						
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
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Failed! The given record dosen't exist in this server"
				                    + " {CallerManagerID: " + managerId + "}");
							return false; //The given record dosen't exist in this server
						}
						if (recordsMap.get(student.getLastName().toUpperCase().charAt(0)).remove(student)) // Delete the record from the Map
						{
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Record removed from HashMap on this server"
				                    + " {CallerManagerID: " + managerId + "}");
						}
						if (indexPerId.remove(recordId.toUpperCase().trim(), student)) // Delete the record from the Index		
						{
							logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Record removed from Index on this server"
				                    + " {CallerManagerID: " + managerId + "}");
						}						
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
				if (!(recMng.createSRecord(student.getFirstName(), 
						student.getLastName(), 
						courses, 
						student.getStatus(), 
						student.getDate().toString(), 
						city + "0001")))
				{
					logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: Failed! The given record is not added to thenew server"
		                    + " {CallerManagerID: " + managerId + "}");
					return false; //The given record dosen't exist in this server
				}
				
				//recMng.shutdown();	
			}						
			
		} catch (InvalidName e)
		{
			System.err.println("Error! InvalidName");
			//e.printStackTrace();
		} catch (NotFound e)
		{
			System.err.println("Error! NotFound");
			//e.printStackTrace();
		} catch (CannotProceed e)
		{
			System.err.println("Error! CannotProceed");
			//e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			System.err.println("Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
			//e.printStackTrace();
		}
		
		logger.logToFile(cityAbbreviation + "[RecordManagerImpl.transferRecord()]: The given record is transfered successfully"
                + " {CallerManagerID: " + managerId + "}");
		
		return true;
	}

	@Override
	public void shutdown()
	{
		orb.shutdown(false);
	}
	
	private String produceNewId(String prefix, String callerId) {
        if (prefix.toUpperCase().equals("TR")) {
            if (lastTeacherId >= 99999) //ID can have 5 digits only not more
            {
                logger.logToFile(cityAbbreviation + "[RecordManagerClass.produceNewId()]: produceNewId failed (Teachers record number reached 99999)"
                        + " {CallerManagerID: " + callerId + "}");
                return null;
            }

            // Need to be locked & Synchronized
            String tId = null;
            synchronized (lastTeacherId) {
                lastTeacherId++;
                tId = lastTeacherId.toString();
            }

            int zeroNum = 5 - tId.length();
            for (int i = 1; i <= zeroNum; i++) {
                tId = "0" + tId;
            }
            tId = "TR" + tId;

            return tId;
        }

        if (prefix.toUpperCase().equals("SR")) {
            if (lastStudentId >= 99999) //ID can have 5 digits only not more
            {
                logger.logToFile(cityAbbreviation + "[RecordManagerClass.produceNewId()]: produceNewId failed (Students record number reached 99999)"
                        + " {CallerManagerID: " + callerId + "}");
                return null;
            }

            // Need to be locked & Synchronized
            String sId = null;
            synchronized (lastStudentId) {
                lastStudentId++;
                sId = lastStudentId.toString();
            }

            int zeroNum = 5 - sId.length();
            for (int i = 1; i <= zeroNum; i++) {
                sId = "0" + sId;
            }
            sId = "SR" + sId;

            return sId;
        }

        return null;
    }

    private boolean isIdFormatCorrect(String id) {
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
	
}
