//package centerServer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import org.omg.CORBA.ORB;
//import org.omg.CORBA.ORBPackage.InvalidName;
//import org.omg.CosNaming.NameComponent;
//import org.omg.CosNaming.NamingContextExt;
//import org.omg.CosNaming.NamingContextExtHelper;
//import org.omg.CosNaming.NamingContextPackage.CannotProceed;
//import org.omg.CosNaming.NamingContextPackage.NotFound;
//import org.omg.PortableServer.POA;
//import org.omg.PortableServer.POAHelper;
//import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
//import org.omg.PortableServer.POAPackage.ServantNotActive;
//import org.omg.PortableServer.POAPackage.WrongPolicy;
//import common.Infrastucture;
//import common.Logger;
//import frontEnd.FrontEnd;
//import frontEnd.FrontEndHelper;
//import frontEnd.RecordManagerImpl;
//import record.Record;
//import udp.UDPServer;
//
//public class CenterServer extends Thread
//{
//	private String[] configuration = new String[4];
//	private String city;
//	private Logger logger;
//	private HashMap<Character, List<Record>> recordsMap = new HashMap<>();
//
//	// Constructor
//	public CenterServer(String city)
//	{
//		super();
//
//		this.city = city;
//
//		configuration[0] = "-ORBInitialPort";
//		configuration[1] = "1050";
//		configuration[2] = "-ORBInitialHost";
//		configuration[3] = "localhost";
//
//		logger = new Logger("SRV_" + city.toUpperCase().trim() + ".log");
//
//		// HashMap initialization
//		for (Character ch = 'A'; ch <= 'Z'; ch++)
//		{
//			ArrayList<Record> recordList = new ArrayList<>();
//			recordsMap.put(ch, recordList);
//		}
//		logger.logToFile(city + "[CenterServerCORBA Constructor]: HashMap initialization is done");
//
//		logger.logToFile(city + "[CenterServerCORBA Constructor]: CORBA Center server is created :)");
//	}
//
//	// Thread Method
//	//since we are running 3 servers on 1 machine, thus we have threads.
//	//in real world example, we have 3 servers in 3 separate machines, without threads
//	public void run()
//	{
//		try
//		{
//			ORB orb = ORB.init(configuration, null);
//			logger.logToFile(city + "[CenterServerCORBA.run()]: ORB is initialized");
//
//			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//			rootPOA.the_POAManager().activate();
//			logger.logToFile(city + "[CenterServerCORBA.run()]: POA Manager is activated");
//
//			/*
//			 * each city has one separate hashmap for records
//			 */
//			RecordManagerImpl recMngImp = new RecordManagerImpl(recordsMap, city, logger);
//			recMngImp.setOrb(orb);
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Servant with ORB is registered");
//
//			org.omg.CORBA.Object ref = rootPOA.servant_to_reference(recMngImp);
//			FrontEnd href = FrontEndHelper.narrow(ref);
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Object reference from Servant recieved");
//
//			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//			logger.logToFile(city + "[CenterServerCORBA.run()]: The Name Service in invoked");
//
//			String name = "RecordManagerCORBA_" + city.toUpperCase().trim();
//			NameComponent path[] = ncRef.to_name(name);
//			ncRef.rebind(path, href);
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Object reference is binded with Name Service");
//
//			// Start UDP CenterServer as a separate thread
//			Integer udpPort = Infrastucture.getServerPortUDP(city);
//			UDPServer srv = new UDPServer(recordsMap, udpPort, city, logger);
//			srv.start();
//
//			logger.logToFile(city + "[CenterServerCORBA.run()]: CORBA Server is started successfully");
//			// wait for invocations from clients
//			orb.run();
//
//		} catch (InvalidName e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! InvalidName");
//		} catch (AdapterInactive e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! AdapterInactive");
//		} catch (ServantNotActive e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! ServantNotActive");
//		} catch (WrongPolicy e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! WrongPolicy");
//		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
//		{
//			logger.logToFile(
//					city + "[CenterServerCORBA.run()]: Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
//		} catch (NotFound e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! NotFound");
//		} catch (CannotProceed e)
//		{
//			logger.logToFile(city + "[CenterServerCORBA.run()]: Error! CannotProceed");
//		}
//	}
//}
