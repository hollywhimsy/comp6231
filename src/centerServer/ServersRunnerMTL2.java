package centerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
import common.ServerInfo;
import frontEnd.FrontEnd;
import frontEnd.FrontEndHelper;
import frontEnd.RecordManagerImpl;
import record.Record;

public class ServersRunnerMTL2
{
	public static void main(String[] args)
	{
		ConcurrentHashMap<String, List<ServerInfo>> servers = new ConcurrentHashMap<String, List<ServerInfo>>();

		List<ServerInfo> mtlServers = new CopyOnWriteArrayList<ServerInfo>();
		mtlServers.add(new ServerInfo("MTL", "localhost", 3101, 11, 1));
		mtlServers.add(new ServerInfo("MTL", "localhost", 3102, 22, 2));
		mtlServers.add(new ServerInfo("MTL", "localhost", 3103, 33, 3));

		List<ServerInfo> lvlServers = new CopyOnWriteArrayList<ServerInfo>();
		lvlServers.add(new ServerInfo("LVL", "localhost", 4101, 14, 1));
		lvlServers.add(new ServerInfo("LVL", "localhost", 4102, 25, 2));
		lvlServers.add(new ServerInfo("LVL", "localhost", 4103, 36, 3));

		List<ServerInfo> ddoServers = new CopyOnWriteArrayList<ServerInfo>();
		ddoServers.add(new ServerInfo("DDO", "localhost", 5101, 17, 1));
		ddoServers.add(new ServerInfo("DDO", "localhost", 5102, 28, 2));
		ddoServers.add(new ServerInfo("DDO", "localhost", 5103, 39, 3));
		
		List<ServerInfo> allServers = new ArrayList<>();
		allServers.addAll(mtlServers);
		allServers.addAll(lvlServers);
		allServers.addAll(ddoServers);

		// MTL group
		
		
		HashMap<Character, List<Record>> recordsMap22 = new HashMap<>();
		HashMap<String, Record> indexPerId22 = new HashMap<>();
		Logger logger22 = new Logger("SRV_MTL2.log");
		CenterServerCore rudpServer22 = new CenterServerCore(recordsMap22, indexPerId22, logger22, allServers, 22 );
		rudpServer22.start();
		
		
	}
}