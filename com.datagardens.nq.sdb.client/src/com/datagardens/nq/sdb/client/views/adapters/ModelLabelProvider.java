package com.datagardens.nq.sdb.client.views.adapters;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.nav.ModelJobNavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject;
import com.datagardens.nq.sdb.commons.model.NQModelObject;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.ModelEnum;

public class ModelLabelProvider extends StyledCellLabelProvider
implements ILabelProvider, ITableLabelProvider
{
	@Override
	public void update(ViewerCell cell) 
	{
		cell.setText(getText(cell.getElement()));
		cell.setImage(getImage(cell.getElement()));
		
		if(cell.getElement() instanceof ModelJobNavigationFolder)
		{
			cell.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
			cell.setFont(SWTResourceManager.getBoldFont(cell.getFont()));
		}
	}

	@Override
	public Image getImage(Object element) 
	{
		
		if(element instanceof ModelNavigationObject)
		{
			ModelNavigationObject item = (ModelNavigationObject) element;
			return item.getImage();
		}
		else if(element instanceof NQModelObject)
		{
			if(element instanceof SDBUser)
			{
				return SWTResourceManager.getImage(ImageKey.ICON_USER);
			}
		}
		
		return null;
	}

	@Override
	public String getText(Object element) 
	{
		if(element instanceof ModelNavigationObject)
		{
			ModelNavigationObject item = (ModelNavigationObject) element;
			return item.getName();
		}
		
		else if(element instanceof NQModelObject)
		{
			NQModelObject modelObj = (NQModelObject) element;
			return modelObj.getId();
		}
		else if (element instanceof ModelEnum)
		{
			return ((ModelEnum) element).getId();
		}
		
		return "";
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) 
	{
		if(element instanceof ModelNavigationObject)
		{
			ModelNavigationObject item = (ModelNavigationObject) element;
			return item.getImage();
		}
		else if(element instanceof NQModelObject)
		{
			if(element instanceof SDBUser)
			{
				return SWTResourceManager.getImage(ImageKey.ICON_USER);
			}
		}
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) 
	{
		if(element instanceof ModelNavigationObject)
		{
			ModelNavigationObject item = (ModelNavigationObject) element;
			return item.getName();
		}
		
		else if(element instanceof NQModelObject)
		{
			NQModelObject modelObj = (NQModelObject) element;
			return modelObj.getId();
		}
		else if (element instanceof ModelEnum)
		{
			return ((ModelEnum) element).getId();
		}
		
		return "";
	}
}
