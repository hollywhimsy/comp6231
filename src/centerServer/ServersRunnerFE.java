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

public class ServersRunnerFE {
	public static void main(String[] args) {

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

		servers.put("MTL", mtlServers);
		servers.put("LVL", lvlServers);
		servers.put("DDO", ddoServers);

		String[] configuration = { "-ORBInitialPort", "1050", "-ORBInitialHost", "localhost" };
		try {
			ORB orb = ORB.init(configuration, null);
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();

			RecordManagerImpl recMngImp = new RecordManagerImpl(servers);
			recMngImp.setOrb(orb);

			org.omg.CORBA.Object ref = rootPOA.servant_to_reference(recMngImp);
			FrontEnd href = FrontEndHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "FrontEnd";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			// wait for invocations from clients
			orb.run();

		} catch (InvalidName e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error! InvalidName");
		} catch (AdapterInactive e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error! AdapterInactive");
		} catch (ServantNotActive e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error!
			// ServantNotActive");
		} catch (WrongPolicy e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error! WrongPolicy");
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error!
			// org.omg.CosNaming.NamingContextPackage.InvalidName");
		} catch (NotFound e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error! NotFound");
		} catch (CannotProceed e) {
			// logger.logToFile(city + "[CenterServerCORBA.run()]: Error! CannotProceed");
		}
	}
}