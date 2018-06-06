package runners;

import network.CenterServer;

public class ServerRunnerMTL {

	public static void main(String[] args) {
        CenterServer srv = new CenterServer("MTL");
        srv.start();
	}

}
