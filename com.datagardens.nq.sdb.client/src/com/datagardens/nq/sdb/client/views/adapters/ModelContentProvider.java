package com.datagardens.nq.sdb.client.views.adapters;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.datagardens.nq.sdb.client.views.nav.ModelNavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;

public class ModelContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
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
		else if(inputElement instanceof Set<?>)
		{
			return ((Set<?>) inputElement).toArray();
		}
		else if(inputElement instanceof Map<?, ?>)
		{
			return ((Map<?, ?>) inputElement).values().toArray();
		}
		
		return new Object[]{};
	}

	@Override
	public Object[] getChildren(Object parentElement) 
	{
		if(parentElement instanceof ModelNavigationObject)
		{
			if(parentElement instanceof ModelNavigationFolder)
			{
				return ((ModelNavigationFolder) parentElement).getChildren().toArray();
			}
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
		return getChildren(element).length > 0;
	}

}
