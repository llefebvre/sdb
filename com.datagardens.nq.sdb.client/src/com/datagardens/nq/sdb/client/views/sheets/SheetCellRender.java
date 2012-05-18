package com.datagardens.nq.sdb.client.views.sheets;

import java.util.Map;

import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.nebula.widgets.grid.internal.TextUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;

import com.datagardens.nq.sdb.client.views.GridCellReference;

@SuppressWarnings("restriction")
public class SheetCellRender extends DefaultCellRenderer 
{
	
	int leftMargin = 4;
	int rightMargin = 4;
    int topMargin = 0;
    int bottomMargin = 0;
    int textTopMargin = 1;
    int textBottomMargin = 2;
    private int insideMargin = 3;
    int treeIndent = 20;
    
//    private CheckBoxRenderer checkRenderer;

    private TextLayout textLayout;
    
	@Override
	public void paint(GC gc, Object value) 
	{

        GridItem item = (GridItem)value;

        gc.setFont(item.getFont(getColumn()));

        boolean drawAsSelected = isSelected();

        boolean drawBackground = true;

        if (isCellSelected())
        {
            drawAsSelected = true;//(!isCellFocus());
        }

        if (drawAsSelected)
        {
            gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        }
        else
        {
            if (item.getParent().isEnabled())
            {
                Color back = item.getBackground(getColumn());

                if (back != null)
                {
                    gc.setBackground(back);
                }
                else
                {
                    drawBackground = false;
                }
            }
            else
            {
                gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
            }
            gc.setForeground(item.getForeground(getColumn()));
        }

        if (drawBackground)
            gc.fillRectangle(getBounds().x, getBounds().y, getBounds().width,
                         getBounds().height);


        int x = leftMargin;


        boolean showCheck = false;
        @SuppressWarnings("unchecked")
		Map<Integer, GridCellReference> refs = 
        		(Map<Integer, GridCellReference>) item.getData("cells_refs");
       
        GridCellReference refForThisColumn = refs.get(getColumn());
        if(refForThisColumn != null)
        {
        	/*showCheck = refForThisColumn.isEditable();*/
        	showCheck = false;
        }
        

        if (showCheck)
        {
        	
        	Rectangle bounds = new Rectangle(
        			getBounds().x + ((getBounds().width - 0) /2),
                	(getBounds().height - 0)/ 2 + getBounds().y,
                	0, 
                	0);
        	
        	 gc.drawLine(bounds.x + 3, bounds.y + 5, bounds.x + 6, bounds.y + 8);
             gc.drawLine(bounds.x + 3, bounds.y + 6, bounds.x + 5, bounds.y + 8);
             gc.drawLine(bounds.x + 3, bounds.y + 7, bounds.x + 5, bounds.y + 9);
             gc.drawLine(bounds.x + 9, bounds.y + 3, bounds.x + 6, bounds.y + 6);
             gc.drawLine(bounds.x + 9, bounds.y + 4, bounds.x + 6, bounds.y + 7);
             gc.drawLine(bounds.x + 9, bounds.y + 5, bounds.x + 7, bounds.y + 7);
             
            /*checkRenderer.setChecked(item.getChecked(getColumn()));
            checkRenderer.setGrayed(item.getGrayed(getColumn()));
            if (!item.getParent().isEnabled())
            {
                checkRenderer.setGrayed(true);
            }
            checkRenderer.setHover(getHoverDetail().equals("check"));

        	if (isCenteredCheckBoxOnly(item))
        	{
        		//Special logic if this column only has a checkbox and is centered
                checkRenderer.setBounds(getBounds().x + ((getBounds().width - checkRenderer.getBounds().width) /2),
                		                (getBounds().height - checkRenderer.getBounds().height)
                                            / 2 + getBounds().y, checkRenderer
                                          .getBounds().width, checkRenderer.getBounds().height);
        	}
        	else
        	{
                checkRenderer.setBounds(getBounds().x + x, (getBounds().height - checkRenderer
                        .getBounds().height)
                                                               / 2 + getBounds().y, checkRenderer
                        .getBounds().width, checkRenderer.getBounds().height);

                    x += checkRenderer.getBounds().width + insideMargin;
        	}

        	checkRenderer.paint(gc, null);*/
        }

        Image image = item.getImage(getColumn());
        if (image != null)
        {
            int y = getBounds().y;

            y += (getBounds().height - image.getBounds().height)/2;

            gc.drawImage(image, getBounds().x + x, y);

            x += image.getBounds().width + insideMargin;
        }

        int width = getBounds().width - x - rightMargin;

        if (drawAsSelected)
        {
            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        }
        else
        {
            gc.setForeground(item.getForeground(getColumn()));
        }

        if (!isWordWrap())
        {
            String text = TextUtils.getShortString(gc, item.getText(getColumn()), width);

            if (getAlignment() == SWT.RIGHT)
            {
                int len = gc.stringExtent(text).x;
                if (len < width)
                {
                    x += width - len;
                }
            }
            else if (getAlignment() == SWT.CENTER)
            {
                int len = gc.stringExtent(text).x;
                if (len < width)
                {
                    x += (width - len) / 2;
                }
            }

            gc.drawString(text, getBounds().x + x, getBounds().y + textTopMargin + topMargin, true);
        }
        else
        {
            if (textLayout == null)
            {
                textLayout = new TextLayout(gc.getDevice());
                item.getParent().addDisposeListener(new DisposeListener()
                {
                    public void widgetDisposed(DisposeEvent e)
                    {
                        textLayout.dispose();
                    }
                });
            }
            textLayout.setFont(gc.getFont());
            textLayout.setText(item.getText(getColumn()));
            textLayout.setAlignment(getAlignment());
            textLayout.setWidth(width < 1 ? 1 : width);
            if (item.getParent().isAutoHeight())
            {
            
              // Look through all columns (except this one) to get the max height needed for this item
            int columnCount = item.getParent().getColumnCount();
            int maxHeight = textLayout.getBounds().height + textTopMargin + textBottomMargin;
            for (int i=0; i<columnCount; i++)
            {
              GridColumn column = item.getParent().getColumn(i);
              if (i != getColumn() && column.getWordWrap())
              {
                int height = column.getCellRenderer().computeSize(gc, column.getWidth(), SWT.DEFAULT, item).y;
                maxHeight = Math.max(maxHeight, height);
              }
            }

            // Also look at the row header if necessary
            if (item.getParent().isWordWrapHeader())
            {
            int height = item.getParent().getRowHeaderRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT, item).y;
          maxHeight = Math.max(maxHeight, height);
            }

            if (maxHeight != item.getHeight())
            {
              item.setHeight(maxHeight);
            }
            }
            textLayout.draw(gc, getBounds().x + x, getBounds().y + textTopMargin + topMargin);
        }


        if (item.getParent().getLinesVisible())
        {
            if (isCellSelected())
            {
                gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
            }
            else
            {
                gc.setForeground(item.getParent().getLineColor());
            }
            
            gc.drawLine(getBounds().x, getBounds().y + getBounds().height, getBounds().x
                                                                           + getBounds().width -1,
                        getBounds().y + getBounds().height);
            gc.drawLine(getBounds().x + getBounds().width - 1, getBounds().y,
                        getBounds().x + getBounds().width - 1, getBounds().y + getBounds().height);
        }

        if (isCellFocus())
        {
            Rectangle focusRect = new Rectangle(getBounds().x, getBounds().y, getBounds().width - 1,
                                                getBounds().height);

            gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
            gc.drawRectangle(focusRect);

            if (isFocus())
            {
                focusRect.x ++;
                focusRect.width -= 2;
                focusRect.y ++;
                focusRect.height -= 2;

//                gc.drawRectangle(focusRect);
            }
        }
	}
	
	 private boolean isCenteredCheckBoxOnly(GridItem item)
	    {
	    	return !isTree() && item.getImage(getColumn()) == null && item.getText(getColumn()).equals("")
			&& getAlignment() == SWT.CENTER;
	    }
}
