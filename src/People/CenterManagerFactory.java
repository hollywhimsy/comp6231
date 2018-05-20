package src.People;

import src.Constants;

public class CenterManagerFactory {

    private static class CenterManagerForLocation extends CenterManager{
        CenterManagerForLocation(Constants.Locations location){
            this.setLocationOfCenterManager(location);
            this.setId(this.generateId());
        };
    }



    public static CenterManager createCenterManagerForMontreal(){
        return new CenterManagerForLocation(Constants.Locations.MTL);
    }
    public static CenterManager createCenterManagerForLaval(){
        return new CenterManagerForLocation(Constants.Locations.LVL);
    }
    public static CenterManager createCenterManagerForDollardDesOrmeaux(){
        return new CenterManagerForLocation(Constants.Locations.DDO);
    }
}
