package com.datagardens.nq.sdb.client.views.nav;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.commands.production.AnalysisAction;
import com.datagardens.nq.sdb.client.commands.production.PrintJobAction;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class ModelJobNavigationFolder extends ModelNavigationFolder {

	public ModelJobNavigationFolder(
			ModelNavigationObject parent,
			Job job) 
	{
		super(parent, job.getId(), ImageKey.ICON_PRODUCTION_JOB, NavigationCategory.JOB);
		setData(job);
		
		/* print job action*/
		addChild(new ModelNavigationAction(this, PrintJobAction.getActionReference(), NavigationCategory.PRINT_JOB, job));
		
		/* data entry folder */
		ModelNavigationFolder dataEntryFolder = new ModelNavigationFolder(this, "Data Entry",
				ImageKey.ICON_PRODUCTION_DATA_ENTRY, NavigationCategory.DATA_ENTRY);
		dataEntryFolder.setData(job);
		dataEntryFolder.addChild(new ModelSawSheetNavigationObject(this, job));
		
		if(job.getNumberOfOperations() > 1)
		{
			ModelNavigationFolder shiftLogFolder = 
					new ModelNavigationFolder(this, "Shift Log", ImageKey.ICON_SHIFT_LOG, NavigationCategory.SHIFT_LOG_SHEET);
			shiftLogFolder.setData(job);
			
			for(ShiftLogSheet sheet : job.getShiftLogSheets().values())
			{
				shiftLogFolder.addChild(
						new ModelOperationNavigationObject(
								shiftLogFolder, job, sheet));
			}
			
			dataEntryFolder.addChild(shiftLogFolder);
		}
	
		
		addChild(dataEntryFolder);
		
		/* analysis action */
		addChild(new ModelNavigationAction(this, AnalysisAction.getActionReference(), null, job));
	}
}
