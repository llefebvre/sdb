package com.datagardens.nq.sdb.server;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class AccessControlFilter extends IoFilterAdapter
{
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception 
	{
		System.out.println("message recived filter");
		
		for(Object att : session.getAttributeKeys())
		{
			System.out.println("att> " + att); 
		}
		nextFilter.messageReceived(session, message);
	}
}
