package com.datagardens.nq.sdb.agent.jdbc;

public class ResultSetGenerator 
{
	private RowSet rowSet;
	public ResultSetGenerator(RowSet rowSet) 
	{
		this.rowSet = rowSet;
	}
		
	public String asString()
	{
		String string = "[[RESULTSET]]";
		int numOfRows = rowSet.length();
		
		string += numOfRows + "";
		string += "[COLUMNS]";
		
		Row row = rowSet.get(0);
		for(int i=0; i<row.length(); i++)
		{
			string += row.getKey(i).trim() + ",";
		}
		
		string = string.substring(0, string.length()-1);
		
		string += "[VALUES]";
		
		for(int i=0; i<numOfRows; i++)
		{
			row = rowSet.get(i);
			string += "["+i+"]";
			for(int j = 0; j<row.length(); j++)
			{
				string += row.get(j).trim() + ",";
			}
			string = string.substring(0, string.length()-1);
		}
		
		return string;
	}
}
