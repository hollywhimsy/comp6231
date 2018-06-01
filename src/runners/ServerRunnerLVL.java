package runners;

import network.CenterServer;

public class ServerRunnerLVL {

	public static void main(String[] args) {
		CenterServer srv = new CenterServer("LVL");
		srv.start();
	}

}
