package src;
import src.AddInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;

public class Client extends Thread {
	private Registry registry = null;
	private String city;
	private AddInterface object = null;
	
	public Client(String ville){
		city = ville;
	}
	
	public static void run(){
		try{
			if(city == "MTL"){
				registry = LocateRegistry.getRegistry(2765);
			}
			if(city == "LVL"){
				registry = LocateRegistry.getRegistry(2865);
			}
			if(city == "DDO"){
				registry = LocateRegistry.getRegistry(2965);
			}
			
			AddInterface obj = (AddInterface) registry.lookup(city);
			obj.createSRecord("Dan", "Soen", "Discrete Math", "Inactive", "2016-12-18");
			obj.createSRecord("Charles", "Lemon", "Math", "Inactive", "2015-12-18");
			obj.createTRecord("Maxim", "Beautin", "Blvd Saint-Andre", "514-333-4493", "Finance", "LVL");
			object = obj;
			object.getCounts();
		}catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
 public static void main(String[] args) throws Exception{
	 
	 Registry registry = LocateRegistry.getRegistry(2964); 
	 
	 AddInterface obj = (AddInterface) registry.lookup("Addition");
	 
	 int n = obj.add(5, 4); 
	 System.out.println("Addition is : " + n);
	 
	 run();
 }
}
