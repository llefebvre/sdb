package com.datagardens.nq.sdb.commons.model.nio;

import java.net.SocketAddress;
import java.rmi.ConnectIOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageProtocol;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;

public class ServerSession implements Callback
{
	
	private IoSession session;
	private IoHandler handler;
	private Map<String, ServerRequest> serverRequests;
	
	public ServerSession() 
	{
		this.handler = new ServerSessionHandlerAdapter(this);
		serverRequests = new HashMap<String, ServerRequest>();
	}
	
	public void connect(SocketAddress address, boolean useSsl)
	throws IllegalStateException, ConnectIOException
	{
		NioSocketConnector connector = new NioSocketConnector();
		
		if(session != null && session.isConnected())
		{
			throw new IllegalStateException("Already connected.");
		}
		
		IoFilter LOGGIN_FILTER = new LoggingFilter();
		
		TextLineCodecFactory tlcf = new TextLineCodecFactory();
		tlcf.setEncoderMaxLineLength(30000);
		tlcf.setDecoderMaxLineLength(30000);
		
		IoFilter CODEC_FILTER = new ProtocolCodecFilter(tlcf);
		
		connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		connector.getFilterChain().addLast("codec", CODEC_FILTER);
		connector.getFilterChain().addLast("logger", LOGGIN_FILTER);
		
		if(useSsl)
		{
			//TODO: Add SSL support
		}
		
		connector.setHandler(handler);
		ConnectFuture future1 = connector.connect(address);
		future1.awaitUninterruptibly();
		if(!future1.isConnected())
		{
			throw new ConnectIOException("Cannot initiate network connection");
		}
		
		session = future1.getSession();
		
	}
	
	public ServerRequest login(String user, String password) 
	{
		return generateServerRequest(new Message(new MessageHeader(MessageProtocol.LOGIN, MessageStatus.OK), 
				new MessageBody(user+"@"+password)));
	}
	
	public ServerRequest logout()
	{
		return generateServerRequest(new Message(new MessageHeader(
				MessageProtocol.QUIT, MessageStatus.OK)));
	}
	
	private ServerRequest generateServerRequest(Message message)
	{
		ServerRequest request = new ServerRequest(message);
		serverRequests.put(request.getUUID(), request);
		session.write(message);
		
		return request;
	}
	
	public ServerRequest generateServerRequest(Command command, MessageParameters parameters, String bodyMessage)
	{
		MessageHeader header = new MessageHeader(MessageProtocol.REQUEST, MessageStatus.OK, command);
		MessageBody body = parameters != null ? new MessageBody(parameters) : new MessageBody();
		
		if(bodyMessage!= null && !bodyMessage.isEmpty())
		{
			body.setMessage(bodyMessage);
		}
		
		return generateServerRequest(new Message(header, body));
	}
	
	public ServerRequest generateServerRequest(Command command)
	{
		return generateServerRequest(command, null, "");
	}
	
	public ServerRequest generateServerRequest(Command command, String bodyMessage)
	{
		return generateServerRequest(command, null, bodyMessage);
	}
	
	public ServerRequest generateServerRequest(Command command, MessageParameters parameters)
	{
		return generateServerRequest(command, parameters, "");
	}
	
	public ServerRequest jobListRequest()
	{
		MessageHeader header = new MessageHeader(MessageProtocol.REQUEST,
				MessageStatus.OK, Command.job_list);
		return generateServerRequest(new Message(header, new MessageBody()));
	}
	
	public ServerRequest findJobById(String id)
	{
		MessageHeader header = new MessageHeader(MessageProtocol.REQUEST, 
				MessageStatus.OK, Command.find_job);
		return generateServerRequest(new Message(header, new MessageBody(id)));
	}
	
	public ServerRequest saveSawSheet(String jobId, SawSheet sawSheet) 
	{
		MessageHeader header = new MessageHeader(MessageProtocol.REQUEST, 
				MessageStatus.OK, Command.saw_sheet_save);
		
		MessageParameters params = new MessageParameters(IndividualCut.class);
		
		for(IndividualCut cut : sawSheet.getCuts().values())
		{
			params.add(cut);
		}
		
		MessageBody body = new MessageBody(jobId);
		body.setContent(params);
		
		return generateServerRequest(new Message(header, body));
	}

	@Override
	public void connected() 
	{
		serverRequests = new HashMap<String, ServerRequest>();
	}

	@Override
	public void loggedIn() {
	}

	@Override
	public void loggedOut(){
		for(ServerRequest request : serverRequests.values())
		{
			request.forceFinish();
		}
	}

	@Override
	public void disconnected() 
	{
		
	}
	
	@Override
	public void error(String message)
	{
		
	}

	@Override
	public void messageRecived(Message message) 
	{
		ServerRequest request = serverRequests.get(message.getHeader().getThreadId());
		
		if(request != null)
		{
			request.setResponseMessage(message);
		}
	}
	
}
