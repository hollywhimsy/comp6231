package common;

import java.util.*;


/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record {
   
	private static final long serialVersionUID = 1L;
	private String address;
    private Integer phone;
    private List<String> specialization;
    private String location;
    private ArrayList<String> full_record = new ArrayList<>();

    public TeacherRecord(String first, String last) {
        super(first, last);

    }

    public TeacherRecord(String first, String last, String spec) {
        super(first, last);

    }

    public TeacherRecord(String recordId,
                         String firstName,
                         String lastName,
                         String address,
                         Integer phoneNumber,
                         List<String> specilization,
                         String location) {
        super(recordId, firstName, lastName);
        this.address = address;
        this.phone = phoneNumber;
        this.specialization = specilization;
        this.location = location;
    }

    public List<String> getSpecialization() {
        return this.specialization;
    }

    public String getLocation() {
        return this.location;
    }

    public void setAddress(String addr) {
        this.address = addr;
    }

    public void setPhoneNumber(Integer tel) {
        this.phone = tel;
    }

    public String getAddress() {
		return address;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setLocation(String loc) {
        if (!loc.toLowerCase().equals("mtl")
                && !loc.toLowerCase().equals("lvl")
                && !loc.toLowerCase().equals("ddo")) {
            System.err.println("This location does not exist!!");
        } else {
            this.location = loc;
        }
    }

    public ArrayList<String> build_record() {
//		full_record.add(this.firstName);
//		full_record.add(this.lastName);
//		full_record.add(this.address);
//		full_record.add(this.phone);
//		full_record.add(this.specialization);
//		full_record.add(this.location);
        return full_record;
    }

}
