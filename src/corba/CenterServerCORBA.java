package corba;

import java.net.DatagramSocket;
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
import record.Record;
import server.RecordManager;

public class CenterServerCORBA
{
	public static void main(String[] args)
	{
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
		RecordManager obj = null;
	    DatagramSocket socket = null;
	    String city = "MTL";
	    int port;
	    Logger logger;
	    HashMap<Character, List<Record>> recordsMap = new HashMap<>();
	    
	    logger = new Logger("SRV_" + city.trim() + ".log");
	    
	    // HashMap initialization        
        for(Character ch = 'A'; ch <= 'Z'; ch ++) 
        {
            ArrayList<Record> recordList = new ArrayList<>();
            recordsMap.put(ch, recordList);
        }
        logger.logToFile(city + "[CenterServer.run()]: HashMap initialization is done");
				
		try
		{
			// create and initialize the ORB
			ORB orb = ORB.init(configuration, null);
			
			// get reference to rootpoa & activate the POAManager
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			
			// create servant and register it with the ORB
			RecordManagerImpl recMngImp= new RecordManagerImpl(recordsMap, city, logger);
			recMngImp.setOrb(orb);
			
			// get object reference from the servant
			org.omg.CORBA.Object ref = rootPOA.servant_to_reference(recMngImp);
			RecordManagerCORBA href= RecordManagerCORBAHelper.narrow(ref);
			
			// get the root naming context
			// NameServiceinvokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExtwhich is part of the Interoperable Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			// bind the Object Reference in Naming
			String name = "RecordManagerCORBA";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);
			System.out.println("RecordManagerCORBA_1 ready and waiting ...");
			
			// wait for invocations from clients
			orb.run();
			
		} catch (InvalidName e)
		{
			System.err.println("Error! InvalidName");
			//e.printStackTrace();
		} catch (AdapterInactive e)
		{
			System.err.println("Error! AdapterInactive");
			//e.printStackTrace();
		} catch (ServantNotActive e)
		{
			System.err.println("Error! ServantNotActive");
			//e.printStackTrace();
		} catch (WrongPolicy e)
		{
			System.err.println("Error! WrongPolicy");
			//e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			System.err.println("Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
			//e.printStackTrace();
		} catch (NotFound e)
		{
			System.err.println("Error! NotFound");
			//e.printStackTrace();
		} catch (CannotProceed e)
		{
			System.err.println("Error! CannotProceed");
			//e.printStackTrace();
		}		
	}
}
