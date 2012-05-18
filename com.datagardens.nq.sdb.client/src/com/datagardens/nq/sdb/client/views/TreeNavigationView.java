package com.datagardens.nq.sdb.client.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.adapters.ModelContentProvider;
import com.datagardens.nq.sdb.client.views.adapters.ModelLabelProvider;
import com.datagardens.nq.sdb.client.views.nav.INavigationChangesListener;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.client.views.nav.TreeNavigationController;

public class TreeNavigationView extends ViewPart {

	public static final String ID = "com.datagardens.nq.sdb.client.views.TreeNaviogationView";
	
	
	
	private TreeViewer navigationViewer;
	private TreeNavigationController controller;

	@Override
	public void createPartControl(Composite parent) 
	{
		
		
		Composite area = new Composite(parent, SWT.BORDER);
		GridLayout areaLayout = new GridLayout(1, true);
		areaLayout.marginHeight = 0;
		areaLayout.marginWidth = 0;
		area.setLayout(areaLayout);
		area.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label logo = new Label(area, SWT.NONE);
		logo.setImage(SWTResourceManager.getImage(ImageKey.LOGO));
		logo.setBackground(area.getBackground());
		
		navigationViewer = new TreeViewer(area, SWT.NONE);
		navigationViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		navigationViewer.setContentProvider(new ModelContentProvider());
		navigationViewer.setLabelProvider(new ModelLabelProvider());

		
		ViewerFilter navFilter = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) 
			{
				boolean showAll = true;
				
				for(ModelNavigationObject mainFolder : controller.getRoot().getChildren())
				{
					
					if(((ModelNavigationFolder) mainFolder).getChildren().size() > 0)
					{
						showAll = false;
						break;
					}
				}
				
				if(element instanceof ModelNavigationFolder)
				{
					return showAll || ((ModelNavigationFolder) element).getChildren().size() > 0;
				}
				
				return true;
			}
		};
		
		controller = TreeNavigationController.createNewController(navigationViewer);
		controller.addNavigationChangesListener(new INavigationChangesListener() {
			
			@Override
			public void itemsChanged() 
			{
				navigationViewer.refresh();
			}
		});
		
		navigationViewer.setFilters(new ViewerFilter[]{navFilter});
		
		navigationViewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				e.doit = false;
			};
		});
		
		getSite().setSelectionProvider(navigationViewer);
		navigationViewer.setInput(controller.createNavigationView());
		navigationViewer.setSelection(null);
	}
	
	@Override
	public void setFocus() {
	}
}
