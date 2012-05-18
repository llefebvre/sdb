package com.datagardens.nq.sdb.client.views;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.Navigation.IProductionItem;
import com.datagardens.nq.sdb.client.views.Navigation.JobNavigationFolder;
import com.datagardens.nq.sdb.client.views.Navigation.JobNavigationItem;
import com.datagardens.nq.sdb.client.views.Navigation.NavigationFolder;
import com.datagardens.nq.sdb.client.views.Navigation.NavigationItem;
import com.datagardens.nq.sdb.commons.model.Job;

public class MainConsoleView extends ViewPart {

	public static final String ID = "com.datagardens.nq.sdb.client.views.mainConsoleView";
	
	private SashForm navigationAndAreaSash;
	private Composite navigationComp;
	private Composite mainAreaComposite;
	private Composite contentComposite;
	private TreeViewer treeViewer;
	
	private Navigation navigation;
	
	private StackLayout stack = new StackLayout();
	private WelcomePage welcomePage;
//	private Map<String, JobView> jobViews;
	private JobGraphicView jobView;
	private PrintJobView printView;
	
	private Job selectedJob;
	
	private ViewerFilter productionFilter;
	
	
	public static MainConsoleView instance;
	
	
	
	
	@Override
	public void createPartControl(Composite parent) 
	{
		instance = this;
		
		productionFilter = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) 
			{
				return (element instanceof IProductionItem);
			}
		};
		
		
		navigation = Navigation.getInstance();
		
		navigationAndAreaSash = new SashForm(parent, SWT.NONE);
		navigationAndAreaSash.setOrientation(SWT.HORIZONTAL);
		
		navigationComp = new Composite(navigationAndAreaSash, SWT.BORDER);
		navigationComp.setLayout(new GridLayout(1, false));
		navigationComp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		mainAreaComposite = new Composite(navigationAndAreaSash, SWT.BORDER);
		GridLayout areaLayout = new GridLayout(1, false);
		areaLayout.marginHeight = 0;
		areaLayout.marginWidth = 0;
		mainAreaComposite.setLayout(areaLayout);
		mainAreaComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		navigationAndAreaSash.setWeights(new int []{2,7});
		
		contentComposite = new Composite(mainAreaComposite, SWT.NONE);
		contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		contentComposite.setLayout(stack);
		
		welcomePage = new WelcomePage(contentComposite, SWT.NONE);
		jobView = new JobGraphicView(contentComposite, SWT.NONE);
//		printView = new PrintJobView(contentComposite, SWT.NONE);
		
		changeTopControl(welcomePage);
	
		Label logo = new Label(navigationComp, SWT.NONE);
		logo.setImage(SWTResourceManager.getImage(ImageKey.LOGO));
		logo.setBackground(navigationComp.getBackground());
		
		treeViewer = new TreeViewer(navigationComp, SWT.NONE);
		
		treeViewer.setContentProvider(new NavigationContentProvider());
		treeViewer.setLabelProvider(new NavigationLabelProvider());
		treeViewer.setInput(navigation.getRoot());
		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		navigation.addNavigationListener(new Navigation.INavigationListener() {
			@Override
			public void itemsChagned()
			{
				treeViewer.refresh();
			}
			
			@Override
			public void navigationFocus(final Class<?> navigationClass) {
				
				System.out.println(">>>>>>>>>>>  !! " + navigationClass);
				if(navigationClass == null)
				{
					treeViewer.removeFilter(productionFilter);
					treeViewer.refresh();
					changeTopControl(welcomePage);
				}
				else
				{
					treeViewer.addFilter(productionFilter);
					
					treeViewer.expandToLevel(2);
					
					for(TreeItem item : treeViewer.getTree().getItems())
					{
						for(TreeItem i : item.getItems())
						{
							if(i.getData() instanceof JobNavigationFolder)
							{
								treeViewer.getTree().select(i);
								break;
							}
						}
							
					}
					treeViewer.refresh();
				}
			}
			
			@Override
			public void newItemAdded(NavigationItem item) 
			{
				
			}
			
		});
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				
				if(selection != null)
				{
					if(selection.getFirstElement() instanceof JobNavigationFolder)
					{
						selectedJob = ((JobNavigationFolder) selection.getFirstElement()).getJob();
						showJobView(selectedJob);
					}
					else if(selection.getFirstElement() instanceof JobNavigationItem)
					{
						JobNavigationItem item = (JobNavigationItem) selection.getFirstElement();
						String name = item.getName();
						selectedJob = item.getJob();
						
						if(name.equals("Saw Sheet"))
						{
							showSawSheetView();
						}
						if(name.equals("Print Job"))
						{
							showPrintView();
						}
					}
						
					
				}
			}
			
		});
	}
	
	private void showSawSheetView()
	{
		
		
	}
	
	private void showPrintView()
	{
		printView.fillWithJob(selectedJob);
		changeTopControl(printView);
		
	}
	
	private void changeTopControl(Composite comp)
	{
		stack.topControl = comp;
		contentComposite.layout();
	}
	
	public void showJobView(Job job)
	{
		jobView.setJob(job);
		changeTopControl(jobView);
	}

	@Override
	public void setFocus() 
	{
		
	}
	
	
	private class NavigationLabelProvider
	extends StyledCellLabelProvider
	{
		@Override
		public void update(ViewerCell cell) 
		{
			NavigationItem item = (NavigationItem) cell.getElement();
			cell.setText(item.getName());
			if(item.getImage() != null)
			{
				cell.setImage(item.getImage());
			}
			
			if(item instanceof JobNavigationFolder)
			{
				cell.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				cell.setFont(SWTResourceManager.getBoldFont(cell.getFont()));
			}
		}
	}
	
	private class NavigationContentProvider
	implements ITreeContentProvider
	{

		@Override
		public void dispose() {
			/* nothing to do */
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			/* nothing to do */
			
		}

		@Override
		public Object[] getElements(Object inputElement) 
		{
			if(inputElement instanceof Object[])
			{
				return (Object[]) inputElement;
			}
			else if (inputElement instanceof List<?>)
			{
				return ((List<?>) inputElement).toArray();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) 
		{
			if(parentElement instanceof NavigationFolder)
			{
				return ((NavigationFolder) parentElement).getChildren().toArray();
			}
			
			return new Object[]{};
			
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) 
		{
			if(element instanceof NavigationFolder)
			{
				return ((NavigationFolder) element).getChildren().size() > 0;
			}
			
			return false;
		}
	}

	public void showWelcome() 
	{
		changeTopControl(welcomePage);	
	}
}
