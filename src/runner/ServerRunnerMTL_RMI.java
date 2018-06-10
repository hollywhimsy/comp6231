package runner;

import rmi.CenterServerRMI;

public class ServerRunnerMTL_RMI {

	public static void main(String[] args) {
        CenterServerRMI srv = new CenterServerRMI("MTL");
        srv.start();
	}

}
