package runner;

import rmi.CenterServerRMI;

public class ServerRunnerLVL_RMI {

	public static void main(String[] args) {
		CenterServerRMI srv = new CenterServerRMI("LVL");
		srv.start();
	}

}
