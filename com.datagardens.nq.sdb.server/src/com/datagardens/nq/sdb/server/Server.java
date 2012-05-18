package com.datagardens.nq.sdb.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server 
{
	public static final Integer PORT = 1234;
	private NioSocketAcceptor acceptor;

	public boolean start(boolean useSecureChannel) 
	throws IOException
	{
		acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
		chain.addLast("mdc", mdcInjectionFilter);
		
		if(useSecureChannel)
		{
			addSSLSupport();
		}
		
		TextLineCodecFactory tlcf = new TextLineCodecFactory();
		tlcf.setEncoderMaxLineLength(30000);
		tlcf.setDecoderMaxLineLength(30000);
		
		chain.addLast("codec", 
				new ProtocolCodecFilter(tlcf));
		addLogger(chain);

		//Bind
		acceptor.setHandler(new ServerProtocolHandler());
		acceptor.bind(new InetSocketAddress(PORT));
		
		/*try
		{
			AgentSession session = AgentSession.open();
			session.findJobById("23535-0000");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("agent requeest done");*/
		
		
		return true;
	}
	
	private void addSSLSupport() {
		// TODO not implemented yet
	}
	
	private static void addLogger(DefaultIoFilterChainBuilder chain) 
	{
		chain.addLast("logger", new LoggingFilter());
	}

	public boolean stop()
	{
		acceptor.unbind();
		
		if(!acceptor.isActive())
		{
			acceptor.dispose();
			return true;
		}
		
		return false;
	}
	
	public void restart()
	throws IOException
	{
		stop();
		start(false);
	}
}