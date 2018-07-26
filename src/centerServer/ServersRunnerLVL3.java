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

public class ServersRunnerLVL3
{
	public static void main(String[] args)
	{
		String[] cities = {"MTL", "LVL", "DDO"};
		List<HashMap<String, Integer>> ports = new ArrayList<>();
		
		for (int i = 0; i < 3; i++)
		{		
			HashMap<String, Integer> ports1 = new HashMap<>();
			for (int j = 0; j < 3; j++)
			{			
				ports1.put(cities[j], 3710 + i*1000 + j*10);				
			}
			ports.add(ports1);
		}
		
		for (int i = 0; i < 3; i++)
		{		
			for (int j = 0; j < 3; j++)
			{			
				HashMap<Character, List<Record>> recordsMap1 = new HashMap<>();
				HashMap<String, Record> indexPerId1 = new HashMap<>();
				Logger logger1 = new Logger("SRV_" + cities[j] + i + ".log");
				CenterServerCore rudpServer1 = new CenterServerCore(recordsMap1, indexPerId1, ports.get(i).get(cities[j]), cities[j], logger1, 
						ports, i);
				rudpServer1.start();
			}
		}
		
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
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