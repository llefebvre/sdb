package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.Employee;
import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.JobStatsOperation;
import com.datagardens.nq.sdb.commons.model.JobStatsShiftLogLine;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.OperatorStats;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogDetail;
import com.datagardens.nq.sdb.commons.model.ShiftLogLine;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;
import com.datagardens.nq.sdb.commons.model.WorkpieceStatus;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageProtocol;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.jdbc.Database;
import com.datagardens.nq.sdb.server.jdbc.Row;

public abstract class CommandHandler 
{
	protected static Database database;
	
	public static void setDatabase(Database database) {
		CommandHandler.database = database;
	}
	
	private Command command;
	
	protected CommandHandler(Command command)
	{
		this.command = command;
	}	 

	
	protected void sendOkResponse(IoSession session, String originalThreadId, MessageParameters parameters)
	{
		MessageHeader header = new MessageHeader(MessageProtocol.RESPONSE, MessageStatus.OK, command, originalThreadId);
		MessageBody body = new MessageBody(parameters);
		session.write(new Message(header, body));
	}
	
	protected void sendErrorMessage(IoSession session, String originalThreadId, String errorMessage)
	{
		MessageHeader header = new MessageHeader(MessageProtocol.RESPONSE, MessageStatus.ERROR, command, originalThreadId);
		MessageBody body = new MessageBody(errorMessage);
		session.write(new Message(header, body));
	}
	
	public abstract void handle(Message message, IoSession session);
	
	
	/////////////////////////////////////////////////////////
	// PARSING UTILITIES
	/////////////////////////////////////////////////////////
	protected IndividualCut parseIndividualCut(int index, Row row)
	{
		System.out.println("############################################## DATE=" + row.get("cut_date"));
		
		IndividualCut cut = new IndividualCut(
				parseNumber(row.get("cut_no")), 
				index, 
				parseNumber(row.get("s_n_first_cut")), 
				parseNumber(row.get("s_n_last_cut")),
				parseNumber(row.get("cut_length")), 
				row.get("emp_no")==null?"":row.get("emp_no"), 
				row.get("cut_date")==null?"":row.get("cut_date"),
				row.get("heat_no")==null?"":row.get("heat_no"),
				row.get("ni_no")==null?"":row.get("ni_no"), 
				parseNumber(row.get("yield_cut")),
				parseBoolean(row.get("signed")));
		return cut;
	}
	
	protected SawSheet parseSawSheet(Row row) 
	{
		SawSheet sheet = SawSheet.parse(
				row.get("job_no"),
				row.get("saw_sheet_no"),
				row.get("qty_blank"),
				row.get("blank_length"), 
				row.get("yield_per_blank"), 
				row.get("planned_net_yield"), 
				row.get("total_actual_yield"),
				row.get("nbr_of_shifts"));
		return sheet;
	}
	
	protected boolean parseBoolean(String value)
	{
		return value.equals("1");
	}
	
	protected ShiftLogSheet parseShiftLog(Operation operation, Row row)
	{
		ShiftLogSheet shiftLog =
				new ShiftLogSheet(
						row.get("job_no"),
						operation,
						parseNumber(row.get("shift_log_no")),
						parseNumber(row.get("nbr_of_shifts")),
						parseNumber(row.get("pieces_cut")),
						parseBoolean(row.get("first_off_insp_req")),
						parseNumber(row.get("inspection_frequency")));
		
		return shiftLog;
		
	}
	
	protected ShiftLogDetail parseShiftLogDetail(Row row) 
	{
		return new ShiftLogDetail(
				row.get("job_no"),
				parseNumber(row.get("operation_no")),
				parseNumber(row.get("shift_log_no")),
				parseNumber(row.get("shift_no")),
				parseNumber(row.get("serial_no")),
				parseNumber(row.get("inspector_emp_no")),
				parseBoolean(row.get("conforms_100_percent")),
				row.get("ior_no"),
				parseBoolean(row.get("machinist_signed")),
				parseBoolean(row.get("first_started_on_shift")),
				parseBoolean(row.get("first_completed_on_shift")));
	}
	
	protected ShiftLogLine parseShiftLogLine(Row row)
	{
		return new ShiftLogLine	(
				row.get("job_no"),
				parseNumber(row.get("operation_no")), 
				parseNumber(row.get("shift_log_no")),
				parseNumber(row.get("shift_no")),
				parseNumber(row.get("line_no")),
				parseNumber(row.get("serial_no")),
				parseNumber(row.get("started_emp_no")),
				WorkpieceStatus.get(parseNumber(row.get("workpiece_status"))),
				row.get("work_date"),
				parseBoolean(row.get("self_gc_signed")),
				parseNumber(row.get("qc_emp_no")),
				parseBoolean(row.get("qc_signed")),
				row.get("ior_no"));
	}
	
	protected OperatorStats parseOperatorStats (Row row)
	{
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> parseFloat(row.get(operator_time)= " + parseFloat(row.get("operator_time")) + ", " + row.get("operator_time"));
		
		Employee emp = new Employee(parseNumber(row.get("emp_no")),row.get("first_name"),row.get("last_name"));
		return new OperatorStats (emp,
				parseNumber(row.get("Complete")), 
				parseNumber(row.get("Scrapped")),
				parseNumber(row.get("Reworked")),
				parseFloat(row.get("operator_time")),
				parseFloat(row.get("mean_cycle")),
				parseFloat(row.get("Routed")),
				parseFloat(row.get("to_peer")));
	}
	protected JobStatsOperation parseJobStatsOperation (Row row)
	{
		
		return new JobStatsOperation (
				row.get("job_no"),
				parseNumber(row.get("operation_no")), 
				parseFloat(row.get("set_up")),
				parseFloat(row.get("cycle")),
				parseFloat(row.get("net_cycle_time")),
				parseFloat(row.get("mean_cycle_time")));
	}

	protected JobStatsShiftLogLine parseJobStatsShiftLogLine (Row row)
	{
		return new JobStatsShiftLogLine (
				row.get("job_no"),
				parseNumber(row.get("operation_no")), 
				parseNumber(row.get("pieces_cut")),
				parseNumber(row.get("Complete")),
				parseNumber(row.get("Scrapped")),
				parseNumber(row.get("Reworked")),
				parseFloat(row.get("Good")),
				parseFloat(row.get("Scrap")),
				parseFloat(row.get("Rework")));		 
	}
	
	public SDBUser parseUser(Row row) 
	{
		return new SDBUser(row.get("login"),row.get("password"), 
				SDBRole.parse(row.get("role")));
	}
	
	private float parseFloat (String string)
	{
		try
		{
			return Float.parseFloat(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	private int parseNumber(String string)
	{
		try
		{
			return Integer.parseInt(string);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
}
