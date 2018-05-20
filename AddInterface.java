import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface AddInterface extends Remote {
	
    void createTRecord(String first, String last, String address, String phone, String specialization, String location)throws RemoteException;
	
	void createSRecord(String first, String last, String courseRegistered, String status, String statusDate)throws RemoteException;
	
	void getRecordCounts() throws RemoteException;
	
	void editRecord(String recordID, String fieldName, String newValue) throws RemoteException ;
	
	public void increment() throws RemoteException;
	
	public void setPorts(int s1, int s2) throws RemoteException;
	
	public String getCounts() throws RemoteException;
	
	public int getCount() throws RemoteException;
        
	
}