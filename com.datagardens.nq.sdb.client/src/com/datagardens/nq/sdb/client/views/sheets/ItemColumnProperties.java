package com.datagardens.nq.sdb.client.views.sheets;

import org.eclipse.swt.SWT;

import com.datagardens.nq.sdb.client.views.GridCellReference.CellDataType;

public class ItemColumnProperties 
{
	public String dynamicCellName;
	public String text;
	public int columnSpan;
	public int rowSpan;
	public int fontColorKey;
	public int fontStyle;
	public int fontSize;
	public int backGroundColorKey;
	public CellDataType dataType;
	
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, int rowSpan, int fontColorKey,
			int fontStyle, int fontSize, int backGroundColorKey, CellDataType dataType) 
	{
		this.dynamicCellName = dynamicCellName;
		this.text = text;
		this.columnSpan = columnSpan;
		this.rowSpan = rowSpan;
		this.fontColorKey = fontColorKey;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.backGroundColorKey = backGroundColorKey;
		this.dataType = dataType;
	}
	
	
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan, int fontColorKey,
			int fontStyle, int fontSize, int backGroundColorKey) 
	{
		this(null, text, columnSpan, rowSpan, fontColorKey, fontStyle, fontSize, backGroundColorKey,
				CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan, int fontColorKey,
			int fontStyle, int fontSize, int backGroundColorKey, CellDataType dataType) 
	{
		this(null, text, columnSpan, rowSpan, fontColorKey, fontStyle, fontSize, backGroundColorKey,
				dataType);
	}
	
	
	protected ItemColumnProperties(String text, int columnSpan, boolean inheritAll)
	{
		this(null, text, columnSpan, inheritAll);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, boolean inheritAll)
	{
		this(dynamicCellName, text, columnSpan, 0,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, 
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, boolean inheritAll, CellDataType dataType)
	{
		this(dynamicCellName, text, columnSpan, 0,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, 
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, dataType);
	}
	
	protected ItemColumnProperties(String text, int columnSpan)
	{
		this(null, text, columnSpan);
	}
	
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan)
	{
		this(null, text, columnSpan, rowSpan, true, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan,  CellDataType dataType)
	{
		this(null, text, columnSpan, rowSpan, true, dataType);
	}
	
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, int rowSpan)
	{
		this(dynamicCellName, text, columnSpan, rowSpan, true, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan)
	{
		this(dynamicCellName, text, columnSpan, 0, true, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, int rowSpan, boolean inheritAll, CellDataType dataType)
	{
		this(dynamicCellName, text, columnSpan, rowSpan,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, 
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, dataType);
	}
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan, boolean inheritAll)
	{
		this(null, text, columnSpan, rowSpan,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, 
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT,
				inheritAll ? SWT.INHERIT_FORCE: SWT.DEFAULT, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, int rowSpan, int fontColorKey, int backGroundColorKey)
	{
		this(dynamicCellName, text, columnSpan, rowSpan, fontColorKey, SWT.INHERIT_FORCE, SWT.INHERIT_FORCE, backGroundColorKey, CellDataType.TEXT);
	}
	
	protected ItemColumnProperties(String dynamicCellName, String text, int columnSpan, int rowSpan, int fontColorKey, int backGroundColorKey, 
			CellDataType dataType)
	{
		this(dynamicCellName, text, columnSpan, rowSpan, fontColorKey, SWT.INHERIT_FORCE, SWT.INHERIT_FORCE, backGroundColorKey, dataType);
	}
	
	protected ItemColumnProperties(String text, int columnSpan, int rowSpan, int fontColorKey, int backGroundColorKey)
	{
		this(null, text, columnSpan, rowSpan, fontColorKey, SWT.INHERIT_FORCE, SWT.INHERIT_FORCE, backGroundColorKey, CellDataType.TEXT);
	}
}
