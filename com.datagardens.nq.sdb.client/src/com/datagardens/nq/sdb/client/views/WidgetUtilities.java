package com.datagardens.nq.sdb.client.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;

import com.datagardens.nq.sdb.client.views.adapters.ITableEditorListener;

public class WidgetUtilities 
{
	public static void addTableEditorListener(final TableViewer viewer, 
			final ITableEditorListener listener) 
	{
		final TableEditor editor = new TableEditor(viewer.getTable());
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		
		viewer.setData("editor", editor); //$NON-NLS-1$
		
		viewer.getTable().addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDown(MouseEvent event) 
			{
				Control old = editor.getEditor();
			    
				if (old != null) 
			    {
			    	old.dispose();
			    }
			      
				// find item by location
				Point pt = new Point(event.x, event.y);
				final TableItem item = viewer.getTable().getItem(pt);
				
				
				// If item found
				if(item != null)
				{
					int columnIndex = -1;
					
					for(int i = 0, n=viewer.getTable().getColumnCount(); i<n; i++)
					{
						Rectangle rect = item.getBounds(i);
						if(rect.contains(pt)) //Columnt index found
						{
							columnIndex = i;
							break;
						}
					}
					
					listener.onItemFound(item, columnIndex);
				}
			}
		});
	}

	public static void selectCComboItemWithLabel(CCombo combo, String label) 
	{
		if(label == null || combo== null)
		{
			return;
		}
		
		int index = -1;
		boolean found = false;
		
		for(String itemLabel : combo.getItems())
		{
			index++;
			
			if(itemLabel.equals(label))
			{
				found = true;
				break;
			}
		}
		
		
		if(found)
		{
			combo.select(index);
		}
		else
		{
			combo.deselectAll();
		}
		
	}
	
	public static void selectCComboItemWithLabel(Combo combo, String label) 
	{
		if(label == null || combo== null)
		{
			return;
		}
		
		int index = -1;
		boolean found = false;
		
		for(String itemLabel : combo.getItems())
		{
			index++;
			
			if(itemLabel.equals(label))
			{
				found = true;
				break;
			}
		}
		
		
		if(found)
		{
			combo.select(index);
		}
		else
		{
			combo.deselectAll();
		}
		
	}

	public static Button createActionButton(final Action action, Composite parent, int style) 
	{
		final Button button = new Button(parent, style);
		
		if(action.getImageDescriptor() != null)
		{
			button.setImage(action.getImageDescriptor().createImage());
		}
		else
		{
			button.setText(action.getText());
		}
		
		button.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				action.run();
			}
		});
		
		action.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) 
			{
				if(event.getProperty() != null && 
						event.getProperty().equals("enabled"))
				{
					button.setEnabled((Boolean) event.getNewValue());
				}
			}
		});
		
		return button;
	}
	
	public static Object getSingleSelectionFromViewer(Viewer viewer)
	{
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		return selection.getFirstElement();
	}
}
