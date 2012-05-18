package com.datagardens.nq.sdb.client.commands.production;

import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.datagardens.nq.sdb.client.dialogs.OpenJobTitleDialog;
import com.datagardens.nq.sdb.client.views.nav.TreeNavigationController;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;

public class OpenJobAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.openJob";

	private IWorkbenchWindow window;
	
	public OpenJobAction(IWorkbenchWindow window) {
		setId(ID);
		setActionDefinitionId(ID);
		setText("Open Job");
		this.window = window;
		window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void run() 
	{
		/*try {
			TreeNavigationController.instance.addJob(NQDataModel.findJobById("23535-0000"));
		} catch (ModelException e) {
			e.printStackTrace();
		}*/
		
		OpenJobTitleDialog dialog = new OpenJobTitleDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		
		if(dialog.open() == IDialogConstants.OK_ID)
		if(true)
		{
			try 
			{
				Job job = NQDataModel.findJobById(dialog.getJobId());
				
				if(job != null)
				{
					TreeNavigationController.instance.addJob(job);
				}
				else
				{
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Open Job", 
							MessageFormat.format("No job with id {0} was found.",
									dialog.getJobId()));
				}
			}
			catch (ModelException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		
	}
}
