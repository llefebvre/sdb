package com.datagardens.nq.sdb.agent.jdbc;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Row
{
  private Vector<String> ordering = new Vector<String>();
  private Hashtable<String, String> hashtable = new Hashtable<String, String>();

  public Row() {
  }

  public void put( String name, String value ) {
    if (!hashtable.containsKey( name))
      ordering.addElement( name );
    hashtable.put(name, value == null ? "" : value);
  }
  
  public void put( String name, int value ) {
	  put(name, value+"");
  }
  
  public void put( String name, float value ) {
	  put(name, value+"");
  }
  
  public void put( String name, boolean value ) {
	  put(name, value?"True":"False");
  }

  public int length() {
    return hashtable.size();
  }

  public String get( String name ) {
    return (String)hashtable.get( name );
  }

  public String get( int which ) {
    String key = (String)ordering.elementAt( which );
    return (String)hashtable.get( key );
  }

  public String getKey( int which ) {
    String key = (String)ordering.elementAt( which );
    return key;
  }

  public void dump() {
    for (Enumeration<String> e=hashtable.keys(); e.hasMoreElements();) {
      String name = (String)e.nextElement();
      String value = (String)hashtable.get( name );
      System.out.print( name+"="+value+", " );
    }
    System.out.println( "" );
  }
}