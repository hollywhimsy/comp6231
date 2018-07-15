package record;

/**
 * Represents a record This is an abstract class as it is inherited by
 * TeacherRecord and StudentRecord classes
 */
public abstract class Record
{
	private String recordId;
	private String firstName;
	private String lastName;

	public Record(String first, String last)
	{
		firstName = first;
		lastName = last;
	}

	public Record(String recordId, String firstName, String lastName)
	{
		this.recordId = recordId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public String getRecordId()
	{
		return recordId;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (recordId == null)
		{
			if (other.recordId != null)
				return false;
		} else if (!recordId.equals(other.recordId))
			return false;

		return true;
	}
}
