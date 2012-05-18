package com.datagardens.nq.sdb.client.views;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Display;

import com.datagardens.nq.sdb.commons.model.NQDataModel;

public class GridCellReference 
{
	public static String YES = "YES";
	public static String NO = "NO";
	
	private GridItem item;
	private int column;
	private CellDataType dataType;
	private boolean selected;
	
	public GridCellReference(GridItem item, int column, CellDataType dataType) 
	{
		this.item = item;
		this.column = column;
		this.dataType = dataType;
	}
	
	public CellDataType getDataType() {
		return dataType;
	}
	
	public void setValue(final String value)
	{
		Display.getCurrent().syncExec(new Runnable() {
			
			@Override
			public void run() {
				item.setText(column, value);	
			}
		});
		
	}
	
	public void setValue(int value)
	{
		setValue(value + "");
	}
	
	public void setValue(float value)
	{
		setValue(value + "");
	}
	
	public void setValue(boolean value)
	{
		selected = value;
		setValue(value?YES:NO);
	}
	
	public String getValue()
	{
		if(dataType == CellDataType.INPUT_BOOLEAN )
		{
			return selected? YES : NO;
		}
		else
		{
			return item.getText(column);
		}
	}
	
	public boolean getBooleanValue()
	{
		if(dataType == CellDataType.INPUT_BOOLEAN)
		{
			return selected;
		}
		
		return false;
	}
	
	public void toggleBooleanValue()
	{
		selected = !selected;
		
		if(dataType==CellDataType.INPUT_BOOLEAN)
		{
			setValue(selected? YES:NO);
		}
	}
	
	public int getColumn() {
		return column;
	}

	public GridItem getItem() {
		return item;
	}
	
	public boolean isEditable() 
	{
		return dataType != CellDataType.TEXT;
	}
	
	
	public String validate(String value)
	{
		switch(dataType)
		{
		case INPUT_BOOLEAN:
			return "";
			
		case INPUT_TEXT:
			return "";
			
		case INPUT_NUMBER:
			return validateIsNumber(value);
			
		case INPUT_DATE:
			return validateIsDateInTheRightFormat(value);
			
		case INPUT_JOB_NUMBER:
			return validateIsJobNumber(value);
			
		case INPUT_PART_NUMBER:
			return validateIsPartNumber(value);
			
		case INPUT_EMPLOYEE_NUMBER:
			return vaidateEmployeeExist(value);
			
		default:
			return "";
		}
	}
	
	private String vaidateEmployeeExist(String value) 
	{
		try
		{
			if(NQDataModel.findEmployeeById(Integer.parseInt(value)) == null)
			{
				return "The employee " + value + " doesn't exist";
			}
		}
		catch(Exception e)
		{
			return "The value " + value + " is an invlid employee number";
		}
		
		
		return "";
	}

	private String validateIsPartNumber(String value) 
	{
		return "";
	}

	private String validateIsJobNumber(String value) 
	{
		return "";
	}

	private String validateIsDateInTheRightFormat(String value) 
	{
		Pattern datePattern = Pattern.compile("^([0-9]{4,4})-([0-9]{2,2})-([0-9]{2,2})$");
		Matcher m = datePattern.matcher(value);
		if(m.matches())
		{
			return "";
		}
		else
		{
			return value + " is and invalid date format";
		}
	}

	private String validateIsNumber(String value) 
	{
		try
		{
			Integer.parseInt(value);
			return "";
		}
		catch(Exception e)
		{
			return "Invalid value, not a number";
		}
	}

	public static enum CellDataType
	{
		TEXT,
		INPUT_BOOLEAN, 
		INPUT_DATE,
		INPUT_TEXT,
		INPUT_NUMBER,
		INPUT_JOB_NUMBER,
		INPUT_PART_NUMBER,
		INPUT_EMPLOYEE_NUMBER;
	}
}
