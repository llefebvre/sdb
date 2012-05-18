package com.datagardens.nq.sdb.client.views.nav;

import java.text.MessageFormat;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class ModelOperationNavigationObject extends ModelNavigationObject 
{
	private int operationNumber;
	
	public ModelOperationNavigationObject(ModelNavigationObject parent, Job job, ShiftLogSheet sheet)
	{
		super(parent, MessageFormat.format("Operation {0}", sheet.getOperationNumber()),
				ImageKey.ICON_OPERATIONS, NavigationCategory.OPERATION);
		setData(job);
		
		operationNumber = sheet.getOperationNumber();
	}
	
	public int getOperationNumber() {
		return operationNumber;
	}
}
