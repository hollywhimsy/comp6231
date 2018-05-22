package src;
import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddInterface extends Remote{

	public int add(int x, int y) throws RemoteException; 
	
	public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) throws RemoteException;
	
	public void createSRecord(String firstName, String lastName, String courseRegistered, String status, String statusDate) throws RemoteException;
	
	public void getRecordCounts() throws RemoteException;
	
	public void editRecord(String recordID, String fieldName, String newValue) throws RemoteException;
	
}

