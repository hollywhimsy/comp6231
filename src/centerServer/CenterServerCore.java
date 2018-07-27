package centerServer;

import common.Logger;
import record.Record;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CenterServerCore extends Thread
{
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbr;// = new String(); // City abbreviation like "MTL"
	private HashMap<Character, List<Record>> recordsMap = new HashMap<>(); // The map which contains the records
	private HashMap<String, Record> indexPerId = new HashMap<>();
	private Logger logger;
	private List<HashMap<String, Integer>> alives = new ArrayList<>(); // 0 -> dead, 1 -> alive
	private HashMap<String, String[]> responses = new HashMap<>();
	private Operations operations;

	// Constructor
	public CenterServerCore(String cityAbbr, Logger logger, List<HashMap<String, Integer>> ports, int groupIndex)
	{
		super();
		this.listenPort = ports.get(groupIndex).get(cityAbbr);
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		
		String[] cities = {"MTL", "LVL", "DDO"};
		for (int i = 0; i < 3; i++)
		{		
			HashMap<String, Integer> aliveGroup = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{			
				aliveGroup.put(cities[j], 1);				
			}
			alives.add(aliveGroup);
		}
		
		HealthChecker healthChecker = new HealthChecker(ports, alives, logger, cityAbbr, groupIndex);
		healthChecker.start();
		
		operations = new Operations(groupIndex, cityAbbr, logger, alives, ports, recordsMap, indexPerId);

		logger.logToFile(cityAbbr + "[RUDPServer Constructor]: UDPServer is initialized");
	}

	public void run()
	{
		DatagramSocket socket = null; // Socket declaration
		try
		{
			socket = new DatagramSocket(listenPort); // Socket initiation by given UDP port number
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: Listening on " + listenPort + " UDP Port");

			while (true) // Always receive the requests and response accordingly
			{
				byte[] buffer = new byte[1024]; // Buffer which receives the request
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				RequestPr requestProcessor = new RequestPr(request, socket, cityAbbr, logger, responses, operations);
				requestProcessor.start();
//				String req = new String(request.getData()); // Extract the request data
////				logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Recieved a Request!");
//				
//				if (req.trim().length() < 41)
//				{
//					socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort())); // Send the reply
//					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (length)! NAK sent to the requester");
//					continue;
//				}
//				
//				String[] parts = splitMessage(req.trim());
//					
//				if (!parts[0].equals(generateChecksum(parts[1] + parts[2] + parts[3])))
//				{
//					socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort())); // Send the reply
//					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (chksm)! NAK sent to the requester");
//					continue;
//				}
//				
//				if(parts[1].equals("REQ"))
//				{
//					if (!responses.containsKey(parts[2])) // if it's the first time
//					{	
//						String[] result = operations.processRequest(parts[3].trim());
//						socket.send(prepareRespons(result[0], parts[2], result[1], request.getAddress(), request.getPort())); // Send the reply
//						logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
//								+ request.getPort());
//						
//						responses.put(parts[2], result);
//					}
//					else
//					{
//						socket.send(prepareRespons(responses.get(parts[2])[0], parts[2], responses.get(parts[2])[1], request.getAddress(), 
//								request.getPort())); 
//					}
//				}
//
//				if(parts[1].equals("DEL"))
//				{
//					if (responses.containsKey(parts[2]))
//					{
//						responses.remove(parts[2]);
//					}
//					
//					socket.send(prepareRespons("ACK", parts[2], "", request.getAddress(), request.getPort())); // Send the reply
//				}				
			}
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP Socket Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP IO Exception Error!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP Socket is Closed!");
		}
	}

//	private String generateChecksum(String str)
//	{
//		MessageDigest md;
//		StringBuffer sb = null;
//		try
//		{
//			md = MessageDigest.getInstance("MD5");
//			md.update(str.getBytes());
//			byte[] digest = md.digest();
//			sb = new StringBuffer();
//			for (byte b : digest)
//			{
//				sb.append(String.format("%02x", b & 0xff));
//			}
//		} catch (NoSuchAlgorithmException e)
//		{
//			e.printStackTrace();
//		}
//
//		return sb.toString();
//	}
//
//	private DatagramPacket prepareRespons(String code, String id, String msg, InetAddress addr, int port)
//	{
//		String rep = generateChecksum(code + id + msg) + code + id + msg;
//		byte[] replyBuffer = new byte[1024];
//		replyBuffer = rep.getBytes(); // Convert String to Byte to send to the client
//		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, addr, port);
//
//		return reply;
//	}
//	
//	private String[] splitMessage(String message)
//	{
//		String[] result = new String[4];
//		result[0] = message.substring(0, 32);
//		result[1] = message.substring(32, 35);
//		result[2] = message.substring(35, 41);
//		result[3] = message.substring(41, message.length());
//		
//		return result;		
//	}
}
