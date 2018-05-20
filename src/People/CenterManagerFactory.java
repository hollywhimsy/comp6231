package src.People;

import src.Constants;

public class CenterManagerFactory {

    private class CenterManagerForLocation extends CenterManager{
        CenterManagerForLocation(Constants.Locations location){
            this.setLocationOfCenterManager(location);
            this.setId(this.generateId());
        };
    }



    public CenterManager createCenterManagerForMontreal(){
        return new CenterManagerForLocation(Constants.Locations.MTL);
    }
    public CenterManager createCenterManagerForLaval(){
        return new CenterManagerForLocation(Constants.Locations.LVL);
    }
    public CenterManager createCenterManagerForDollardDesOrmeaux(){
        return new CenterManagerForLocation(Constants.Locations.DDO);
    }
}
