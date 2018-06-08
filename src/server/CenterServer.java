package server;

import common.Logger;
import record.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.DatagramSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;


public class CenterServer extends Thread 
{
    RecordManager obj = null;
    DatagramSocket socket = null;
    private String city;
    private int port;
    private Logger logger; // This CenterServer unique logger

    /*
     * here we may create different CenterServer threads and set
     * MTL's sock = 2765
     * LVL's sock = 2865
     * DDO's sock = 2965
     */

    // Constructor
    public CenterServer(String city) 
    {
        this.city = city;
        this.port = Infrastucture.getServerPort(city);
        logger = new Logger("SRV_" + city.trim() + ".log");
        logger.logToFile(city + "[CenterServer Constructor]: Center server is created :)");
    }

    // Thread Method
    public void run() 
    {
    	try
        {
        	Registry registry = LocateRegistry.createRegistry(port); // Connect to RMI registry
            logger.logToFile(city + "[CenterServer.run()]: RMI registration is started");
        
	        // HashMap initialization
	        HashMap<Character, List<Record>> recordsMap = new HashMap<>();
	        for(Character ch = 'A'; ch <= 'Z'; ch ++) 
	        {
	            ArrayList<Record> recordList = new ArrayList<>();
	            recordsMap.put(ch, recordList);
	        }
	        logger.logToFile(city + "[CenterServer.run()]: HashMap initialization is done");
	
	        RecordManager obj = new RecordManager(city, recordsMap, logger);
	                 
	        // Start UDP CenterServer as a separate thread
	        Integer udpPort = Infrastucture.getServerPortUDP(city);
	        UDPServer srv = new UDPServer(recordsMap, udpPort, city, logger);
	        srv.start();
        
            registry.bind(city, obj);
            logger.logToFile(city + "[CenterServer.run()]: RMI registration is successfully done");
            
            System.out.println(city + " server is started!");
            
        } catch (AccessException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: AccessException occurred");
            //e.printStackTrace();
        } catch (AlreadyBoundException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: AlreadyBoundException occurred (This server is already registered)");
            //e.printStackTrace();
        } catch (RemoteException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: RecordManagerClass oject initiation is failed");
            //e.printStackTrace();
        }      
    }
}
