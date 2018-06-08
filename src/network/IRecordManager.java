package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface IRecordManager extends Remote {

    boolean createTRecord(String firstName,
                          String lastName,
                          String address,
                          Integer phoneNumber,
                          List<String> specialization,
                          String location,
                          String callerId) throws RemoteException;

    boolean createSRecord(String firstName,
                          String lastName,
                          List<String> coursesRegistred,
                          boolean status,
                          Date statusDate,
                          String callerId) throws RemoteException;

    String getRecordCounts(String callerId) throws RemoteException;

    Integer getLocalRecordsCount(String callerId) throws RemoteException;

    Boolean editRecord(String recordID,
                       String fieldName,
                       Object newValue,
                       String callerId) throws RemoteException;
    
    boolean recordExist(String recordId) throws RemoteException;

//    Record returnRecord(String recordId, String callerId) throws RemoteException;
}
