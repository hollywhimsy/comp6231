package src;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server extends Thread{
	AddClass obj = null;
	DatagramSocket socket = null;
	private String city;
	private int port;
	private registry;
	private int sock
	
	public Server(String ville, int porte, int s){
		obj = new AddClass();
		city = ville;
		port = porte;
		sock = s;
	}
	public static void run(){
		try{
			registry = LocateRegistry.createRegistry(port);
			registry.bind(city, obj);
			socket = new DatagramSocket(sock);
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				String count_s = Integer.toString(obj.getCount());
				byte[] count_b = count_s.getBytes();
				DatagramPacket reply = new DatagramPacket(count_b, count_b.length, request.getAddress(), request.getPort());
				socket.send(reply);
			}
		}
	}

	public static void main(String[] args) throws Exception{
		
		AddClass obj = new AddClass(); 
		Registry registry = LocateRegistry.createRegistry(2964); 
		registry.bind("Addition", obj);
		System.out.println("Server is started");
	}
}
