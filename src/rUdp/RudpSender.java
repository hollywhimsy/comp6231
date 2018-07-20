package rUdp;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class RudpSender
{

	public static void main(String args[]) throws Exception
	{
		final String hostName = "localhost";
		final int port = 2734;

		createAndSend(hostName, port);
	}

	public static void createAndSend(String hostName, int port)
	{
		// System.out.println("Sending the file");

		// Create the socket, set the address and create the file to be sent
		DatagramSocket socket = null;
		try
		{
			socket = new DatagramSocket();

			InetAddress address;

			address = InetAddress.getByName(hostName);

			byte[] request = "This is a simple test!".getBytes();

			// Create a flag to indicate the last message and a 16-bit sequence number
			int sequenceNumber = 0;
			boolean lastMessageFlag = false;

			// Create a flag to indicate the last acknowledged message and a 16-bit sequence
			// number
			int ackSequenceNumber = 0;
			int lastAckedSequenceNumber = 0;
			boolean lastAcknowledgedFlag = false;

			// Vector to store the sent messages
			Vector<byte[]> sentMessageList = new Vector<byte[]>();

			// Increment sequence number
			sequenceNumber += 1;

			// Create new byte array for message
			byte[] message = new byte[1024];

			// Set the first and second bytes of the message to the sequence number
			message[0] = (byte) (sequenceNumber >> 8);
			message[1] = (byte) (sequenceNumber);

			lastMessageFlag = false;
			message[2] = (byte) (0);

			for (int j = 0; j != request.length; j++)
			{
				message[j + 3] = request[j];// [i + j];
			}

			// Package the message
			DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port);

			// Add the message to the sent message list
			sentMessageList.add(message);

			// Send the message
			socket.send(sendPacket);

			// Check for acknowledgement
			boolean ackPktReceived = false;
			byte[] ack1 = new byte[1024];
			DatagramPacket ackpack1 = new DatagramPacket(ack1, ack1.length);

			socket.setSoTimeout(10);
			socket.receive(ackpack1);
			ackSequenceNumber = ((ack1[0] & 0xff) << 8) + (ack1[1] & 0xff);
			ackPktReceived = true;

			// Note any acknowledgement and move window forward
			if (ackPktReceived)
			{
				if (ackSequenceNumber >= (lastAckedSequenceNumber + 1))
				{
					byte[] tmpArray = new byte[1024];
					for (int i = 2; i < 1024; i++)
					{
						tmpArray[i - 2] = ack1[i];
					}
					String reply = new String(tmpArray);
					
					System.out.println(reply.trim());
					
					lastAckedSequenceNumber = ackSequenceNumber;
				}
			}

			// Continue to check and resend until we receive final ack
			while (!lastAcknowledgedFlag)
			{

				boolean ackRecievedCorrect = false;
				boolean ackPacketReceived = false;

				while (!ackRecievedCorrect)
				{
					// Check for an ack
					byte[] ack = new byte[2];
					DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

					socket.setSoTimeout(50);
					socket.receive(ackpack);
					ackSequenceNumber = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff);
					ackPacketReceived = true;

					// If its the last packet
					if (lastMessageFlag)
					{
						lastAcknowledgedFlag = true;
						break;
					}
					// Break if we receive acknowledgement so that we can send next packet
					if (ackPacketReceived)
					{

						if (ackSequenceNumber >= (lastAckedSequenceNumber + 1))
						{
							lastAckedSequenceNumber = ackSequenceNumber;
						}
						ackRecievedCorrect = true;
						break; // Break if there is an ack so the next packet can be sent
					} else
					{
						// Resend the packet following the last acknowledged packet and all following
						// that (cumulative acknowledgement)
						for (int j = 0; j != (sequenceNumber - lastAckedSequenceNumber); j++)
						{
							byte[] resendMessage = new byte[1024];
							resendMessage = sentMessageList.get(j + lastAckedSequenceNumber);
							DatagramPacket resendPacket = new DatagramPacket(resendMessage, resendMessage.length, address, port);
							socket.send(resendPacket);

						}
					}
				}
			}

		} catch (SocketException e2)
		{
			e2.printStackTrace();
		} catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		} finally
		{
			if (socket != null)
				socket.close();
		}
	}
}
