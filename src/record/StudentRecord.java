package record;

import java.util.*;

/**
 * Represents a student record
 */
public class StudentRecord extends Record
{	
	private List<String> coursesRegistred = new ArrayList<>();
	private boolean status;
	private Date statusDate;
	
	public StudentRecord(String first, String last)
	{
		super(first, last);
	}

	public StudentRecord(String recordId, String firstName, String lastName, List<String> coursesRegistred, boolean status, Date statusDate)
	{
		super(recordId, firstName, lastName);
		this.coursesRegistred = coursesRegistred;
		this.status = status;
		this.statusDate = statusDate;
	}
	
	public Date getDate()
	{
		return this.statusDate;
	}

	public List<String> getCourseList()
	{
		return this.coursesRegistred;
	}

	public void addCourse(String course)
	{
		coursesRegistred.add(course);
	}

	public void setDate(Date date)
	{
		statusDate = date;
	}

	public List<String> getCoursesRegistred()
	{
		return coursesRegistred;
	}

	public void setCoursesRegistred(List<String> coursesRegistred)
	{
		this.coursesRegistred = coursesRegistred;
	}

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public Date getStatusDate()
	{
		return statusDate;
	}

	public void setStatusDate(Date statusDate)
	{
		this.statusDate = statusDate;
	}

}
