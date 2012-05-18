package com.datagardens.nq.sdb.client.commands.reporting;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class IORReportAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	public static final String ID = "com.datagardens.nq.sdb.client.commands.reporting.ior";
	
	private static IORReportAction INSTANCE;
	
	public static IORReportAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new IORReportAction(window);
		return getActionReference();
	}
	
	public static IORReportAction getActionReference()
	{
		return INSTANCE;
	}
	
	private IWorkbenchWindow window;

	private IORReportAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("IOR Report");
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		//TODO: Implement
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
