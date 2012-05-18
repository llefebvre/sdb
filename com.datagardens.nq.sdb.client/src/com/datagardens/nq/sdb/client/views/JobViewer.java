package com.datagardens.nq.sdb.client.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.commands.production.AddShiftToOperationAction;
import com.datagardens.nq.sdb.client.commands.production.CloseJobAction;
import com.datagardens.nq.sdb.client.commands.production.PrintJobAction;
import com.datagardens.nq.sdb.client.commands.production.RemoveShiftFromOperationAction;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.client.views.nav.ModelOperationNavigationObject;
import com.datagardens.nq.sdb.client.views.sheets.ShiftLogSheetViewer;
import com.datagardens.nq.sdb.client.views.sheets.SawSheetsViewer;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;

public class JobViewer extends Composite
implements ISelectionListener
{

	private FormToolkit formToolkit;
	private Form areaForm;
	
	private StackedComposite area;
	private PrintJobView printView;
	private JobGraphicView graphicView;
	private Map<Integer, ShiftLogSheetViewer> operationsViews;
	private SawSheetsViewer sawSheetsViewer;
	private IWorkbenchWindow window;
	
	private Job selectedJob;
	
	public JobViewer(Composite parent, int style, IWorkbenchWindow window) {
		super(parent, style);
		
		this.window = window;
		this.window.getSelectionService().addSelectionListener(this);
		
		setLayout(new FillLayout());
		
		formToolkit = new FormToolkit(PlatformUI.getWorkbench().getDisplay());
		areaForm = formToolkit.createForm(this);
		areaForm.setSeparatorVisible(true);
		formToolkit.paintBordersFor(areaForm);
		
		ToolBarManager manager =  (ToolBarManager) areaForm.getToolBarManager();
		formToolkit.decorateFormHeading(areaForm);
		
		manager.add(PrintJobAction.getActionReference());
		manager.add(new Separator());
		manager.add(AddShiftToOperationAction.getActionReference());
		manager.add(RemoveShiftFromOperationAction.getActionReference());
		manager.add(new Separator());
		manager.add(CloseJobAction.getActionReference());
		manager.update(true);
		
		areaForm.setTextBackground(new Color[] {
				SWTResourceManager.getColor(79,129,189),
				SWTResourceManager.getColor(56,93,138)}, 
				new int[] {100},
				true);
		areaForm.setSeparatorVisible(true);
		areaForm.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		areaForm.setText("job viewer");
		
		areaForm.getBody().setLayout(new FillLayout());
		area = new StackedComposite(areaForm.getBody(), SWT.NONE);
		
		printView = new PrintJobView(area, SWT.NONE, this.window);
		graphicView = new JobGraphicView(area, SWT.NONE);
		sawSheetsViewer = new SawSheetsViewer(area, SWT.NONE);
		operationsViews = new HashMap<Integer, ShiftLogSheetViewer>();
		
		area.setTopComposite(sawSheetsViewer);
	}
	
	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object selected = structuredSelection.getFirstElement();
		
		if(selected instanceof ModelNavigationObject)
		{
			
			ModelNavigationObject navObj = (ModelNavigationObject) selected;
			NavigationCategory category = navObj.getSubCategory();
			
			if(category != null)
			{
				switch(category)
				{
					case JOB:
					if(allChangesAreSaved())
					{
						fillWithNewJob(navObj.getData());
					}
					
					break;
					
					case PRINT_JOB:
						printView.fillWithJob((Job) navObj.getData());
						area.setTopComposite(printView);
						break;
						
					case SAW_SHEET:
						System.out.println("SAW SHEET area.setTopComposite(sawSheetsViewer");
						fillWithNewJob((Job) navObj.getData());
						navObj.setUpdateListener(sawSheetsViewer);
						area.setTopComposite(sawSheetsViewer);
						break;
						
					case OPERATION:
						
						ModelOperationNavigationObject opNavObj = 
								(ModelOperationNavigationObject) navObj;
						
						ShiftLogSheetViewer view = 
								operationsViews.get(opNavObj.getOperationNumber());
						
						if(view == null)
						{
							view = new ShiftLogSheetViewer(area, SWT.NONE, opNavObj.getOperationNumber());
							operationsViews.put(opNavObj.getOperationNumber(), view);
						}
						
						fillWithNewJob(opNavObj.getData());
						System.out.println("setting opNavObj.setUpdateListener(view)");
						opNavObj.setUpdateListener(view);
						area.setTopComposite(view);
						
						break;
						
					case SHIFT_LOG_SHEET:
						break;
						
					default:
						area.setTopComposite(graphicView);
						break;
				}
			}
		}
	}

	private boolean allChangesAreSaved() {
		return true;
	}

	private void fillWithNewJob(Object data)
	{
		Job job = (Job) data;
		areaForm.setText("Job #" + job.getId());
		printView.fillWithJob(job);
		sawSheetsViewer.fillWithJob(job);
		area.setTopComposite(graphicView);
	}   

}
