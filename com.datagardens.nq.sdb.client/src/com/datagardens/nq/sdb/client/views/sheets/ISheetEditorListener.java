package com.datagardens.nq.sdb.client.views.sheets;

import java.util.Map;

import org.eclipse.jface.viewers.ICellEditorListener;

import com.datagardens.nq.sdb.client.views.GridCellReference;

public interface ISheetEditorListener 
extends ICellEditorListener 
{
	public void endOfLineReached(int lineNumber, Map<Integer, GridCellReference> refs);
}
