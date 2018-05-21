package src.common;

import src.People.Teacher;

/**
 * Represents a teacher record
 */
public class TeacherRecord implements Record {
	private String Id;
	protected static int uniqueFiveDigitNumber = 10000;

	public String generateId(){
		Id = "TR" + uniqueFiveDigitNumber;
		uniqueFiveDigitNumber++;
		return Id;
	}
	public Teacher getTeacher() {
		return teacher;
	}

	private Teacher teacher;


	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String Id) {

	}
}
