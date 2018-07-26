package centerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import common.Logger;
import frontEnd.FrontEnd;
import frontEnd.FrontEndHelper;
import frontEnd.RecordManagerImpl;
import record.Record;

public class ServersRunnerMTL1 {
	public static void main(String[] args) {
		
		HashMap<Character, List<Record>> recordsMap1 = new HashMap<>();
		HashMap<String, Record> indexPerId1 = new HashMap<>();
		List<HashMap<String, Integer>> ports = new ArrayList<>();
		Logger logger1 = new Logger("SRV_MTL_" + 1 + ".log");
		CenterServerCore rudpServer1 = new CenterServerCore(recordsMap1, indexPerId1, 3101, "MTL", logger1, ports, 0);
		rudpServer1.start();

	}
}