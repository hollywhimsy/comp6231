package runner;

import server.CenterServer;

public class ServerRunnerLVL_RMI {

	public static void main(String[] args) {
		CenterServer srv = new CenterServer("LVL");
		srv.start();
	}

}
