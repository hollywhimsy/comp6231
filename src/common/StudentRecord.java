package src.common;

import java.util.Date;
import java.util.List;

/**
 * Represents a student record
 */
public class StudentRecord extends Record {

	
	private List<String> courses ;
	
	private String status;
	
	private Date date;
	
	
	
	public List<String> getCourses() {
		return courses;
	}



	public void setCourses(List<String> courses) {
		this.courses = courses;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public StudentRecord(String firstName, String lastName) {
		super(firstName, lastName);
	}

}
