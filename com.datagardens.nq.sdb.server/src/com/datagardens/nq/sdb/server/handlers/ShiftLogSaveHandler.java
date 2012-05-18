/**
 * Copyright (c) DataGardens Inc. 2012-2013
 */
package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogSaveHandler extends CommandHandler {

	public ShiftLogSaveHandler() 
	{
		super(Command.shift_log_save);
	}
	
	@Override
	public void handle(Message message, IoSession session)
	{
		try
		{
			ShiftLogSheet sheet = 
					(ShiftLogSheet) message.getBody().getContent().getFirstElement();
			
			Table shiftLogTable = database.getTable("ShiftLog");
			
			System.out.println("saving shift Log= " + sheet.getNumberOfShifts());
			
			Row sheetRow = new Row();
			sheetRow.put("job_no", sheet.getJobId());
			sheetRow.put("operation_no", sheet.getOperationNumber());
			sheetRow.put("shift_log_date", "2012-01-01");
			sheetRow.put("pieces_cut", sheet.getPiecesCut());
			sheetRow.put("first_off_insp_req", sheet.isFirstOffInspectionRequired());
			sheetRow.put("inspection_frequency", sheet.getInProcessInspectionFrequency());
			sheetRow.put("nbr_of_shifts", sheet.getNumberOfShifts());
			
			String conditions = "job_no='"+sheet.getJobId()+"' AND operation_no='"+sheet.getOperationNumber()
					+"' AND shift_log_no='"+sheet.getSheetNumber()+"'";
			
			shiftLogTable.putRow(sheetRow, conditions);
			
			/* save line cuts into [ShiftLogLine] table */
//			Table lineTable = database.getTable("ShiftLogLine");
			
			/*for(ShiftLogLine line : sheet.getLines().values())
			{
				Row lineRow = new Row();
				lineRow.put("job_no", line.getJobId());
				lineRow.put("operation_no", line.getOperationNumber());
				lineRow.put("shift_log_no", line.getShiftLogNumber());
				lineRow.put("shift_log_line_no", line.getShiftLogLineNumber());
				lineRow.put("serial_no", line.getSerialNumber());
				lineRow.put("started_emp_no", line.getStartedEmployeeNumber());
				lineRow.put("adjusted_serial_no", line.getSerialNumber());
				lineRow.put("workpiece_status", line.getWorkpieceStatus().toString());
				lineRow.put("work_date", line.getDate());
				lineRow.put("self_gc_signed", line.isSelfQCSigned());
				lineRow.put("qc_emp_no", line.getQcEmployeeNumber());
				lineRow.put("qc_signed", line.isQcSigned());
				lineRow.put("ior_no", line.getIORNumber());
				
				conditions = "job_no=\u0027"+line.getJobId()+"\u0027 AND operation_no=\u0027"+line.getOperationNumber()+"\u0027 AND shift_log_no=\u0027"+line.getShiftLogNumber()
						+"\u0027 AND shift_log_line_no=\u0027" + line.getShiftLogLineNumber()+"\u0027";
				lineTable.putRow(lineRow, conditions);
			}*/
			
			sendOkResponse(session, message.getHeader().getThreadId(), null);
			
		}
		catch(Exception e)
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] " + e.getMessage());
		}
	}

}
