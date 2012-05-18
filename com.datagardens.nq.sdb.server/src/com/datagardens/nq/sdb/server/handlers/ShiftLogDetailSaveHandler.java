package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.ShiftLogDetail;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogDetailSaveHandler extends CommandHandler {

	public ShiftLogDetailSaveHandler() {
		super(Command.save_shift_log_detail);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		try
		{
			MessageParameters params = message.getBody().getContent();
			ShiftLogDetail detail = (ShiftLogDetail) params.getFirstElement();
			
			Table shiftLogTable = database.getTable("ShiftLogDetail");
			
			Row updateRow = new Row();
			updateRow.put("job_no", detail.getJobId());
			updateRow.put("operation_no", detail.getOperationNumber());
			updateRow.put("shift_log_no", detail.getShiftLogNumber());
			updateRow.put("shift_no", detail.getShiftNumber());
			updateRow.put("serial_no", detail.getSerialNumber());
			updateRow.put("inspector_emp_no", detail.getInspectorEmployeeNumber());
			updateRow.put("conforms_100_percent", detail.isConfirms100Percent());
			updateRow.put("ior_no", detail.getIorNumber());
			updateRow.put("machinist_signed", detail.isMachinistSigned());
			updateRow.put("first_started_on_shift", detail.isFirstStartedOnShift());
			updateRow.put("first_completed_on_shift", detail.isFirstCompletedOnShift());
			
			shiftLogTable.putRow(updateRow, "operation_no= '"+detail.getOperationNumber()+
					"' AND shift_log_no='"+detail.getShiftLogNumber()+
					"' AND shift_no='"+detail.getShiftNumber()+"'");
			
			sendOkResponse(session, message.getHeader().getThreadId(),null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] " + e.getMessage());
		}
	}
}
