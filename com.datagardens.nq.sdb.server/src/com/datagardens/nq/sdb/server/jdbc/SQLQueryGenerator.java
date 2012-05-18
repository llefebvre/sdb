package com.datagardens.nq.sdb.server.jdbc;

import java.util.UUID;

public class SQLQueryGenerator 
{
	public static final String THREAD  = "[[THREAD]]"; 
	public static final String QUERY   = "[[QUERY]]";
	public static final String TABLE   = "[TABLE]";
	public static final String COLUMNS = "[COLUMNS]";
	public static final String WHERE   = "[WHERE]";
	public static final String IS      = "[IS]";
	
	private String query;
	private String threadId;
	
	/**
	 * Generates a proper message in the format <br>
	 * [[THREAD]][[QUERY]][TABLE]tableName[COLUMNS]col1,col2,col3,colN[WHERE]colA,colB,colC[IS]valA,valB,valC
	 * from an SELECT SQL query in the format <br>
	 * SELECT [*|col1,col2,col3,colN] FROM tableName [WHERE colA=valA,colB=valB]
	 * 
	 * @param query
	 */
	public SQLQueryGenerator(String query) 
	{
//		this.query = query.replaceAll("\\s", ""); //remove all blanks
		this.query = query;
		threadId = UUID.randomUUID().toString();
	}
	
	public String getThreadId() {
		return threadId;
	}
	
	public String generate()
	{
		String message = THREAD + threadId;
		message += QUERY ;
		
		message += query;
		
		/*boolean hasConditions = query.indexOf("WHERE") != -1;
		
		String tableName = query.substring(
				query.indexOf("FROM") + "FROM".length(),
				hasConditions?query.indexOf("WHERE"):query.length());
		
		message += TABLE;
		message += tableName;
		
		String requieredColmns = query.substring(
				"SELECT".length(),
				query.indexOf("FROM"));
		
		message += COLUMNS;
		
		if(requieredColmns.equals("*"))
		{
			message += "*";
		}
		else
		{
			String [] reqColNames = requieredColmns.split(",");
			
			for(int i = 0; i<reqColNames.length;i++)
			{
				message += reqColNames[i] + ",";
			}
			
			
			message = message.substring(0, message.length()-1); //remove last comma
		}
		
		if(hasConditions)
		{
			String conditionsPairs = query.substring(
					query.indexOf("WHERE") + "WHERE".length(),
					query.length());
			
			String [] conditionsArray = conditionsPairs.split(",");
			
			String conditionsCols = "";
			String conditionsVals = "";
			
			for(int i = 0; i<conditionsArray.length; i++)
			{
				conditionsCols += conditionsArray[i].split("=")[0] + ",";
				conditionsVals += conditionsArray[i].split("=")[1] + ",";
			}
			
			conditionsCols = conditionsCols.substring(0, conditionsCols.length()-1);
			conditionsVals = conditionsVals.substring(0, conditionsVals.length()-1);
			
			message += WHERE + conditionsCols;
			message += IS + conditionsVals;
		}*/
		
		return message;
	}

	private String escapedQuery() 
	{
	
		String q = query.replaceAll("'", "\u00039");
		return q;
	}
}
