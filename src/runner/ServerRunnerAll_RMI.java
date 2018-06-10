package runner;

import common.Constants;
import rmi.CenterServerRMI;

public class ServerRunnerAll_RMI {

    public static void main(String[] args) {
        CenterServerRMI srvMTL = new CenterServerRMI(Constants.Locations.MTL.name());
        srvMTL.start();

        CenterServerRMI srvLVL = new CenterServerRMI(Constants.Locations.LVL.name());
        srvLVL.start();

        CenterServerRMI srvDDO = new CenterServerRMI(Constants.Locations.DDO.name());
        srvDDO.start();
    }

}
