package centerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import common.Logger;

public class RequestManager extends Thread
{
	private DatagramPacket request;
	private DatagramSocket socket;
	private HashMap<String, String[]> responses;
	private Operations operations;

	public RequestManager(DatagramPacket request, DatagramSocket socket, String cityAbbr, Logger logger, HashMap<String, String[]> responses,
			Operations operations)
	{
		super();
		this.request = request;
		this.socket = socket;
		this.responses = responses;
		this.operations = operations;
	}

	public void run()
	{
		String req = new String(request.getData()); // Extract the request data

		try
		{
			if (req.trim().length() < 41) // Request is corrupted
			{
				socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort()));				
				return;
			}
			
			String[] parts = splitMessage(req.trim());
			
			if (!parts[0].equals(generateChecksum(parts[1] + parts[2] + parts[3]))) // Request is corrupted
			{
				socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort())); 		
				return;
			}

			if (parts[1].equals("REQ")) // Code = "REQ"
			{
				if (!responses.containsKey(parts[2])) // if it's the first time to process this request
				{
					String[] result = operations.processRequest(parts[3].trim()); // Call operations to process the request and return the result
					socket.send(prepareRespons(result[0], parts[2], result[1], request.getAddress(), request.getPort())); 
					
					responses.put(parts[2], result); // Save the result for the future if retransmission is required
				} else // The request was processed before, we need to retransmit the previous result
				{
					socket.send(prepareRespons(responses.get(parts[2])[0], parts[2], responses.get(parts[2])[1], request.getAddress(), 
							request.getPort()));
				}
			}

			if (parts[1].equals("DEL")) // Code = "DEL" -> client ACK, when the result is received, so we can delete the result from "responses"
			{
				if (responses.containsKey(parts[2])) // if there is an entry with the DEL request key
				{
					responses.remove(parts[2]); // Remove the entry
				}

				socket.send(prepareRespons("ACK", parts[2], "", request.getAddress(), request.getPort())); // Send ACK
			}
		} catch (IOException e)
		{
			// e.printStackTrace();
		}
	}

	// Assemble parts of the response to a UDP message
	private DatagramPacket prepareRespons(String code, String id, String msg, InetAddress addr, int port)
	{
		String rep = generateChecksum(code + id + msg) + code + id + msg;
		byte[] replyBuffer = new byte[1024];
		replyBuffer = rep.getBytes(); // Convert String to Byte to send to the client
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, addr, port);

		return reply;
	}

	// Split parts of the request 
	private String[] splitMessage(String message)
	{
		String[] result = new String[4];
		result[0] = message.substring(0, 32);
		result[1] = message.substring(32, 35);
		result[2] = message.substring(35, 41);
		result[3] = message.substring(41, message.length());

		return result;
	}

	// Generate Checksum for a given message
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
			// e.printStackTrace();
		}

		return sb.toString();
	}
}
