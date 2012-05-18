/**
 * Copyright (c) 2012-2013. DataGardens Inc.
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.server.agentclient;

import java.util.HashMap;
import java.util.Map;

public class AgentProcess 
{
	private AgentProcessStatus status;
	private boolean breakTask;
	private Map<Integer, Map<String, String>> resultSet;
	private String errorMessage;
	
	public AgentProcess() {
		status = AgentProcessStatus.PROGRESS;
		errorMessage = "";
	}
	
	public boolean isComplete()
	{
		return status != AgentProcessStatus.PROGRESS;
	}
	
	public AgentProcessStatus getStatus() {
		return status;
	}
	
	protected void forceFinish()
	{
		breakTask = true;
	}
	
	public synchronized void setResponseMessage(String responseMessage, boolean isOk) 
	{
		System.out.println("setting response message");
		AgentProcessStatus status = processStatus(responseMessage);
		
		if(/*status == AgentProcessStatus.OK*/isOk)
		{
			resultSet = processResultSet(responseMessage);
		}
		else
		{
			errorMessage = processErrorMessage(responseMessage);
		}
		
		this.status = status;
		System.out.println("status= " + this.status);
	}
	
	private synchronized Map<Integer, Map<String, String>> processResultSet(String msg) 
	{
		Map<Integer, Map<String, String>> mapSet =
				new HashMap<Integer, Map<String,String>>();
		
		
		String numOfResultsString =  msg.substring(
				msg.indexOf("[[RESULTSET]]") + "[[RESULTSET]]".length(),
				msg.indexOf("[COLUMNS]"));
		
		int numOfResults = Integer.parseInt(numOfResultsString); 
		
		/*
		 * [COLUMNS]fjobno,fpartno,fjob_name,fquantity[VALUES][0]23235-0000,machining,Casing,2.00000
		 * [1]23235-0000,machining,Casing,2.00000[2]23235-0000,machining,Casing,2.00000
		 */
		String resultSetString = msg.substring(
				msg.indexOf("[[RESULTSET]]") + "[[RESULTSET]]".length(),
				msg.length());
		
		/*
		 * fjobno,fpartno,fjob_name,fquantity
		 */
		
		String columnsString = resultSetString.substring(
				resultSetString.indexOf("[COLUMNS]") + "[COLUMNS]".length(),
				resultSetString.indexOf("[VALUES]"));

		String [] cols = columnsString.split(",");
		
		/*
		 * [0]23235-0000,machining,Casing,2.00000[1]23235-0000,machining,Casing,2.00000
		 * [2]23235-0000,machining,Casing,2.00000
		 */
		String resultsString = resultSetString.substring(
				resultSetString.indexOf("[VALUES]") + "[VALUES]".length(),
				resultSetString.length());
		
		for(int i=0; i<numOfResults; i++)
		{
			int until = (i+1) == numOfResults ? resultsString.length() : 
				resultsString.indexOf("["+(i+1)+"]");
			
			String valStr = resultsString.substring(0, until);
			
			mapSet.put(i, getValuesMap(cols, valStr));
			
			resultsString = resultsString.substring(
					resultsString.indexOf(valStr) + valStr.length(),
					resultsString.length());
		}
		
		return mapSet;
	}

	private Map<String, String> getValuesMap(String [] cols, String valStr) 
	{
		Map<String, String> map = new HashMap<String, String>();
		
		valStr = valStr.substring(
				valStr.indexOf("]") + 1, 
				valStr.length());
		
		String [] vals = valStr.split(",",-1);
		
		for(int i=0; i<cols.length; i++)
		{
			map.put(cols[i], vals[i]);
		}
		
		return map;
	}

	private AgentProcessStatus processStatus(String msg) 
	{
		return AgentProcessStatus.OK;
	}

	private String processErrorMessage(String msg) 
	{
		return "Todo esta mal";
	}

	public String getErrorMesssage()
	{
		return errorMessage;
	}
	

	public void waitForRequestToComplete()
	{
		while(!isComplete() && !breakTask)
		{
			try
			{
				Thread.sleep(200);
			}
			catch(Exception e)
			{
				/* force to finish */
				forceFinish();
			}
		}
	}
	
	public Map<Integer, Map<String, String>> getResultSet() 
	{
		return resultSet;
	}
	
	/////////////////////////////////////
	// INNER CLASSES
	/////////////////////////////////////
	public static enum AgentProcessStatus
	{
		PROGRESS,OK,ERROR;
	}
}
