package runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.Constants;
import manager.ManagerClientRMI;

public class ClientRunnerCMD_RMI 
{
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception
	{
		System.out.println("Client #1");
		System.out.println("---------------------------");

		// Call Create Teacher Record by mng1 as a sample
		List<String> spec = new ArrayList<String>();
		spec.add("Math");
		spec.add("Computer");
		ManagerClientRMI mng = new ManagerClientRMI(Constants.RemoteProcedures.CreateTeacher.name(), "MTL0001", "Bob", "Azadi", "Garland", 123458886, spec);
		mng.start();		
				
		// Call Create Student Record by mng2 as a sample
		List<String> courses = new ArrayList<String>();
		courses.add("COEN");
		courses.add("Dist");
		Date date = new Date();
		date.getTime();
		ManagerClientRMI mng2 = new ManagerClientRMI(Constants.RemoteProcedures.CreateStudent.name(), "MTL0002", "Alice", "Amani", courses, true, date);
		mng2.start();
		
		// Call Edit Record by mng2 as a sample
		Date date2 = new Date();
		date2.getTime();
		date2.setDate(11);
		ManagerClientRMI mng3 = new ManagerClientRMI(Constants.RemoteProcedures.EditRecords.name(), "MTL0003", "SR00001", "statusDate", date2);
		mng3.start();
		
		// Call Get Records Count by mng3 as a sample
		ManagerClientRMI mng4 = new ManagerClientRMI(Constants.RemoteProcedures.EditRecords.name(), "MTL0004");
		mng4.start();
			
		// Test if record exists
		ManagerClientRMI mng5 = new ManagerClientRMI("MTL0005");
		String id = "SR00001";
		if (mng5.callRecordExist(id))
			System.out.println("There is a record corresponding to ID: " + id);			
		else
			System.out.println("There is NO record corresponding to ID: " + id);
	}	
}
