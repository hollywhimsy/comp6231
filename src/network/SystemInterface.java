package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

import common.OperationResult;

public interface SystemInterface extends Remote {

    public void createTRecord(String firstName, 
    		String lastName,
    		String address,
    		String phone,
    		String specialization,
    		String location) throws RemoteException;

    public void createSRecord(String firstName, 
    		String lastName, 
    		List<String> courseRegistered, 
    		String status, 
    		String statusDate) throws RemoteException;

    public Integer getRecordsCount() throws RemoteException;
    
    public Integer getLocalRecordsCount() throws RemoteException;

    public OperationResult editRecord(String recordID,
    		String fieldName,
    		String newValue) throws RemoteException;
}
