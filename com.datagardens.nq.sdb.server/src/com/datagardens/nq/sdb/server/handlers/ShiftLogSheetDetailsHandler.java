package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.ShiftLogLine;
import com.datagardens.nq.sdb.commons.model.ShiftLogDetail;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogSheetDetailsHandler extends CommandHandler {

	public ShiftLogSheetDetailsHandler() {
		super(Command.shift_log_sheet_details);
	}

	@Override
	public void handle(Message message, IoSession session)
	{
		MessageBody body = message.getBody();
		MessageParameters parameters = body.getContent();
		
		Integer sheetNumber = (Integer) parameters.getAll().get(0);
		Integer shiftIndex     = (Integer) parameters.getAll().get(1);
		
		try
		{
			Table table = database.getTable("ShiftLogDetail");
			
			RowSet dataRows = 
					table.getRows("shift_log_no='"+ sheetNumber +
							"' AND shift_no='" + shiftIndex + "'" );
			
			MessageParameters responseParams = new MessageParameters(ShiftLogDetail.class);
			
			if(dataRows.length() > 0)
			{
				ShiftLogDetail detail = parseShiftLogDetail(dataRows.get(0));
				responseParams.add(detail);
			}
			
			sendOkResponse(session, message.getHeader().getThreadId(), responseParams);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
		}

	}

	
}
