package com.datagardens.nq.sdb.client;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.datagardens.nq.sdb.client.views.EditorView;
import com.datagardens.nq.sdb.client.views.TreeNavigationView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout)
	{
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(TreeNavigationView.ID, false, 
				IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		
		layout.addStandaloneView(EditorView.ID, false, 
				IPageLayout.LEFT, 0.8f, layout.getEditorArea());
	}
}
