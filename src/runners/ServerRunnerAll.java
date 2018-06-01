package runners;

import network.CenterServer;

public class ServerRunnerAll {

    public static void main(String[] args) {
        CenterServer srvMTL = new CenterServer("MTL");
        srvMTL.start();

        CenterServer srvLVL = new CenterServer("LVL");
        srvLVL.start();

        CenterServer srvDDO = new CenterServer("DDO");
        srvDDO.start();
    }

}
