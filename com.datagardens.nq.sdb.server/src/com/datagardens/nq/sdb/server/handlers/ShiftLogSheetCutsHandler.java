package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.ShiftLogLine;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ShiftLogSheetCutsHandler extends CommandHandler {

	public ShiftLogSheetCutsHandler() {
		super(Command.shift_log_sheet_cuts);
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
			Table individualCutsTable = database.getTable("ShiftLogLine");
			
			RowSet cutRows = 
					individualCutsTable.getRows("shift_log_no='"+ sheetNumber +
							"' AND shift_no='" + shiftIndex + "'" ); 

			MessageParameters responseParams = new MessageParameters(ShiftLogLine.class);
			
			if(cutRows.length() > 0)
			{
				for(int i=0; i<cutRows.length();i++)
				{
					ShiftLogLine cut = parseShiftLogLine(cutRows.get(i));
					responseParams.add(cut);
				}
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
