package centerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import common.Logger;

public class RudpClient
{
	private int serverPort; // CenterServer listen port that client should connect to
	private Logger logger;
	private String cityAbbreviation = new String();
	private int timeout = 1000; // (Millisecond) to wait for the reply, if don't receive, means packet is lost

	// Constructor
	public RudpClient(int serverPort, String cityAbbreviation, Logger logger)
	{
		super();
		this.serverPort = serverPort;
		this.logger = logger;
		this.cityAbbreviation = cityAbbreviation;
	}

	/*
	 * request can be: 
	 * HeartBit: server returns ACK to show it's alive
	 * createTRecord~[String]: the [String] gives the parameters and server creates a teacher record and returns a boolean 
	 * createSRecord~[String]: the [String] gives the parameters and server creates a student record and returns a boolean 
	 * getRecordsCount~[String]: the [String] gives the parameters and server returns the records count 
	 * editRecord~[String]: the [String] gives the parameters and server edits the record and returns a boolean
	 * recordExist~[String]: the [String] gives the parameters and server returns true/false 
	 * transferRecord~[String]: the [String] gives the parameters and server transfers the record and returns a boolean
	 */
	public String requestRemote(String request)
	{
		if (request.trim().length() > 983)
		{
			return "LNG";
		}
		
		String id = randomIdGenerator();
		
		String response = sender("REQ", id, request); // Send the request and receive the response
		
		sender("DEL", id, ""); // Ask the receiver to delete the response
		
		return response;
	}

	private String sender(String code, String id, String request)
	{
		DatagramSocket socket = null;
		String respons = null;

		try
		{
			socket = new DatagramSocket();
			
			String req = generateChecksum(code + id + request.trim()) + code + id + request.trim();

			byte[] message = req.getBytes(); // client must send "Count" as request

			InetAddress serverIP = InetAddress.getByName("localhost"); // CenterServer and client have same IP address
			DatagramPacket udpRequest = new DatagramPacket(message, message.length, serverIP, serverPort);
			
			boolean isValid = false;
			
			String[] parts = new String[4];
			int iteration = 1;
			
			// Try to send the request until receive ACK in reply
			while (!isValid)
			{
				socket.send(udpRequest);
				logger.logToFile(cityAbbreviation + "[RUDPClient]: Request " + code + " is sent! Iteration: " + iteration);		
				iteration ++;
					
				byte[] buffer = new byte[1024];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				
				// makes the "receive" non-blocking and wait only for "timeout" milliseconds
				try
				{
					socket.setSoTimeout(timeout);
				} catch (SocketException e)
				{
					// the receiver is down and didn't reply
					return "DWN"; 
				} 
				
				socket.receive(reply);
				String rep = new String(reply.getData());				
				
//				logger.logToFile("Rep: " + rep.trim());
				
				if (rep.trim().length() >= 41)
				{
					parts = splitMessage(rep.trim());
									
					if (parts[0].equals(generateChecksum(parts[1] + parts[2] + parts[3])))
					{
						if (!parts[1].equals("NAK"))
						{							
							isValid = true;
						}
					}								
				}				
			}
			
			respons = parts[1] + parts[3];
						
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Reply received! Response: " + respons);
		} 
		catch (UnknownHostException e)
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: UnknownHostException Error!");
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbreviation + "[RUDPClient]: IOException Error!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbreviation + "[RUDPClient]: Socket is closed!");
		}
		return 	respons;
	}
	
	private String generateChecksum(String str)
	{
		MessageDigest md;
		StringBuffer sb = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			sb = new StringBuffer();
			for (byte b : digest)
			{
				sb.append(String.format("%02x", b & 0xff));
			}
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	private String randomIdGenerator()
	{
		String result = "";
		for (int i = 0; i < 6; i++)
		{
			Random rand = new Random();
			int  n = rand.nextInt(10);
			result += n;
		}
		
		return result;	
	}
	
	private String[] splitMessage(String message)
	{
//		System.out.println(message);
		String[] result = new String[4];
		result[0] = message.substring(0, 32);
//		System.out.println(result[0]);
		result[1] = message.substring(32, 35);
//		System.out.println(result[1]);
		result[2] = message.substring(35, 41);
//		System.out.println(result[2]);
		result[3] = message.substring(41, message.length());
		
		return result;		
	}
}
