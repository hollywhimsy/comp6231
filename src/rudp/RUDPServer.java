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

	// Constructor
	public RUDPServer(HashMap<Character, List<Record>> recordsMap, HashMap<String, Record> indexPerId, int listenPort, String cityAbbr, 
			Logger logger)
	{
		super();
		this.recordsMap = recordsMap;
		this.indexPerId = indexPerId;
		this.listenPort = listenPort;
		this.cityAbbr = cityAbbr;
		this.logger = logger;		

		logger.logToFile(cityAbbr + "[UDPServer Constructor]: UDPServer is created on port: " + listenPort);
	}

	public void run()
	{
		DatagramSocket socket = null; // Socket declaration
		try
		{
			socket = new DatagramSocket(listenPort); // Socket initiation by given UDP port number
			logger.logToFile(cityAbbr + "[UDPServer.run()]: Listening on " + listenPort + " UDP Port");

			byte[] buffer = new byte[1000]; // Buffer which receives the request

			while (true) // Always receive the requests and response accordingly
			{
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				logger.logToFile(cityAbbr + "[UDPServer.run()]: UDP CenterServer Recieved a Request!");

				String req = new String(request.getData()); // Extract the request data
				String[] parts = req.split("^");
				if (parts.length != 2)
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[UDPServer.run()]: Request is corrupted! NAK sent to the requester");
					continue;
				}
				if (!parts[1].equals(generateChecksum(parts[0])))
				{
					socket.send(prepareRespons("NAK", request)); // Send the reply
					logger.logToFile(cityAbbr + "[UDPServer.run()]: Request is corrupted! NAK sent to the requester");
					//corrupted request
					continue;
				}				

				CenterImpl centerImpl = new CenterImpl(recordsMap, indexPerId, cityAbbr, logger);
					
				socket.send(prepareRespons(centerImpl.processRequest(req), request)); // Send the reply
					
				logger.logToFile(cityAbbr + "[UDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
						+ request.getPort() + " @@ I'm alive! @@");				
			}
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbr + "[UDPServer.run()]: UDP Socket Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbr + "[UDPServer.run()]: UDP IO Exception Error!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbr + "[UDPServer.run()]: UDP Socket is Closed!");
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
		String str = rep + "^" + generateChecksum(rep);
		byte[] replyBuffer = new byte[512];
		replyBuffer = str.getBytes(); // Convert String to Byte to send to the client
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, request.getAddress(), request.getPort());
		
		return reply;
	}
}
