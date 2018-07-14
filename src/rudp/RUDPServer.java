package rudp;

import common.Logger;
import record.Record;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

public class RUDPServer extends Thread
{
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbreviation = new String(); // City abbreviation like "MTL"
	private HashMap<Character, List<Record>> records; // The map which contains the records
	private Logger logger;

	// Constructor
	public RUDPServer(HashMap<Character, List<Record>> records, int listenPort, String cityAbbreviation, Logger logger)
	{
		super();
		this.records = records;
		this.listenPort = listenPort;
		this.cityAbbreviation = cityAbbreviation;
		this.logger = logger;

		logger.logToFile(cityAbbreviation + "[UDPServer Constructor]: UDPServer is created on port: " + listenPort);
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
	public void run()
	{
		DatagramSocket socket = null; // Socket declaration
		try
		{
			socket = new DatagramSocket(listenPort); // Socket initiation by given UDP port number
			logger.logToFile(cityAbbreviation + "[UDPServer.run()]: Listening on " + listenPort + " UDP Port");

			byte[] buffer = new byte[1000]; // Buffer which receives the request

			while (true) // Always receive the requests and response accordingly
			{
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); // Receive request
				logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP CenterServer Recieved a Request!");

				String req = new String(request.getData()); // Extract the request data

				// check if the client send a correct request or not?
				boolean isRequestFormatCorrect = false;
				if (req.trim().toLowerCase().contains("HeartBit".toLowerCase()))
				{
					isRequestFormatCorrect = true;
					
					String tmp = "alive"; // create response in a temporary variable
					byte[] replyBuffer = new byte[1000];
					replyBuffer = tmp.getBytes(); // Convert String to Byte to send to the client

					DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length,
							request.getAddress(), request.getPort()); // Create reply construct

					socket.send(reply); // Send the reply
					logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
							+ request.getPort() + " @@ I'm alive! @@");
				}
				if (req.trim().toLowerCase().contains("createTRecord".toLowerCase()))
				{
					isRequestFormatCorrect = true;
				}
				if (req.trim().toLowerCase().contains("createSRecord".toLowerCase()))
				{
					isRequestFormatCorrect = true;
				}
				if (req.trim().toLowerCase().contains("editRecord".toLowerCase()))
				{
					isRequestFormatCorrect = true;
				}
				if (req.trim().toLowerCase().contains("recordExist".toLowerCase()))
				{
					isRequestFormatCorrect = true;
				}
				if (req.trim().toLowerCase().contains("transferRecord".toLowerCase()))
				{
					isRequestFormatCorrect = true;
				}
				if (req.trim().toLowerCase().contains("getRecordsCount".toLowerCase()))
				{
					isRequestFormatCorrect = true;
					
					int count = 0; // The number of records will be accumulated in "count"

					// Lets go through the HashMap
					synchronized (records)
					{
						for (Character ch = 'A'; ch <= 'Z'; ch++)
						{
							if (records.get(ch) != null) // If corresponding list is not empty
								count += records.get(ch).size(); // Add number of records of this list to count
						}
					}

					String tmp = cityAbbreviation + " " + count; // Format the response in a temporary variable
					byte[] replyBuffer = new byte[1000];
					replyBuffer = tmp.getBytes(); // Convert String to Byte to send to the client

					DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length,
							request.getAddress(), request.getPort()); // Create reply construct

					socket.send(reply); // Send the reply
					logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
							+ request.getPort());
				}
				if (!isRequestFormatCorrect)
				{
					logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP Client Request Was Invalid!");
				}
			}
		} catch (SocketException e)
		{
			logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP Socket Exception Error!");
		} catch (IOException e)
		{
			logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP IO Exception Error!");
		} finally
		{
			if (socket != null)
				socket.close();
			logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP Socket is Closed!");
		}
	}
}
