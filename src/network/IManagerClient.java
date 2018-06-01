package network;

import common.Record;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface IManagerClient extends Remote {

	boolean callCreateTRecord() throws RemoteException;

	boolean callCreateSRecord() throws RemoteException;

    String callGetRecordCounts() throws RemoteException;

    Integer getLocalRecordsCount() throws RemoteException;

    boolean callEditRecord() throws RemoteException;

    Record callReturnRecord(String recordId) throws RemoteException;
}
