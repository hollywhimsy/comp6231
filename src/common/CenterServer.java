package common;

import java.util.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class CenterServer extends UnicastRemoteObject implements SystemInterface {

    private HashMap<Character, HashMap<String, Record>> records = new HashMap<Character, HashMap<String, Record>>();
    private HashMap<String, TeacherRecord> Teachers = new HashMap<>();
    private HashMap<String, StudentRecord> Students = new HashMap<>();
    private ArrayList<String> IDs = new ArrayList<>();
    private Logger logger ;
    private String city ;

    public CenterServer(String city) throws Exception {
        super();
        this.city = city;
        logger = new Logger("/tmp/logs", city + "_server");

    }

    public int add(int x, int y) throws RemoteException {

        return x + y;

    }

    public String createID(boolean isTeacher) {
        String id = "";
        if (isTeacher) {
            id = "TR";
            String formatted = String.format("%05d", Teachers.size());
            id = id + formatted;
            while (IDs.contains(id)) {
                id = "TR";
                id = id + String.format("%05d", Teachers.size() + 1);
            }
            IDs.add(id);

        } else {
            id = "SR";
//            String formatted = String.format("%05d", Sfiles.size());
//            id = id + formatted;
            while (IDs.contains(id)) {
                id = "SR";
//                id = id + String.format("%05d", Sfiles.size() + 1);
            }
            IDs.add(id);
        }
        return id;
    }

    @Override
    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) {
        synchronized (this) {

            System.out.println("creating Trecord");
            logger.write("calling creating Trecord");

            TeacherRecord teacher = new TeacherRecord(firstName, lastName);
//        (firstName, lastName, specialization);
//            teacher.setAddress(address);
//            teacher.setLocation(location);
//            teacher.setPhone(phone);
            String lastTemp = lastName.toUpperCase().trim();
            Character firstLetter = lastTemp.charAt(0);
            HashMap<String, Record> file = new HashMap<>();
            String TID = createID(true);
            file.put(TID, teacher);
            Teachers.put(TID, teacher);
            records.put(firstLetter, file);
        }

    }
    
    
    @Override
    public void createSRecord(String firstName, String lastName, List<String> courseRegistered, String status, String statusDate) throws RemoteException {
    	synchronized (this){
		    StudentRecord student = new StudentRecord(firstName, lastName);
			String lastTemp = lastName.toUpperCase().trim();
	        Character firstLetter = lastTemp.charAt(0);
			
//            student.addCourse(courseRegistered);
//            student.setStatus(status);
//            student.setDate(statusDate);
	        HashMap<String, Record> file = new HashMap<>();
            String SID = createID(false);
	        file.put(SID, student);         
            Students.put(SID, student);
            records.put(firstLetter, file);
	}
    }
    
    
    @Override
    public Integer getRecordsCount() throws RemoteException {
        return null;
    }

    @Override
    public Boolean editRecord(String recordID, String fieldName, String newValue) throws RemoteException {
    	    if(IDs.contains(recordID)) {
    	    	    
    	    }
    	    else {
    	    	    System.err.println("such ID does not exit");
    	    }
        return null;
    }
}
