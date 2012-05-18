package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class ModifyUserHandler extends CommandHandler {

	public ModifyUserHandler() {
		super(Command.modify_user);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		try
		{
			SDBUser user = (SDBUser) message.getBody().getContent().getFirstElement();
			Table table = database.getTable("RBAC");
			
			Row row = new Row();
			row.put("login", user.getUsername());
			row.put("password", user.getPassword());
			row.put("role", user.getRole().toString());
			
			table.putRow(row, "login='"+user.getUsername()+"'");
			
			sendOkResponse(session, message.getHeader().getThreadId(), null);
			
		}
		catch(Exception e)
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), "[SERVER] "+ e.getMessage());
		}
	}

}
