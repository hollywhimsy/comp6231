package common;

import people.Teacher;

/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record {
	private String specialization;
	
	public TeacherRecord(String first, String last) {
		super(first, last);
	}
	
	public String getSpecialization() {
		return this.specialization;
	}
	
}
