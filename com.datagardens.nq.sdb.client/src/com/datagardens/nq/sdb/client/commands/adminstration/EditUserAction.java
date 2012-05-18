package com.datagardens.nq.sdb.client.commands.adminstration;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.dialogs.EditUserDialog;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;

public class EditUserAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	///////////////////////////////////////
	//CLASS
	//////////////////////////////////////
	public static final String ID = "com.datagardens.nq.sdb.client.commands.administration.editUser";
	
	private static EditUserAction INSTANCE;
	
	public static EditUserAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new EditUserAction(window);
		return getActionReference();
	}
	
	public static EditUserAction getActionReference()
	{
		return INSTANCE;
	}
	
	///////////////////////////////////////
	//INSTANCE
	//////////////////////////////////////
	private IWorkbenchWindow window;
	private SDBUser selectedUser;

	private EditUserAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Edit User");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_USER_EDIT));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		IStructuredSelection iss = (IStructuredSelection) selection;
		Object selected = iss.getFirstElement();
		
		if(selected instanceof SDBUser)
		{
			selectedUser = (SDBUser) selected;
		}
		else
		{
			selectedUser = null;
		}
		
		setEnabled(selected instanceof SDBUser);
	}
	
	@Override
	public void run() 
	{
		EditUserDialog dialog = new EditUserDialog(Display.getCurrent().getActiveShell(), selectedUser);
		if(dialog.open() == IDialogConstants.OK_ID)
		{
			SDBUser user = dialog.getUser();
			try
			{
				NQDataModel.editUser(user);
			}
			catch(ModelException e)
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						getText(), 
						e.getMessage());
			}	
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
