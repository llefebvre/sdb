package com.datagardens.nq.sdb.server.agentclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.datagardens.nq.sdb.commons.model.Employee;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;

public class AgentParsingUtils 
{
	public synchronized static Job parseJob(Map<String, String> resultSet) 
	{
		Job job = new Job(
				resultSet.get("job_no"), 
				new Date(),
				parseInteger(resultSet.get("job_quantity")),
				resultSet.get("part_number"),
				resultSet.get("drawing_number"),
				resultSet.get("part_name"));
		return job;
	}
	
	public static List<Operation> parseOperationsList(Map<Integer, Map<String, String>> resultSet) 
	{
		List<Operation> list = new ArrayList<Operation>();
		
		for(Map<String, String> map : resultSet.values())
		{
			list.add(new Operation(
					map.get("job_no"),
					parseInteger(map.get("operation_no")),
					parseFloat(map.get("setup_time")),
					parseFloat(map.get("cycle"))));
		}
		
		return list;
	}
	
	public static List<Employee> parseEmployeeList(Map<Integer, Map<String, String>> resultSet) 
	{
		List<Employee> list = new ArrayList<Employee>();
		
		for(Map<String, String> result : resultSet.values())
		{
			list.add(new Employee(
					parseInteger(result.get("emp_no")),
					result.get("first_name"),
					result.get("last_name")));
		}
			
		return list;
	}
	
	
	public static Map<Integer, Float> parseOperationTimes(Map<Integer, Map<String, String>> resultSet) 
	{
		Map<Integer, Float> times = new HashMap<Integer, Float>();
		
		if(resultSet != null)
		{
			for(Map<String, String> result : resultSet.values())
			{

				times.put(parseInteger(result.get("emp_no")), 
						parseFloat(result.get("emp_oper_time")));
			}
		}
		
		return times;
	}
	
	private synchronized static int parseInteger(String value)
	{
		
		if(value== null)
		{
			return 0;
		}
		
		try
		{
			return Integer.parseInt(value);
		}
		catch(Exception e)
		{
			try
			{
				/* try to normalize float numbers 
				 * to use them as integers */
				if(value.indexOf(".") != -1)
				{
					value = value.substring(0, value.indexOf("."));
					return Integer.parseInt(value);
				}
				
				return 0;
			}
			catch(Exception ex)
			{
				e.printStackTrace();
			}
			
			return 0;
		}
	}
	
	private synchronized static float parseFloat(String value)
	{
		if(value== null)
		{
			return 0f;
		}
		
		try
		{
			return Float.parseFloat(value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0f;
		}
	}
	
	private synchronized static boolean parseBoolean(String value)
	{
		return false;
	}
	
	private synchronized static Date parseDate(String value)
	{
		return new Date();
	}		
	
}
