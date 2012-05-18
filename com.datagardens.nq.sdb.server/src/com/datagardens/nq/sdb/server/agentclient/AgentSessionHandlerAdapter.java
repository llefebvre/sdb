package com.datagardens.nq.sdb.server.agentclient;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class AgentSessionHandlerAdapter
extends IoHandlerAdapter 
{
	private final SimpleCallBack callback;
	
	public AgentSessionHandlerAdapter(SimpleCallBack callback)
	{
		this.callback = callback;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)
	throws Exception 
	{
		
		callback.messageRecived((String)message);
	}
}

