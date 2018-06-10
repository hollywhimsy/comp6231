package runner;

import common.Constants;
import corba.CenterServerCORBA;

public class ServerRunnerAll_CORBA
{
	public static void main(String[] args)
	{
		CenterServerCORBA srvMTL = new CenterServerCORBA(Constants.Locations.MTL.name());
        srvMTL.start();

        CenterServerCORBA srvLVL = new CenterServerCORBA(Constants.Locations.LVL.name());
        srvLVL.start();

        CenterServerCORBA srvDDO = new CenterServerCORBA(Constants.Locations.DDO.name());
        srvDDO.start();
	}
}
