package common;

import people.Student;
import java.util.*;



/**
 * Represents a student record
 */
public class StudentRecord extends Record {
	
	List<String> courseRegistered = new ArrayList<>();
	String status;
	String statusDate;
	
	public StudentRecord(String first, String last){
		super(first, last);
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void addCourse(String course) {
		courseRegistered.add(course);
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String etat) {
		status = etat;
	}
	
	public String getDate() {
		return this.statusDate;
	}
	
	public void setDate(String date) {
		statusDate = date;
	}

}
