package com.datagardens.nq.sdb.client.views.nav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.datagardens.nq.sdb.client.BusyProcess;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;

public class TreeNavigationController extends ModelNavigationObject 
implements ISelectionChangedListener
{

	public static TreeNavigationController instance;
	public static TreeNavigationController createNewController(TreeViewer viewer) 
	{
		if(instance == null)
		{
			instance = new TreeNavigationController(viewer);
		}
		
		return instance;
	}
	
	private ModelNavigationFolder root;
	private ModelNavigationFolder tracking;
	private ModelNavigationFolder production;
	private ModelNavigationFolder reporting;
	private ModelNavigationFolder administrator;
	
	private TreeViewer viewer;
	
	private List<INavigationChangesListener> listeners;
	private ModelNavigationObject currentSelection;
	
	private TreeNavigationController(TreeViewer viewer) 
	{
		super(null, "", null);
		root = new ModelNavigationFolder(this, "root", null);
		listeners = Collections.synchronizedList(new ArrayList<INavigationChangesListener>());
		this.viewer = viewer;
		
		viewer.addSelectionChangedListener(this);
		
		instance = this;
	}
	
	public TreeViewer getViewer() {
		return viewer;
	}
	
	public Object[] createNavigationView()
	{
		
		tracking = new ModelNavigationFolder(root, "Tracking",
				ImageKey.ICON_TRACKING, null);
		root.addChild(tracking);
		
		production = new ModelNavigationFolder(root, "Production", 
				ImageKey.iCON_PRODUCTION,null);
		root.addChild(production);
		
		reporting = new ModelNavigationFolder(root, "Reporting", 
				ImageKey.ICON_REPORTING, null);
		root.addChild(reporting);
		
		if(NQDataModel.getClientUser().hasLevel(SDBRole.ADMINISTRATOR))
		{
			administrator = new ModelNavigationFolder(root, "Administrator", 
					ImageKey.ICON_ADMINSTRATION, NavigationCategory.ADMINISTRATION);
			root.addChild(administrator);
		}
		
		return root.getChildren().toArray();
	}
	
	public void addJob(Job job)
	{
		ModelJobNavigationFolder jobFolder = new ModelJobNavigationFolder(production, job);
		production.addChild(jobFolder);
	}
	
	public void removeJob(Job job)
	{
		ModelNavigationObject toRemove = null;
		for(ModelNavigationObject obj : production.getChildren())
		{
			if(obj instanceof ModelJobNavigationFolder)
			{
				ModelJobNavigationFolder folder = (ModelJobNavigationFolder) obj;
				if(folder.getData().equals(job))
				{
					toRemove = folder;
					break;
				}
			}
		}
		
		if(toRemove != null)
		{
			production.removeChild(toRemove);
		}
	}
	
	public ModelNavigationFolder getRoot() {
		return root;
	}
	
	public void addNavigationChangesListener(INavigationChangesListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeNavigationChangesListener(INavigationChangesListener listener)
	{
		listeners.remove(listener);
	}
	
	public void fireNavigationChanges()
	{
		for(INavigationChangesListener listener : listeners)
		{
			listener.itemsChanged();
		}
	}

	@Override
	public void itemsChanged() 
	{
		fireNavigationChanges();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) 
	{
		IStructuredSelection iss = (IStructuredSelection) event.getSelection();
		if(iss != null && iss.getFirstElement() != null &&
				iss.getFirstElement() instanceof ModelNavigationObject)
		{
			ModelNavigationObject nav = (ModelNavigationObject) iss.getFirstElement();
			currentSelection = nav;
		
			try
			{
				BusyProcess.run(Display.getDefault(), new Runnable() {
					
					@Override
					public void run() {
						currentSelection.fireUpdateEvent();
					}
				});				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"Error Ocurred", e.getMessage());
			}
		}
	}
}
