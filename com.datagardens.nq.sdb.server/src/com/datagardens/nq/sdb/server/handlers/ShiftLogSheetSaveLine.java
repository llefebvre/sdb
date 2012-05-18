package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.ShiftLogLine;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogSheetSaveLine extends CommandHandler {

	public ShiftLogSheetSaveLine()
	{
		super(Command.shift_log_save_line);
	}

	@Override
	public void handle(Message message, IoSession session)
	{
		try
		{
			ShiftLogLine line = 
					(ShiftLogLine) message.getBody().getContent().getFirstElement();
			Table table = database.getTable("ShiftLogLine");
			Row row = new Row();
			
			row.put("job_no", line.getJobId());
			row.put("operation_no", line.getOperationNumber());
			row.put("shift_log_no", line.getShiftLogNumber());
			row.put("shift_no", line.getShiftNumber());
			row.put("line_no", line.getShiftLogLineNumber());
			row.put("serial_no", line.getSerialNumber());
			row.put("started_emp_no", line.getStartedEmployeeNumber());
			row.put("workpiece_status", line.getWorkpieceStatus().getCode());
			row.put("work_date", line.getDate());
			row.put("self_gc_signed", line.isSelfQCSigned());
			row.put("qc_emp_no", line.getQcEmployeeNumber());
			row.put("qc_signed", line.isQcSigned());
			row.put("ior_no", line.getIORNumber());
			
			table.putRow(row, "job_no='"+ line.getJobId() +"' " +
					"AND shift_log_no='"+line.getShiftLogNumber()+"' " +
					"AND operation_no='"+line.getOperationNumber()+"' " +
					"AND line_no='"+line.getShiftLogLineNumber() +"' " +
					"AND shift_no='"+line.getShiftNumber()+"'");
			
			sendOkResponse(session, message.getHeader().getThreadId(), null);
		}
		catch(Exception e)
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] " + e.getMessage());
		}

	}

}
