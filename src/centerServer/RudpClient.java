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
	private String cityAbbr = new String();
	private int timeout = 2000; // (Millisecond) to wait for the reply, if don't receive, means server is down

	// Constructor
	public RudpClient(int serverPort, String cityAbbr, Logger logger)
	{
		super();
		this.serverPort = serverPort;
		this.logger = logger;
		this.cityAbbr = cityAbbr;
	}

	public String requestRemote(String request)
	{
		if (request.trim().length() > 983)
		{
			return "LNG";
		}

		String id = idGenerator();

		String response = sender("REQ", id, request); // Send the request and receive the response

		if (response.substring(0, 3).equals("ACK")) // if ACK received
			sender("DEL", id, ""); // Ask the receiver to delete the response

		return response;
	}

	private String sender(String code, String id, String request)
	{
		DatagramSocket socket = null;
		String respons = "";

		try
		{
			socket = new DatagramSocket();

			String req = generateChecksum(code + id + request.trim()) + code + id + request.trim(); // Assemble the request parts
			byte[] message = req.getBytes();
			InetAddress serverIP = null;
			serverIP = InetAddress.getByName("localhost"); // CenterServer and client have same IP address
			DatagramPacket udpRequest = new DatagramPacket(message, message.length, serverIP, serverPort);

			String[] parts = new String[4];

			boolean isDelivered = false; // if ACk receives, isDelivered = true

			while (!isDelivered) // Retry to send the request until receive ACK in reply
			{
				socket.send(udpRequest);
				byte[] buffer = new byte[1024];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

				socket.setSoTimeout(timeout); // makes the "receive" non-blocking and wait only for "timeout" milliseconds

				try
				{
					socket.receive(reply);
				} catch (IOException e)
				{
					return "DWN"; // No response from the server after "timeout" elapsed
				}

				String rep = new String(reply.getData());

				if (rep.trim().length() >= 41) // header length is correct
				{
					parts = splitMessage(rep.trim());

					if (parts[0].equals(generateChecksum(parts[1] + parts[2] + parts[3]))) // The response is not corrupted
					{
						if (!parts[1].equals("NAK")) // The server did not receive a corrupt request
						{
							isDelivered = true;
						}
					}
				}
			}

			respons = parts[1] + parts[3]; // Concatenate the Code and the response Body
		} catch (UnknownHostException e)
		{
			logger.logToFile(cityAbbr + "[RUDPClient]: UnknownHostException Error!");
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbr + "[RUDPClient]: Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbr + "[RUDPClient]: IOException Error!");
		} finally
		{
			if (socket != null)
				socket.close();
		}
		return respons;
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
			e.printStackTrace();
		}

		return sb.toString();
	}

	// generate unique ID for the message
	private String idGenerator()
	{
		String result = "";
		for (int i = 0; i < 6; i++)
		{
			Random rand = new Random();
			int n = rand.nextInt(10);
			result += n;
		}

		return result;
	}

	// Split parts of the response
	private String[] splitMessage(String message)
	{
		String[] result = new String[4];

		result[0] = message.substring(0, 32);
		result[1] = message.substring(32, 35);
		result[2] = message.substring(35, 41);
		result[3] = message.substring(41, message.length());

		return result;
	}
}
