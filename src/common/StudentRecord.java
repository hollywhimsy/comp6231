package src.common;

import src.People.Student;

import java.util.Date;
import java.util.List;

/**
 * Represents a student record
 */
public class StudentRecord implements Record {
	private String Id;
	protected static int uniqueFiveDigitNumber = 10000;

	public String generateId(){
		Id = "SR" + uniqueFiveDigitNumber;
		uniqueFiveDigitNumber++;
		return Id;
	}

	private Student student;

	public StudentRecord(){

	}
	public StudentRecord(Student student){
		this.student = student;
	}
	public StudentRecord(Student student, String Id){
		this.student = student;
		this.Id = Id;
	}


	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}


	@Override
	public String getId() {
		return Id;
	}

	@Override
	public void setId(String Id) {
		this.Id = Id;
	}
}
