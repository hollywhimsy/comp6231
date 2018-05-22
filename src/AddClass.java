package src;

import java.util.*;


import src.common.SystemInterface;
import src.common.Record;
import src.common.TeacherRecord;
import src.common.StudentRecord;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class AddClass extends UnicastRemoteObject implements SystemInterface {

    private HashMap<Character, HashMap<String, Record>> records = new HashMap<Character, HashMap<String, Record>>();
    private HashMap<String, TeacherRecord> Teachers = new HashMap<String, TeacherRecord>();
    private HashMap<String, StudentRecord> Students = new HashMap<String, StudentRecord>();
    private ArrayList<String> IDs = new ArrayList<String>();

    public AddClass() throws Exception {
        super();
    }

    public int add(int x, int y) throws RemoteException {

        return x + y;

    }



    @Override
    public void createSRecord(String firstName, String lastName, String courseRegistered, String status, String statusDate) throws RemoteException {

    }

    @Override
    public Integer getRecordsCount() throws RemoteException {
        return null;
    }

    @Override
    public Boolean editRecord(String recordID, String fieldName, String newValue) throws RemoteException {
        return null;
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


    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) {
        synchronized (this) {
        TeacherRecord teacher = new TeacherRecord();
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
}

