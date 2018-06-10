package manager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IManagerClientRMI extends Remote {

	boolean callCreateTRecord() throws RemoteException;

	boolean callCreateSRecord() throws RemoteException;

    String callGetRecordCounts() throws RemoteException;

    Integer getLocalRecordsCount() throws RemoteException;

    boolean callEditRecord() throws RemoteException;
    
//    Record callReturnRecord(String recordId) throws RemoteException;
    
    boolean callRecordExist(String recordId) throws RemoteException;
}
