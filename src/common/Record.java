package src.common;
/**
 * Represents a record
 * This is an abstract class as it is inherited by TeacherRecord and StudentRecord classes 
 */
public abstract class Record {
	
	String firstName;
	
	String lastName;

	public Record(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
