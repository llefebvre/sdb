package com.datagardens.nq.sdb.client.nio;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientProtocolHandler extends IoHandlerAdapter
{	
	private final Callback callback;
	public ClientProtocolHandler(Callback callback)
	{
		this.callback = callback;
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		callback.connected();
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)
	throws Exception 
	{
		
		/*String wholeMesage = (String) message;
		
		System.out.println("Message reciven on client " + message);
		
		String [] result = wholeMesage.split(" ", 3);
		
		MessageStatus status = MessageStatus.parse(result[1]);
		MessageType type = MessageType.parse(result[0]);
		String messageText = result.length == 3 ? result[2] : null;
		
		System.out.println(">> Status: " + status);
		System.out.println(">> type: " + type);
		Gson gson = new Gson();
		NQJob job = null;
		
		if(MessageStatus.OK == status)
		{
			switch(type)
			{
			case BROADCAST:
				
				String [] data = messageText.split("::");
				job = gson.fromJson(data[1], NQJob.class);
				callback.messageRecived(MessageFormat.format("The user {0} has modified {1}", data[0], job));
				break;
				
			case PRIVATE:
				if(messageText != null)
				{
					job = gson.fromJson(messageText, NQJob.class);
					callback.messageRecived("Job reciverd from server: " + job);
				}
				break;
				
			case LOGIN:
				callback.loggedIn();
				break;
				
			case QUIT:
				callback.loggedOut();
				break;
					
			default:
				if(messageText != null)
				callback.error(messageText);
			}
		}
		else if(MessageStatus.ERROR == status)
		{
			callback.error("Cannot login " + messageText);
		}*/
	}
	
	@Override
	public void sessionClosed(IoSession session) 
	throws Exception 
	{
		callback.disconnected();
	}
	
}
