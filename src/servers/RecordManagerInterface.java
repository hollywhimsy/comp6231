package servers;

public interface RecordManagerInterface
{
	public String createTRecord(String firstName, String lastName, String address, Integer phoneNumber, String specialization, String location, 
			String managerId);

	public String createSRecord(String firstName, String lastName, String coursesRegistred, boolean status, String statusDate, String managerId);

	public String getRecordsCount(String managerId);

	public String editRecord(String recordId, String fieldName, String newValue, String managerId);

	public boolean recordExist(String recordId, String managerId);

	public String transferRecord(String managerId, String recordId, String remoteCenterServerName);
}
