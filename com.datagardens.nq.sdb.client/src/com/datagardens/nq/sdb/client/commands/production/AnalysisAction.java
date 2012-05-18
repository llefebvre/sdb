package com.datagardens.nq.sdb.client.commands.production;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;

public class AnalysisAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.analysis";
	
	private static AnalysisAction INSTANCE;
	
	public static AnalysisAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new AnalysisAction(window);
		return getActionReference();
	}
	
	public static AnalysisAction getActionReference()
	{
		return INSTANCE;
	}
	
	
	private IWorkbenchWindow window;

	private AnalysisAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Analysis");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_PRODUCTION_ANALYSIS));
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
