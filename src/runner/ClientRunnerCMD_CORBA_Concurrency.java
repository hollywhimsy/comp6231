package runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import common.Constants;
import manager.ManagerClientCORBA;

public class ClientRunnerCMD_CORBA_Concurrency
{
	public static void main(String[] args)
	{
		for (int i = 0; i < 10; i++)
		{
			// Call Create Teacher Record
			List<String> spec = new ArrayList<String>();
			spec.add("Math");
			spec.add("Computer");
			ManagerClientCORBA mng = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "MTL0001",
					"Bob", "Azadi", "Garland", 123458886, spec);
			mng.start();
			ManagerClientCORBA mng2 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "LVL0001",
					"Bob", "Azadi", "Garland", 123458886, spec);
			mng2.start();
			ManagerClientCORBA mng3 = new ManagerClientCORBA(Constants.RemoteProcedures.CreateTeacher.name(), "DDO0001",
					"Bob", "Azadi", "Garland", 123458886, spec);
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

		try
		{
			TimeUnit.MILLISECONDS.sleep(100);							
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < 10; i++)
		{
			// Call Edit Record
			ManagerClientCORBA mng3 = new ManagerClientCORBA(Constants.RemoteProcedures.EditRecords.name(), "MTL0003",
					"TR0000" + i, "address", "st. Catrine");
			ManagerClientCORBA mng6 = new ManagerClientCORBA("TransferRecord", "MTL0001", "TR0000" + i, "LVL");			
			
			long num = 0;
			try
			{
				num = (long)(Math.random() * 49);
				TimeUnit.MILLISECONDS.sleep(num);
				System.out.println(num);				
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (num > 25)
			{
				mng3.start();
				try
				{
					TimeUnit.MILLISECONDS.sleep(20);									
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mng6.start(); 
			}
			else
			{
				mng6.start();
				try
				{
					TimeUnit.MILLISECONDS.sleep(20);									
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mng3.start();
			}			
		}

		// Call Get Records Count
		ManagerClientCORBA mng4 = new ManagerClientCORBA("MTL0001");
		System.out.println(mng4.callGetRecordCounts()); // This can be called by "run" as a thread too

	}
}
