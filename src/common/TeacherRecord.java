package common;

/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record {

	private String address ;
	
	private String phone ;
	
	private String specialization;
	
	private String location;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public TeacherRecord(String firstName, String lastName) {
		super(firstName, lastName);
	}

}
