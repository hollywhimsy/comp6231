package common;

import people.Teacher;

/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record {
	private String specialization;
	private String location;
	public TeacherRecord(String first, String last) {
		super(first, last);
	}
	
	public String getSpecialization() {
		return this.specialization;
	}
	
	public String getLocation() {
		return this.location;
	}
	
}
