package src;
import src.AddInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
 public static void main(String[] args) throws Exception{
	 
	 Registry registry = LocateRegistry.getRegistry(2964); 
	 
	 AddInterface obj = (AddInterface) registry.lookup("Addition");
	 
	 int n = obj.add(5, 4); 
	 System.out.println("Addition is : " + n);
 }
}
