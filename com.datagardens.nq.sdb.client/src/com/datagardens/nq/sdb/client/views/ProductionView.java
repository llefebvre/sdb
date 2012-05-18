package com.datagardens.nq.sdb.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;

public class ProductionView extends Composite {

	private JobViewer jobViewer;
	
	/**
	 * The Production Viewer acts as a holder of the Jobs Viewer
	 * and also as a controller to react to the user's job selection.
	 * 
	 * @param parent
	 * @param style
	 */
	public ProductionView(Composite parent, int style, IWorkbenchWindow window) {
		super(parent, style);
		
		//Set properties for parent composite
		setLayout(new FillLayout());
		
		// The only contents available in the production view
		// is the jobs viewer
		jobViewer = new JobViewer(this, SWT.NONE, window);
	}
}
