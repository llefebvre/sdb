package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class SawSheetCutsHandler extends CommandHandler {

	public SawSheetCutsHandler() {
		super(Command.saw_sheet_cuts);
	}
	
	@Override
	public void handle(Message message, IoSession session) 
	{
		
		MessageBody body = message.getBody();
		MessageParameters parameters = body.getContent();
		
		Integer sawSheetNumber = (Integer) parameters.getAll().get(0);
		Integer shiftIndex     = (Integer) parameters.getAll().get(1);
		
		try 
		{
			Table individualCutsTable = database.getTable("IndividualLine");
			
			RowSet cutRows = 
					individualCutsTable.getRows("saw_sheet_no='"+ sawSheetNumber +
							"' AND shift_no='" + shiftIndex + "'" ); 

			MessageParameters responseParams = new MessageParameters(IndividualCut.class);
			
			if(cutRows.length() > 0)
			{
				for(int i=0; i<cutRows.length();i++)
				{
					IndividualCut cut = parseIndividualCut(i, cutRows.get(i));
					responseParams.add(cut);
				}
			}
			
			sendOkResponse(session, message.getHeader().getThreadId(),
					responseParams);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
		}
	}
}
