package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogHanlder extends CommandHandler {

	public ShiftLogHanlder() 
	{
		super(Command.shift_log);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		Operation operation = 
				(Operation) message.getBody().getContent().getFirstElement();
		
		ShiftLogSheet sheet = null;
		
		try
		{
			Table shiftLogTable = database.getTable("ShiftLog");
			RowSet shiftLogsRow = shiftLogTable.getRows("operation_no='"+ operation.getOperationNumber()+"'");
			if(shiftLogsRow.length() == 0)
			{
				// There is not shift log for this operation
				// create a new one
				Row updateShiftLogRow = new Row();
				updateShiftLogRow.put("job_no", operation.getJobId());
				updateShiftLogRow.put("operation_no", operation.getOperationNumber());
				updateShiftLogRow.put("shift_log_no", 0);
				updateShiftLogRow.put("nbr_of_shifts", 1);
				
				shiftLogTable.putRow(updateShiftLogRow, "job_no='" + operation.getJobId()+"' AND operation_no='"+operation.getOperationNumber()+"'");
				
//				sheet = ShiftLogSheet.getEmptySheet(operation.getJobId(), operation.getOperationNumber()+"", 0);
			}
			else
			{
//				sheet = parseShiftLog(shiftLogsRow.get(0));
			}
			
			if(sheet != null)
			{
				MessageParameters params = new MessageParameters(ShiftLogSheet.class);
				params.add(sheet);
				sendOkResponse(session, message.getHeader().getThreadId(), params);
			}
			else
			{
				sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] shift log sheet cannot be retrieved");
			}
		}
		catch(Exception e)
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] " + e.getMessage());
		}
		
	}

}
