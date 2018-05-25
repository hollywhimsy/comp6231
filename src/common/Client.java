package common;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;

public class Client extends Thread implements SystemInterface  {
    private Registry registry = null;
    private String city;
    private SystemInterface remoteObject = null;
    private Logger logger;

    public Client(String city) {
        this.city = city;

        logger = new Logger("/tmp/logs", city + "_client");
        try {
            if (city == "MTL") {
                registry = LocateRegistry.getRegistry(2270);
            }
            if (city == "LVL") {
                registry = LocateRegistry.getRegistry(2865);
            }
            if (city == "DDO") {
                registry = LocateRegistry.getRegistry(2965);
            }

            remoteObject = (SystemInterface) registry.lookup(city);

        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }



    public void run() {
        try {
            if (city == "MTL") {
                registry = LocateRegistry.getRegistry(2270);
            }
            if (city == "LVL") {
                registry = LocateRegistry.getRegistry(2865);
            }
            if (city == "DDO") {
                registry = LocateRegistry.getRegistry(2965);
            }

            SystemInterface obj = (SystemInterface) registry.lookup(city);
            obj.createSRecord("Dan", "Soen", "Discrete Math", "Inactive", "2016-12-18");
            obj.createSRecord("Charles", "Lemon", "Math", "Inactive", "2015-12-18");
            obj.createTRecord("Maxim", "Beautin", "Blvd Saint-Andre", "514-333-4493", "Finance", "LVL");

        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

// 	 Registry registry = LocateRegistry.getRegistry(2964); 

// 	 AddInterface obj = (AddInterface) registry.lookup("Addition");

// 	 int n = obj.add(5, 4); 
// 	 System.out.println("Addition is : " + n);


        String city = "MTL";
        Client instance = new Client(city);
        instance.run();
    }

    @Override
    public void createTRecord(String firstName, String lastName, String address, String phone, String specialization, String location) throws RemoteException {

        logger.write("calling server to createTRecord ");
        remoteObject.createTRecord(firstName, lastName, address, phone, specialization,location);
    }

    @Override
    public void createSRecord(String firstName, String lastName, String courseRegistered, String status, String statusDate) throws RemoteException {

        logger.write("calling server to createSRecord ");

    }

    @Override
    public Integer getRecordsCount() throws RemoteException {
        return null;
    }

    @Override
    public Boolean editRecord(String recordID, String fieldName, String newValue) throws RemoteException {
        return null;
    }
}
