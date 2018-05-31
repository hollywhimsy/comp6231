package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;

import common.Record;

// UDP Server needs to be run as a separate thread
// because it's always running in the background
public class UDPServer extends Thread
{
	private int listenPort; //UDP Port number to listen on that
	private String centerName = new String(); //Center Acronym like "MTL"
	private HashMap<Character, List<Record>> records; //The map which contains the records
	
	//Constructor
	public UDPServer(HashMap<Character, List<Record>> records, int port, String centerName) 
	{
		super();
		this.records = records;
		this.listenPort = port;
		this.centerName = centerName;
	}

	//Thread method to be called by "start" method as a separate thread
	public void run()
	{
		DatagramSocket socket = null; //Socket declaration
		try 
		{
			socket = new DatagramSocket(listenPort); //Socket initiation by given UDP port number
			
			byte[] buffer = new byte[1000]; //Buffer which receives the request 
			
			while(true) //Always receive the requests and response accordingly
			{
				// "request" will contain request data, client's IP address, and client's port number
				// which we need to respond back
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request); //Receive request
								
				String req = new String(request.getData()); // Extract the request data
				
				// check if the client send a correct request or not?
				// Client is supposed to send "Count" as request
				if(req.trim().toLowerCase().equals("Count".toLowerCase()))
				{
					int count = 0; // The number of records will be accumulated in "count"
					
					// Lets go through the HashMap
					for(Character ch = 'A'; ch <= 'Z'; ch ++)
					{
						if(records.get(ch) != null) // If corresponding list is not empty
							count += records.get(ch).size(); // Add number of records of this list to count
					}
					
					String tmp = centerName + " " + count; // Format the response in a temporary variable
					byte[] replyBuffer = new byte[1000];
					replyBuffer = tmp.getBytes(); // Convert String to Byte to send to the client
					
					DatagramPacket reply = new DatagramPacket(replyBuffer, replyBuffer.length, 
					request.getAddress(), request.getPort()); // Create reply construct 
					
					socket.send(reply); // Send the reply
				}
				else
				{
					//System.out.println("Wrong Request!");
				}
			}
		} 
		catch (SocketException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			if(socket != null)
				socket.close();
		}		
	}
}
