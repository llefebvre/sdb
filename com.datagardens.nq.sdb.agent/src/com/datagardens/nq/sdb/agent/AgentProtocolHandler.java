package com.datagardens.nq.sdb.agent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.agent.jdbc.Database;
import com.datagardens.nq.sdb.agent.jdbc.ResultSetGenerator;
import com.datagardens.nq.sdb.agent.jdbc.RowSet;
import com.datagardens.nq.sdb.agent.jdbc.SQLQuery;


public class AgentProtocolHandler extends IoHandlerAdapter 
{
	private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
	private final Set<String> users = Collections.synchronizedSet(new HashSet<String>());
	
	private ExecutorService executorService =  Executors.newCachedThreadPool();
	private Database database;
	public AgentProtocolHandler() 
	{
		try 
		{
//			database = new Database("jdbc:sqlserver://nq007;databaseName=M2MDATA77;", "sdb", "trlabs1");
			database = new Database("jdbc:sqlserver://nq007;databaseName=M2MDATA01;", "trakquest", "trlabs1");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	throws Exception 
	{
		session.close(true);
	}
	
	
	@Override
	public void messageReceived(final IoSession session, Object message)
	throws Exception 
	{
		/*
		 * -> [[THREAD]][[QUERY]][TABLE]tableName[COLUMNS]col1,col2,col3,colN[WHERE]colA,colB,colC[IS]valA,valB,valC
		 * <- [[THREAD]][[OK|ERROR]][[RESULTSET]][COLUMNS]col1,col2,col3[VALUES]val1,val2,val3|[[MESSAGE]]error message
		 */
		final String msg = (String) message;
		executorService.execute(new Runnable() {
			
			public void run() 
			{
				handleSQLRequest(session, msg);
			}
		});
	}	
	
	private synchronized void handleSQLRequest(IoSession session, String message) 
	{
		String orginialThreadId = message.substring(
				10, message.indexOf(SQLQuery.QUERY));
		
		try
		{
			SQLQuery query = SQLQuery.parse(message);
			
			System.out.println("[AGENT].[SQL] " + query.asString());
			
			RowSet rowSet = database.runQuery(query);
			
			if(rowSet.length() > 0)
			{
				sendOkResponse(session, orginialThreadId, 
						new ResultSetGenerator(rowSet).asString());
			}
			else
			{
				sendErrorResponse(session, orginialThreadId, "No items found");
			}
		}
		catch(Exception e)
		{
			sendErrorResponse(session, orginialThreadId, e.getMessage());
			e.printStackTrace();
		}
	}

	private synchronized void sendOkResponse(IoSession session,
			String threadId, String messageBody)
	{
		String message = "[[THREAD]]"+ threadId +"[[OK]]";
		message += messageBody;
		session.write(message);
	}
	
	private synchronized void sendErrorResponse(IoSession session,
			String threadId, String messageBody)
	{
		String message = "[[THREAD]]"+ threadId +"[[ERROR]][[MESSAGE]]";
		message += messageBody;
		session.write(message);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void broadcast(String message) 
	{
		synchronized (sessions)
		{
			for(IoSession session : sessions)
			{
				if(session.isConnected())
				{
					session.write("BROADCAST OK " + message);
				}
			}
		}
	}
	
	
	@Override
	public void sessionClosed(IoSession session)
	throws Exception 
	{
		String user = (String) session.getAttribute("user");
		users.remove(user);
		sessions.remove(session);
		broadcast(user + "has left the session");
	}
	
	public boolean isChatUser(String name)
	{
		return users.contains(name);
	}
	
	public int getNumberOfUsers()
	{
		return users.size();
	}
	
	public void kick(String name)
	{
		synchronized (sessions)
		{
			for(IoSession session : sessions)
			{
				if(name.equals(session.getAttribute("user")))
				{
					session.close(true);
					break;
				}
			}
		}
	}
}
