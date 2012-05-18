/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/13/2012
 *
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.commons.protocol;

import java.util.UUID;

public class MessageHeader extends AbstractGsonMessagePart
{
	public MessageProtocol protocol;
	public MessageStatus status;
	public String contentType;
	public MimeType mimeType;
	public Command command;
	public String threadId;
	
	public MessageHeader(MessageProtocol protocol, 
			MessageStatus status, Command command, String threadId)
	{
		super();
		this.protocol = protocol;
		this.status = status;
		this.command = command == null ? Command.no_command : command;
		this.threadId = threadId;
	}
	
	public MessageHeader(MessageProtocol protocol, MessageStatus status)
	{
		this(protocol, status, null, null);
	}
	
	public MessageHeader(MessageProtocol protocol, 
			MessageStatus status, Command command)
	{
		this(protocol, status, command, null);
	}
	
	
	public MessageProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(MessageProtocol protocol) {
		this.protocol = protocol;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public MimeType getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public void setCommand(Command command) {
		this.command = command;
	}
	
	public String getThreadId() {
		return threadId;
	}
	
	public void setThreadId(UUID threadId) {
		this.threadId = threadId.toString();
	}
	//////////////////////////////////////
	//INNER CLASSES
	//////////////////////////////////////
	
	public static enum MessageProtocol{
		/**
		 * <p>Attempts to start a session with the server</p>
		 * <p>PROTOCOL:<b>LOGIN|status|[credentials]</b></p>
		 * <ul>
		 * <li><b>status</b> {@link MessageStatus} this is always 'OK' in the client-server direction</li>
		 * <li><b>credentials</b> is an variable {@link Credentials} that holds the login username and password,
		 * this parameters must appear in the client to server message</li>
		 * </ul>
		 */
		LOGIN,              
		
		/**
		 * <p>Attempts the end a session with the server
		 * it requires no parameters </p>
		 * <p>PROTOCOL: <b>QUIT|status</b><p>
		 * <p>
		 * <ul>
		 * <li><b>status</b> {@link MessageStatus}, this is always 'OK' in the client-server direction</li>
		 * </ul>	 
		 * </p>
		 */
		QUIT,		          
		
		/**
		 * <p>Request the server to run an specific command, such command has to defined at {@link Command}<p>
		 * <p>PROTOCOL:<b>REQUEST|status|thread-id|command-name|command-parameters</b></p>
		 * <p>
		 * <ul>
		 * <li><b>status</b> {@link MessageStatus}</li>
		 * <li><b>thread-id</b> its an unique id for this specific command</li>
		 * <li><b>command-name</b> its one of the SDB command defined here: {@link Command}</li>
		 * <li><b>command-parameters</b> its a {@link MessageParameters} object holding the command-specific parameters</li>
		 * </ul>	 
		 * </p>
		 */
		REQUEST,      
		
		/**
		 * <p>Request for an specific command, such response is only intended for the 
		 * REQUEST generator, this is not a broadcasted response.</p>
		 * <p>PROTOCOL: <b> RESPONSE|status|thread-id|command-name|response-parameters</b></p>
		 * <ul>
		 * <li><b>status</b> {@link MessageStatus}</li>
		 * <li><b>thread-id</b> its an unique id for this specific command</li>
		 * <li><b>command-name</b> its one of the SDB command defined here: {@link Command}</li>
		 * <li><b>response-parameters</b> is a {@link MessageParameters} object holding the response-specific results</li>
		 */
		RESPONSE,
		
		/**
		 * This message will be broadcasted from the server to all the clients
		 * <p>PROTOCOL:<b>NOTIFICATION</b></p>
		 */
		NOTIFICATION,
		
		/**
		 * This message type marks the message as 'Unknown' and us up to the application
		 * to handle it.
		 */
		UNKNOWN;
	}
	
	public static enum MessageStatus{
		OK, ERROR, UNKNOWN;
	}
	
	public static enum MimeType {
		message, json;
	}
	
	public static enum Command{
		job_list, print_saw_sheet, print_serialization_sheets, 
		saw_sheet_save, saw_sheet_cuts, find_job,
		add_user, modify_user, delete_user,return_users, shift_log,
		shift_log_save, employee_list,shift_log_sheet_cuts,shift_log_sheet_details,
		save_shift_log_detail,shift_log_save_line,operation_tally_sheet,
		no_command;
	}
}
