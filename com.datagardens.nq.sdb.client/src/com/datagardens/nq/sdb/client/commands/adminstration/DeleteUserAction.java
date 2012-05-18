package com.datagardens.nq.sdb.client.commands.adminstration;

import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.views.AdministrationView;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;

public class DeleteUserAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	///////////////////////////////////////
	//CLASS
	//////////////////////////////////////
	public static final String ID = "com.datagardens.nq.sdb.client.commands.administration.deleteUser";
	
	private static DeleteUserAction INSTANCE;
	
	public static DeleteUserAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new DeleteUserAction(window);
		return getActionReference();
	}
	
	public static DeleteUserAction getActionReference()
	{
		return INSTANCE;
	}
	
	///////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////
	private IWorkbenchWindow window;
	private SDBUser selectedUser;

	private DeleteUserAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Delete User");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_USER_DELETE));
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
		if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				getText(),
				MessageFormat.format("Are you you want to delete user {0}",
						selectedUser.getUsername())))
		{
			BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(),new Runnable() {
				
				@Override
				public void run() {
					try
					{
						NQDataModel.userDelete(selectedUser);
						AdministrationView.instance.fillViewer();
					}
					catch(ModelException e)
					{
						//TODO: pup-up window
						e.printStackTrace();
					}
				}
			});
			
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
