package runner;

import server.CenterServer;

public class ServerRunnerMTL_RMI {

	public static void main(String[] args) {
        CenterServer srv = new CenterServer("MTL");
        srv.start();
	}

}
