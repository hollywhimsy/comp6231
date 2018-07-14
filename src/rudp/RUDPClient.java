package rudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import common.Logger;

public class RUDPClient 
{
	private int serverPort; // CenterServer listen port that client should connect to
	private Logger logger;
	private String cityAbbreviation = new String();
	private int timeout = 500; //Millisecond
	
	// Constructor
	public RUDPClient(int serverPort, String cityAbbreviation, Logger logger) 
	{
		super();
		this.serverPort = serverPort;
		this.logger = logger;
		this.cityAbbreviation = cityAbbreviation;
	}
	
	/*
	 * request can be:
	 * 		HeartBit: server returns ACK to show it's alive
	 * 		createTRecord,[String]: the [String] gives the parameters and server creates a teacher record and returns a boolean
	 * 		createSRecord,[String]: the [String] gives the parameters and server creates a student record and returns a boolean
	 * 		getRecordsCount,[String]: the [String] gives the parameters and server returns the records count
	 * 		editRecord,[String]: the [String] gives the parameters and server edits the record and returns a boolean
	 * 		recordExist,[String]: the [String] gives the parameters and server returns true/false
	 * 		transferRecord,[String]: the [String] gives the parameters and server transfers the record and returns a boolean
	 */
	public String requestRemote(String city, String request) 
	{
		DatagramSocket socket = null;
		
		try 
		{
			socket = new DatagramSocket();
			
			byte[] message = request.getBytes(); // client must send "Count" as request
			
			InetAddress serverIP = InetAddress.getByName("localhost"); // CenterServer and client have same IP address
			DatagramPacket udpRequest = new DatagramPacket(message, message.length, serverIP, serverPort);
			socket.send(udpRequest);
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Request is sent!");
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.setSoTimeout(timeout); // makes the "receive" non-blocking
			socket.receive(reply);
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Reply received!");
			
			String rep = new String(reply.getData());
			
			return rep;
		} 
		catch (UnknownHostException e) 
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: UnknownHostException Error!");
		}		 
		catch (SocketException e) 
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Exception Error!");			
		} 
		catch (IOException e) 
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: IOException Error!");
		}
		finally 
		{
			if(socket != null)
				socket.close();
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Socket is closed!");
		}
		return null;		
	}
}
