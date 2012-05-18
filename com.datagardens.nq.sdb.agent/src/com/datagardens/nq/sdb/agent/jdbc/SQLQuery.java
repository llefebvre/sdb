package com.datagardens.nq.sdb.agent.jdbc;

import java.util.List;
import java.util.Map;

public class SQLQuery 
{
	
	///////////////////////////////////////////////
	//CLASS
	///////////////////////////////////////////////
	
	public static final String QUERY = "[[QUERY]]";
	public static final String TABLE = "[TABLE]";
	public static final String COLUMNS = "[COLUMNS]";
	public static final String WHERE = "[WHERE]";
	public static final String IS = "[IS]";
	
	
	public static SQLQuery parse(String message)
	throws Exception
	{
		if(message.indexOf(QUERY) == -1)
		{
			throw new Exception("Invalid Query String: " + message);
		}
		
		/*String tableName = getTableName(message);
		
		if(tableName == null)
		{
			throw new Exception("Invalid Table name");
		}
		
		return new SQLQuery(tableName,
				getColumnNames(message),
				getConditions(message));*/
		
		String query = message.substring(
				message.indexOf(QUERY) + QUERY.length(),
				message.length());
		
		return new SQLQuery(query);
	}
	
	/*private static Map<String, String> getConditions(String message) 
	{
		if(message.indexOf(WHERE) == -1)
		{
			return null;
		}
		
		int from = message.indexOf(WHERE) + WHERE.length();
		int until = message.length();

		String [] conditions = message.substring(from, until).split("IS");
		String [] where      = conditions[0].substring(0, conditions[0].length()-1).split(",");
		String [] is         = conditions[1].substring(1, conditions[1].length()).split(",");
		
		if(where.length == is.length)
		{
			Map<String, String> condMap = new HashMap<String, String>();
			
			for(int i=0; i<where.length; i++)
			{
				condMap.put(where[i], is[i]);
			}
			
			return condMap;
		}
		
		return null;
			
	}

	private static List<String> getColumnNames(String message) 
	{
		int from = message.indexOf(COLUMNS) + COLUMNS.length();
		int until = message.indexOf(WHERE);
		
		if(from != until)
		{
			String columsString = message.substring(from, until);
			
			List<String> columns = new ArrayList<String>();
			
			for(String colName : columsString.split(","))
			{
				columns.add(colName);
			}
			
			return columns;
		}
		
		return null;
	}

	private static String getTableName(String message) 
	{
		int from = message.indexOf(TABLE) + TABLE.length();
		int until = message.indexOf(COLUMNS);
		
		if(from != until)
		{
			return message.substring(from, until);
		}
		
		return null;
	}*/

	///////////////////////////////////////////////
	//INSTNACE
	//////////////////////////////////////////////
	private String tableName;
	private List<String> columns;
	private Map<String, String> conditions;
	private String query;
	
	public SQLQuery(String tableName, List<String> columns, 
			 Map<String, String> conditions)
	{
		super();
		this.tableName = tableName;
		this.columns = columns;
		this.conditions = conditions;
	}
	
	public SQLQuery(String query) 
	{
		this.query = query;
	}
	
	public String asString() {
		return query;
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public List<String> getColumns() {
		return columns;
	}
	
	public Map<String, String> getConditions()
	{
		return conditions;
	}
	
	/*public boolean isColumnRequested(String key)
	{
		for(String k : columns)
		{
			if(k.equals(key))
			{
				return true;
			}
		}
		
		return false;
	}*/
	
	@Override
	public String toString() 
	{
		/*String string = "";
		string += "SELECT ";
		if(columns != null)
		{
			for(String column : columns)
			{
				string += column + ",";
			}
			
			string = string.substring(0, string.length()-1);//remove the last comma			
		}
		else
		{
			string += "* ";
		}
		
		string += " FROM ";
		string += tableName + " ";
		if(conditions != null)
		{
			string += "WHERE ";
			
			for(String key : conditions.keySet())
			{
				string +=key + "='" + conditions.get(key)+"',";
			}
			
			string = string.substring(0, string.length()-1);//remove the last comma
		}
		
		return string;*/
		return asString();
	}

	/*public String getConditionsAsString() 
	{
		String string = "";
		if(conditions != null)
		{
			for(String key : conditions.keySet())
			{
				string +=key + "='" + conditions.get(key)+"',";
			}
			
			string = string.substring(0, string.length()-1);//remove the last comma
		}
		
		return string;
	}*/
}
