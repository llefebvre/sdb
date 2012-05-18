package com.datagardens.nq.sdb.client.views.nav;

import com.datagardens.nq.sdb.client.ImageKey;

public class ModelShiftLogSheetNavigationObject extends ModelNavigationObject {

	public ModelShiftLogSheetNavigationObject(ModelNavigationObject parent, Object shifLogSheet) 
	{
		super(parent, "Shift Log", ImageKey.ICON_SHIFT_LOG, NavigationCategory.SHIFT_LOG_SHEET);
		setData(shifLogSheet);
	}
}
