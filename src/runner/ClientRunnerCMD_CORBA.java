package runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import common.Constants;
import manager.ManagerClientCORBA;

public class ClientRunnerCMD_CORBA
{
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		// Call Create Teacher Record
		List<String> spec = new ArrayList<String>();
		spec.add("Math");
		spec.add("Computer");
		ManagerClientCORBA mng = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "MTL0001", "Bob", "Azadi", "Garland", 123458886, spec);
		mng.start();		
				
		// Call Create Student Record
		List<String> courses = new ArrayList<String>();
		courses.add("COEN");
		courses.add("Dist");
		Date date = new Date();
		date.getTime();
		ManagerClientCORBA mng2 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateStudent.name(), "MTL0002", "Alice", "Amani", courses, true, date);
		mng2.start();
		
		// Call Edit Record
		Date date2 = new Date();
		date2.getTime();
		date2.setDate(11);
		ManagerClientCORBA mng3 = new ManagerClientCORBA(Constants.RemoteProcedures.EditRecords.name(), "MTL0003", "SR00001", "statusDate", date2);
		mng3.start();
		
		// Call Get Records Count
		ManagerClientCORBA mng4 = new ManagerClientCORBA("MTL0004");
		System.out.println(mng4.callGetRecordCounts()); // This can be called by "run" as a thread too
			
		// Test if record exists
		ManagerClientCORBA mng5 = new ManagerClientCORBA("MTL0005");
		String id = "TR00001";
		if (mng5.callRecordExist(id))
			System.out.println("There is a record corresponding to ID: " + id);			
		else
			System.out.println("There is NO record corresponding to ID: " + id);
		
		 //Call transfer Record
		ManagerClientCORBA mng6 = new ManagerClientCORBA("TransferRecord", "MTL0001", "TR00001", "LVL");
		if (mng6.callTransferRecord()) // Also this can called by "run" as a thread
		{
			System.out.println("Treansfered");
		}
			
		try
		{
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Call Get Records again
		ManagerClientCORBA mng7 = new ManagerClientCORBA("DDO0001");
		System.out.println(mng7.callGetRecordCounts()); // This can be called by "run" as a thread too
	}
}
