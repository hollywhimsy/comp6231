package src;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server extends Thread{
	private String city;
	private int port;
	
	public Server(String ville, int porte){
		city = ville;
		port = porte;
	}
	
	public static void run(){
		
	}

	public static void main(String[] args) throws Exception{
		
		AddClass obj = new AddClass(); 
		Registry registry = LocateRegistry.createRegistry(2964); 
		registry.bind("Addition", obj);
		System.out.println("Server is started");
	}
}
