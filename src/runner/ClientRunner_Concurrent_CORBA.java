package runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import common.Constants;
import manager.ManagerClientCORBA;

public class ClientRunner_Concurrent_CORBA 
{
	public static void main(String[] args)
	{
		for (int i = 0; i < 20; i++)
		{
			// Call Create Teacher Record
			List<String> spec = new ArrayList<String>();
			spec.add("Math");
			spec.add("Computer");
			ManagerClientCORBA mng = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "MTL0001", "Bob", "Azadi", "Garland",
					123458886, spec);
			mng.start();
			ManagerClientCORBA mng2 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "LVL0001", "Bob", "Azadi", "Garland",
					123458886, spec);
			mng2.start();
			ManagerClientCORBA mng3 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "DDO0001", "Bob", "Azadi", "Garland",
					123458886, spec);
			mng3.start();

			// Call Create Student Record
			List<String> courses = new ArrayList<String>();
			courses.add("COEN");
			courses.add("Dist");
			Date date = new Date();
			date.getTime();
			ManagerClientCORBA mng4 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateStudent.name(), "MTL0002",
					"Alice", "Amani", courses, true, date);
			mng4.start();
			ManagerClientCORBA mng5 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateStudent.name(), "LVL0002",
					"Alice", "Amani", courses, true, date);
			mng5.start();
			ManagerClientCORBA mng6 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateStudent.name(), "DDO0002",
					"Alice", "Amani", courses, true, date);
			mng6.start();
		}	
		
		for (int i = 1; i <= 20; i++)
		{
			// You can indicate the records ID range by assigning a value to "startId", e.g.:
			// 0 => start transferring and editing from the record TR00001
			// 10 => start transferring and editing from the record TR00011
			int startId = 0; 
			
			int idIndex = i + startId;
			String id = "TR" + String.format("%05d", idIndex);
			ManagerClientCORBA mng8 = new ManagerClientCORBA("TransferRecord", "MTL0006", id, "LVL");		
			mng8.start(); // Start transferring the record
			
			Random rand = new Random();
			int  num = rand.nextInt(500);
			try
			{
				TimeUnit.MICROSECONDS.sleep(num); // Wait in a random manner for 0 to 500 Microsecond
			} catch (InterruptedException e)
			{			
				e.printStackTrace();
			}
			
			ManagerClientCORBA mng7 = new ManagerClientCORBA(Constants.RemoteProcedures.EditRecords.name(), "MTL0006", id, "address", "st. Catrine");
			mng7.start(); // Try to edit the record, if it's not transferred yet
		}
		
		for (int i = 1; i <= 20; i++)
		{
			// You can indicate the records ID range by assigning a value to "startId", e.g.:
			// 0 => start transferring and editing from the record TR00001
			// 10 => start transferring and editing from the record TR00011
			int startId = 0; 
			
			int idIndex = i + startId;
			String id = "SR" + String.format("%05d", idIndex);
			ManagerClientCORBA mng9 = new ManagerClientCORBA("TransferRecord", "DDO0006", id, "LVL");		
			mng9.start(); // Start transferring the record
			
			Random rand = new Random();
			int  num = rand.nextInt(500);
			try
			{
				TimeUnit.MICROSECONDS.sleep(num); // Wait in a random manner for 0 to 500 Microsecond
			} catch (InterruptedException e)
			{			
				e.printStackTrace();
			}
			
			ManagerClientCORBA mng10 = new ManagerClientCORBA(Constants.RemoteProcedures.EditRecords.name(), "DDO0006", id, "status", false);
			mng10.start(); // Try to edit the record, if it's not transferred yet
		}

		try
		{
			TimeUnit.MILLISECONDS.sleep(500); // Wait to make sure that all previous jobs are done
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		ManagerClientCORBA mng9 = new ManagerClientCORBA("MTL0001");
		System.out.println(mng9.callGetRecordCounts()); // Call Get Records Count to make sure that all records are transferred

	}	
}
