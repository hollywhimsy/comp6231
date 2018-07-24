package frontEnd;

import centerServer.RudpClient;

// this class implements a non blocking socket listening 
// for heart beats from the clients and decide and make the appropriate decision 
// in electing the new leader if the current crashes crashes
public class HealthMonitor extends Thread {

	RecordManagerImpl frontEndInstance;

	public HealthMonitor(RecordManagerImpl frontEndInstance) {
		this.frontEndInstance = frontEndInstance;

	}

	public void run() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
