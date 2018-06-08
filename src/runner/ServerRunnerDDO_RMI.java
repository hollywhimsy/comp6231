package runner;

import server.CenterServer;

public class ServerRunnerDDO_RMI {

	public static void main(String[] args) {
		CenterServer srv = new CenterServer("DDO");
		srv.start();
	}

}
