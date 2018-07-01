package records;

import java.util.*;

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

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public Integer getPhone()
	{
		return phone;
	}

	public void setPhone(Integer phone)
	{
		this.phone = phone;
	}

	public List<String> getSpecialization()
	{
		return specialization;
	}

	public void setSpecialization(List<String> specialization)
	{
		this.specialization = specialization;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
}
