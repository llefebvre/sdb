package com.datagardens.nq.sdb.client.nio;


public interface Callback {
	void connected();
	void loggedIn();
	void loggedOut();
	void disconnected();
	void messageRecived(String message);
	void error(String message);
}
