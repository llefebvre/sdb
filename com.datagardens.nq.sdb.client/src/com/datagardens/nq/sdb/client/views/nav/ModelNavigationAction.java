package com.datagardens.nq.sdb.client.views.nav;

import org.eclipse.jface.action.Action;

public class ModelNavigationAction extends ModelNavigationObject 
{
	public ModelNavigationAction(INavigationChangesListener chainListener, Action action, 
			NavigationCategory category) 
	{
		this(null, action, category, null);
	}
	
	public ModelNavigationAction(INavigationChangesListener chainListener, Action action, 
			NavigationCategory category, Object data) 
	{
		super(action.getText(), action.getImageDescriptor(), category);
		
		if(data != null)
		{
			setData(data);			
		}	
	}
	
	public Action getAction()
	{
		return (Action) getData();
	}
}
