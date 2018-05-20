package src.People;

import src.Constants;
import src.Constants.Locations;
// Use class "CenterManagerFactory" to make instance of this type
public abstract class CenterManager implements  Person {
    private Locations locationOfCenterManager;
    private String Id;
    private static int uniqueFourDigitNumber = 1000;



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
}
