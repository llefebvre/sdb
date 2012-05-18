package com.datagardens.nq.sdb.client.test;

import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.datagardens.nq.sdb.client.nio.Callback;
import com.datagardens.nq.sdb.client.nio.ClientProtocolHandler;
import com.datagardens.nq.sdb.client.nio.Console;
import com.datagardens.nq.sdb.commons.model.ModelException;

public class ProtocolConnectionTest implements Callback
{
	
	
	public static void main(String[] args) 
	throws ModelException 
	{	
		ProtocolConnectionTest newTest = new ProtocolConnectionTest();
		newTest.startTest();
	}

	private Console client;
	private ClientProtocolHandler handler;
	private NioSocketConnector connector;
	
	private void startTest() throws ModelException 
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
		System .out.println("message recived: " + message);
		
//		Message newMsg = Message.parseMessageFromString(message);
		
		
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub	
	}
}
