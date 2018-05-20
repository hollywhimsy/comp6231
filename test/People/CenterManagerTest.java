package test.People;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.Constants;
import src.People.CenterManager;

public class CenterManagerTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private class ConcreteCenterManager extends CenterManager{
        public int getUniqueFourDigitNumber(){
            return uniqueFourDigitNumber;
        }
    }

    private Constants.Locations getLocationFromIdString (String id){
        String locationAbbreviation  = id.substring(0, 3);
        return Constants.Locations.valueOf(locationAbbreviation); //returns MTL, DDO, or LVL as an enum type Constants.Locations
    }
    private String getIdNumberFromIdString (String id){
        return (String) id.substring(3); //returns the 4 or 5 digit ID number in the ID
    }

    @Test
    public void generateId() {
        ConcreteCenterManager manager = new ConcreteCenterManager();
        Constants.Locations locationOfManager = Constants.Locations.MTL;
        manager.setLocationOfCenterManager(locationOfManager);

        manager.setId(manager.generateId());
        Assert.assertEquals(getLocationFromIdString(manager.getId()), locationOfManager);
        Assert.assertEquals(getIdNumberFromIdString(manager.getId()), ""+ (manager.getUniqueFourDigitNumber()-1));


        ConcreteCenterManager managerLaval = new ConcreteCenterManager();
        Constants.Locations locationOfManagerLaval = Constants.Locations.LVL;
        managerLaval.setLocationOfCenterManager(locationOfManagerLaval);
        managerLaval.setId(managerLaval.generateId());

        Assert.assertEquals(getLocationFromIdString(managerLaval.getId()), locationOfManagerLaval);
        Assert.assertEquals(getIdNumberFromIdString(managerLaval.getId()), ""+ (managerLaval.getUniqueFourDigitNumber()-1));


    }

    @Test
    public void getId() {
    }

    @Test
    public void setId() {
    }

    @Test
    public void setLocationOfCenterManager() {
    }
}