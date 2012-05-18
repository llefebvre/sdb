package com.datagardens.nq.sdb.commons.model;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datagardens.nq.sdb.commons.model.nio.ServerRequest;
import com.datagardens.nq.sdb.commons.model.nio.ServerSession;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;

public final class NQDataModel
{
	
	private static Map<Integer, Employee> employees;
	
	private static NQDataModel instance;
	public synchronized static NQDataModel initialize(String user, String password)
	throws ModelException
	{
		if(instance != null)
		{
			throw new ModelException("Model already initialized.");	
		}
		
		NQDataModel model = new NQDataModel();
		model.connect();
		model.login(new SDBUser(user, password));
		instance = model;
		
		employees = getEmployeesList();
		
		return instance;
	}
	
	public synchronized static SDBUser getClientUser()
	{
		return instance.getClientUser_property();
	}
	
	public synchronized static Job findJobById(String jobId)
	throws ModelException
	{
		checkModelIsReady();
		return instance.findJob(jobId);
	}
	
	public synchronized static List<Job> getAllJobs()
	throws ModelException
	{
		checkModelIsReady();
		return instance.getJobs();
	}
	
	public static void editUser(SDBUser user) 
	throws ModelException 
	{
		checkModelIsReady();
		instance.editUser_command(user);
	}
	
	public synchronized static List<SDBUser> getUsersList()
	throws ModelException
	{
		checkModelIsReady();
		return instance.getUsersLiist_command();
	}
	
	public synchronized static void userAdd(SDBUser user)
	throws ModelException
	{
		checkModelIsReady();
		instance.userAdd_command(user);
		
	}
	
	public synchronized static void userDelete(SDBUser user)
	throws ModelException
	{
		checkModelIsReady();
		instance.userDelete_command(user);			
	}
	
	public static void saveSawSheet(SawSheet sawSheet) 
	throws ModelException 
	{
		checkModelIsReady();
		instance.saveSawSheet_command(sawSheet);
	}
	
	public static ShiftLogSheet getShiftLog(Operation operation)
	throws ModelException
	{
		checkModelIsReady();
		return instance.getShiftLog_command(operation);
	}
	
	/**
	 * Retrieves the Cuts log for the specified Saw Sheet shift.
	 * @param job
	 * @param shiftIndex
	 * @return
	 * @throws ModelException
	 */
	public static Map<Integer, IndividualCut> getCutsForSawSheetShift(SawSheet sheet, int shiftIndex)
	throws ModelException
	{
		checkModelIsReady();
		return instance.getCutsForSawSheetShift_command(sheet, shiftIndex);
	}
	
	public static Map<Integer, ShiftLogLine> getCutsForShiftLogShift(ShiftLogSheet sheet, int shiftIndex)
	throws ModelException
	{
		checkModelIsReady();
		return instance.getCutsForShiftLogShift_command(sheet, shiftIndex);
	}
	
	public static ShiftLogDetail getShiftLogDetials(
			ShiftLogSheet selectedSheet,int shiftIndex) 
	throws ModelException 
	{
		checkModelIsReady();
		return instance.getShiftLogQCData_command(selectedSheet, shiftIndex);
	}
	
	/**
	 * Modifies an shift log in the database and 
	 * creates new lines if they don't exist
	 * @param sheet
	 * @throws ModelException
	 */
	public static void saveShiftLogSheet(ShiftLogSheet sheet)
	throws ModelException
	{
		checkModelIsReady();
		instance.saveShiftLogSheet_command(sheet);
	}
	
	public static void saveShiftLogDetail(ShiftLogDetail detail)
	throws ModelException
	{
		checkModelIsReady();
		instance.saveShiftLogDetail_command(detail);
	}
	
	public static void saveShiftLogLine(ShiftLogLine line)
	throws ModelException
	{
		checkModelIsReady();
		instance.saveShiftLogLine_command(line);
	}
	
	public static OpTallySheet getOperationTallyForJob(ShiftLogSheet sheet) 
			throws ModelException
	{
		checkModelIsReady();
		return instance.getOperationTallyForJob_command(sheet);
	}
	
	public static Employee findEmployeeById(Integer employeeNumber) 
	throws ModelException
	{
		checkModelIsReady();
		return employees.get(employeeNumber);
	}
	
	public static Map<Integer, Employee> getEmployeesList() 
	throws ModelException
	{
		checkModelIsReady();
		if(employees == null)
		{
			employees = new HashMap<Integer, Employee>();
			
			for(Employee emp : instance.getEmployeesList_command())
			{
				employees.put(emp.getEmployeeNumber(), emp);
			}
		}
		
		return employees;
		
	}
	
