package network;

import common.Logger;
import common.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.rmi.Remote;
import java.rmi.Naming;
import java.rmi.NotBoundException;


public class CenterServer extends Thread {
    RecordManager obj = null;
    DatagramSocket socket = null;

    private String city;
    private int port;
    private Registry registry;

    /*
     *  This CenterServer unique logger
     */
    private Logger logger;

    private int sock;

    /*
     * here we may create different CenterServer threads and set
     * MTL's sock = 2765
     * LVL's sock = 2865
     * DDO's sock = 2965
     */

    public CenterServer(String city) {
        this.city = city;
        this.port = Infrastucture.getServerPort(city);

        logger = new Logger("SRV_" + city.trim() + ".log");
        logger.logToFile(city + "[CenterServer Constructor]: Center server is created :)");

    }

    public void run() {
        Registry registry = null;
        try
        {
            registry = LocateRegistry.createRegistry(port);
            logger.logToFile(city + "[CenterServer.run()]: RMI registration is started");
        }
        catch (RemoteException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: RMI registration is failed");
         }

        HashMap<Character, List<Record>> recordsMap = new HashMap<>();
        for(Character ch = 'A'; ch <= 'Z'; ch ++) // HashMap initialization
        {
            ArrayList<Record> recordList = new ArrayList<>();
            recordsMap.put(ch, recordList);
        }
        logger.logToFile(city + "[CenterServer.run()]: HashMap initialization is done");

        RecordManager obj = null;
        try
        {
            obj = new RecordManager(city,
                    recordsMap,
                    logger);
        } catch (RemoteException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: RecordManagerClass is failed");
            //e.printStackTrace();
        }

        // Start UDP CenterServer as a separate thread
        Integer udpPort = Infrastucture.getServerPortUDP(city);
        UDPServer srv = new UDPServer(recordsMap, udpPort, city, logger);
        srv.start();

        try
        {
            registry.bind(city, obj);
            logger.logToFile(city + "[CenterServer.run()]: RMI registration is successfully done");
        } catch (AccessException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: AccessException occurred");
            //e.printStackTrace();
        } catch (RemoteException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: RemoteException occurred ");
            //e.printStackTrace();
        } catch (AlreadyBoundException e)
        {
            logger.logToFile(city + "[CenterServer.run()]: AlreadyBoundException occurred (This server is already registered)");
            //e.printStackTrace();
        }

        System.out.println(city + " server is started!");
    }


}
