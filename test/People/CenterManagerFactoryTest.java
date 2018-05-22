package test.People;

import org.junit.Assert;
import org.junit.Test;
import common.Constants;
import people.CenterManager;
import people.CenterManagerFactory;

public class CenterManagerFactoryTest {

    @Test
    public void createCenterManagerForMontrealTest(){
        CenterManager manager = CenterManagerFactory.createCenterManagerForMontreal();
        Assert.assertEquals("Manager should have the problem location set", manager.getLocationOfCenterManager(),
                Constants.Locations.MTL);


    }

    @Test
    public void createCenterManagerForLaval(){
        CenterManager manager = CenterManagerFactory.createCenterManagerForLaval();
        Assert.assertEquals("Manager should have the problem location set", manager.getLocationOfCenterManager(),
                Constants.Locations.LVL);
    }

    @Test
    public void createCenterManagerForDollardDesOrmeaux(){
        CenterManager manager = CenterManagerFactory.createCenterManagerForDollardDesOrmeaux();
        Assert.assertEquals("Manager should have the problem location set", manager.getLocationOfCenterManager(),
                Constants.Locations.DDO);
    }
}
