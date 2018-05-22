package src;


import src.common.SystemInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddClass extends UnicastRemoteObject implements SystemInterface{

	public AddClass() throws Exception{
		super();
	}

	public int add(int x, int y) throws RemoteException {
	
		return x + y;
		
	}

	@Override
	public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) throws RemoteException {

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
}

