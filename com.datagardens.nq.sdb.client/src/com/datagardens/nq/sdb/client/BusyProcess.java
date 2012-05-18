package com.datagardens.nq.sdb.client;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

public class BusyProcess 
{
	public static String errorMessage;
	public synchronized static void run(Display display, final Runnable runnable)
	throws Exception
	{
		errorMessage = "";
		BusyIndicator.showWhile(display, new Runnable() {
			
			@Override
			public void run() 
			{
				try
				{
					runnable.run();
				}
				catch(Exception e)
				{
					errorMessage = e.getMessage();
				}
			}
		});
		
		if(!errorMessage.isEmpty())
		{
			throw new Exception(errorMessage);
		}
	}
}
