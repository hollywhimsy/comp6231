package client;

import java.util.Date;
import ddoStub.RecordManagerDDOImplService;
import lvlStub.RecordManagerLVLImplService;
import mtlStub.RecordManagerMTLImplService;

public class ClientCMD
{
	public static void main(String[] args)
	{		
		RecordManagerMTLImplService caller1 = new RecordManagerMTLImplService();
		RecordManagerLVLImplService caller2 = new RecordManagerLVLImplService();
		RecordManagerDDOImplService caller3 = new RecordManagerDDOImplService();
		
		// Create a Teacher Record
		System.out.println(caller1.getRecordManagerMTLImpl().createTRecord("sdf", "sdfb", "dfgb", 2342, "sdfb,sdf", "dgfb", "MTL0001"));
		System.out.println(caller2.getRecordManagerLVLImpl().createTRecord("sdf", "sdfb", "dfgb", 2342, "sdfb,sdf", "dgfb", "LVL0001"));
		System.out.println(caller3.getRecordManagerDDOImpl().createTRecord("sdf", "sdfb", "dfgb", 2342, "sdfb,sdf", "dgfb", "DDO0001"));
		
		// Create a Student Record
		System.out.println(caller3.getRecordManagerDDOImpl().createSRecord("sdf", "dgb", "dvd,sdfv", true, (new Date()).toString(), "DDO0001"));
		
		// Edit a Record
		System.out.println(caller2.getRecordManagerLVLImpl().editRecord("TR00001", "address", "Toronto", "LVL0001"));
		
		// Check if the Record exists?
		System.out.println(caller1.getRecordManagerMTLImpl().recordExist("TR00001", "MTL0001"));
		
		// Transfer a Record
		System.out.println(caller1.getRecordManagerMTLImpl().transferRecord("MTL0001", "TR00001", "LVL"));
		
		// Get Records Count
		System.out.println(caller3.getRecordManagerDDOImpl().getRecordsCount("MTL0009"));
		
	}
}