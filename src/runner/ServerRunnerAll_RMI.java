package runner;

import common.Constants;
import server.CenterServer;

public class ServerRunnerAll_RMI {

    public static void main(String[] args) {
        CenterServer srvMTL = new CenterServer(Constants.Locations.MTL.name());
        srvMTL.start();

        CenterServer srvLVL = new CenterServer(Constants.Locations.LVL.name());
        srvLVL.start();

        CenterServer srvDDO = new CenterServer(Constants.Locations.DDO.name());
        srvDDO.start();
    }

}
