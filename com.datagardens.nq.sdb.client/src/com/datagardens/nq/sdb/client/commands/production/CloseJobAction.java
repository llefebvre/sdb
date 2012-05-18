package com.datagardens.nq.sdb.client.commands.production;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.client.views.nav.TreeNavigationController;
import com.datagardens.nq.sdb.commons.model.Job;

public class CloseJobAction extends Action 
implements IWorkbenchAction, ISelectionListener
{

	///////////////////////////////////////////
	//CLASS
	///////////////////////////////////////////
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.closeJob";
	private static CloseJobAction INSTANCE;
	
	public static CloseJobAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new CloseJobAction(window);
		return getActionReference();
	}
	
	public static CloseJobAction getActionReference()
	{
		return INSTANCE;
	}
	///////////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////////

	private IWorkbenchWindow window;
	private Job selectedJob;
	
	private CloseJobAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Close Job");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_CLOSE));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	}
	@Override
	public void run() 
	{
		if(selectedJob != null)
		{
			if(MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), getText(),
					"Are you sure you want to close the job '"+selectedJob.getId()+"'"))
			{
				TreeNavigationController.instance.removeJob(selectedJob);
			}
		}		
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection)
	{
		IStructuredSelection ss = (IStructuredSelection) selection;
		Object selectedObject = ss.getFirstElement();
		
		if(selectedObject  != null &&
				selectedObject instanceof ModelNavigationObject)
		{
			ModelNavigationObject navObj =
					(ModelNavigationObject) selectedObject;
			
			Job selectedJob = null;
			
			if(navObj.getMainCategory() != null && 
					navObj.getMainCategory() == NavigationCategory.PRODUCTION)
			{
				switch(navObj.getSubCategory())
				{
				case JOB:	
				case PRINT_JOB:
				case SAW_SHEET:
				case DATA_ENTRY:
				case SHIFT_LOG_SHEET:
				/*case OPERATION:*/
					selectedJob = (Job) navObj.getData();
				break;
				
				default:
					break;
				}
				
				if(selectedJob != null)
				{
					this.selectedJob = selectedJob; 
				}
			}
		}
	}

	@Override
	public void dispose()
	{
		this.window.getSelectionService().removeSelectionListener(this);
	}
}
