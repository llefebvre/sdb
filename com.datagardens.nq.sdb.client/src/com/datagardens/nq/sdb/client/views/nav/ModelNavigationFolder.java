package com.datagardens.nq.sdb.client.views.nav;

import java.util.LinkedHashSet;
import java.util.Set;

public class ModelNavigationFolder 
extends ModelNavigationObject 
{
	private Set<ModelNavigationObject> children;
	
	public ModelNavigationFolder(ModelNavigationObject parent, String name, NavigationCategory category) 
	{
		this(parent, name, null, category);
	}
	
	public ModelNavigationFolder(ModelNavigationObject parent, String name, String imagePath, NavigationCategory category) 
	{
		super(parent, name, imagePath, category);
		children = new LinkedHashSet<ModelNavigationObject>();
	}
	
	public void addChild(ModelNavigationObject child)
	{
		children.add(child);
		itemsChanged();
	}
	
	public void removeChild(ModelNavigationObject child)
	{
		children.remove(child);
		itemsChanged();
	}
	
	public Set<ModelNavigationObject> getChildren() {
		return children;
	}
	
	public void setChildren(Set<ModelNavigationObject> children) {
		this.children = children;
	}
}
