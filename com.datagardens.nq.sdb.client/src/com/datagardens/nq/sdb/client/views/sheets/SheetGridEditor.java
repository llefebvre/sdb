package com.datagardens.nq.sdb.client.views.sheets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.datagardens.nq.sdb.client.views.GridCellReference;

public class SheetGridEditor extends GridEditor {

	private Grid grid;
	private List<ISheetEditorListener> listeners; 
	
	public SheetGridEditor(Grid grid) {
		super(grid);
		this.grid = grid;
		listeners = Collections.synchronizedList(new ArrayList<ISheetEditorListener>());
	}


	public void showEditor(GridCellReference ref)
	{
		hideAllEditors();
		grabHorizontal = true;
		grabVertical = true;
		
		
		switch(ref.getDataType())
		{
		case INPUT_JOB_NUMBER:
		case INPUT_NUMBER:
		case INPUT_DATE:
		case INPUT_TEXT:
		case INPUT_EMPLOYEE_NUMBER:
			showTextEditor(ref);
			break;
		
		case INPUT_BOOLEAN:
			showBooleanEdito(ref);
			break;
			
		default: break;
		}
	}
	
	private void showBooleanEdito(final GridCellReference ref) 
	{
		final Text booleanEditor = 
				new Text(grid, SWT.CENTER | SWT.READ_ONLY);
		
		booleanEditor.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				e.doit = false;
				if(e.character == 'Y'  || e.character == 'y')
				{
					e.doit = true;
					ref.setValue(true);
					booleanEditor.setText(ref.getValue());
				}
				else if(e.character == 'N' || e.character == 'n')
				{
					e.doit = true;
					ref.setValue(false);
					booleanEditor.setText(ref.getValue());
				}
				else if(e.keyCode == SWT.SPACE)
				{
					e.doit = true;
					ref.toggleBooleanValue();
					booleanEditor.setText(ref.getValue());
				}
				
				if(e.keyCode == SWT.KEYPAD_CR ||
						e.keyCode == SWT.CR  ||
						e.character == 'Y'  || e.character == 'y' ||
						e.character == 'N' || e.character == 'n')
				{
					selectNextField(ref, SWT.RIGHT);
					disposeEditor(booleanEditor);
				}
				
				if(e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP || 
						e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT ||
						e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
				{
					disposeEditor(booleanEditor);
				
					int direction = -1;
					
					switch(e.keyCode)
					{
					case SWT.ARROW_UP:
						direction = SWT.UP;
						break;
					
					case SWT.ARROW_DOWN:
						direction = SWT.DOWN;
						break;
					
					case SWT.ARROW_LEFT:
						direction = SWT.LEFT;
						break;
					
					/*case SWT.KEYPAD_CR:
					case SWT.CR:*/
					case SWT.ARROW_RIGHT:
						direction = SWT.RIGHT;
						break;
					}
					
					if(direction != -1)
					{
						
						selectNextField(ref,   direction);
					}
				}
			}
		});
		
		booleanEditor.setText(ref.getValue());
		booleanEditor.setFocus();
		
		setEditor(booleanEditor, ref.getItem(), ref.getColumn());
	}


	private void showTextEditor(final GridCellReference ref) 
	{
		
		final Text textEditor = 
				new Text(grid, SWT.RIGHT | SWT.BORDER);
		
		textEditor.addKeyListener(new KeyAdapter() 
		{	
			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = Character.isDigit(e.character) || 
						Character.isLetter(e.character) ||
						e.character == '-' ||
						e.character == '/' ||
						(e.keyCode == SWT.DEL ||
						 e.keyCode == SWT.BS  ||
						 e.keyCode == SWT.END || 
						 e.keyCode == SWT.HOME ||
						 e.keyCode == SWT.ESC) ;
				
				if(e.keyCode == SWT.KEYPAD_CR ||
						e.keyCode == SWT.CR )
				{
					
					String value = textEditor.getText();
					
					
					if(isValidData(ref, value))
					{
						ref.setValue(value);
						disposeEditor(textEditor);
						selectNextField(ref, SWT.RIGHT);
						
					}
					else
					{
						textEditor.setText("");
					}
				}
				
				if(e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP || 
						e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT)
				{
					disposeEditor(textEditor);
				
					int direction = -1;
					
					switch(e.keyCode)
					{
					case SWT.ARROW_UP:
						direction = SWT.UP;
						break;
					
					case SWT.ARROW_DOWN:
						direction = SWT.DOWN;
						break;
					
					case SWT.ARROW_LEFT:
						direction = SWT.LEFT;
						break;
					
					case SWT.ARROW_RIGHT:
						direction = SWT.RIGHT;
						break;
					}
					
					if(direction != -1)
					{
						
						selectNextField(ref,   direction);
					}
				}
			}			
		});
		
		textEditor.setText(ref.getValue());
		textEditor.selectAll();
		textEditor.setFocus();
		
		
		setEditor(textEditor, ref.getItem(), ref.getColumn());
	}
	
	private boolean isValidData(GridCellReference ref, String value) 
	{
		String errorMessage = ref.validate(value);
		
		if(!errorMessage.isEmpty())
		{
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "Invalid Input",
					errorMessage);
			return false;
		}
		
		return true;
	}
	
	private void selectNextField(GridCellReference currentReference, int direction) 
	{
		
		//reset variables
		int minItemIndex = -1;
		int minItemColum = -1;
		
		int maxItemIndex   = -1;
		int maxItemColumn = -1;
			
		int currentItemIndex  = -1;
		int currentItemColumn = -1;
		
		int nextItemIndex  = -1;
		int nextItemColumn = -1;
		
		boolean finalColumn = false;
		
		
		GridItem selectedGridItem = currentReference.getItem();
		currentItemIndex = grid.getIndexOfItem(selectedGridItem);
		currentItemColumn = currentReference.getColumn();
		
		@SuppressWarnings("unchecked")
		Map<Integer, GridCellReference> refs = 
			(Map<Integer, GridCellReference>) selectedGridItem.getData("cells_refs");
		
		
		/* find minimum column */
		for(Integer key : refs.keySet())
		{
			minItemColum = key;
			
			if(key == currentItemColumn)
			{
				
				break;
			}
		}
		
		/* find minimum index */
		
		/* find maximum column */
		for(Integer key : refs.keySet())
		{
			maxItemColumn = key > maxItemColumn ? key : maxItemColumn;
		}
		
		/* find maximum index */
		maxItemIndex = grid.getItemCount()-1;
		
		
		/* calculate the next position */
		
		switch(direction)
		{
		case SWT.DOWN:
			nextItemColumn = currentItemColumn;
			nextItemIndex = currentItemIndex+1;
			
		break;
		
		case SWT.RIGHT:
			for(Integer key : refs.keySet())
			{
				nextItemColumn = key;
				if(key > currentItemColumn)
				{
					break;
				}
			}
			
			nextItemIndex = currentItemIndex;
			finalColumn = maxItemColumn == currentItemColumn;
		}
		
		hideAllEditors();
		
		// Select the next item, and force the 
		// grid to show the next editor
		grid.deselectAll();
		
		if(!finalColumn)
		{
			grid.selectCell(new Point(nextItemColumn, nextItemIndex));
			grid.setFocusColumn(grid.getColumn(nextItemColumn));
			grid.setFocusItem(grid.getItem(nextItemIndex));
			
			@SuppressWarnings("unchecked")
			Map<Integer, GridCellReference> nextRefs = 
					(Map<Integer, GridCellReference>) grid.getItem(nextItemIndex).getData("cells_refs");
			
			GridCellReference newRef = nextRefs.get(nextItemColumn);
			if(newRef != null)
			{
				showEditor(newRef);
			}
		}
		else
		{
			fireEndOfLineEvent(currentItemIndex, refs);
		}
	}
	
	public void hideAllEditors() 
	{
		disposeEditor(getEditor());
	}
	
	private void disposeEditor(Control editor)
	{
		if(editor != null && !editor.isDisposed())
		{
			editor.dispose();
		}
	}
	
	public void addEditorListener(ISheetEditorListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeEditorListener(ISheetEditorListener listener)
	{
		listeners.remove(listener);
	}
	
	
	protected void fireEndOfLineEvent(int lineNumber, Map<Integer, GridCellReference> refs)
	{
		for(ISheetEditorListener listener : listeners)
		{
			listener.endOfLineReached(lineNumber, refs);
		}
	}
}
