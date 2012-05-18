package com.datagardens.nq.sdb.client.views.sheets;


public class SheetColumnInformation 
{
	public int width;
	public boolean checkable;
	public boolean cellSelectionEnabled;
	public EditorType editorType;
	
	public SheetColumnInformation(int width, boolean checkable,
			boolean cellSelectionEnabled, EditorType editorType)
	{
		this.width = width;
		this.checkable = checkable;
		this.cellSelectionEnabled = true;
		this.editorType = editorType;
	}
	
	public SheetColumnInformation(int width)
	{
		this(width, false, false, EditorType.TEXT);
	}
	
	public SheetColumnInformation(int width, EditorType type)
	{
		this(width, false, false, type);
	}
	
	public SheetColumnInformation(int width, boolean cellSelectionEnabled)
	{
		this(width, false, cellSelectionEnabled, EditorType.TEXT);
	}
	
	public SheetColumnInformation(int width, boolean cellSelectionEnabled, EditorType type)
	{
		this(width, false, cellSelectionEnabled, type);
	}
	
	public static enum EditorType
	{
		TEXT, DATE;
	}
}
