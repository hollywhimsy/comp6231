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

public class ServersRunner
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

		servers.put("MTL", mtlServers);
		servers.put("LVL", lvlServers);
		servers.put("DDO", ddoServers);
		
		// MTL group
		HashMap<Character, List<Record>> recordsMap11 = new HashMap<>();
		HashMap<String, Record> indexPerId11 = new HashMap<>();
		Logger logger11 = new Logger("SRV_MTL1.log");
		CenterServerCore rudpServer11 = new CenterServerCore(recordsMap11, indexPerId11, logger11, allServers, 11 );
		rudpServer11.start();
		
		
		HashMap<Character, List<Record>> recordsMap22 = new HashMap<>();
		HashMap<String, Record> indexPerId22 = new HashMap<>();
		Logger logger22 = new Logger("SRV_MTL2.log");
		CenterServerCore rudpServer22 = new CenterServerCore(recordsMap22, indexPerId22, logger22, allServers, 22 );
		rudpServer22.start();
		
		HashMap<Character, List<Record>> recordsMap33 = new HashMap<>();
		HashMap<String, Record> indexPerId33 = new HashMap<>();
		Logger logger33 = new Logger("SRV_MTL3.log");
		CenterServerCore rudpServer33 = new CenterServerCore(recordsMap33, indexPerId33, logger33, allServers, 33 );
		rudpServer33.start();

		// LVL group
		
		HashMap<Character, List<Record>> recordsMap14 = new HashMap<>();
		HashMap<String, Record> indexPerId14 = new HashMap<>();
		Logger logger14 = new Logger("SRV_LVL1.log");
		CenterServerCore rudpServer14 = new CenterServerCore(recordsMap14, indexPerId14, logger14, allServers, 14 );
		rudpServer14.start();
		
		
		HashMap<Character, List<Record>> recordsMap25 = new HashMap<>();
		HashMap<String, Record> indexPerId25 = new HashMap<>();
		Logger logger25 = new Logger("SRV_LVL2.log");
		CenterServerCore rudpServer25 = new CenterServerCore(recordsMap25, indexPerId25, logger25, allServers, 25 );
		rudpServer25.start();
		
		HashMap<Character, List<Record>> recordsMap36 = new HashMap<>();
		HashMap<String, Record> indexPerId36 = new HashMap<>();
		Logger logger36 = new Logger("SRV_LVL3.log");
		CenterServerCore rudpServer36 = new CenterServerCore(recordsMap36, indexPerId36, logger36, allServers, 36 );
		rudpServer36.start();
		
		// DDO Group
		HashMap<Character, List<Record>> recordsMap17 = new HashMap<>();
		HashMap<String, Record> indexPerId17 = new HashMap<>();
		Logger logger17 = new Logger("SRV_DDO1.log");
		CenterServerCore rudpServer17 = new CenterServerCore(recordsMap17, indexPerId17, logger17, allServers, 17 );
		rudpServer17.start();
		
		
		HashMap<Character, List<Record>> recordsMap28 = new HashMap<>();
		HashMap<String, Record> indexPerId28 = new HashMap<>();
		Logger logger28 = new Logger("SRV_DDO2.log");
		CenterServerCore rudpServer28 = new CenterServerCore(recordsMap28, indexPerId28, logger28, allServers, 28 );
		rudpServer28.start();
		
		HashMap<Character, List<Record>> recordsMap39 = new HashMap<>();
		HashMap<String, Record> indexPerId39 = new HashMap<>();
		Logger logger39 = new Logger("SRV_DDO3.log");
		CenterServerCore rudpServer39 = new CenterServerCore(recordsMap39, indexPerId39, logger39, allServers, 39 );
		rudpServer39.start();
		
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
		try
		{
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

		} catch (InvalidName e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! InvalidName");
		} catch (AdapterInactive e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! AdapterInactive");
		} catch (ServantNotActive e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! ServantNotActive");
		} catch (WrongPolicy e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! WrongPolicy");
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
		} catch (NotFound e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! NotFound");
		} catch (CannotProceed e)
		{
			//logger.logToFile(city + "[CenterServerCORBA.run()]: Error! CannotProceed");
		}
	}
}