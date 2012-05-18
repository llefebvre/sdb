package com.datagardens.nq.sdb.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageProtocol;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.MessageStatus;
import com.datagardens.nq.sdb.server.handlers.CommandHandler;
import com.datagardens.nq.sdb.server.handlers.DeleteUserHanddler;
import com.datagardens.nq.sdb.server.handlers.EmployeeListHandler;
import com.datagardens.nq.sdb.server.handlers.ListJobsCommandHandler;
import com.datagardens.nq.sdb.server.handlers.ModifyUserHandler;
import com.datagardens.nq.sdb.server.handlers.OpTallySheetHandler;
import com.datagardens.nq.sdb.server.handlers.ReturnUsersHandler;
import com.datagardens.nq.sdb.server.handlers.SawSheetCutsHandler;
import com.datagardens.nq.sdb.server.handlers.SawSheetSaveHandler;
import com.datagardens.nq.sdb.server.handlers.ShiftLogDetailSaveHandler;
import com.datagardens.nq.sdb.server.handlers.ShiftLogHanlder;
import com.datagardens.nq.sdb.server.handlers.ShiftLogSaveHandler;
import com.datagardens.nq.sdb.server.handlers.ShiftLogSheetCutsHandler;
import com.datagardens.nq.sdb.server.handlers.ShiftLogSheetDetailsHandler;
import com.datagardens.nq.sdb.server.handlers.ShiftLogSheetSaveLine;
import com.datagardens.nq.sdb.server.jdbc.Database;
import com.datagardens.nq.sdb.server.session.LoginAgent;


public class ServerProtocolHandler extends IoHandlerAdapter 
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ServerProtocolHandler.class);
	private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
	private final Set<String> users = Collections.synchronizedSet(new HashSet<String>());
	
	private ExecutorService executorService =  Executors.newCachedThreadPool();
	
	
	private String hostName = "192.168.4.203";
	boolean useSDB = true;
	
	private String databaseName = "trakquestdb";
	private String loginName = "trakquest";
 
	private String loginPwd = "Trlabs1!";
	private String connectionString = "jdbc:sqlserver://" + hostName + ";databaseName=" + databaseName + ";";
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	throws Exception 
	{
		LOGGER.warn("Unexpected exception", cause);
		session.close(true);
	}
	
	public ServerProtocolHandler() {
		Database database = new Database(connectionString, loginName, loginPwd);
		CommandHandler.setDatabase(database);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception 
	{
		
		Message inputMessage = Message.parseMessageFromString((String) message);		
		if(inputMessage != null)
		{
			MessageHeader header = inputMessage.getHeader();
			MessageBody body = inputMessage.getBody();
			
			switch(header.getProtocol())
			{
			case LOGIN:
				String user = body.getMessage().split("@")[0];
				String password = body.getMessage().split("@")[1];
				
				handleLogin(header.getThreadId(), user, 
						password, session);
				
				break;
				
			case REQUEST:
				handleResquest(inputMessage, session);
				break;
				
				
			case QUIT:
				handleQuit(header.getThreadId(), session);
				break;
				
			default:
				break;
			}
		}
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
	
	
	/* Handle LOGIN */
    private synchronized void handleLogin(final String threadId, final String user,
            final String password, final IoSession session)
    {
        executorService.execute(new Runnable() {

            @Override
            public void run()
            {
                if(LoginAgent.handleLogin(user, password))
                {
                    try 
                    {
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                    }
                    catch (InstantiationException e) 
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) 
                    {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) 
                    {
                        e.printStackTrace();
                    }
                    
                    Connection con = null;
                    
                    try 
                    {
                        con = DriverManager.getConnection(connectionString,loginName,loginPwd);
                        Statement stmt = con.createStatement();
                        String query = "SELECT role FROM RBAC WHERE login = '" + user + "' AND password = '" + password + "'";
                        ResultSet rs = stmt.executeQuery(query);
                        boolean next = rs.next();
                        
                        if((!users.contains(user)) && (next))
                        {
                            acceptSessionForUser(threadId, user, session, rs.getString("role"));
                        }
                        else if (!next) 
                        {
                            session.write(new Message(new MessageHeader(MessageProtocol.LOGIN, MessageStatus.ERROR, null, threadId),
                                    new MessageBody("Invalid user name/password")));
                        }
                        else
                        {
                            session.write(new Message(new MessageHeader(MessageProtocol.LOGIN, MessageStatus.ERROR, null, threadId),
                                    new MessageBody("User already in use")));
                        }

                    }
                    catch (SQLException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void acceptSessionForUser(String threadId, String user, IoSession session, String role)
    {
        users.add(user);
        sessions.add(session);
        session.setAttribute("user", user);
        session.write(new Message(new MessageHeader(MessageProtocol.LOGIN, MessageStatus.OK, null, threadId), new MessageBody(role)));
    } 
	
	
    private synchronized void handleResquest(final Message message, final IoSession session)
	{
    	executorService.execute(new Runnable() {
			
			@Override
			public void run() 
			{
				switch(message.getHeader().command)
				{				
				case find_job:
					new ListJobsCommandHandler().handle(message, session);
				break;
				
				case saw_sheet_cuts:
					new SawSheetCutsHandler().handle(message, session);
					break;
				
				case saw_sheet_save:
					new SawSheetSaveHandler().handle(message, session);
					break;
				
				case shift_log:
					new ShiftLogHanlder().handle(message, session);
					break;
					
				case shift_log_sheet_cuts:
					new ShiftLogSheetCutsHandler().handle(message, session);
					break;
					
				case shift_log_sheet_details:
					new ShiftLogSheetDetailsHandler().handle(message, session);
					break;
				
				case shift_log_save:
					new ShiftLogSaveHandler().handle(message, session);
					break;
					
				case save_shift_log_detail:
					new ShiftLogDetailSaveHandler().handle(message, session);
					break;
					
				case shift_log_save_line:
					new ShiftLogSheetSaveLine().handle(message, session);
					break;
					
				case operation_tally_sheet:
					new OpTallySheetHandler().handle(message, session);
					break;

					
				case employee_list:
					new EmployeeListHandler().handle(message, session);
					break;
					
				case return_users:
					new ReturnUsersHandler().handle(message, session);
					break;
					
				case delete_user:
					new DeleteUserHanddler().handle(message, session);
					break;
					
				case add_user:
				case modify_user:
					new ModifyUserHandler().handle(message, session);
					break;
					
				default:
					//TODO:
//					new UnknownCommandHandler().handle(message, session);
				break;
				}
			}
		});
	}
    
	public synchronized void handleQuit(String threadId, IoSession session)
	throws Exception
	{
		sessionClosed(session);
	}
	
	@Override
	public void sessionClosed(IoSession session)
	throws Exception 
	{
		String user = (String) session.getAttribute("user");
		users.remove(user);
		session.close(true);
		sessions.remove(session);
		
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