	////////////////////////////////////////////////
	
	
	private static void checkModelIsReady()
	throws ModelException 
	{
		if(instance == null)
		{
			throw new ModelException("Model not initialized.");
		}
	}
	
	public static void disconnect()
	{
		try
		{
			instance.disconnect_command();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	///////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////
	
	private ServerSession session;
	private SDBUser clientUser;
	
	protected NQDataModel()
	{
		
	}
	
	private SDBUser getClientUser_property() {
		return clientUser;
	}
	
	private void connect()
	throws ModelException
	{
		session = new ServerSession();
		
		try
		{
			session.connect(new InetSocketAddress("localhost", 1234),
					false);
		}
		catch(Exception e)
		{
			throw new ModelException("Cannot initiate server session.");
		}
	}
	
	private void login(SDBUser clientUser)
	throws ModelException
	{
		ServerRequest request = session.login( clientUser.getUsername(), clientUser.getPassword());
		request.waitForRequestToComplete();
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
		else
		{
			String roleString = request.getResponseMessage().getBody().getMessage().trim();
			clientUser.setRole(SDBRole.parse(roleString));
		}
		
		this.clientUser = clientUser;
	}
	
	
	private List<Job> getJobs()
	{
		List<Job> jobs = new ArrayList<Job>();
		
		ServerRequest request = session.jobListRequest();
		request.waitForRequestToComplete();
		
		for(Object object : request.getResult())
		{
			if(object instanceof Job)
			{
				jobs.add((Job) object);
			}
		}
		
		return jobs;
	}
	
	private Job findJob(String id)
	{
		ServerRequest request = session.findJobById(id);
		request.waitForRequestToComplete();
		
		for(Object obj :  request.getResult())
		{
			return (Job) obj;
		}
		
		return null;
	}
	
	private List<SDBUser> getUsersLiist_command() 
	{
		List<SDBUser> users = new ArrayList<SDBUser>();
		
		if(getClientUser().hasLevel(SDBRole.ROOT))
		{
			ServerRequest request = session.generateServerRequest(Command.return_users);
			request.waitForRequestToComplete();
			
			for (Object obj : request.getResult())
			{
				if(obj instanceof SDBUser)
				{
					users.add((SDBUser) obj);
				}
			}
		}
		
		return users;
	}
	
	private Map<Integer, IndividualCut> getCutsForSawSheetShift_command(SawSheet sawSheet, int shiftIndex)
	throws ModelException
	{
		Map<Integer, IndividualCut> cuts = new HashMap<Integer, IndividualCut>();
		
		MessageParameters parameters = new MessageParameters(Integer.class);
		parameters.add(sawSheet.getSawSheetNumber());
		parameters.add(shiftIndex);
		
		ServerRequest request = session.generateServerRequest(Command.saw_sheet_cuts, parameters);
		request.waitForRequestToComplete();
		
		if(request.getStatus() == MessageStatus.OK )
		{
			if(request.getResult() != null && 
					request.getResult().get(0) != null)
			{
				for(Object obj : request.getResult())
				{
					if(obj instanceof IndividualCut)
					{
						IndividualCut cut = (IndividualCut) obj;
						cuts.put(cut.getIndividualCutNumber(), cut);
					}
				}
			}
		}
		else
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		return cuts;
	}
	
	private Map<Integer, ShiftLogLine> getCutsForShiftLogShift_command(ShiftLogSheet sheet, int shiftIndex)
	throws ModelException
	{
		Map<Integer, ShiftLogLine> cuts = new HashMap<Integer, ShiftLogLine>();
		
		MessageParameters parameters = new MessageParameters(Integer.class);
		parameters.add(sheet.getSheetNumber());
		parameters.add(shiftIndex);
		
		ServerRequest request =
				session.generateServerRequest(Command.shift_log_sheet_cuts, parameters);
		request.waitForRequestToComplete();
		
		if(request.getStatus() == MessageStatus.OK )
		{
			if(request.getResult() != null && 
					request.getResult().get(0) != null)
			{
				for(Object obj : request.getResult())
				{
					if(obj instanceof ShiftLogLine)
					{
						ShiftLogLine cut = (ShiftLogLine) obj;
						cuts.put(cut.getShiftLogLineNumber(), cut);
					}
				}
			}
		}
		else
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		return cuts;
	}
	
	private ShiftLogDetail getShiftLogQCData_command(ShiftLogSheet sheet, int shiftIndex)
	throws ModelException
	{
		MessageParameters parameters = new MessageParameters(Integer.class);
		parameters.add(sheet.getSheetNumber());
		parameters.add(shiftIndex);
		
		
		ServerRequest request =
				session.generateServerRequest(Command.shift_log_sheet_details, parameters);
		request.waitForRequestToComplete();
		
		if(request.getStatus() == MessageStatus.OK )
		{
			if(request.getResult() != null && 
					request.getResult().get(0) != null)
			{
				return (ShiftLogDetail) request.getResult().get(0);
			}
		}
		
		throw new ModelException(request.getErrorMessage());
	}
	
	private List<IndividualCut> getSawSheetCuts(int sawSheetNumber)
	throws ModelException
	{
		
		List<IndividualCut> cuts = new ArrayList<IndividualCut>();
		ServerRequest request = session.generateServerRequest(Command.saw_sheet_cuts, sawSheetNumber + "");
		request.waitForRequestToComplete();
		if(request.getStatus() == MessageStatus.OK)
		{
			if(request.getResult().get(0) != null)
			{
				for(Object obj : request.getResult())
				{
					if(obj instanceof IndividualCut)
					{
						cuts.add((IndividualCut) obj);
					}
				}
			}
			else
			{
				throw new ModelException("Saw Sheet cannot be updated");
			}
		}
		else
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		return cuts;
	}
	
	private void saveSawSheet_command(SawSheet sawSheet) 
	throws ModelException
	{
		MessageParameters param = new MessageParameters(SawSheet.class);
		param.add(sawSheet);
		
		ServerRequest request = session.generateServerRequest(Command.saw_sheet_save, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private ShiftLogSheet getShiftLog_command(Operation operation)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(Operation.class);
		param.add(operation);
		
		ServerRequest request = session.generateServerRequest(Command.shift_log, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		return (ShiftLogSheet)request.getResult().get(0); 
	}
	
	
	private void saveShiftLogSheet_command(ShiftLogSheet sheet) throws ModelException
	{
		MessageParameters param = new MessageParameters(ShiftLogSheet.class);
		param.add(sheet);
	
		ServerRequest request = session.generateServerRequest(Command.shift_log_save, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private void saveShiftLogDetail_command(ShiftLogDetail detail)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(ShiftLogDetail.class);
		param.add(detail);
	
		ServerRequest request = session.generateServerRequest(Command.save_shift_log_detail, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private void saveShiftLogLine_command(ShiftLogLine line)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(ShiftLogLine.class);
		param.add(line);
		
		ServerRequest request = session.generateServerRequest(Command.shift_log_save_line, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private OpTallySheet getOperationTallyForJob_command(ShiftLogSheet sheet) 
			throws ModelException
	{
		MessageParameters params = new MessageParameters(String.class);
		params.add(sheet.getJobId());
		params.add(sheet.getOperationNumber()+"");
		
		List<Object> result = runRequest(Command.operation_tally_sheet, params);
		
		if(result.size() > 0)
		{
			return (OpTallySheet) result.get(0);
		}
		
		throw new ModelException("Cannot retrieve tally sheet for operation " + sheet.getOperationNumber());
	}
	
	private List<Object> runRequest(Command command, MessageParameters params) 
			throws ModelException
	{
		ServerRequest request = session.generateServerRequest(command, params);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		return request.getResult() == null? new ArrayList<Object>() : 
			request.getResult();
	}
	
	
	private void userAdd_command(SDBUser user)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(SDBUser.class);
		param.add(user);
		
		ServerRequest request = session.generateServerRequest(Command.add_user, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private void userDelete_command(SDBUser user)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(SDBUser.class);
		param.add(user);
		
		ServerRequest request = session.generateServerRequest(Command.delete_user, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	private void editUser_command(SDBUser user)
	throws ModelException
	{
		MessageParameters param = new MessageParameters(SDBUser.class);
		param.add(user);
		
		ServerRequest request = session.generateServerRequest(Command.modify_user, param);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
	}
	
	public List<Employee> getEmployeesList_command()
	throws ModelException
	{
		List<Employee> list = new ArrayList<Employee>();
		ServerRequest request = session.generateServerRequest(Command.employee_list);
		request.waitForRequestToComplete();
		
		if(request.getStatus() != MessageStatus.OK)
		{
			throw new ModelException(request.getErrorMessage());
		}
		
		for(Object obj : request.getResult())
		{
			if(obj instanceof Employee)
			{
				list.add((Employee) obj);
			}
		}
		
		return list;
	}
	
	private void disconnect_command()
	{
		session.logout();
	}	
}
