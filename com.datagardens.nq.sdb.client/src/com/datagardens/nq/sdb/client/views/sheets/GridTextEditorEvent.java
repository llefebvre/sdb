package com.datagardens.nq.sdb.client.views.sheets;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;

public class GridTextEditorEvent 
{
	public int keyCode;
	public GridItem item;
	public Grid grid;
	public String value;
	
	public GridTextEditorEvent(GridItem item, int keyCode, String value)
	{
		this.keyCode = keyCode;
		this.value = value;
		this.item = item;
		this.grid = item.getParent();
	}
}
