package src.People;

import src.Constants;
import src.Constants.Locations;
// Use class "CenterManagerFactory" to make instance of this type
// for example CenterManagerFactory.createCenterManagerForMontreal() makes a CenterManager with its location in montreal
public abstract class CenterManager implements  Person {
    public Locations getLocationOfCenterManager() {
        return locationOfCenterManager;
    }

    private Locations locationOfCenterManager;
    private String Id;
    protected static int uniqueFourDigitNumber = 1000;



    public synchronized String generateId(){
       String id =  locationOfCenterManager.name() + uniqueFourDigitNumber;
       incrementFourDigitNumber();
       return id;
    }
    private synchronized void incrementFourDigitNumber(){
        uniqueFourDigitNumber++;
    }

    @Override
    public String getId() {
        return Id;
    }


    public void setId(String Id) {
        this.Id = Id;
    }

    public void setLocationOfCenterManager(Locations locationOfCenterManager) {
        this.locationOfCenterManager = locationOfCenterManager;
    }
    public void resetUniqueFourDidgitNumber(){
        uniqueFourDigitNumber = 1000;
    }
}
