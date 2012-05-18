package com.datagardens.nq.sdb.client.commands.adminstration;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.dialogs.AddUserDialog;
import com.datagardens.nq.sdb.client.views.AdministrationView;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;

public class AddUserAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	///////////////////////////////////////
	//CLASS
	//////////////////////////////////////

	public static final String ID = "com.datagardens.nq.sdb.client.commands.administration.addUser";
	
	private static AddUserAction INSTANCE;
	
	public static AddUserAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new AddUserAction(window);
		return getActionReference();
	}
	
	public static AddUserAction getActionReference()
	{
		return INSTANCE;
	}
	
	///////////////////////////////////////
	//INSTANCE
	//////////////////////////////////////
	private IWorkbenchWindow window;

	private AddUserAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Add User");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_USER_ADD));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		//TODO: Implement
	}

	@Override
	public void run()
	{	
		AddUserDialog dialog = new AddUserDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		
		if(dialog.open() == IDialogConstants.OK_ID)
		{
			SDBUser user = new SDBUser(dialog.getUsername(), dialog.getPassword());
			
			try
			{
				NQDataModel.userAdd(user);
				AdministrationView.instance.fillViewer();
			}
			catch(ModelException e)
			{
				//TODO: pup-up error window
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
