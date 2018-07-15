package rudp;

import common.Logger;
import record.Record;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import centerImpl.CenterImpl;

public class RUDPServer extends Thread
{
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbr = new String(); // City abbreviation like "MTL"
	private HashMap<Character, List<Record>> recordsMap; // The map which contains the records
	private HashMap<String, Record> indexPerId;
	private Logger logger;
	private List<Integer> othersPort;

	// Constructor
	public RUDPServer(HashMap<Character, List<Record>> recordsMap, HashMap<String, Record> indexPerId, int listenPort, String cityAbbr, 
			Logger logger, List<Integer> othersPort)
	{
		super();
		this.recordsMap = recordsMap;
		this.indexPerId = indexPerId;
		this.listenPort = listenPort;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.othersPort = othersPort;

		logger.logToFile(cityAbbr + "[RUDPServer Constructor]: UDPServer is created on port: " + listenPort);
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
				byte[] buffer = new byte[1000]; // Buffer which receives the request
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				String req = new String(request.getData()); // Extract the request data
				logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Recieved a Request!");
				String[] parts = req.trim().split("#");
//				logger.logToFile("len:" + parts.length);
				logger.logToFile(req.trim());
//				logger.logToFile(parts[1].trim());
				if (parts.length != 2)
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (length)! NAK sent to the requester");
					continue;
				}
				if (!parts[1].trim().equals(generateChecksum(parts[0])))
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (checksum)! NAK sent to the requester");
					//corrupted request
					continue;
				}				

				CenterImpl centerImpl = new CenterImpl(recordsMap, indexPerId, cityAbbr, logger, othersPort);
				String result = centerImpl.processRequest(parts[0].trim());
				logger.logToFile(result);
				socket.send(prepareRespons(result, request)); // Send the reply
				
				logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
						+ request.getPort());				
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
	
	private DatagramPacket prepareRespons(String rep, DatagramPacket request)
	{
		String str = rep + "#" + generateChecksum(rep);
		byte[] replyBuffer = new byte[512];
		replyBuffer = str.getBytes(); // Convert String to Byte to send to the client
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, request.getAddress(), request.getPort());
		
		return reply;
	}
}
