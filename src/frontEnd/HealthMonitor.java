package frontEnd;

import centerServer.RudpClient;

// This class will call each runnning server 
// to check if they are alive and make the appropriate decision 
// in electing the new leader if the current crashes crashes
public class HealthMonitor extends Thread {

	RecordManagerImpl frontEndInstance;

	public HealthMonitor(RecordManagerImpl frontEndInstance) {
		this.frontEndInstance = frontEndInstance;

	}

	public void run() {
		try {
			checkHealthStatus();
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// checks the health of all servers in the distributed system
	private void checkHealthStatus() {

	}
}
