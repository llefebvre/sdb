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

public class SaveSawSheetAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.saveSawSheet";
	private static SaveSawSheetAction INSTANCE;
	
	public static SaveSawSheetAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new SaveSawSheetAction(window);
		return getActionReference();
	}
	
	public static SaveSawSheetAction getActionReference()
	{
		return INSTANCE;
	}
	///////////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////////
	private IWorkbenchWindow window;
	private SaveSawSheetAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Save Saw Sheet");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_SAVE));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		
	}

	@Override
	public void dispose() 
	{
		window.getSelectionService().removeSelectionListener(this);
	}
}
