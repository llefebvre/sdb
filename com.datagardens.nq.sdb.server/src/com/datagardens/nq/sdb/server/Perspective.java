package com.datagardens.nq.sdb.server;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) 
	{
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView("com.datagardens.nq.sdb.server.views.control", 
				false, IPageLayout.LEFT, 1.0F, 
				layout.getEditorArea());
	}
}
