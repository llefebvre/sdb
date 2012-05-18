/**
 * Copyright (c) DataGardens Inc. 2012
 */
package com.datagardens.nq.sdb.client.views.sheets;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


public class ShiftLogViewer extends Composite {
	
	private TabFolder folder;
	private Map<Integer, TabItem> operationsMap;
	
	public ShiftLogViewer(Composite parent, int style) {
		super(parent, style);
		
		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 0;
		setLayout(layout);
		
		folder = new TabFolder(this, SWT.NONE);	
//		createOperationsTabs(4);
	}
	 
	public void createOperationsTabs(int numOfOperations)
	{
		operationsMap = new HashMap<Integer, TabItem>();
		
		
		for(int i=0; i<numOfOperations; i++)
		{
			int operationNumber = 20+(10*i);
			TabItem item = new TabItem(folder, SWT.NONE);
			item.setText(MessageFormat.format("Operation {0}", operationNumber));
			
			item.setControl(new ShifLogSheetView(folder, SWT.NONE, operationNumber));
			
			
			operationsMap.put(operationNumber, item);
		}
	}
}