package com.datagardens.nq.sdb.client.views.sheets;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class EditableGrid{

	///////////////////////////////////////
	//CLASS
	///////////////////////////////////////
	
	
	//////////////////////////////////////
	//INSTANCE
	//////////////////////////////////////
	private Composite outerComposite;
	private GridEditorsLayer editor;
	private Grid grid;
	
	public EditableGrid(Composite parent, int style)
	{
		outerComposite = new Composite(parent, style);
		outerComposite.setLayout(null);
		
		editor = new GridEditorsLayer(outerComposite);
		editor.setBounds(0, 0, 200, 18);
		
		grid = new Grid(outerComposite, SWT.BORDER);
		grid.setBounds(outerComposite.getBounds());
	}

	public void update() {
		outerComposite.update();
	}

	public void layout() {
		outerComposite.layout();
		grid.setBounds(outerComposite.getBounds());
	}
}
