package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import common.Logger;

public class UDPClient
{
	private int serverPort; // CenterServer listen port that client should connect to
	private Logger logger;
	
	// Constructor
	public UDPClient(int serverPort, Logger logger) 
	{
		super();
		this.serverPort = serverPort;
		this.logger = logger;
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
			logger.logToFile("[UDPClient.run()]: Request is sent to remote server");
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			logger.logToFile("[UDPClient.run()]: Reply recieved from the remote server");
			
			String rep = new String(reply.getData());
			
			return rep;
		} 
		catch (UnknownHostException e) 
		{
			logger.logToFile("[UDPClient.run()]: UnknownHost Exception Error!");
		}		 
		catch (SocketException e) 
		{
			logger.logToFile("[UDPClient.run()]: UDP Socket Exception Error!");
		} 
		catch (IOException e) 
		{
			logger.logToFile("[UDPClient.run()]: UDP IO Exception Error!");
		}
		finally 
		{
			if(socket != null)
				socket.close();
		}
		return null;		
	}
}
