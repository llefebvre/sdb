package com.datagardens.nq.sdb.client.test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.datagardens.nq.sdb.client.nio.Callback;
import com.datagardens.nq.sdb.client.nio.ClientProtocolHandler;
import com.datagardens.nq.sdb.client.nio.Console;
import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageProtocol;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;

public class DataEntryTest implements Callback {

	
	public static void main(String[] args) 
	throws Exception 
	{
		DataEntryTest newTest = new DataEntryTest();
		newTest.startTest();
	}
	
	
	private Console client;
	private ClientProtocolHandler handler;
	private NioSocketConnector connector;
	
	public void startTest()
	throws Exception
	{
		
	}

	@Override
	public void connected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loggedIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loggedOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageRecived(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		
	}
}
