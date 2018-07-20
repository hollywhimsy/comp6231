package rUdp;

import java.io.*;
import java.net.*;

public class RudpListener
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("Ready to receive the file!");

		final int port = 2734;
		
		receive(port);
	}

	public static void receive(int port)
	{
		String request;
		
		// Create the socket, set the address and create the file to be sent
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket(port);
		
			InetAddress address;			
	
			// For each message we will receive
			while (true) 
			{
				// Store sequence number
				int sequenceNumber = 0;
				int lastSequenceNumber = 0;				
				
				// Create byte array for full message and another for file data without header
				byte[] message = new byte[1024];
				byte[] fileByteArray = new byte[1021];
	
				// Receive packet and retrieve message
				DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
				socket.setSoTimeout(0);
				socket.receive(receivedPacket);
				message = receivedPacket.getData();
	
				// Get port and address for sending ack
				address = receivedPacket.getAddress();
				port = receivedPacket.getPort();
	
				// Retrieve sequence number
				sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
	
				if (sequenceNumber == (lastSequenceNumber + 1))
				{
	
					// Update latest sequence number
					lastSequenceNumber = sequenceNumber;
	
					// Retrieve data from message
					for (int i = 3; i < 1024; i++)
					{
						fileByteArray[i - 3] = message[i];
					}
					request = new String(fileByteArray);
					
//					System.out.println("Request: " + request);
					
					// Send acknowledgement
					sendAck(lastSequenceNumber, socket, address, port);
				} else
				{
					// If packet has been received, send ack for that packet again
					if (sequenceNumber < (lastSequenceNumber + 1))
					{
						// Send acknowledgement for received packet
						sendAck(sequenceNumber, socket, address, port);
					} else
					{
						// Resend acknowledgement for last packet received
						sendAck(lastSequenceNumber, socket, address, port);
					}
				}
			}
		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}finally
		{
			if (socket != null)
				socket.close();			
		}
	}
	
	public static void sendAck(int lastSequenceNumber, DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		// Resend acknowledgement
		byte[] ackPacket = new byte[2];
		ackPacket[0] = (byte) (lastSequenceNumber >> 8);
		ackPacket[1] = (byte) (lastSequenceNumber);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( ackPacket );
		outputStream.write( "Here is the reply!".getBytes() );
		byte[] reply = outputStream.toByteArray( );
		
		DatagramPacket acknowledgement = new DatagramPacket(reply, reply.length, address, port);
		socket.send(acknowledgement);
	}
}
