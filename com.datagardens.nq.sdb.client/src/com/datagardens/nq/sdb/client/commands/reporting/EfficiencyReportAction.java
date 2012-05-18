package com.datagardens.nq.sdb.client.commands.reporting;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class EfficiencyReportAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	public static final String ID = "com.datagardens.nq.sdb.client.commands.reporting.efficiency";
	
	private IWorkbenchWindow window;

	public EfficiencyReportAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Efficiency");
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
