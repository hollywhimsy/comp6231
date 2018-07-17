package manager;

import java.util.Date;
import java.util.List;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import common.Constants;
import common.Infrastucture;
import common.Logger;
import frontEnd.FrontEnd;
import frontEnd.FrontEndHelper;

public class ManagerClientCORBA extends Thread
{
	private String[] configuration = { "-ORBInitialPort", "1050", "-ORBInitialHost", "localhost" };
	private ORB orb;
	private org.omg.CORBA.Object objRef;
	private NamingContextExt ncRef;
	private FrontEnd recordManager;
	private boolean corbaInitiation = true;
	private String managerId;
	private String city;
	private Logger logger;
	private Logger operationResultLogger;
	private String methodToCall;
	private String firstName;
	private String lastName;
	private String address;
	private Integer phoneNumber;
	private List<String> specilization;
	private List<String> coursesRegistred;
	private boolean status;
	private Date statusDate;
	private String recordId;
	private String fieldName;
	private Object newValue;
	private String remoteCenterServerName;

	// Constructor (default: no new thread)
	public ManagerClientCORBA(String managerId)
	{
		this.managerId = managerId;
		initialize();
	}

	// Constructor1
	public ManagerClientCORBA(String methodToCall, String managerId)
	{
		this.managerId = managerId;
		this.methodToCall = methodToCall;
		initialize();
	}

	// Constructor2
	public ManagerClientCORBA(String methodToCall, String managerId, String firstName, String lastName, String address,
			Integer phoneNumber, List<String> specilization)
	{
		this.managerId = managerId;
		this.methodToCall = methodToCall;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.specilization = specilization;
		initialize();
	}

	// Constructor3
	public ManagerClientCORBA(String methodToCall, String managerId, String firstName, String lastName,
			List<String> coursesRegistred, boolean status, Date statusDate)
	{
		this.managerId = managerId;
		this.methodToCall = methodToCall;
		this.firstName = firstName;
		this.lastName = lastName;
		this.coursesRegistred = coursesRegistred;
		this.status = status;
		this.statusDate = statusDate;
		initialize();
	}

	// Constructor4
	public ManagerClientCORBA(String methodToCall, String managerId, String recordId, String fieldName, Object newValue)
	{
		this.managerId = managerId;
		this.methodToCall = methodToCall;
		this.fieldName = fieldName;
		this.newValue = newValue;
		this.recordId = recordId;
		initialize();
	}

	// Constructor5
	public ManagerClientCORBA(String methodToCall, String managerId, String recordId, String remoteCenterServerName)
	{
		this.managerId = managerId;
		this.methodToCall = methodToCall;
		this.remoteCenterServerName = remoteCenterServerName;
		this.recordId = recordId;
		initialize();
	}

	// Thread Method
	public void run()
	{
		if (methodToCall.equals(Constants.RemoteProcedures.CreateTeacher.name()))
		{
			callCreateTRecord();
		}

		if (methodToCall.equals(Constants.RemoteProcedures.CreateStudent.name()))
		{
			callCreateSRecord();
		}

		if (methodToCall.equals(Constants.RemoteProcedures.GetCounts.name()))
		{
			callGetRecordCounts();
		}

		if (methodToCall.equals(Constants.RemoteProcedures.EditRecords.name()))
		{
			callEditRecord();
		}

		if (methodToCall.equals("TransferRecord"))
		{
			callTransferRecord();
		}
	}

