package com.datagardens.nq.sdb.client.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.datagardens.nq.sdb.client.views.Navigation.NavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;

public class EditorView extends ViewPart
implements ISelectionListener
{

	public static final String ID = "com.datagardens.nq.sdb.client.views.editorView";
	public static int selectedShift;
	
	
	private StackedComposite area;
	
	private WelcomePage welcomePage;
	
	private StackedComposite trackingArea;
	private ProductionView productionArea;
	private AdministrationView administrationArea;

	@Override
	public void createPartControl(Composite parent) 
	{
		area = new StackedComposite(parent, SWT.NONE);
		
		welcomePage = new WelcomePage(area, SWT.NONE);
		trackingArea = new StackedComposite(area, SWT.NONE);
		productionArea = new ProductionView(area, SWT.NONE, getSite().getWorkbenchWindow());
		
		if(NQDataModel.getClientUser().hasLevel(SDBRole.ADMINISTRATOR))
		{
			administrationArea = new AdministrationView(area, SWT.NONE, getSite());
		}
		
		area.setTopComposite(welcomePage);
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	@Override
	public void setFocus() 
	{
		
	}
	
	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object selected = structuredSelection.getFirstElement();
		
		if(selected instanceof ModelNavigationObject)
		{
			ModelNavigationObject navObj = (ModelNavigationObject) selected;
			if(navObj.getMainCategory() != null)
			{
				switch(navObj.getMainCategory())
				{
				case TRACKING:
					area.setTopComposite(welcomePage);
					break;
					
				case PRODUCTION:
					area.setTopComposite(productionArea);
					
					break;
					
				case ADMINISTRATION:
					System.out.println("opene adim");
					area.setTopComposite(administrationArea);
					System.out.println(" >>>>>>>>>>>>>>>>>>> Done");
					break;
				}
			}
		}
		else
		{
//			area.setTopComposite(welcomePage);
		}
	}
}
