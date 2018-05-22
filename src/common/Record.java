package common;



/**
 * Represents a record
 * This is an abstract class as it is inherited by TeacherRecord and StudentRecord classes 
 */
public abstract class Record {
	String firstName;
	String lastName;
	
	
	public Record(String first, String last) {
		firstName = first;
		lastName = last;
	}
	
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}

}
