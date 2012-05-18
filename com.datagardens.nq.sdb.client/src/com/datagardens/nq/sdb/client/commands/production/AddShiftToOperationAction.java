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
import com.datagardens.nq.sdb.client.views.nav.ModelOperationNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class AddShiftToOperationAction extends Action 
implements IWorkbenchAction, ISelectionListener
{

	///////////////////////////////////////////
	//CLASS
	///////////////////////////////////////////
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.addShift";
	private static AddShiftToOperationAction INSTANCE;
	
	public static AddShiftToOperationAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new AddShiftToOperationAction(window);
		return getActionReference();
	}
	
	public static AddShiftToOperationAction getActionReference()
	{
		return INSTANCE;
	}
	
	///////////////////////////////////////////
	//INSTANCE
	///////////////////////////////////////////

	private IWorkbenchWindow window;
	private ModelNavigationObject selectedNav;
	
	private AddShiftToOperationAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Add Shift");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_SHIFT_ADD));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection)
	{
		IStructuredSelection currentSelection = (IStructuredSelection) selection;
		Object selectedObject = currentSelection.getFirstElement();
		
		if(selectedObject  != null &&
				selectedObject instanceof ModelNavigationObject)
		{
			ModelNavigationObject navObj =
					(ModelNavigationObject) selectedObject;
			
			if(navObj.getMainCategory() != null && 
					(navObj.getSubCategory() == NavigationCategory.OPERATION ||
					navObj.getSubCategory() == NavigationCategory.SAW_SHEET))
			{
				selectedNav = navObj;			
				setEnabled(true);
			}
			else
			{
				setEnabled(true);
			}
		}
		else
		{
			setEnabled(true);
		}
	}

	@Override
	public void run()
	{
		if(selectedNav instanceof ModelNavigationObject)
		{
			System.out.println("runnung add");
			ModelNavigationObject navObj = (ModelNavigationObject) selectedNav;
			if(navObj.getMainCategory() != null)
			{
				if(navObj.getSubCategory() == NavigationCategory.OPERATION)
				{
					ModelOperationNavigationObject opVan = 
							(ModelOperationNavigationObject) navObj;
					
					Job job = (Job) opVan.getData();
					ShiftLogSheet sheet = job.getShiftLogSheet(opVan.getOperationNumber());
					sheet.setNumberOfShifts(sheet.getNumberOfShifts() + 1);
					try
					{
						NQDataModel.saveShiftLogSheet(sheet);
						navObj.fireUpdateEvent();
					}
					catch (Exception e) 
					{
						MessageDialog.openError(Display.getCurrent().getActiveShell(),
								getText(),
								"Cannot add shift: " + e.getMessage());
						e.printStackTrace();
					}
					
				}
				else if(navObj.getSubCategory() == NavigationCategory.SAW_SHEET)
				{
					Job selectedJob = (Job) selectedNav.getData();
					SawSheet sheet = selectedJob.getSawSheet();
					
					try
					{
						sheet.addShift();
						NQDataModel.saveSawSheet(sheet);
						selectedNav.fireUpdateEvent();
					}
					catch (ModelException e) 
					{
						MessageDialog.openError(Display.getCurrent().getActiveShell(),
								getText(),
								"Cannot add shift: " + e.getMessage());
						e.printStackTrace();
					}
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
