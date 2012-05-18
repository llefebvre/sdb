package com.datagardens.nq.sdb.client.views.sheets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.nebula.widgets.calendarcombo.ICalendarListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.GridCellReference;
import com.datagardens.nq.sdb.client.views.StackedComposite;
import com.datagardens.nq.sdb.client.views.sheets.SheetColumnInformation.EditorType;

public class GridEditorsLayer extends StackedComposite {

	private int margin = 2;
	private Text textEditor;
	private CalendarCombo calendarEditor;
	private List<IGridTextEditorListener> listeners;
	private GridCellReference cellReferece;
	
//	private StackLayout layout;
	
	public GridEditorsLayer(Composite parent) {
		super(parent, SWT.NONE);
		
		StackLayout layout = (StackLayout) getLayout();
		layout.marginHeight = margin;
		layout.marginWidth  = margin;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		
		listeners = Collections.synchronizedList(new ArrayList<IGridTextEditorListener>());
		
		configureEditors();
		
	}

	private void configureEditors() {
		calendarEditor = new CalendarCombo(this, SWT.READ_ONLY);
		calendarEditor.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() 
			{
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				saveVauleToCell(SWT.CR, format.format(calendarEditor.getDate().getTime()));
			}
			
			@Override
			public void dateRangeChanged(Calendar start, Calendar end) 
			{
			}
			
			@Override
			public void dateChanged(Calendar date) {	
			}
		});
		
		textEditor = new Text(this, SWT.RIGHT);
		textEditor.setTextLimit(10);
		
		textEditor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				e.doit = Character.isDigit(e.character) || 
						Character.isLetter(e.character) ||
						e.character == '-' ||
						e.character == '/' ||
						(e.keyCode == SWT.DEL ||
						 e.keyCode == SWT.BS  ||
						 e.keyCode == SWT.END || 
						 e.keyCode == SWT.HOME || 
						 /*e.keyCode == SWT.TAB ||*/
						 e.keyCode == SWT.ESC) ;
				
				if(e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.CR || 
						e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN ||
						e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT ||
						e.keyCode == SWT.TAB)
				{
					String value = textEditor.getText();
					textEditor.setText("");
					saveVauleToCell(e.keyCode, value);
				}
			}
		});
	}
	
	
	private void saveVauleToCell(int keyCode, String value)
	{
		cellReferece.setValue(value);
		fireValueEnteredEvent(keyCode, value);
	}
	
	public void activate(EditorType type, Rectangle bounds, GridCellReference cellReferece)
	{
		this.cellReferece = cellReferece;
		
		bounds.x -= margin;
		bounds.y -= margin;
		
		bounds.width  += margin * 2;
		bounds.height += margin * 2;
		
		setVisible(true);
		setBounds(bounds);
		
		switch(type)
		{
		case TEXT:
			textEditor.setText(cellReferece.getValue());
			textEditor.setFocus();
			textEditor.setSelection(0, textEditor.getText().length());
			textEditor.showSelection();
			setTopComposite(textEditor);
			break;
			
		case DATE:
			calendarEditor.setFocus();
			setTopComposite(calendarEditor);
			break;
		}
		
		
		/*editor.setText(cellReferece.getValue());
		editor.setFocus();		*/
	}
	
	public void deactivate()
	{
		setVisible(false);
	}
	
	public void fireValueEnteredEvent(int keyCode, String value)
	{
		for(IGridTextEditorListener listener: listeners)
		{
			listener.valueEntered(new GridTextEditorEvent(cellReferece.getItem(), 
					keyCode, value));
		}
	}
	
	public void addEditorListener(IGridTextEditorListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeEditorListener(IGridTextEditorListener listener)
	{
		listeners.remove(listener);
	}
}
