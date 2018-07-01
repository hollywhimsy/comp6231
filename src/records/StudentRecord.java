package records;

import java.util.*;

public class StudentRecord extends Record
{
	private List<String> coursesRegistred = new ArrayList<>();
	private boolean status;
	private Date statusDate;

	public StudentRecord(String recordId, String firstName, String lastName, List<String> coursesRegistred, boolean status, Date statusDate)
	{
		super(recordId, firstName, lastName);
		this.coursesRegistred = coursesRegistred;
		this.status = status;
		this.statusDate = statusDate;
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
