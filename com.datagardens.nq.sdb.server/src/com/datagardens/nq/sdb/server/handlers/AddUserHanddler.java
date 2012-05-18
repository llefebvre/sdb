package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;

public class AddUserHanddler extends CommandHandler {

	protected AddUserHanddler() {
		super(Command.add_user);
	}

	@Override
	public void handle(Message message, IoSession session) {
		// TODO Auto-generated method stub

	}

}
