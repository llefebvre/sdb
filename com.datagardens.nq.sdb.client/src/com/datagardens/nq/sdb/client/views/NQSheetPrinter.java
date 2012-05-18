package com.datagardens.nq.sdb.client.views;

import java.io.IOException;

import com.datagardens.nq.sdb.commons.model.NQSheet;

public class NQSheetPrinter 
{
	private static final String SAW_SHEET_TEMPLATE_LOCATION = "c:\\shaw_sheet.pdf";
	
	
	private NQSheet sheet;
	
	public NQSheetPrinter(NQSheet sheet)
	{
		this.sheet = sheet;
	}
	
	public void print(boolean fillInformation) 
	throws IOException
	{
		if(fillInformation)
		{
			if(sheet == null)
			{
				throw new IOException("Can only print blank sheets");
			}
			
			printUsingSheet();
		}
		else
		{
			printBlankSheet();
		}
	}

	private void printBlankSheet() 
	{
		
	}

	private void printUsingSheet() 
	{
		
	}
	
	
}
