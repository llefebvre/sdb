package com.datagardens.nq.sdb.commons.model.nio;

import com.datagardens.nq.sdb.commons.protocol.Message;


public interface Callback {
	void connected();
	void loggedIn();
	void loggedOut();
	void disconnected();
	void messageRecived(Message message);
	void error(String message);
}
