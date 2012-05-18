package com.datagardens.nq.sdb.agent.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class Table
{
  private Database database;
  private String name;

  public Table( Database database, String name ) {
    this.database = database;
    this.name = name;
  }

  private RowSet execute( String criteria ) throws SQLException {
    Connection con = database.getConnection();
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery( "select * from "+name+
      (criteria==null?"":(" where "+criteria)) );
    ResultSetMetaData rsmd = rs.getMetaData();
    RowSet rows = new RowSet();
    int cols = rsmd.getColumnCount();
    while (rs.next()) {
      Row row = new Row();
      for (int i=0; i<cols; ++i) {
        String name = rsmd.getColumnName( i+1 );
        String value = rs.getString( i+1 );
        row.put( name, value );
      }
      rows.add( row );
    }
    con.close();
    return rows;
  }

  public Row getRow( String criteria ) throws SQLException {
    RowSet rs = execute( criteria );
    return rs.get( 0 );
  }

  public RowSet getRows( String criteria ) throws SQLException {
    RowSet rs = execute( criteria );
    return rs;
  }

  public RowSet getRows() throws SQLException {
    return getRows( null );
  }

  public ResultSet putRow( Row row ) throws SQLException {
    return putRow( row, null );
  }

  /**
   * returns the generated keys result set
   */
  public ResultSet putRow( Row row, String conditions) 
  throws SQLException 
  {
	
	  String query = MessageFormat.format("UPDATE {0} SET {1} ", name, getValuesPairs(row)) + (conditions == null ? "" : "WHERE " + conditions);
	  query += MessageFormat.format(" IF @@ROWCOUNT=0 INSERT INTO {0} ({1}) VALUES ({2})", name, getAffectedRowsKeys(row),  getAffectedRowsValues(row));
	  
	  System.out.println( "[SQL QUERY] "+ query );
	
	  Connection con = database.getConnection();
	  PreparedStatement st = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	  st.executeUpdate();
    
	  return st.getGeneratedKeys();
  }

	private String getValuesPairs(Row row) 
	{
		String pairs = "";
		for(int k= 0; k < row.length();k++)
		{
			pairs += row.getKey(k) + "='" + row.get(k) + "',";
		}
		
		return pairs.substring(0, pairs.length()-1);
	}

	private String getAffectedRowsKeys(Row row)
	{
		String keys = "";
		for(int k= 0; k < row.length();k++)
		{
			keys += row.getKey(k) + ",";
			
		}
		
		return keys.substring(0, keys.length()-1);
	}
	
	private String getAffectedRowsValues(Row row) 
	{
		String values = "";
		for(int k= 0; k < row.length();k++)
		{
			values += "'" + row.get(k) + "',";
			
		}
		
		return values.substring(0, values.length()-1);
	}
}
