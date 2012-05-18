/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/13/2012
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.commons.protocol;

public class MessageBody
{
	private String message;
	private MessageParameters content;
	
	
	public MessageBody() {
		super();
	}
	
	public MessageBody(String message)
	{
		this.message = message;
	}
	
	public MessageBody(MessageParameters content)
	{
		this.content = content;
	}
	
	
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public MessageParameters getContent() {
		return content;
	}
	
	public void setContent(MessageParameters content) {
		this.content = content;
	}
	
	@Override
	public String toString() 
	{
		if(message != null)
		{
			return message;
		}
		else if(content != null)
		{
			return content.toString();
		}
		
		return "";
	}
}
