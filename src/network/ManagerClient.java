package network;

import java.util.*;

import common.Logger;
import common.OperationResult;
import common.Record;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;

public class ManagerClient implements IManagerClient {
    private String managerId;
    private Registry registry = null;
    private String city;
    private IRecordManager remoteObject = null;
    private Logger logger;

    public ManagerClient(String managerId) {


        logger = new Logger("MNG_" + managerId + ".log");

        if (isIdFormatCorrect(managerId))
        {
            this.city = managerId.substring(0, 3).toUpperCase();
            this.managerId = managerId;
        }
        else
        {
            logger.logToFile("ERROR! [Manager Constructor]: Manager ID is not valid");
        }

        try
        {
            String serverHost = Infrastucture.getServerHost(city);
            Integer serverPort = Infrastucture.getServerPort(city);

            registry = LocateRegistry.getRegistry(serverHost, serverPort);
            remoteObject = (IRecordManager) registry.lookup(city);

//            registry = LocateRegistry.getRegistry(registryPort);
//            object = (RecordManagerInterface) registry.lookup("GetManager"); // object initialized

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



    @Override
    public boolean createTRecord(String firstName, String lastName, String address, Integer phoneNumber, List<String> specialization) throws RemoteException {
        if (remoteObject == null)
        {
            return false;
        }

        if (remoteObject.createTRecord(firstName, lastName, address, phoneNumber, specialization, city, managerId))
        {
            logger.logToFile(managerId + "[Manager.callCreateTRecord()]: callCreateTRecord called on " +
                    city + " server and performed successfully");
            return true;
        }

        logger.logToFile(managerId + "[Manager.callCreateTRecord()]: callCreateTRecord called on " +
                city + " server and failed");
        return false;
    }

    @Override
    public boolean createSRecord(String firstName, String lastName, List<String> coursesRegistred, boolean status, Date statusDate) throws RemoteException {
        if (remoteObject == null)
        {
            return false;
        }

        if (remoteObject.createSRecord(firstName, lastName, coursesRegistred, status, statusDate, managerId))
        {
            logger.logToFile(managerId + "[Manager.callCreateSRecord()]: callCreateSRecord called on " +
                    city + " server and performed successfully");
            return true;
        }

        logger.logToFile(managerId + "[Manager.callCreateTRecord()]: callCreateSRecord called on " +
                city + " server and failed");
        return false;
    }

    @Override
    public String getRecordCounts() throws RemoteException {
        if (remoteObject == null)
        {
            return null;
        }

        String result = remoteObject.getRecordCounts(managerId);
        if(result != null)
        {
            logger.logToFile(managerId + "[Manager.callGetRecordCounts()]: callGetRecordCounts called on " +
                    city + " server and performed successfully");
            return result;
        }

        logger.logToFile(managerId + "[Manager.callGetRecordCounts()]: callGetRecordCounts called on " +
                city + " server and failed");
        return null;
    }

    @Override
    public Integer getLocalRecordsCount() throws RemoteException {
        logger.logToFile("calling server to getLocalRecordsCount ");
        return remoteObject.getLocalRecordsCount(managerId);
    }

    @Override
    public Boolean editRecord(String recordID, String fieldName, Object newValue) throws RemoteException {
        if (remoteObject.editRecord(recordID, fieldName, newValue, managerId))
        {
            logger.logToFile(managerId + "[Manager.callEditRecord()]: callEditRecord called on " +
                    city + " server and performed successfully");
            return true;
        }

        logger.logToFile(managerId + "[Manager.callEditRecord()]: callEditRecord called on " +
                city + " server and failed");
        return false;
    }

    @Override
    public Record returnRecord(String recordId) throws RemoteException {
        if (remoteObject == null)
        {
            return null;
        }

        Record result = remoteObject.returnRecord(recordId, managerId);
        if(result != null)
        {
            logger.logToFile(managerId + "[Manager.callReturnRecord()]: callReturnRecord called on " +
                    city + " server and performed successfully");
            return result;
        }

        logger.logToFile(managerId + "[Manager.callReturnRecord()]: callReturnRecord called on " +
                city + " server and failed");
        return null;
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
