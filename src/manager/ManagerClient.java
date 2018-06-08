package manager;

import java.util.*;
import common.Constants;
import common.Logger;
import server.IRecordManager;
import server.Infrastucture;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ManagerClient extends Thread implements IManagerClient 
{
    private String managerId;
    private Registry registry = null;
    private String city;
    private IRecordManager remoteObject = null;
    private Logger logger;
	private Logger operationResultLogger;	
	private String methodToCall;
	private String firstName;
	private String lastName;
	private String address;
	private Integer phoneNumber;
	private List<String> specilization;
	private List<String> coursesRegistred;
	private boolean status;
	private Date statusDate;
	private String recordId;
	private String fieldName;
	private Object newValue;	

	// Constructor (default)
    public ManagerClient(String managerId) 
    {
    	this.managerId = managerId;
    	initialize();
    }    
    // Constructor1
 	public ManagerClient(String methodToCall, String managerId) 
 	{
 		this.managerId = managerId;
 		this.methodToCall = methodToCall;		
 		initialize();		
 	}
 	// Constructor2
 	public ManagerClient(String methodToCall, 
 			String managerId, 
 			String firstName, 
 			String lastName, 
 			String address,
 			Integer phoneNumber, 
 			List<String> specilization) 
 	{
 		this.managerId = managerId;
 		this.methodToCall = methodToCall;
 		this.firstName = firstName;
 		this.lastName = lastName;
 		this.address = address;
 		this.phoneNumber = phoneNumber;
 		this.specilization = specilization;
 		initialize();		
 	}
 	// Constructor3
 	public ManagerClient(String methodToCall, 
 			String managerId, 
 			String firstName, 
 			String lastName, 
 			List<String> coursesRegistred,
 			boolean status, 
 			Date statusDate) 
 	{
 		this.managerId = managerId;
 		this.methodToCall = methodToCall;
 		this.firstName = firstName;
 		this.lastName = lastName;
 		this.coursesRegistred = coursesRegistred;
 		this.status = status;
 		this.statusDate = statusDate;
 		initialize();		
 	}
 	// Constructor4
 	public ManagerClient(String methodToCall, 
 			String managerId,
 			String recordId,
 			String fieldName, 			
 			Object newValue) 
 	{
 		this.managerId = managerId;
 		this.methodToCall = methodToCall;
 		this.fieldName = fieldName;
 		this.newValue = newValue;
 		this.recordId = recordId;
 		initialize();		
 	}
 	
 	// Thread Method
 	public void run()
	{
		if(methodToCall.equals(Constants.RemoteProcedures.CreateTeacher.name()))
		{
			try 
			{
				callCreateTRecord();
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
			}
		}
		
		if(methodToCall.equals(Constants.RemoteProcedures.CreateStudent.name()))
		{
			try 
			{
				callCreateSRecord();
			} 
			catch (RemoteException e) 
			{
				e.printStackTrace();
			}
		}
		
		if(methodToCall.equals(Constants.RemoteProcedures.GetCounts.name()))
		{
			try 
			{
				callGetRecordCounts();				
			} 
			catch (RemoteException e) 
			{				
				e.printStackTrace();
			}
		}
		
		if(methodToCall.equals(Constants.RemoteProcedures.EditRecords.name()))
		{
			try 
			{
				callEditRecord();		
			} 
			catch (RemoteException e) 
			{				
				e.printStackTrace();
			}
		}	
	}

    @Override
    public boolean callCreateTRecord() throws RemoteException
	{
		if (remoteObject == null)
		{
			logger.logToFile(managerId + "[Manager.callCreateTRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[Manager.callCreateTRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			return false;
		}
		
		if (remoteObject.createTRecord(firstName, lastName, address, phoneNumber, specilization, city, managerId))
		{
			logger.logToFile(managerId + "[Manager.callCreateTRecord()] {Thread ID: "+ this.getId() +
					"}: Teacher record created");
			operationResultLogger.logToFile(managerId + "[Manager.callCreateTRecord()] {Thread ID: "+ this.getId() +
					"}: Teacher record created");
			return true;
		}
		
		logger.logToFile(managerId + "[Manager.callCreateTRecord()]: callCreateTRecord called on " +
				city + " server and failed");
		return false;
	}
	
    @Override
	public boolean callCreateSRecord() throws RemoteException
	{
		if (remoteObject == null)
		{
			logger.logToFile(managerId + "[Manager.callCreateSRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[Manager.callCreateSRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			return false;
		}
		
		if (remoteObject.createSRecord(firstName, lastName, coursesRegistred, status, statusDate, managerId))
		{
			logger.logToFile(managerId + "[Manager.callCreateSRecord()]: callCreateSRecord called on " +
					city + " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[Manager.callCreateSRecord()] {Thread ID: "+ this.getId() +
					"}: Student record created");
			return true;
		}
		
		logger.logToFile(managerId + "[Manager.callCreateTRecord()]: callCreateSRecord called on " +
				city + " server and failed");
		return false;
	}
    
    @Override
	public boolean callEditRecord() throws RemoteException
	{
		if (remoteObject == null)
		{
			logger.logToFile(managerId + "[Manager.callEditRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[Manager.callEditRecord()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			return false;
		}
		
		if (remoteObject.editRecord(recordId, fieldName, newValue, managerId))
		{
			logger.logToFile(managerId + "[Manager.callEditRecord()]: callEditRecord called on " +
					city + " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[Manager.callEditRecord()] {Thread ID: "+ this.getId() +
					"}: Edit record performed successfully");
			return true;
		}
		
		logger.logToFile(managerId + "[Manager.callEditRecord()]: callEditRecord called on " +
				city + " server and failed");
		return false;
	}
    
    @Override
	public String callGetRecordCounts() throws RemoteException
	{
		if (remoteObject == null)
		{
			logger.logToFile(managerId + "[Manager.callGetRecordCounts()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[Manager.callGetRecordCounts()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			return null;
		}
		
		String result = remoteObject.getRecordCounts(managerId);
		if(result != null)
		{
			logger.logToFile(managerId + "[Manager.callGetRecordCounts()]: callGetRecordCounts called on " +
					city + " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[Manager.getRecordCounts()] {Thread ID: "+ this.getId() +
					"}: Records Count: " + result);
			return result;
		}
		
		logger.logToFile(managerId + "[Manager.callGetRecordCounts()]: callGetRecordCounts called on " +
				city + " server and failed");
		return null;
				
	}
	
    @Override
	public boolean callRecordExist(String recordId) throws RemoteException
	{
		if (remoteObject == null)
		{
			logger.logToFile(managerId + "[Manager.callRecordExist()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[Manager.callRecordExist()] {Thread ID: "+ this.getId() + 
					"}: Registry connection failed for the city: " + city);			
			return false;
		}
		
		return remoteObject.recordExist(recordId);
	}
	
    @Override
    public Integer getLocalRecordsCount() throws RemoteException 
    {
        logger.logToFile("calling server to getLocalRecordsCount ");
        return remoteObject.getLocalRecordsCount(managerId);
    }
    
    private void initialize()
	{
		logger = new Logger("MNG_" + managerId + ".log");
		operationResultLogger = new Logger("Result.log");
		
		if (isIdFormatCorrect(managerId))
		{			
			city = managerId.substring(0, 3).toUpperCase();				
		}
		else
		{
			logger.logToFile("ERROR! [Manager Constructor]: Manager ID is not valid");
		}
		
		try 
		{
			String serverHost = Infrastucture.getServerHost(city);
            Integer serverPort = Infrastucture.getServerPort(city);

            //System.out.println(serverHost + " " + serverPort);
            
            registry = LocateRegistry.getRegistry(serverHost, serverPort);
            remoteObject = (IRecordManager) registry.lookup(city);
         
			logger.logToFile(managerId + "[Manager Constructor]: Manager connected to RMI registry");
		} 
		catch (RemoteException e) 
		{
			logger.logToFile(managerId + "[Manager Constructor]: Manager connection to RMI registry failed (RemoteException Error!)");
			//e.printStackTrace();
		} 
		catch (NotBoundException e) 
		{
			logger.logToFile(managerId + "[Manager Constructor]: Manager connection to RMI registry failed (NotBoundException Error!)");
			//e.printStackTrace();
		}
	}
    
    private boolean isIdFormatCorrect(String id)
    {
        if (id == null)
        {
            return false;
        }

        if (id.length() != 7)
        {
            return false;
        }

        String srvName = id.substring(0, 3).toUpperCase();
        if(!Infrastucture.isSystemServerName(srvName))
        {
            return false;
        }

        if(!(id.substring(3, 4).chars().allMatch(Character::isDigit)))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((managerId == null) ? 0 : managerId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManagerClient other = (ManagerClient) obj;
        if (managerId == null) {
            if (other.managerId != null)
                return false;
        } else if (!managerId.equals(other.managerId))
            return false;
        return true;
    }

    public String getManagerId()
    {
        return managerId;
    }

}
