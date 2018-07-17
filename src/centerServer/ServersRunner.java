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
import common.Infrastucture;
import common.Logger;
import frontEnd.FrontEnd;
import frontEnd.FrontEndHelper;
import frontEnd.RecordManagerImpl;
import record.Record;
import udp.UDPServer;

public class ServersRunner
{
	public static void main(String[] args)
	{
		HashMap<String, Integer> ports1 = new HashMap<>();
		ports1.put("MTL", 3710);
		ports1.put("LVL", 3720);
		ports1.put("DDO", 3730);
		HashMap<String, Integer> ports2 = new HashMap<>();
		ports2.put("MTL", 4710);
		ports2.put("LVL", 4720);
		ports2.put("DDO", 4730);
		HashMap<String, Integer> ports3 = new HashMap<>();
		ports3.put("MTL", 5710);
		ports3.put("LVL", 5720);
		ports3.put("DDO", 5730);		
		List<HashMap<String, Integer>> ports = new ArrayList<>();
		ports.add(ports1);
		ports.add(ports2);
		ports.add(ports3);

		HashMap<Character, List<Record>> recordsMap1 = new HashMap<>();
		HashMap<String, Record> indexPerId1 = new HashMap<>();
		Logger logger1 = new Logger("SRV_" + "MTL" + ".log");
		CenterServerCore rudpServer1 = new CenterServerCore(recordsMap1, indexPerId1, 3710, "MTL", logger1, ports.get(0));
		rudpServer1.start();

		HashMap<Character, List<Record>> recordsMap2 = new HashMap<>();
		HashMap<String, Record> indexPerId2 = new HashMap<>();
		Logger logger2 = new Logger("SRV_" + "LVL" + ".log");
		CenterServerCore rudpServer2 = new CenterServerCore(recordsMap2, indexPerId2, 3720, "LVL", logger2, ports.get(0));
		rudpServer2.start();

		HashMap<Character, List<Record>> recordsMap3 = new HashMap<>();
		HashMap<String, Record> indexPerId3 = new HashMap<>();
		Logger logger3 = new Logger("SRV_" + "DDO" + ".log");
		CenterServerCore rudpServer3 = new CenterServerCore(recordsMap3, indexPerId3, 3730, "DDO", logger3, ports.get(0));
		rudpServer3.start();
		
		
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
		String city;
		Logger logger;
		HashMap<Character, List<Record>> recordsMap = new HashMap<>();
		
		try
		{
			ORB orb = ORB.init(configuration, null);
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			
			RecordManagerImpl recMngImp = new RecordManagerImpl(ports);
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