package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class DeleteUserHanddler extends CommandHandler {

	public DeleteUserHanddler() {
		super(Command.delete_user);
	}

	@Override
	public void handle(Message message, IoSession session) {

		try
		{
			SDBUser user = (SDBUser) message.getBody().getContent().getFirstElement();
			Table table = database.getTable("RBAC");
			table.delete("login='"+user.getUsername()+"'");
			
			sendOkResponse(session, message.getHeader().getThreadId(), null);
		}
		catch(Exception e)
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
		}
		
	}

}
