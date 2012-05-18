package com.datagardens.nq.sdb.agent;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Agent {

	/**
	 * @param args
	 */
	public static final Integer PORT = 1235;
	private static NioSocketAcceptor acceptor;
	
	public static void main(String[] args) 
  throws IOException, InterruptedException 
  {
		acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		
		MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
		chain.addLast("mdc", mdcInjectionFilter);
		
		IoFilter LOGGIN_FILTER = new LoggingFilter();
		
		TextLineCodecFactory tlcf = new TextLineCodecFactory();
		tlcf.setEncoderMaxLineLength(30000);
		tlcf.setDecoderMaxLineLength(30000);
		
		chain.addLast("codec", new ProtocolCodecFilter(tlcf));
		chain.addLast("logger", LOGGIN_FILTER);
		
		//Bind
		acceptor.setHandler(new AgentProtocolHandler());
		acceptor.bind(new InetSocketAddress(PORT));
		
		System.out.println("AGENT READY ON PORT " + PORT);
		
		while (true) {
			Thread.sleep(1000);
		}
	}
}
