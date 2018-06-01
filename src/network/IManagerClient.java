package network;

import common.Record;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface IManagerClient extends Remote {

    boolean createTRecord(String firstName,
                          String lastName,
                          String address,
                          Integer phoneNumber,
                          List<String> specialization) throws RemoteException;

    boolean createSRecord(String firstName,
                          String lastName,
                          List<String> coursesRegistred,
                          boolean status,
                          Date statusDate) throws RemoteException;

    String getRecordCounts() throws RemoteException;

    Integer getLocalRecordsCount() throws RemoteException;

    Boolean editRecord(String recordID,
                       String fieldName,
                       Object newValue) throws RemoteException;

    Record returnRecord(String recordId) throws RemoteException;
}
