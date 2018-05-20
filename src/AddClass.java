package src;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddClass extends UnicastRemoteObject implements AddInterface {

	public AddClass() throws Exception{
		super();
	}

	public int add(int x, int y) throws RemoteException {
	
		return x + y;
		
	}
}

