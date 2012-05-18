package com.datagardens.nq.sdb.commons.model.nio;

import java.util.ArrayList;
import java.util.List;

import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;

public class ServerRequest 
{
	
	private boolean breakTask;
	private Message requestMessage;
	private Message responseMessage;
	
	protected ServerRequest(Message requestMessage)
	{
		this.requestMessage = requestMessage;
	}
	
	public String getUUID() {
		return requestMessage.getHeader().getThreadId();
	}
	
	public boolean isComplete()
	{
		return responseMessage != null;
	}
	
	public void waitForRequestToComplete()
	{
		while(!isComplete() && !breakTask)
		{
			try
			{
				Thread.sleep(200);
			}
			catch(Exception e)
			{
				/* force to finish */
				forceFinish();
			}
		}
	}
	
	protected void forceFinish()
	{
		breakTask = true;
	}

	public void setResponseMessage(Message message) {
		responseMessage = message;
	}
	
	public MessageStatus getStatus()
	{
		return responseMessage.getHeader().getStatus();
	}
	
	public String getErrorMessage()
	{
		return responseMessage.getBody().getMessage() == null ? "" :
			responseMessage.getBody().getMessage();
	}
	
	public Message getResponseMessage() {
		return responseMessage;
	}
	
	public List<Object> getResult()
	{
		if(responseMessage.getBody() != null && 
				responseMessage.getBody().getContent() != null)
		{
			return responseMessage.getBody().getContent().getAll();
		}
		
		return new ArrayList<Object>();
	}
}
