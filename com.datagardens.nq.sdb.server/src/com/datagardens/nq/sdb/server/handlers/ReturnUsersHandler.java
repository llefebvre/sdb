package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.RowSet;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ReturnUsersHandler extends CommandHandler {

	public ReturnUsersHandler() {
		super(Command.return_users);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		Table rbacTable = database.getTable("RBAC");
		try
		{
			MessageParameters params = new MessageParameters(SDBUser.class);
			
			RowSet rowSet = rbacTable.getRows();
			
			if(rowSet.length() > 0)
			{
				for(int i=0; i<rowSet.length(); i++)
				{
					Row row = rowSet.get(i);
					params.add(parseUser(row));
				}
			}
			
			sendOkResponse(session, 
					message.getHeader().getThreadId(), params);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
		}
		
	}	

}
