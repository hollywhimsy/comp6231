package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient 
{
	private int serverPort; // CenterServer listen port that client should connect to
	
	// Constructor
	public UDPClient(int serverPort) 
	{
		super();
		this.serverPort = serverPort;
	}
	
	// Method to request record numbers from a server
	// Returns the result with the assignment format (e.g. "MTL 7")
	public String requestCount() 
	{
		DatagramSocket socket = null;
		
		try 
		{
			socket = new DatagramSocket();
			
			byte[] message = "Count".getBytes(); // client must send "Count" as request
			
			InetAddress serverIP = InetAddress.getByName("localhost"); // CenterServer and client have same IP address
			DatagramPacket request = new DatagramPacket(message, message.length, serverIP, serverPort);
			socket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			
			String rep = new String(reply.getData());
//			System.out.println(rep); // Show the reply
			
			return rep;
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			if(socket != null)
				socket.close();
		}
		return null;		
	}
}
