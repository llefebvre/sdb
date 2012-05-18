package com.datagardens.nq.sdb.client.nio;

import java.net.SocketAddress;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageProtocol;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;

public class Console 
{
	
	private IoSession session;
	private IoHandler handler;
	
	public Console(IoHandler handler) 
	{
		this.handler = handler;
	}
	
	public boolean connect(NioSocketConnector connector, 
			SocketAddress address, boolean useSsl, String user, String password)
	{
		
		if(session != null && session.isConnected())
		{
			throw new IllegalStateException("Already connected.");
		}
		
		try
		{
			IoFilter LOGGIN_FILTER = new LoggingFilter();
			
			TextLineCodecFactory tlcf = new TextLineCodecFactory();
			tlcf.setEncoderMaxLineLength(10240);
			tlcf.setDecoderMaxLineLength(10240);
			
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
				return false;
			}
			session = future1.getSession();
			
			login(user, password);
			
			return true;
		}
		catch(Exception e)
		{
			
		}
		return false;
	}
	
	private void login(String user, String password) 
	{
		session.write(new Message(new MessageHeader(MessageProtocol.LOGIN, MessageStatus.OK), 
				new MessageBody(user+"@"+password)));
	}
	
	public void sendMessage(Message message)
	{
		session.write(message);
	}
}
