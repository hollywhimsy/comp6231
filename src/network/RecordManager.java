package network;

import common.Record;
import common.TeacherRecord;
import common.StudentRecord;
import common.Logger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Each CenterServer Center has one object instance of this class
public class RecordManager extends UnicastRemoteObject implements IRecordManager 
{
    private static final long serialVersionUID = 1L;
    private HashMap<Character, List<Record>> recordsMap; // Needs synchronization
    private HashMap<String, Record> indexPerId = new HashMap<>(); // Needs synchronization, acts as an index to find records by ID
    private Integer lastTeacherId = 0; // Needs synchronization
    private Integer lastStudentId = 0; // Needs synchronization
    private String cityAbbreviation;
    private List<Integer> otherServersUDPPorts;
    private Logger logger;

    // Constructor
    public RecordManager(String abbr, HashMap<Character, List<Record>> recordsMap, Logger logger) throws RemoteException 
    {
        super();

        this.recordsMap = recordsMap;
        this.cityAbbreviation = abbr;
        this.otherServersUDPPorts = Infrastucture.getOtherServersUDPPorts(abbr);
        this.logger = logger;

        logger.logToFile(cityAbbreviation + "[RecordManagerClass Constructor]: An instance of RecordManagerClass is created");
    }

    @Override
    public boolean createTRecord(String firstName,
                                 String lastName,
                                 String address,
                                 Integer phoneNumber,
                                 List<String> specialization,
                                 String location,
                                 String callerId) throws RemoteException 
    {
        if ((firstName == null) ||
                (lastName == null) ||
                (address == null) ||
                (phoneNumber == null) ||
                (specialization == null) ||
                (location == null)) 
        {
            logger.logToFile(cityAbbreviation + "[RecordManagerClass.createTRecord()]: createTRecord failed (at least one property was NULL)" +
                    " {CallerManagerID: " + callerId + "}");
            return false;
        }

        String id = produceNewId("TR", callerId);
        if (id != null) {
            TeacherRecord teacher = new TeacherRecord(id,
                    firstName,
                    lastName,
                    address,
                    phoneNumber,
                    specialization,
                    location);

            // locked
            synchronized (recordsMap) {
                recordsMap.get(teacher.getLastName().toUpperCase().charAt(0)).add(teacher);
            } //Unlocked
            // Locked
            synchronized (indexPerId) {
                indexPerId.put(id, teacher);
            } // Unlocked

            logger.logToFile(cityAbbreviation + "[RecordManagerClass.createTRecord()]: createTRecord is successfully done (ID: " + id + ")" +
                    " {CallerManagerID: " + callerId + "}");
            return true;
        }

        return false;
    }

    @Override
    public boolean createSRecord(String firstName,
                                 String lastName,
                                 List<String> coursesRegistred,
                                 boolean status,
                                 Date statusDate,
                                 String callerId) throws RemoteException {
        if ((firstName == null) ||
                (lastName == null) ||
                (coursesRegistred == null) ||
                (statusDate == null)) {
            logger.logToFile(cityAbbreviation + "[RecordManagerClass.createSRecord()]: createSRecord failed (at least one property was NULL)" +
                    " {CallerManagerID: " + callerId + "}");
            return false;
        }

        String id = produceNewId("SR", callerId);
        if (id != null) {
            StudentRecord student = new StudentRecord(id,
                    firstName,
                    lastName,
                    coursesRegistred,
                    status,
                    statusDate);

            // locked
            synchronized (recordsMap) {
                recordsMap.get(student.getLastName().toUpperCase().charAt(0)).add(student);
            }
            synchronized (indexPerId) {
                indexPerId.put(id, student);
            }

            logger.logToFile(cityAbbreviation + "[RecordManagerClass.createSRecord()]: createSRecord is successfully done (ID: " + id + ")" +
                    " {CallerManagerID: " + callerId + "}");
            return true;
        }

        return false;
    }

    @Override
    public String getRecordCounts(String callerId) throws RemoteException {
        int count = 0;
        synchronized (recordsMap) {
            for (Character ch = 'A'; ch <= 'Z'; ch++) {
                count += recordsMap.get(ch).size();
            }
        }
        String result = cityAbbreviation + " " + count;

        for (Integer udpPort: otherServersUDPPorts) {
            UDPClient client = new UDPClient(udpPort);
            String tempStr = client.requestCount().trim();

            if (tempStr == null) {
                logger.logToFile(cityAbbreviation + "[RecordManagerClass.getRecordCounts()]: UDP server did not respond on port:" +
                        udpPort + " {CallerManagerID: " + callerId + "}");
            } else {
                result = result + ", " + tempStr;
            }
        }

        logger.logToFile(cityAbbreviation + "[RecordManagerClass.getRecordCounts()]: getRecordCounts is successfully done" +
                " {CallerManagerID: " + callerId + "}");
        return result;
    }

    @Override
    public Integer getLocalRecordsCount(String callerId) throws RemoteException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean editRecord(String recordId,
                              String fieldName,
                              Object newValue,
                              String callerId) throws RemoteException {
        if ((recordId == null) || (fieldName == null) || (newValue == null)) {
            logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (recordId and/or fieldName and/or newValue is(are) NULL)"
                    + " {CallerManagerID: " + callerId + "}");
            return false;
        }

        if (!(isIdFormatCorrect(recordId))) {
            logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (recordId format is incorrect)"
                    + " {CallerManagerID: " + callerId + "}");
            return false;
        }

        Character ch = recordId.toUpperCase().charAt(0);
        if (ch.equals('T')) {
            TeacherRecord teacher = (TeacherRecord) indexPerId.get(recordId);
            if (teacher == null) {
                logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (Given ID doesn't exist)" +
                        " {CallerManagerID: " + callerId + "}");
                return false;
            }

            synchronized (teacher) {
                switch (fieldName) {
                    case "address":
                        teacher.setAddress((String) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: address" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    case "phoneNumber":
                        teacher.setPhoneNumber((Integer) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: phoneNumber" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    case "location":
                        teacher.setLocation((String) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: phoneNumber" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    default:
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (Given fieldName is invalid)" +
                                " {CallerManagerID: " + callerId + "}");
                        return false;
                }
            }
        }
        if (ch.equals('S')) {
            StudentRecord student = (StudentRecord) indexPerId.get(recordId);
            if (student == null) {
                logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (Given ID doesn't exist)" +
                        " {CallerManagerID: " + callerId + "}");
                return false;
            }

            synchronized (student) {
                switch (fieldName) {
                    case "coursesRegistred":
                        student.setCoursesRegistred((List<String>) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: coursesRegistred" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    case "status":
                        student.setStatus((boolean) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: status" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    case "statusDate":
                        student.setStatusDate((Date) newValue);
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord is successfully done for: statusDate" +
                                " {CallerManagerID: " + callerId + "}");
                        break;
                    default:
                        logger.logToFile(cityAbbreviation + "[RecordManagerClass.editRecord()]: editRecord failed (Given fieldName is invalid)" +
                                " {CallerManagerID: " + callerId + "}");
                        return false;
                }
            }
        }

        return true;
    }

    @Override
	public boolean recordExist(String recordId) throws RemoteException
	{
		if (indexPerId.containsKey(recordId))
			return true;
		else
			return false;
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
