package src.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SystemInterface extends Remote {

    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) throws RemoteException;

    public void createSRecord(String firstName, String lastName, String courseRegistered, String status, String statusDate) throws RemoteException;

    public Integer getRecordsCount() throws RemoteException;

    public Boolean editRecord(String recordID, String fieldName, String newValue) throws RemoteException;
}
