package com.datagardens.nq.sdb.server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.datagardens.nq.sdb.server.agentclient.SQLQuery;

public class Database
{
  private String uri, username, password;

  public Database( String uri, String username, String password ) {
    this.uri = uri;
    this.username = username;
    this.password = password;
  }

  public Table getTable( String name ) {
    return new Table( this, name );
  }

  Connection getConnection() throws SQLException {
    Connection connection =
      DriverManager.getConnection( uri, username, password );
    return connection;
  }
 
  public RowSet runQuery(SQLQuery query) 
  throws SQLException {
	  
	  System.out.println("[SQL QUERY] > " + query.asString());
	  PreparedStatement st = getConnection().prepareStatement(query.asString());
	  ResultSet rs = st.executeQuery();
	  ResultSetMetaData rsmd = rs.getMetaData();
	  RowSet rows = new RowSet();
	  
	  int cols = rsmd.getColumnCount();
	    while (rs.next()) 
	    {
	      Row row = new Row();
	      for (int i=0; i<cols; ++i) 
	      {
	        String name = rsmd.getColumnName( i+1 );
	        String value = rs.getString( i+1 );
	        row.put( name, value );
	      }
	      rows.add( row );
	    }
	    
	   return rows;
  }
  
  public void runDelete(String table, String conditions)
  throws SQLException
  {
	  PreparedStatement st = getConnection().prepareStatement("DELETE FROM " + table + " WHERE " + conditions);
	  st.executeUpdate();
  }
}