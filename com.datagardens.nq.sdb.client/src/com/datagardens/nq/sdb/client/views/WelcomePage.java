package com.datagardens.nq.sdb.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;

public class WelcomePage extends Composite {
	
	private Image planet;

	WelcomePage(Composite parent, int style) {
		super (parent, style);
		
		planet = SWTResourceManager.getImage(ImageKey.WELCOME);
		createContents();
	}

	private void createContents() 
	{
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);
		
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		new Label(this, SWT.NONE).setImage(planet);
	}
	
}
