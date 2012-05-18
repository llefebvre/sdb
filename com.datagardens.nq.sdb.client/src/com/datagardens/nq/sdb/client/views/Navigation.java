package com.datagardens.nq.sdb.client.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.datagardens.nq.sdb.client.Client;
import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.commons.model.Job;

public class Navigation {

	private static Navigation instance;
	
	static
	{
		instance = new Navigation();
	}
	
	public static Navigation getInstance()
	{
		return instance;
	}
	
	private List<NavigationItem> root;
	private ProductionNavigation production;
	private List<INavigationListener> listeners;
	
	private Navigation() 
	{
		root = new ArrayList<Navigation.NavigationItem>();
		production = new ProductionNavigation();
		listeners = Collections.synchronizedList(new ArrayList<INavigationListener>());
		
		root.add(new NavigationItem("Tracking", AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID,
				ImageKey.ICON_TRACKING).createImage()));
		root.add(production);
		root.add(new NavigationItem("Reporting",AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID,
				ImageKey.ICON_REPORTING).createImage()));
		root.add(new NavigationItem("Administrator", 
				AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID,
						ImageKey.ICON_ADMINSTRATION).createImage()));
		
	}
	
	////////////////////////////////////////////
	//OPERATIONS
	////////////////////////////////////////////
	public List<NavigationItem> getRoot() 
	{
		return root;
	}
	
	public int getNumberOfOpenJobs()
	{
		return production.getChildren().size();
	}
	
	public void addJob(Job job)
	{
		production.addJob(job);
		fireNavigationChangedEvent();
	}
	
	public void removeJob(Job job) 
	{
		production.removeJob(job);
		fireNavigationChangedEvent();
	}
	
	private void fireNavigationChangedEvent() 
	{
		for(INavigationListener listener : listeners)
		{
			listener.itemsChagned();
		}
	}
	
	public void addNavigationListener(INavigationListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeNavigationListener(INavigationListener listener)
	{
		listeners.remove(listener);
	}

	public interface INavigationListener
	{
		void itemsChagned();
		void navigationFocus(Class<?> navigationClass);
		void newItemAdded(NavigationItem item);
	}
	
	/**
	 * Single Navigation Item
	 * @author Francisco
	 *
	 */
	public class NavigationItem
	{
		private String name;
		private Image image;
		
		public NavigationItem(String name, Image image)
		{
			this.name = name;
			this.image = image;
		}
		
		public NavigationItem(String name) 
		{
			this(name, null);
		}
		
		public String getName() {
			return name;
		}
		
		public Image getImage() {
			return image;
		}
	}
	
	/**
	 * Navigation Item that holds more navigation items
	 * @author Francisco
	 *
	 */
	public class NavigationFolder
	extends NavigationItem
	{
		private List<NavigationItem> children;
		
		public NavigationFolder(String name, Image image){
			super(name, image);
			children = new ArrayList<NavigationItem>();
		}
		
		public NavigationFolder(String name){
			this(name, null);
		}
		
		public List<NavigationItem> getChildren(){
			return children;
		}
		
		public void addChild(NavigationItem item){
			children.add(item);
		}
		
		public void removeChild(NavigationItem item){
			children.remove(item);
		}
	}
	
	
	/////////////////////////////////////////////
	// PRODUCTION
	//////////////////////////////////////////////
	
	public interface IProductionItem {
	}
	
	/**
	 * ProductionNavigation
	 * Navigation Folder specific for Production operation
	 * @author Francisco
	 *
	 */
	public class ProductionNavigation
	extends NavigationFolder
	implements IProductionItem
	{
		private Map<String, JobNavigationFolder> openJobs;
		
		public ProductionNavigation() {
			super("Production", AbstractUIPlugin.imageDescriptorFromPlugin(Client.PLUGIN_ID,
					ImageKey.iCON_PRODUCTION).createImage());
			openJobs = new HashMap<String, Navigation.JobNavigationFolder>();
		}
		
		public void addJob(Job job)
		{
			JobNavigationFolder jobFolder = openJobs.get(job.getId());
			
			if(jobFolder == null)
			{
				openJobs.put(job.getId(), new JobNavigationFolder(job));
				addChild(openJobs.get(job.getId()));
			}
		}
		
		public void removeJob(Job job)
		{
			JobNavigationFolder jobToRemove = 
					openJobs.get(job.getId());
			
			if(jobToRemove != null)
			{
				openJobs.remove(job.getId());
				removeChild(jobToRemove);
			}
		}
	}
	
	/**
	 * JobNavigationFolder
	 * @author Francisco
	 *
	 */
	public class JobNavigationFolder
	extends NavigationFolder
	implements IProductionItem
	{
		private Job job;
		
		public JobNavigationFolder(Job job)
		{
			super(job.getId(), 
					SWTResourceManager.getImage(ImageKey.ICON_PRODUCTION_JOB));
			
			this.job = job;
			
			addChild(new JobNavigationItem(this, "Print Job", SWTResourceManager.getImage(ImageKey.ICON_PRODUCTION_PRINT)));
			
			JobNavigationItem dataEntry = new JobNavigationItem(this, "Data Entry", SWTResourceManager.getImage(ImageKey.ICON_PRODUCTION_DATA_ENTRY));
			
			JobNavigationItem sawSheet = new JobNavigationItem(this, "Saw Sheet", SWTResourceManager.getImage(ImageKey.ICON_SAW_SHEET));			
			dataEntry.addChild(sawSheet);
			dataEntry.addChild(new JobNavigationItem(this, "Operation 20", SWTResourceManager.getImage(ImageKey.ICON_OPERATION)));
			dataEntry.addChild(new JobNavigationItem(this, "Operation 30", SWTResourceManager.getImage(ImageKey.ICON_OPERATION)));
			dataEntry.addChild(new JobNavigationItem(this, "Operation 40", SWTResourceManager.getImage(ImageKey.ICON_OPERATION)));
			dataEntry.addChild(new JobNavigationItem(this, "Operation 50", SWTResourceManager.getImage(ImageKey.ICON_OPERATION)));
			dataEntry.addChild(new JobNavigationItem(this, "Operation 60", SWTResourceManager.getImage(ImageKey.ICON_OPERATION)));
			
			addChild(dataEntry);
			addChild(new JobNavigationItem(this, "Analysis",SWTResourceManager.getImage(ImageKey.ICON_PRODUCTION_ANALYSIS)));
		}
		
		public Job getJob() {
			return job;
		}
	}
	
	public class JobNavigationItem
	extends NavigationFolder
	implements IProductionItem
	{

		private JobNavigationFolder parent;
		
		public JobNavigationItem(JobNavigationFolder parent, String name) {
			this(parent, name, null);
		}
		
		public JobNavigationItem(JobNavigationFolder parent, 
				String name, Image image) {
			super(name, image);
			this.parent = parent;
		}
		
		public JobNavigationFolder getParent() {
			return parent;
		}
		
		public Job getJob()
		{
			return getParent().getJob();
		}
	}

	public void setFocus(Class<?> clazz) 
	{
		for(INavigationListener listener : listeners)
		{
			listener.navigationFocus(clazz);
		}
	}	
}
