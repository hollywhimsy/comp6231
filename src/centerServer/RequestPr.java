package centerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import common.Logger;

public class RequestPr extends Thread
{
	private DatagramPacket request;
	private DatagramSocket socket;
	private String cityAbbr;
	private Logger logger;
	private HashMap<String, String[]> responses;
	private Operations operations;

	public RequestPr(DatagramPacket request, DatagramSocket socket, String cityAbbr, Logger logger, HashMap<String, String[]> responses,
			Operations operations)
	{
		super();
		this.request = request;
		this.socket = socket;
		this.cityAbbr = cityAbbr;
		this.logger = logger;
		this.responses = responses;
		this.operations = operations;
	}

	public void run()
	{
		String req = new String(request.getData()); // Extract the request data
		// logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Recieved a Request!");

		try
		{
			if (req.trim().length() < 41)
			{
				socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort()));
				logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (length)! NAK sent to the requester");
				return;
			}

			String[] parts = splitMessage(req.trim());

			if (!parts[0].equals(generateChecksum(parts[1] + parts[2] + parts[3])))
			{
				socket.send(prepareRespons("NAK", "000000", "", request.getAddress(), request.getPort())); // Send the reply
				logger.logToFile(cityAbbr + "[RUDPServer.run()]: Request is corrupted (chksm)! NAK sent to the requester");
				return;
			}

			if (parts[1].equals("REQ"))
			{
				if (!responses.containsKey(parts[2])) // if it's the first time
				{
					String[] result = operations.processRequest(parts[3].trim());
					socket.send(prepareRespons(result[0], parts[2], result[1], request.getAddress(), request.getPort())); // Send the reply
					logger.logToFile(cityAbbr + "[RUDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
							+ request.getPort());

					responses.put(parts[2], result);
				} else
				{
					socket.send(prepareRespons(responses.get(parts[2])[0], parts[2], responses.get(parts[2])[1], request.getAddress(),
							request.getPort()));
				}
			}

			if (parts[1].equals("DEL"))
			{
				if (responses.containsKey(parts[2]))
				{
					responses.remove(parts[2]);
				}

				socket.send(prepareRespons("ACK", parts[2], "", request.getAddress(), request.getPort())); // Send the reply
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Send the reply
	}

	private DatagramPacket prepareRespons(String code, String id, String msg, InetAddress addr, int port)
	{
		String rep = generateChecksum(code + id + msg) + code + id + msg;
		byte[] replyBuffer = new byte[1024];
		replyBuffer = rep.getBytes(); // Convert String to Byte to send to the client
		DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, addr, port);

		return reply;
	}

	private String[] splitMessage(String message)
	{
		String[] result = new String[4];
		result[0] = message.substring(0, 32);
		result[1] = message.substring(32, 35);
		result[2] = message.substring(35, 41);
		result[3] = message.substring(41, message.length());

		return result;
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
}
