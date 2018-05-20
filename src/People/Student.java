package src.People;

import src.Constants;
import src.Constants.StudentStatus;

import java.util.Date;
import java.util.List;

public class Student {
    private List<String> courses ;
    private String firstName;
    private StudentStatus status;
    private String lastName;
    private Date date;



    public StudentStatus getStatus() {
        return status;
    }

    public String getLastName() {
        return lastName;
    }

    public Student() {
    }

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(List<String> courses, String firstName, StudentStatus status, String lastName, Date date) {
        this.courses = courses;
        this.firstName = firstName;
        this.status = status;
        this.lastName = lastName;
        this.date = date;
    }

    public List<String> getCourses() {
        return courses;
    }



    public void setCourses(List<String> courses) {
        this.courses = courses;
    }




    public Date getDate() {
        return date;
    }



    public void setDate(Date date) {
        this.date = date;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }


}
