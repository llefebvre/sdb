package com.datagardens.nq.sdb.client.views;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class StackedComposite extends Composite {

	private StackLayout layout;
	
	public StackedComposite(Composite parent, int style) {
		super(parent, style);
		layout = new StackLayout();
		setLayout(layout);
	}

	
	public void setTopComposite(Control control)
	{
		if(control == null)
		{
			return ;
		}
		
		layout.topControl = control;
		layout();
	}
}
