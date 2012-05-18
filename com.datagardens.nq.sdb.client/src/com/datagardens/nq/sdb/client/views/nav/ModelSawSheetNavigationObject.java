package com.datagardens.nq.sdb.client.views.nav;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.commons.model.Job;

public class ModelSawSheetNavigationObject
extends ModelNavigationObject
{

	public ModelSawSheetNavigationObject(ModelNavigationObject parent, Job job) 
	{
		super(parent, "Saw Sheet", ImageKey.ICON_SAW_SHEET, NavigationCategory.SAW_SHEET);
		setData(job);
	}
}
