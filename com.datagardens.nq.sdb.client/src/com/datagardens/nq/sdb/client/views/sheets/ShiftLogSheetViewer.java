package com.datagardens.nq.sdb.client.views.sheets;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.EditorView;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.INavigationUpdateListener;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class ShiftLogSheetViewer extends Composite 
implements /*ISelectionListener, */INavigationUpdateListener
{

	private Job selectedJob;
	private TabFolder folder;
	private Map<Integer, ShifLogSheetView> shiftsMap;
	private JobTallySheetView tallyView;
	private int operationIndex;
	
	public ShiftLogSheetViewer(Composite parent, int style,int opreationIndex)
	{
		
		super(parent, style);
		
		this.operationIndex = opreationIndex;
		
		FillLayout layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 0;
		setLayout(layout);
	
		folder = new TabFolder(this, SWT.NONE);
	}
	public void fillWithJob(Job job)
	{		
		selectedJob = job;
		updateShiftsTabs();
		updateShiftTables();
		update();
		layout();
	}
	
	private void updateShiftTables() 
	{
		/*for(int i=0; i<selectedJob.getSawSheet().getNumberOfShifts(); i++)
		{
			ShifLogSheetView view = (ShifLogSheetView) shiftsMap.get(i);
			
			if(view == null)
			{
				view = new ShifLogSheetView(folder, SWT.NONE, i);
				shiftsMap.put(i, view);
			}
			
			view.fillWithJob(selectedJob, );
		}*/
	}

	public void updateShiftsTabs()
	{
		
		shiftsMap = new HashMap<Integer, ShifLogSheetView>();
		
		if(folder != null && !folder.isDisposed())
		{
			folder.dispose();
			folder = null;
		}
		
		folder = new TabFolder(this, SWT.NONE);
		folder.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(shiftsMap != null)
				{
					if(folder.getSelectionIndex() < shiftsMap.size())
					{
						EditorView.selectedShift = folder.getSelectionIndex();
						
						ShifLogSheetView shiftLogView = shiftsMap.get(folder.getSelectionIndex());
						
						if(shiftLogView != null)
						{
							shiftLogView.updateShiftInformation();
						}
					}
					else
					{
						if(tallyView != null)
						{
							tallyView.updateTallyInformation(selectedJob, operationIndex);
						}
					}
					
				}
			}
		});
		
		ShiftLogSheet sheet = 
				selectedJob.getShiftLogSheet(operationIndex);
	
		
		for(int i=0; i<sheet.getNumberOfShifts(); i++)
		{
			TabItem item = new TabItem(folder, SWT.NONE);
			item.setText(MessageFormat.format("Shift {0}", i+1));
			item.setImage(SWTResourceManager.getImage(ImageKey.ICON_SHIFT));
		
			ShifLogSheetView view = new ShifLogSheetView(folder, SWT.NONE, i);
			item.setControl(view);
			shiftsMap.put(i, view);
			view.fillWithJobAndSheet(selectedJob, sheet);
		}
		if(shiftsMap.get(0) != null)
		{
			shiftsMap.get(0).updateShiftInformation();
		}
		
		TabItem tally = new TabItem(folder, SWT.NONE);
		tally.setText("Operation Tally");
		tallyView = new JobTallySheetView(folder, SWT.NONE);
		tally.setControl(tallyView);
	}
	
	
	/*public void createShiftsTabs(int numOfOperations)
	{
		if(operation == null || shiftsMap != null)
		{
			return;
		}
		
		if(shiftsMap == null)
		{
			shiftsMap = new HashMap<Integer, TabItem>();
		}
		
		for(int i=0; i<numOfOperations; i++)
		{
			TabItem item = new TabItem(folder, SWT.NONE);
			item.setText(MessageFormat.format("Shift {0}", folder.getItemCount()));
			item.setControl(new ShifLogSheetView(folder, SWT.NONE, operation.getOperationNumber()));
			item.setImage(SWTResourceManager.getImage(ImageKey.ICON_SHIFT));
			
			shiftsMap.put(i, item);
		}
		
		TabItem tallyItem = new TabItem(folder, SWT.NONE);
		tallyItem.setText("Job Tally");
		tallyItem.setControl(new JobTallySheetView(folder, SWT.NONE));
		
		update();
		layout();
	}*/
	
	public void fillWithOperation(Job job, Operation operation)
	{
		/*
		this.operation = operation;
		
		System.out.println("folder.getItemCount()= " + (folder.getItemCount()-1) + ", operation.getTotalShifts()= " + operation.getTotalShifts());
		
		if((folder.getItemCount()-1) < operation.getTotalShifts())
		{
			System.out.println("creating shifts");
			//If the tabs are not created yet
			createShiftsTabs(operation.getTotalShifts() - (folder.getItemCount()-1));
		}
		
		//Tabs exist, fill them with the new information
		try
		{
			ShiftLogSheet shiftLog = NQDataModel.getShiftLog(operation);
			
			ShifLogSheetView sheetView = (ShifLogSheetView) folder.getTabList()[0];
			sheetView.fillWithSheet(job, shiftLog);
		}
		catch(ModelException e)
		{
			MessageDialog.openError(getShell(), "Shift Log", e.getMessage());
		}*/
	}
	
	/*@Override
	public void update(ModelNavigationObject updatedObject) 
	{
		if(updatedObject.getSubCategory() == NavigationCategory.OPERATION)
		{
			Operation operation = (Operation) updatedObject.getData();
			Job job = (Job) updatedObject.getParent().getParent().getData();
			fillWithOperation(job, operation);
		}
	}*/
	
	@Override
	public void update(ModelNavigationObject updatedObject) 
	{
		tallyView = null;
		if(updatedObject.getSubCategory() == NavigationCategory.OPERATION)
		{
			fillWithJob((Job) updatedObject.getData());
		}
	}
	
	@Override
	public void dispose()
	{
//		window.getSelectionService().removeSelectionListener(this);
		super.dispose();
	}

	/*@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		System.out.println("selection has changed from opreation");
		
		IStructuredSelection ss = (IStructuredSelection) selection;

		if(ss.getFirstElement() != null 
				&& ss.getFirstElement() instanceof ModelNavigationObject
				&& ((ModelNavigationObject)ss.getFirstElement()).getSubCategory() == NavigationCategory.OPERATION)
		{
			ModelNavigationObject operationNavObj = (ModelNavigationObject) ss.getFirstElement();
			
			
			Operation selectedOperation = (Operation) operationNavObj.getData();
			Job selectedJob = (Job) operationNavObj.getParent().getParent().getData();
			
			if(selectedOperation.getOperationNumber() == operationIndex)
			{
//				fillWithOperation(selectedJob, selectedOperation);
			}
		}
	}*/
}
