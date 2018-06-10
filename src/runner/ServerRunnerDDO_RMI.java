package runner;

import rmi.CenterServerRMI;

public class ServerRunnerDDO_RMI {

	public static void main(String[] args) {
		CenterServerRMI srv = new CenterServerRMI("DDO");
		srv.start();
	}

}
