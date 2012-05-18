package com.datagardens.nq.sdb.client.views.adapters;

import org.eclipse.swt.widgets.Item;

public interface ITableEditorListener 
{
	/**
	 * <p>Event fired by the user when clicking in a Table-based viewer.</p>
	 * <p>It passes back the selected Item along with the index of column where the selection occurs.</p>
	 * @param item
	 * @param columnIndex
	 */
	void onItemFound(Item item, int columnIndex);
}

