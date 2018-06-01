package runners;

import common.Constants;
import network.CenterServer;

public class ServerRunnerAll {

    public static void main(String[] args) {
        CenterServer srvMTL = new CenterServer(Constants.Locations.MTL.name());
        srvMTL.start();

        CenterServer srvLVL = new CenterServer(Constants.Locations.LVL.name());
        srvLVL.start();

        CenterServer srvDDO = new CenterServer(Constants.Locations.DDO.name());
        srvDDO.start();
    }

}
