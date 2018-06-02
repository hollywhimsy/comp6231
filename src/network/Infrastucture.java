package network;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import common.Constants;

import java.util.*;

/**
 * Infrastructure class
 * Responsible for all settings of the servers in the system
 */
public class Infrastucture {


    public static Integer getServerPort(String serverName) {
        return serverPorts.get(serverName);
    }

    public static String getServerHost(String serverName) {
        return serverHosts.get(serverName);
    }

    public static Integer getServerPortUDP(String serverName) {
        return serverPortsUDP.get(serverName);
    }

    public static Boolean isSystemServerName(String serverName)
    {
        return serverHosts.containsKey(serverName);
    }

    public static List<Integer> getOtherServersUDPPorts(String serverName) {
        List<Integer> result = new ArrayList<Integer>();
        for (String key : serverPortsUDP.keySet()) {
            if (!key.equals(serverName))
                result.add(serverPortsUDP.get(key));
        }
        return result;
    }


    private static final Map<String, String> serverHosts = initHostsMap();

    private static Map<String, String> initHostsMap() {
        Map<String, String> map = new HashMap<>();

        map.put(Constants.Locations.MTL.name(), "127.0.0.1");
        map.put(Constants.Locations.LVL.name(), "127.0.0.1");
        map.put(Constants.Locations.DDO.name(), "127.0.0.1");

        return Collections.unmodifiableMap(map);
    }


    private static final Map<String, Integer> serverPorts = initPortsMap();

    private static Map<String, Integer> initPortsMap() {

        Map<String, Integer> map = new HashMap<>();
        map.put(Constants.Locations.MTL.name(), 2270);
        map.put(Constants.Locations.LVL.name(), 2280);
        map.put(Constants.Locations.DDO.name(), 2290);

        return Collections.unmodifiableMap(map);
    }

    private static final Map<String, Integer> serverPortsUDP = initPortsUDPMap();

    private static Map<String, Integer> initPortsUDPMap() {

        Map<String, Integer> map = new HashMap<>();
        map.put(Constants.Locations.MTL.name(), 3270);
        map.put(Constants.Locations.LVL.name(), 3280);
        map.put(Constants.Locations.DDO.name(), 3290);

        return Collections.unmodifiableMap(map);
    }

}
