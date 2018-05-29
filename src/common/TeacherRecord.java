package common;

import java.util.*;


/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record {
	private String address;
	private String phone;
	private String specialization;
	private String location;
	private ArrayList<String> full_record = new ArrayList<>();

	public TeacherRecord(String first, String last) {
		super(first, last);

	}

	public TeacherRecord(String first, String last, String spec) {
		super(first, last);
		this.specialization = spec;
	}
	
	public String getSpecialization() {
		return this.specialization;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setAddress(String addr) {
		this.address = addr;
	}
	
	public void setPhone(String tel) {
		this.phone = tel;
	}
	
	public void setLocation(String loc) {
		if(!loc.toLowerCase().equals("mtl")
				&& !loc.toLowerCase().equals("lvl")
				&& !loc.toLowerCase().equals("ddo")) {
			System.err.println("This location does not exist!!");
		}
		else {
			this.location = loc;
		}
	}
	
	public ArrayList<String> build_record(){
//		full_record.add(this.firstName);
//		full_record.add(this.lastName);
//		full_record.add(this.address);
//		full_record.add(this.phone);
//		full_record.add(this.specialization);
//		full_record.add(this.location);
		return full_record;
	}
	
}