	public boolean callCreateTRecord()
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return false;
		}

		String spec = "";
		String spliter = "";
		for (int i = 0; i < specilization.size(); i++)
		{
			// form the acceptable format by remote CORBA server
			spec = spec + spliter + specilization.get(i);
			spliter = ",";
		}
		if (recordManager.createTRecord(firstName, lastName, address, phoneNumber.toString(), spec, city, managerId))
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()] {Thread ID: " + this.getId()
					+ "}: Teacher record created");
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()] {Thread ID: "
					+ this.getId() + "}: Teacher record created");
			return true;
		}

		logger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()]: callCreateTRecord called on " + city
				+ " server and failed");
		return false;
	}

	public boolean callCreateSRecord()
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callCreateSRecord()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callCreateSRecord()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return false;
		}

		String courses = "";
		String spliter = "";
		for (int i = 0; i < coursesRegistred.size(); i++)
		{
			// form the acceptable format by remote CORBA server
			courses = courses + spliter + coursesRegistred.get(i);
			spliter = ",";
		}
		if (recordManager.createSRecord(firstName, lastName, courses, status, statusDate.toString(), managerId))
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callCreateSRecord()]: callCreateSRecord called on " + city
					+ " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callCreateSRecord()] {Thread ID: "
					+ this.getId() + "}: Student record created");
			return true;
		}

		logger.logToFile(managerId + "[ManagerClientCORBA.callCreateTRecord()]: callCreateSRecord called on " + city
				+ " server and failed");
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean callEditRecord()
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callEditRecord()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callEditRecord()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return false;
		}

		String newVal;
		switch (fieldName)
		{
		case "coursesRegistred":
			newVal = "";
			String spliter = "";
			for (int i = 0; i < ((List<String>) newValue).size(); i++)
			{
				// form the acceptable format by remote CORBA server
				newVal = newVal + spliter + ((List<String>) newValue).get(i);
				spliter = ",";
			}
			break;
		case "status":
			if ((boolean) newValue)
				newVal = "true";
			else
				newVal = "false";
			break;
		case "statusDate":
			newVal = ((Date) newValue).toString();
			break;
		case "phoneNumber":
			newVal = ((Integer) newValue).toString();
			break;
		default:
			newVal = (String) newValue;
			break;
		}

		if (recordManager.editRecord(recordId, fieldName, newVal, managerId))
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callEditRecord()]: callEditRecord called on " + city
					+ " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callEditRecord()] {Thread ID: "
					+ this.getId() + "}: Edit record performed successfully");
			return true;
		}

		logger.logToFile(managerId + "[ManagerClientCORBA.callEditRecord()]: callEditRecord called on " + city
				+ " server and failed");
		return false;
	}

	public String callGetRecordCounts()
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callGetRecordCounts()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callGetRecordCounts()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return null;
		}

		String result = recordManager.getRecordCounts(managerId);
		if (result != null)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callGetRecordCounts()]: callGetRecordCounts called on "
					+ city + " server and performed successfully");
			operationResultLogger.logToFile(managerId + "[Manager.getRecordCounts()] {Thread ID: " + this.getId()
					+ "}: Records Count: " + result);
			return result;
		}

		logger.logToFile(managerId + "[Manager.callGetRecordCounts()]: callGetRecordCounts called on " + city
				+ " server and failed");
		return null;
	}

	public boolean callRecordExist(String recordId)
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callRecordExist()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callRecordExist()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return false;
		}

		return recordManager.recordExist(recordId, managerId);
	}

	public boolean callTransferRecord()
	{
		if (!corbaInitiation)
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: " + this.getId()
					+ "}: Error! CORBA Initialization failed for the city: " + city);
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: "
					+ this.getId() + "}: Error! CORBA Initialization failed for the city: " + city);
			return false;
		}

		//System.out.println(managerId + " " + recordId + " " + remoteCenterServerName.toUpperCase().trim());
		if (recordManager.transferRecord(managerId, recordId, remoteCenterServerName.toUpperCase().trim()))
		{
			logger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: " + this.getId()
					+ "}: Record is trasfered successfully");
			operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: "
					+ this.getId() + "}: Record is trasfered successfully");
			return true;
		}

		logger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: " + this.getId()
				+ "}: Record transferring failed");
		operationResultLogger.logToFile(managerId + "[ManagerClientCORBA.callTransferRecord()] {Thread ID: "
				+ this.getId() + "}: Record transferring failed");

		return false;
	}

	private void initialize()
	{
		logger = new Logger("MNG_" + managerId.toUpperCase().trim() + ".log");
		operationResultLogger = new Logger("Result.log");

		if (isManagerIdFormatCorrect(managerId))
		{
			city = managerId.substring(0, 3).toUpperCase();
		} else
		{
			logger.logToFile("[ManagerClientCORBA Constructor]: ERROR! Manager ID is not valid");
			corbaInitiation = false;
		}

		// get the root naming context
		try
		{
			// create and initialize the ORB
			orb = ORB.init(configuration, null);

			objRef = orb.resolve_initial_references("NameService");
			ncRef = NamingContextExtHelper.narrow(objRef);
			String name1 = "FrontEnd"; // resolve the Object Reference in Naming
			recordManager = FrontEndHelper.narrow(ncRef.resolve_str(name1));

		} catch (InvalidName e1)
		{
			logger.logToFile("[ManagerClientCORBA Constructor]: Error! Invalid Context Name");
			corbaInitiation = false;			
		} catch (NotFound e)
		{
			logger.logToFile("[ManagerClientCORBA Constructor]: Error! Context NotFound");
			corbaInitiation = false;			
		} catch (CannotProceed e)
		{
			logger.logToFile("[ManagerClientCORBA Constructor]: Error! CannotProceed");
			corbaInitiation = false;			
		} catch (org.omg.CosNaming.NamingContextPackage.InvalidName e)
		{
			logger.logToFile("[ManagerClientCORBA Constructor]: Error! org.omg.CosNaming.NamingContextPackage.InvalidName");
			corbaInitiation = false;			
		}
	}

	private boolean isManagerIdFormatCorrect(String id)
	{
		if (id == null)
		{
			return false;
		}

		if (id.length() != 7)
		{
			return false;
		}

		String srvName = id.substring(0, 3).toUpperCase();
		if (!Infrastucture.isSystemServerName(srvName))
		{
			return false;
		}

		if (!(id.substring(3, 4).chars().allMatch(Character::isDigit)))
		{
			return false;
		}

		return true;
	}

	public String getManagerId()
	{
		return managerId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((managerId == null) ? 0 : managerId.hashCode());
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
		ManagerClientCORBA other = (ManagerClientCORBA) obj;
		if (managerId == null)
		{
			if (other.managerId != null)
				return false;
		} else if (!managerId.equals(other.managerId))
			return false;
		return true;
	}
}