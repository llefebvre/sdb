package com.datagardens.nq.sdb.server.agentclient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
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

import com.datagardens.nq.sdb.commons.model.Employee;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.server.agentclient.AgentProcess.AgentProcessStatus;
import com.datagardens.nq.sdb.server.jdbc.SQLQueryGenerator;

public class AgentSession implements SimpleCallBack {
	
	////////////////////////////////////////////////
	// CLASS
	////////////////////////////////////////////////
	public static AgentSession open()
	throws AgentException
	{
		AgentSession newSession = new AgentSession();
		newSession.connect(new InetSocketAddress("nq007"/*"localhost"*/, 1235), 
				false);	
		return newSession;
	}
	
	////////////////////////////////////////////////
	// INSTANCE
	////////////////////////////////////////////////
	private IoSession session;
	private IoHandler handler;
	
	private Map<String, AgentProcess> agentProcesses;
	
	private AgentSession() 
	{
		this.handler = new AgentSessionHandlerAdapter(this);
		agentProcesses = new HashMap<String, AgentProcess>();
	}
	
	public void connect(SocketAddress address, boolean useSsl)
	throws AgentException
	{
		NioSocketConnector connector = new NioSocketConnector();	
		if(session != null && session.isConnected())
		{
			throw new IllegalStateException("Already connected.");
		}
		
		try
		{
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
				throw new Exception("Session already in use");
			}
			session = future1.getSession();
		}
		catch(Exception e)
		{
			throw new AgentException(e.getMessage());
		}
	}
	
	
	public Job findJobById(String jobId) 
	throws AgentException
	{
		String query= "SELECT jomast.fjobno  AS job_no, jomast.fpartno AS part_number, joitem.fdesc   AS part_name, joitem.fmqty   AS job_quantity," +
				" inmast.fdrawno AS drawing_number  FROM jomast LEFT OUTER JOIN joitem ON " +
				"(jomast.fjobno  = joitem.fjobno AND jomast.fpartno = joitem.fpartno) LEFT OUTER " +
				"JOIN inmast ON (joitem.fpartno = inmast.fpartno) WHERE jomast.fjobno ='"+jobId+"' ORDER BY jomast.job_no";
		
		AgentProcess process = runSQL(query);
		process.waitForRequestToComplete();
		if(process.getStatus() == AgentProcessStatus.OK)
		{
			Map<Integer, Map<String, String>> resultSet = process.getResultSet();
			return AgentParsingUtils.parseJob(resultSet.get(0));
		}
		else
		{
			throw new AgentException(process.getErrorMesssage());
		}
	}
	
	public List<Operation> getOperationsForJob(String jobId) 
	throws AgentException
	{
		String query = "SELECT fjobno AS job_no, foperno AS operation_no, fsetuptime AS setup_time, " +
				"fuprodtime, foperqty, (fuprodtime * foperqty) AS cycle FROM jodrtg " +
				"WHERE fjobno = '"+ jobId +"' ORDER by job_no, operation_no";
		
		AgentProcess process = runSQL(query);
		process.waitForRequestToComplete();
		if(process.getStatus() == AgentProcessStatus.OK)
		{
			Map<Integer, Map<String, String>> resultSet = process.getResultSet();
			return AgentParsingUtils.parseOperationsList(resultSet);
		}
		else
		{
			throw new AgentException(process.getErrorMesssage());
		}
	}
	
	public List<Employee> getEmployeeList() 
	throws AgentException
	{
		String query = "SELECT fempno AS emp_no, fname  AS last_name, ffname AS first_name " +
				"FROM prempl WHERE fendate = '1900-01-01 00:00:00' ORDER BY fempno";
		
		AgentProcess process = runSQL(query);
		process.waitForRequestToComplete();
		if(process.getStatus() == AgentProcessStatus.OK)
		{
			Map<Integer, Map<String, String>> resultSet = process.getResultSet();
			return AgentParsingUtils.parseEmployeeList(resultSet);
		}
		else
		{
			throw new AgentException(process.getErrorMesssage());
		}
	}
		
	private AgentProcess runSQL(String query)
	{
		SQLQueryGenerator generator = new SQLQueryGenerator(query);
		AgentProcess process = new AgentProcess();
		agentProcesses.put(generator.getThreadId(), process);
		session.write(generator.generate());
		
		return process;
	}
	
	@Override
	public void messageRecived(String message) 
	{
		boolean isOkMessage = message.indexOf("[[OK]]") != -1;
		
		String originalTreadId = message.substring(
				message.indexOf("[[THREAD]]") + "[[THREAD]]".length(),
				isOkMessage? message.indexOf("[[OK]]") : message.indexOf("[[ERROR]]"));
		
		AgentProcess process = agentProcesses.get(originalTreadId);
		if(process != null)
		{
			process.setResponseMessage(message, isOkMessage);
		}
	}

	public Map<Integer, Float> getOperatorTimesForOperation(Operation op)
	throws AgentException 
	{
		String query = "SELECT fjobno  AS job_no, foperno AS operation_no, fempno  AS emp_no, " +
				"SUM(ROUND(datediff(second,fsdatetime,fedatetime)/3600,0) + ROUND((datediff(second,fsdatetime,fedatetime)%3600)/60,0)/60.0 ) AS emp_oper_time " +
				"FROM ladetail WHERE fjobno = '"+op.getJobId()+"'  AND foperno = '"+op.getOperationNumber()+"' GROUP by fjobno, foperno, fempno ";
		
		AgentProcess process = runSQL(query);
		process.waitForRequestToComplete();
		if(process.getStatus() == AgentProcessStatus.OK)
		{
			Map<Integer, Map<String, String>> resultSet = process.getResultSet();
			return AgentParsingUtils.parseOperationTimes(resultSet);
		}
		else
		{
			throw new AgentException(process.getErrorMesssage());
		}
	}

}
