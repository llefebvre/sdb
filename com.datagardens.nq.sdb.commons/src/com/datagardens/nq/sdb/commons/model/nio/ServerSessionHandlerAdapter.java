package com.datagardens.nq.sdb.commons.model.nio;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.protocol.Message;

public class ServerSessionHandlerAdapter
extends IoHandlerAdapter 
{
	private final Callback callback;
	
	public ServerSessionHandlerAdapter(Callback callback)
	{
		this.callback = callback;
	}
	
	@Override
	public void sessionOpened(IoSession session)
	throws Exception 
	{
		callback.connected();
	}
	
	
	@Override
	public void messageReceived(IoSession session, Object message)
	throws Exception 
	{
		
		callback.messageRecived(Message.parseMessageFromString((String)message));
	}
	
	@Override
	public void sessionClosed(IoSession session)
	throws Exception
	{
		callback.disconnected();
	}
}
