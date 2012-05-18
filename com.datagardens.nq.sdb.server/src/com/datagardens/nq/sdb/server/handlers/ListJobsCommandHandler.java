package com.datagardens.nq.sdb.server.handlers;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.agentclient.AgentException;
import com.datagardens.nq.sdb.server.agentclient.AgentSession;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ListJobsCommandHandler extends CommandHandler {

	public ListJobsCommandHandler() 
	{
		super(Command.job_list);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		try
		{
			MessageHeader header = message.getHeader();
			MessageBody body = message.getBody();
			
			MessageParameters parameters = new MessageParameters(Job.class);
			
			if(body != null && body.getMessage() != null 
					&& !body.getMessage().isEmpty())
			{
				
				Job job = null;
				
				try
				{
					AgentSession agentSession = AgentSession.open();
					job = agentSession.findJobById(body.getMessage());
					
					
					if(job == null)
					{
						throw new AgentException("Cannot find job with id '" + body.getMessage() + "'");
					}
					
					
					Table jobTable = database.getTable("Job");
					
					Row row = new Row();
					
					row.put("job_no",  job.getId());
					row.put("job_quantity",  job.getQuantity());
					row.put("drawing_number",  "");
					row.put("part_name", "");
					row.put("part_number", "");
					jobTable.putRow(row, "job_no='"+job.getId()+"'");
					
					List<Operation> operations = 
							agentSession.getOperationsForJob(body.getMessage());
					
					
					Table table = database.getTable("ShiftLog");
					Table operationTable = database.getTable("Operation");
					
					for(Operation op : operations)
					{
						row = new Row();
						row.put("job_no",  op.getJobId());
						row.put("operation_no",  op.getOperationNumber());
						row.put("set_up",  op.getSetup());
						row.put("cycle",  op.getCycle());
						row.put("net_cycle_time",  op.getSetup()/job.getQuantity()+op.getCycle());
						operationTable.putRow(row,"job_no = '" +op.getJobId()+"' AND operation_no = "+op.getOperationNumber());
						
						
						
						if(op.getOperationNumber() == 10)
						{
							continue;
						}
						
						
						Map<Integer, Float> operationTimes = 
								agentSession.getOperatorTimesForOperation(op);
						op.setOperationTimes(operationTimes);
						
						Table operatorTimeTable  = database.getTable("OperatorTime");
						
						for(Integer empNo : operationTimes.keySet())
						{
							Row operatorTimeRow = new Row();
							
							operatorTimeRow.put("job_no", job.getId());
							operatorTimeRow.put("operation_no", op.getOperationNumber());
							operatorTimeRow.put("emp_no",  empNo);
							operatorTimeRow.put("operator_time", operationTimes.get(empNo));
							
							operatorTimeTable.putRow(operatorTimeRow, "job_no='"+job.getId()+"' " +
									"AND operation_no="+op.getOperationNumber()+" AND emp_no='"+ empNo+"'");
						}
						
						ShiftLogSheet shiftLogSheet = null;
						
						RowSet rowSet = table.getRows("job_no='"+job.getId()+"' AND operation_no = '"
								+op.getOperationNumber()+"'");
						
						if(rowSet.length() > 0)
						{
							shiftLogSheet = parseShiftLog(op, rowSet.get(0));
						}
						else
						{
							Row shiftLogRow = new Row();
							shiftLogRow.put("job_no", job.getId());
							shiftLogRow.put("operation_no", op.getOperationNumber());
							
							ResultSet rs = table.putRow(shiftLogRow, "job_no='"+job.getId()+"' AND operation_no = '"
									+op.getOperationNumber()+"'");
							
							int generatedSawSheetKey = rs.next() ? rs.getInt(1) : 0;
							
							shiftLogSheet = ShiftLogSheet.getEmptySheet(job.getId(),
									op, generatedSawSheetKey);
						}
						
						
						if(shiftLogSheet != null)
						{
							job.putShiftLogSheet(shiftLogSheet);
						}
					}
				}
				catch(AgentException e)
				{
					sendErrorMessage(session, header.getThreadId(), e.getMessage());
					return;
				}
				
				//Retrieve basic Saw Sheet information
				Table table = database.getTable("SawSheet");
				
				//Verify if the row exist
				RowSet sheetsRows = table.getRows("job_no='" + job.getId() + "'"); 
				
				if(sheetsRows.length() > 0)
				{
					//Sheets exist
					for(int i=0; i<sheetsRows.length();i++)
					{
						Row row = sheetsRows.get(i);
						
						SawSheet sheet = parseSawSheet(row);
						job.setSawSheet(sheet);
					}
				}
				else
				{
					// No sheets on the database, 
					// create a new sheet for shift 1
					
					Row newSheetRow = new Row();
					newSheetRow.put("job_no", job.getId());
					ResultSet rs = table.putRow(newSheetRow, "job_no='"+job.getId()+"'");
					
					int generatedSawSheetKey = rs.next() ? rs.getInt(1) : 0;

					SawSheet newSawSheet = SawSheet.parse(job.getId(), generatedSawSheetKey + "", 
							"0", "0", "0", "0", "0", "1");
					job.setSawSheet(newSawSheet);
				}
				
				
				parameters.add(job);
				
				sendOkResponse(session, header.threadId, parameters);
			}
			else
			{
				//Return all the jobs
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] " + e.getMessage());
		}
		
	}	
}
