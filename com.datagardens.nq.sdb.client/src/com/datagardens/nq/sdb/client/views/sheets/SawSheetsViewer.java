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
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.INavigationUpdateListener;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.commons.model.Job;

public class SawSheetsViewer
extends Composite 
implements INavigationUpdateListener
{
	private TabFolder folder;	
	private Job selectedJob;
	private Map<Integer, SawSheetView> shiftsMap;
	
	public SawSheetsViewer(Composite parent, int style)
	{
		super(parent, style);
		
		FillLayout layout = new FillLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
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
			SawSheetView view = (SawSheetView) shiftsMap.get(i);
			if(view == null)
			{
				view = new SawSheetView(folder, SWT.NONE, i);
				shiftsMap.put(i, view);
			}
			
			view.fillWithJob(selectedJob);
		}*/
	}

	public void updateShiftsTabs()
	{
		shiftsMap = new HashMap<Integer, SawSheetView>();
		
		if(folder != null && !folder.isDisposed())
		{
			folder.dispose();
		}
		
		
		folder = new TabFolder(this, SWT.NONE);
		folder.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(shiftsMap != null)
				{
					SawSheetView sawSheet = shiftsMap.get(folder.getSelectionIndex());
					
					if(sawSheet != null)
					{
						sawSheet.updateShiftInformation();
					}
				}
			}
		});
		
		for(int i=0; i<selectedJob.getSawSheet().getNumberOfShifts(); i++)
		{
			TabItem item = new TabItem(folder, SWT.NONE);
			item.setText(MessageFormat.format("Shift {0}", i+1));
			item.setImage(SWTResourceManager.getImage(ImageKey.ICON_SHIFT));
			SawSheetView view = new SawSheetView(folder, SWT.NONE, i);
			item.setControl(view);
			shiftsMap.put(i, view);
			
			view.fillWithJob(selectedJob);
		}
		
		shiftsMap.get(0).updateShiftInformation();
	}
	
	@Override
	public void update(ModelNavigationObject updatedObject) 
	{
		System.out.println("update object saw sheet = " + updatedObject);
		if(updatedObject.getSubCategory() == NavigationCategory.SAW_SHEET)
		{
			fillWithJob((Job) updatedObject.getData());
		}
	}
}
