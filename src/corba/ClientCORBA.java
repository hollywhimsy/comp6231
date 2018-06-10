package corba;

import java.util.Date;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class ClientCORBA
{

	static RecordManagerCORBA recMng1;
	static RecordManagerCORBA recMng2;
	static RecordManagerCORBA recMng3;
	
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
			
			String name1 = "RecordManagerCORBA_MTL"; // resolve the Object Reference in Naming
			recMng1= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name1));
			recMng1.createTRecord("Siamak", "Azadiabad", "Montreal", "1234567", "Network,security,programming", "MTL", "MTL0001");
			System.out.println(recMng1.recordExist("TR00001", "MTL0001"));
			System.out.println(recMng1.getRecordCounts("MTL0001"));
			//recMng1.shutdown();
			
			String name2 = "RecordManagerCORBA_LVL"; // resolve the Object Reference in Naming
			recMng2= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name2));
			recMng2.createSRecord("Bob", "qazwsx", "Network,COMP6231,C++", true, (new Date()).toString(), "LVL0001");
			System.out.println(recMng2.recordExist("SR00001", "LVL0001"));
			System.out.println(recMng2.getRecordCounts("LVL0001"));
			//recMng.shutdown();
			
			String name3 = "RecordManagerCORBA_DDO"; // resolve the Object Reference in Naming
			recMng3= RecordManagerCORBAHelper.narrow(ncRef.resolve_str(name3));
			recMng3.createTRecord("Siamak", "Azadiabad", "Montreal", "1234567", "Network,security,programming", "DDO", "DDO0001");
			recMng3.editRecord("TR00001", "phoneNumber", "333333333", "DDO0001");
			System.out.println(recMng3.recordExist("TR00001", "DDO0001"));
			System.out.println(recMng3.getRecordCounts("DDO0001"));
			
			System.out.println(recMng3.transferRecord("DDO0001", "TR00001", "LVL"));
			System.out.println(recMng2.recordExist("TR00001", "LVL0001"));
			System.out.println(recMng3.getRecordCounts("DDO0001"));
			
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
