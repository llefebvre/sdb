/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/12/2012
 * 
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.commons.protocol;

import java.lang.reflect.Type;
import java.util.UUID;

import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MimeType;
import com.google.gson.reflect.TypeToken;


public class Message extends AbstractGsonMessagePart
{	
	///////////////////////////////////////
	//CLASS
	///////////////////////////////////////
	public static Message parseMessageFromString(String message)
	{
		Type type = new TypeToken<Message>(){}.getType();
		return gson.fromJson(message, type);
	}
	
	///////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////
	
	private MessageHeader header;
	private MessageBody body;
	
	public Message() {
	}
	
	public Message(MessageHeader header, MessageBody body)
	{
		this.header = header;
		this.body = body == null ? new MessageBody() : body;
		
		if(this.body.getContent() != null)
		{
			this.header.setContentType(this.body.getContent().parametersClass);
			this.header.setMimeType(MimeType.json);
		}
		else
		{
			this.header.setContentType("");
			this.header.setMimeType(MimeType.message);
		}
		
		if(this.header.getThreadId() == null)
		{
			this.header.setThreadId(generateThreadId());
		}
		
	}
	
	private UUID generateThreadId() {
		return UUID.randomUUID();
	}

	public Message(MessageHeader header)
	{
		this(header, null);
	}
	
	public MessageHeader getHeader() {
		return header;
	}
	
	public MessageBody getBody() {
		return body;
	}
	
	public void setHeader(MessageHeader header) {
		this.header = header;
	}
	
	public void setBody(MessageBody body) {
		this.body = body;
	}
}
