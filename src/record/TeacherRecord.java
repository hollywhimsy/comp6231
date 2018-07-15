package record;

import java.util.*;

/**
 * Represents a teacher record
 */
public class TeacherRecord extends Record
{

	private String address;
	private Integer phone;
	private List<String> specialization;
	private String location;

	public TeacherRecord(String recordId, String firstName, String lastName, String address, Integer phoneNumber, List<String> specilization,
			String location)
	{
		super(recordId, firstName, lastName);
		this.address = address;
		this.phone = phoneNumber;
		this.specialization = specilization;
		this.location = location;
	}

	public List<String> getSpecialization()
	{
		return this.specialization;
	}

	public String getLocation()
	{
		return this.location;
	}

	public void setAddress(String addr)
	{
		this.address = addr;
	}

	public void setPhoneNumber(Integer tel)
	{
		this.phone = tel;
	}

	public String getAddress()
	{
		return address;
	}

	public Integer getPhone()
	{
		return phone;
	}

	public void setLocation(String loc)
	{
		if (!loc.toLowerCase().equals("mtl")
				&& !loc.toLowerCase().equals("lvl")
				&& !loc.toLowerCase().equals("ddo"))
		{
			System.err.println("This location does not exist!!");
		} else
		{
			this.location = loc;
		}
	}	
}
