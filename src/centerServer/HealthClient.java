package centerServer;

import java.security.NoSuchAlgorithmException;

// This class should run in a separate thread on each server center core as a child thread
// if the parent process will crash then it will be killed as well 
// the system will not get any heartbeat signals from this process 
public class HealthClient extends Thread {

	public void run() {
		try {
			RudpClient client = new RudpClient(4,"",null);
			
			while (true) {
				String result = client.requestRemote("HeartBit");

				if (result.contains("ACK"))
				{
					// Log success request
				}
				else
				{
					// log failed request
				}	
				
				Thread.sleep(3000);
				
			}
	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

}
