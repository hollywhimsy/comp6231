package runners;

import network.CenterServer;

public class ServerRunnerDDO {

	public static void main(String[] args) {
		CenterServer srv = new CenterServer("DDO");
		srv.start();
	}

}
