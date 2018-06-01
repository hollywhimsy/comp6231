package runners;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.Constants;
import common.StudentRecord;
import network.ManagerClient;

public class ClientRunner 
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
		ManagerClient mng = new ManagerClient(Constants.RemoteProcedures.CreateTeacher.name(), "MTL0001", "Bob", "Azadi", "Garland", 123458886, spec);
		mng.start();		
				
		// Call Create Student Record by mng2 as a sample
		List<String> courses = new ArrayList<String>();
		courses.add("COEN");
		courses.add("Dist");
		Date date = new Date();
		date.getTime();
		ManagerClient mng2 = new ManagerClient(Constants.RemoteProcedures.CreateStudent.name(), "MTL0001", "Alice", "Amani", courses, true, date);
		mng2.start();
		
		// Call Edit Record by mng2 as a sample
		Date date2 = new Date();
		date2.getTime();
		date2.setDate(11);
		ManagerClient mng3 = new ManagerClient(Constants.RemoteProcedures.EditRecords.name(), "MTL0001", "SR00001", "statusDate", date2);
		mng3.start();
		
		// Call Get Records Count by mng3 as a sample
		ManagerClient mng4 = new ManagerClient(Constants.RemoteProcedures.EditRecords.name(), "MTL0001");
		mng4.start();
		
		// Call Return Record by mng1 to retrieve a Teacher record as a sample
		ManagerClient mng5 = new ManagerClient("MTL0001");
		StudentRecord std = (StudentRecord) mng5.callReturnRecord("SR00001");
		if (std != null)
		{
			System.out.println("Sample \"Return Date Value\" for a Student recored:");
			System.out.println(std.getStatusDate().toString());
		}		
	}	
}
