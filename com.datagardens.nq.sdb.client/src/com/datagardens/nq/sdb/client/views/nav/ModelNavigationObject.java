package com.datagardens.nq.sdb.client.views.nav;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.datagardens.nq.sdb.client.SWTResourceManager;


public class ModelNavigationObject
implements INavigationChangesListener
{
	/////////////////////////////////////////////
	// CLASS
	/////////////////////////////////////////////
	public static interface INavigationUpdateListener
	{
		public void update(ModelNavigationObject updatedObject);
	}
	
	/////////////////////////////////////////////
	// INSTANCE
	////////////////////////////////////////////
	private String name;
	private String imagePath;
	private ImageDescriptor imageDescriptor;
	private Object data;
	private NavigationCategory category;
	private ModelNavigationObject parent;
	
//	private List<INavigationUpdateListener> listeners;
	private INavigationUpdateListener listener;
	
	public static enum NavigationCategory
	{
		TRACKING, 
		PRODUCTION, 
			JOB,
			PRINT_JOB,
			DATA_ENTRY,
				SAW_SHEET,
				SHIFT_LOG_SHEET,
					OPERATION,
		REPORTING,
		ADMINISTRATION;
	}
	
	public ModelNavigationObject(ModelNavigationObject parent, String name, NavigationCategory category) 
	{
		this(parent, name, "", category);
	}
	
	public ModelNavigationObject(ModelNavigationObject parent, String name, String imagePath, NavigationCategory category) 
	{
		this.name = name;
		this.imagePath = imagePath == "" ? null : imagePath;
		this.category = category;
		this.parent = parent;
		
//		listeners  = Collections.synchronizedList(new ArrayList<INavigationUpdateListener>());
	}
	
	public ModelNavigationObject(String name, ImageDescriptor imageDescriptor, NavigationCategory category) 
	{
		this.name = name;
		this.imagePath = null;
		this.imageDescriptor = imageDescriptor;
		this.category = category;
		
//		listeners  = Collections.synchronizedList(new ArrayList<INavigationUpdateListener>());
	}
	
	/*public void addUpdateListener(INavigationUpdateListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeUpdateListener(INavigationUpdateListener listener)
	{
		listeners.remove(listener);
	}*/
	
	public void setUpdateListener(INavigationUpdateListener listener)
	{
		this.listener = listener;
	}
	
	public void fireUpdateEvent()
	{
		System.out.println("listener= " + listener);
		if(listener != null)
		{
			listener.update(this);
		}
		
		/*for(INavigationUpdateListener listener : listeners)
		{
			listener.update(this);
		}*/
	}
	
	@Override
	public void itemsChanged() {
		if(parent != null)
		{
			parent.itemsChanged();
		}
	}
	
	public ModelNavigationObject getParent() {
		return parent;
	}
	
	public String getName() {
		return name;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public NavigationCategory getMainCategory() 
	{
		if(category == null)
		{
			return null;
		}
			
		switch(category)
		{
			case TRACKING:
			case PRODUCTION:
			case REPORTING:
			case ADMINISTRATION:
				return category;
			case JOB:
			case PRINT_JOB:
			case DATA_ENTRY:
			case SAW_SHEET:
			case SHIFT_LOG_SHEET:
			case OPERATION:
				return NavigationCategory.PRODUCTION;
			
			default: return null;
		}
		
	}
	
	public NavigationCategory getSubCategory() 
	{
		if(category == null)
		{
			return null;
		}
		
		switch(category)
		{
			case TRACKING:
			case PRODUCTION:
			case REPORTING:
			case ADMINISTRATION:
				return null;
				
			default:
				return category;
		}
	}
	
	public Image getImage()
	{
		if(imageDescriptor != null)
		{
			return imageDescriptor.createImage();
		}
		else if(imagePath != null)
		{
			return SWTResourceManager.getImage(imagePath);
		}
			
		return null;
	}
}
