package udp;

import common.Logger;
import records.Record;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

// UDP CenterServer needs to be run as a separate thread
// because it's always running in the background
public class UDPServer extends Thread
{	
	private int listenPort; // UDP Port number to listen on that
	private String cityAbbreviation = new String(); // City abbreviation like "MTL"
	private HashMap<String, Record> indexPerId; // The map which contains the records
	private Logger logger;

	// Constructor
	public UDPServer(HashMap<String, Record> indexPerId, int listenPort, String cityAbbreviation, Logger logger)
	{
		super();
		this.indexPerId = indexPerId;
		this.listenPort = listenPort;
		this.cityAbbreviation = cityAbbreviation;
		this.logger = logger;

		logger.logToFile(cityAbbreviation + "[UDPServer Constructor]: UDPServer is created on port: " + listenPort);
	}

	// Thread method to be called by "start" method as a separate thread
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
				// ManagerClient is supposed to send "Count" as request
				if (req.trim().toLowerCase().equals("Count".toLowerCase()))
				{
					int count = 0; // The number of records will be accumulated in "count"

					// Lets go through the HashMap
					synchronized (indexPerId)
					{
						count += indexPerId.size(); // Add number of records to count						
					}

					String tmp = cityAbbreviation + " " + count; // Format the response in a temporary variable
					byte[] replyBuffer = new byte[1000];
					replyBuffer = tmp.getBytes(); // Convert String to Byte to send to the client

					DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length,
							request.getAddress(), request.getPort()); // Create reply construct

					socket.send(reply); // Send the reply
					logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP CenterServer Replyed To " + request.getAddress().toString() + ":"
							+ request.getPort());
				} else
				{
					logger.logToFile(cityAbbreviation + "[UDPServer.run()]: UDP ManagerClient Request Was Invalid!");
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
