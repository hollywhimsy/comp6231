package corba;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class ClientCORBA
{

	static RecordManagerCORBA recMng;
	
	public static void main(String[] args)
	{
		String[] configuration = {"-ORBInitialPort", "1050", "-ORBInitialHost", "localhost"};
		
		try
		{
			// create and initialize the ORB
			ORB orb= ORB.init(configuration, null);
			
			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExtinstead of NamingContext. This is part of the Interoperable naming Service.
			NamingContextExt ncRef= NamingContextExtHelper.narrow(objRef);
			
			// resolve the Object Reference in Naming
			String name1 = "RecordManagerCORBA_MTL";
			recMng= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name1));
			//System.out.println("Obtained a handle on server object: " + recMng);
			recMng.createTRecord("Siamak", "Azadiabad", "Montreal", "1234567", "Network,security,programming", "MTL", "MTL0001");
			System.out.println(recMng.recordExist("TR00001"));
			System.out.println(recMng.getRecordCounts("MTL0001"));
			//recMng.shutdown();
			
			// resolve the Object Reference in Naming
			String name2 = "RecordManagerCORBA_LVL";
			recMng= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name2));
			//System.out.println("Obtained a handle on server object: " + recMng);
			recMng.createTRecord("Siamak", "Azadiabad", "Montreal", "1234567", "Network,security,programming", "LVL", "LVL0001");
			System.out.println(recMng.recordExist("TR00001"));
			System.out.println(recMng.getRecordCounts("LVL0001"));
			//recMng.shutdown();
			
			// resolve the Object Reference in Naming
			String name3 = "RecordManagerCORBA_DDO";
			recMng= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name3));
			//System.out.println("Obtained a handle on server object: " + recMng);
			recMng.createTRecord("Siamak", "Azadiabad", "Montreal", "1234567", "Network,security,programming", "DDO", "DDO0001");
			System.out.println(recMng.recordExist("TR00001"));
			System.out.println(recMng.getRecordCounts("DDO0001"));
			//recMng.shutdown();
			
		} catch (InvalidName e)
		{
			System.err.println("Error! InvalidName");
			//e.printStackTrace();
		} catch (NotFound e)
		{
			System.err.println("Error! NotFound");
			//e.printStackTrace();
		} catch (CannotProceed e)
		{
			System.err.println("Error! CannotProceed");
			//e.printStackTrace();
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			System.err.println("Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
			//e.printStackTrace();
		}
		
	}
}
