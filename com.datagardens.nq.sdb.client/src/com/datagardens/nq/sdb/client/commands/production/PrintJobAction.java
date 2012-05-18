package com.datagardens.nq.sdb.client.commands.production;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SheetPrinter;
import com.datagardens.nq.sdb.client.views.EditorView;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.client.views.nav.ModelOperationNavigationObject;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class PrintJobAction extends Action 
implements IWorkbenchAction, ISelectionListener
{
	
	public static final String ID = "com.datagardens.nq.sdb.client.commands.production.printJob";
	private static PrintJobAction INSTANCE;
	
	public static PrintJobAction generateAction(IWorkbenchWindow window)
	{
		INSTANCE = new PrintJobAction(window);
		return getActionReference();
	}
	
	public static PrintJobAction getActionReference()
	{
		return INSTANCE;
	}
	
	private IWorkbenchWindow window;
	private ModelNavigationObject nav;

	private PrintJobAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId(ID);
		setActionDefinitionId(ID);
		setText("Print Job");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID, 
				ImageKey.ICON_PRODUCTION_PRINT));
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
	
	}
	
	@Override
	public void run() 
	{
		if(nav.getSubCategory() != null &&
				nav.getSubCategory() == NavigationCategory.SAW_SHEET)
		{
			Display.getCurrent().asyncExec(new Runnable() {
				
				@Override
				public void run()
				{
					Shell dialogsShell = Display.getCurrent().getActiveShell();
					final Job job = (Job) nav.getData();
					
					boolean confirm = MessageDialog.openConfirm(dialogsShell,
							getText(),
							"Are you sure you want to print the Saw Sheet for job " + job.getId());
							
					if(!confirm)
					{
						return;
					}
					
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(dialogsShell);
					try {
						dialog.run(true, true, new IRunnableWithProgress(){
							@Override
							public void run(IProgressMonitor monitor)
							throws InvocationTargetException, InterruptedException 
							{
								try 
								{
									monitor.beginTask("Printing Saw Sheet", 100);
									monitor.worked(25);
									monitor.subTask("Updating Job information...");
									Job updatedJob = NQDataModel.findJobById(job.getId());
									monitor.worked(25);
									monitor.subTask("Sending document to print...");
									
									SheetPrinter printer = new SheetPrinter(updatedJob, updatedJob.getSawSheet(), 
											EditorView.selectedShift);
									
									printer.print();
									monitor.done();
								}
								catch (Exception e) 
								{
									throw new InvocationTargetException(e);
								}
							}
						});
					}
					catch (InvocationTargetException e)
					{
						e.printStackTrace();
					}
					catch (InterruptedException ie) 
					{
						ie.printStackTrace();
					}
					
					
				}
			});
		}
		else if(nav.getSubCategory() != null &&
				nav.getSubCategory() == NavigationCategory.OPERATION)
		{
			
			Display.getCurrent().asyncExec(new Runnable(){
				
				@Override
				public void run() 
				{
					ModelOperationNavigationObject navOp = (ModelOperationNavigationObject) nav;
					Job job =  (Job) navOp.getData();
					ShiftLogSheet sheet = job.getShiftLogSheet(navOp.getOperationNumber());
					
					boolean confirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(),
							getText(),
							"Are you sure you want to print the Shift Log Sheet for operation " + sheet.getOperationNumber());
							
					if(!confirm)
					{
						return;
					}
					
					/*ShiftLogSheet sheet;
					try 
					{
						sheet = NQDataModel.getShiftLog(sheet);
						SheetPrinter printer = new SheetPrinter(job, sheet);
						System.out.println("printing....");
						printer.print();
						System.out.println("done !");
					}
					catch (Exception e) 
					{
						e.printStackTrace();
					}*/
				}
			});	
		}
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		IStructuredSelection ss = (IStructuredSelection) selection;
		
		
		if(ss != null && ss.getFirstElement() != null
				&& ss.getFirstElement() instanceof ModelNavigationObject)
		{
			nav = (ModelNavigationObject) ss.getFirstElement();
			
			setEnabled(nav.getSubCategory() != null &&
					(nav.getSubCategory() == NavigationCategory.SAW_SHEET || 
					nav.getSubCategory() == NavigationCategory.OPERATION));
		}
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
