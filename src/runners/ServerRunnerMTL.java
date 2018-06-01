package runners;

import network.CenterServer;
import network.Infrastucture;

public class ServerRunnerMTL {

	public static void main(String[] args) {
        CenterServer srv = new CenterServer("MTL");
        srv.start();
	}

}
