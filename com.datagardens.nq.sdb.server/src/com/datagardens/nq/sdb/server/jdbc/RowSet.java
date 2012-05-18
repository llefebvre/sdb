package com.datagardens.nq.sdb.server.jdbc;

import java.util.Enumeration;
import java.util.Vector;

public class RowSet
{
  private Vector<Row> vector = new Vector<Row>();

  public RowSet() {
  }

  public void add( Row row ) {
    vector.addElement( row );
  }

  public int length() {
    return vector.size();
  }

  public Row get( int which ) {
    return (Row)vector.elementAt( which );
  }

  public void dump() {
    for (Enumeration<Row> e=vector.elements(); e.hasMoreElements();) {
      ((Row)e.nextElement()).dump();
    }
  }
}