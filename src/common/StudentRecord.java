package common;

import people.Student;
import java.util.*;



/**
 * Represents a student record
 */
public class StudentRecord extends Record {
	
	private List<String> courseRegistered = new ArrayList<>();
	private String status;
	private String statusDate;
	private ArrayList<String> full_record = new ArrayList<>();
	
	public StudentRecord(String first, String last){
		super(first, last);
	}
	
	public String getDate() {
		return this.statusDate;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public List<String> getCourseList(){
		return this.courseRegistered;
	}
	
	public void addCourse(String course) {
		courseRegistered.add(course);
	}

	public void setStatus(String etat) {
		status = etat;
	}
	
	public void setDate(String date) {
		statusDate = date;
	}
	
	public ArrayList<String> build_record() {
		full_record.add(this.firstName);
		full_record.add(this.lastName);
		full_record.add(this.courseRegistered.toString());
		full_record.add(this.status);
		full_record.add(this.statusDate);
		return full_record;
	}

}
