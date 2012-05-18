/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/13/2012
 *
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.commons.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonSyntaxException;

public class MessageParameters extends AbstractGsonMessagePart
{
	/////////////////////////////
	//CLASS
	/////////////////////////////
	public static MessageParameters parseFromString(String jsonString) 
	{
		return gson.fromJson(jsonString, MessageParameters.class);
	}
	
	//////////////////////////////
	//INSTANCE
	//////////////////////////////
	public Map<Integer, String> parameters;
	public String parametersClass;
	
	public MessageParameters(Class<?> paramtersClass) 
	{
		parameters = new HashMap<Integer, String>();
		this.parametersClass = paramtersClass.getCanonicalName();
	}
	
	public void add(Object object)
	{
		if(object.getClass().getCanonicalName().equals(parametersClass))
		{
			parameters.put(parameters.size(),
					gson.toJson(object));
		}
	}
	
	/**
	 * Returns an list of the parameters, they respect 
	 * the index order.
	 * @return <b>null</b> if there is not parameters in the list
	 */
	public List<Object> getAll()
	{
		if(parameters.size() == 0)
		{
			return null;
		}
		
		List<Object> list = new ArrayList<Object>();
		
		for(int i=0, imax=parameters.size(); i<imax; i++)
		{
			try
			{
				list.add(parseToObject(i));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		
		return list;
	}
	
	
	public Object getFirstElement()
	{
		try
		{
			return parseToObject(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int size()
	{
		return parameters.size();
	}
	
	private Object parseToObject(int index) 
	throws JsonSyntaxException, ClassNotFoundException
	{
		String jsonObject = parameters.get(index);
		
		if(jsonObject == null)
		{
			return null;
		}
		
		return gson.fromJson(jsonObject, getClass().getClassLoader().loadClass(parametersClass));
	}		
}
