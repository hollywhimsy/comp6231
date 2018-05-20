package src.People;

import src.Constants;

public class Student implements Person {
    private final String firstName;
    private final String lastName;
    private String address;
    private String phone;
    private String specialization;
    private Constants.Locations location;

    private String Id;



    public Student(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Student(String firstName, String lastName, String address, String phone, String specialization, Constants.Locations location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.location = location;
    }


    @Override
    public String getId() {
        return Id;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setLocation(Constants.Locations location) {
        this.location = location;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Constants.Locations getLocation() {
        return location;
    }

}
