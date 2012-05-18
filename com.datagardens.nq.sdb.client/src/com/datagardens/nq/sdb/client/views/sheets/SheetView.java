package com.datagardens.nq.sdb.client.views.sheets;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.views.GridCellReference;
import com.datagardens.nq.sdb.client.views.sheets.SheetColumnInformation.EditorType;
import com.datagardens.nq.sdb.commons.model.ModelException;

public abstract class SheetView extends ScrolledComposite {

	private Composite outerComposite;
	private Composite sheetComposite;
	protected SheetColumnInformation [] columnsInformation;
	protected Map<String, GridCellReference> cells;
	private GridEditorsLayer editors;
	
	protected Map<Integer, Color> cellColors;
	protected Map<Integer, ItemColumnProperties> holder;
	
	protected Map<Grid, SheetGridEditor> editorsMap;
	
	
	public SheetView(Composite parent, int style,
			boolean createEmpoyerHeaderInformation, String title, String subtitle) 
	
	{
		super(parent, style | SWT.V_SCROLL | SWT.H_SCROLL);
		
		cellColors = new HashMap<Integer, Color>();
		cellColors.put(SWT.COLOR_DARK_GRAY, SWTResourceManager.getColor(221, 221, 221));
		cellColors.put(SWT.COLOR_GRAY, SWTResourceManager.getColor(234, 234, 234));
		cellColors.put(SWT.COLOR_YELLOW, SWTResourceManager.getColor(255, 255, 204));
		cellColors.put(SWT.COLOR_BLUE, SWTResourceManager.getColor(204, 236, 255));
		cellColors.put(SWT.COLOR_DARK_BLUE, SWTResourceManager.getColor(153, 204, 255));
		cellColors.put(SWT.COLOR_RED, SWTResourceManager.getColor(255, 234, 213));
		cellColors.put(SWT.COLOR_GREEN, SWTResourceManager.getColor(221, 255, 221));
		cellColors.put(SWT.COLOR_DARK_GREEN, SWTResourceManager.getColor(51, 204, 204));
		cellColors.put(SWT.COLOR_MAGENTA, SWTResourceManager.getColor(255, 204, 0));
		
		//change to blue
		cellColors.put(SWT.COLOR_CYAN, SWTResourceManager.getColor(204, 236, 255));
		
		cells = new LinkedHashMap<String, GridCellReference>();
		editorsMap = new HashMap<Grid, SheetGridEditor>();
		
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setColumnsInformation(generateColumnsInformation());
		
		createSheetArea(getSheetSize());
		createSheetHeader(title, subtitle, createEmpoyerHeaderInformation);
		createSheet();
		
		sheetComposite.layout();
		
		setExpandHorizontal(true);
		setExpandVertical(true);
		setMinSize(outerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	
	
	public void setColumnsInformation(
			SheetColumnInformation[] columnsInformation) {
		this.columnsInformation = columnsInformation;
	}

	private void createSheetArea(Point sheetSize) 
	{
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setLayout(new FillLayout());
		
		outerComposite = new Composite(this, SWT.NONE);
		outerComposite.setLayout(new GridLayout(1, false));
		setContent(outerComposite);
		outerComposite.setBackground(getBackground());
		
		Composite abosulteComp = new Composite(outerComposite, SWT.NONE);
		GridData sheetData = new GridData(SWT.CENTER, SWT.TOP, true, true);
		sheetData.widthHint = sheetSize.x;
		sheetData.heightHint = sheetSize.y;
		abosulteComp.setLayoutData(sheetData);
		abosulteComp.setLayout(null);
		
		editors = new GridEditorsLayer(abosulteComp);
		editors.addEditorListener(new IGridTextEditorListener() {
			
			@Override
			public void valueEntered(GridTextEditorEvent e) 
			{
				if(moveToTheNextCell(e.grid, e.keyCode))
				{
					
				}
				else
				{
					commitSheetLine();
				}
			}
		});
		
		sheetComposite = new Composite(abosulteComp, SWT.NONE);
		sheetComposite.setBounds(0, 0, sheetSize.x, sheetSize.y);
		sheetComposite.setLayout(new GridLayout(1, false));
		sheetComposite.setBackground(getBackground());
	}
	
	private void createSheetHeader(String title, String subTitle,
			boolean createEmpoyerHeaderInformation) 
	{
		Composite headerComp = new Composite(sheetComposite, SWT.NONE);
		headerComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 0;
		layout.marginLeft = 20;
		layout.marginRight = 80;
		layout.verticalSpacing = 2;
		headerComp.setLayout(layout);
		headerComp.setBackground(sheetComposite.getBackground());
		
		Label logo = new Label(headerComp, SWT.NONE);
		logo.setImage(SWTResourceManager.getImage(ImageKey.LOGO));
		logo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, createEmpoyerHeaderInformation?4:2));
		logo.setBackground(headerComp.getBackground());
		
		Label sheetTitle = new Label(headerComp, SWT.NONE);
		sheetTitle.setFont(SWTResourceManager.getBoldFont(sheetTitle.getFont()));
		sheetTitle.setText(title);
		sheetTitle.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 2, 1));
		sheetTitle.setBackground(headerComp.getBackground());
		
		Label sheetInfo = new Label(headerComp, SWT.NONE);
		sheetInfo.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		sheetInfo.setFont(SWTResourceManager.getBoldFont(sheetTitle.getFont()));
		sheetInfo.setText(subTitle);
		sheetInfo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 2, 1));
		sheetInfo.setBackground(headerComp.getBackground());
		
		if(createEmpoyerHeaderInformation)
		{
			Label employeeNo = new Label(headerComp, SWT.NONE);
			employeeNo.setText("Employee:");
			employeeNo.setFont(SWTResourceManager.getBoldFont(employeeNo.getFont()));
			employeeNo.setBackground(getBackground());
			
			Label empoyeeField = new Label(headerComp, SWT.NONE);
			empoyeeField.setText("____________________________");
			empoyeeField.setBackground(getBackground());
			
			Label employeeName = new Label(headerComp, SWT.NONE);
			employeeName.setText("Empoyee Name:");
			employeeName.setFont(SWTResourceManager.getBoldFont(employeeNo.getFont()));
			employeeName.setBackground(getBackground());
			
			Label empoyeeNameField = new Label(headerComp, SWT.NONE);
			empoyeeNameField.setText("____________________________");
			empoyeeNameField.setBackground(getBackground());
			
		}
	}
	
	protected Grid createSheetSection()
	{
		
		Composite border = new Composite(sheetComposite, SWT.NONE);
		FillLayout layout = new FillLayout();
		layout.marginHeight= 2;
		layout.marginWidth= 2;
		border.setLayout(layout);
		border.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		
		final Grid grid = new Grid(border, SWT.NONE);
		grid.setCellSelectionEnabled(true);
		grid.setLinesVisible(true);
		grid.setLineColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		
		SheetGridEditor cellEditor = new SheetGridEditor(grid);
		editorsMap.put(grid, cellEditor);
		
		grid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				showGridEditor(grid);
			}
		});
		
		cellEditor.addEditorListener(new SheetEditorAdapter()
		{
			@Override
			public void endOfLineReached(int lineNumber,
					Map<Integer, GridCellReference> refs) 
			{
				try 
				{
					System.out.println("end of line reached");
					saveLine(lineNumber, refs);
				}
				catch (ModelException e) 
				{
					e.printStackTrace();
					MessageDialog.openError(getShell(), "Error", e.getMessage());
				}
			}
		});
		
		
 		createColumsForGrid(grid);
		
		
		return grid;
	}
	
	private void showGridEditor(Grid grid) 
	{
		SheetGridEditor editor = editorsMap.get(grid);
		
		if(grid.getSelectionCount() == 1)
		{
			GridItem item = grid.getSelection()[0];
			Point cell =  grid.getCellSelection()[0];
			
			Map<Integer, GridCellReference> refs = getCellsReferences(item);
			GridCellReference ref = refs.get(cell.x);
			
			if(ref != null && ref.isEditable())
			{
				editor.showEditor(ref);
			}
		}
		else
		{
			editor.hideAllEditors();
		}
	}
	
	
	
	protected Grid createSheetSection(String title, int fontColor, int backgroundColor)
	{
		return null;
	}
	
	protected GridItem createGridItem(Grid grid, Map<Integer, ItemColumnProperties> properties)
	{
		GridItem newItem = new GridItem(grid, SWT.CENTER);
		Map<Integer, GridCellReference> itemCells = 
				new HashMap<Integer, GridCellReference>();
		newItem.setData("cells_refs", itemCells);
		
		/* control variables */
		int globalIndex = 0;
		boolean usingBoldFont = false;
		Font boldFont = SWTResourceManager.getBoldFont(getFont());
		Font lastFontSized = null;
		Color lastFontColor = null;
		Color lastBackgroundColor = null;
		
		for(int i=0, imax=properties.size(); i<imax; i++)
		{
			ItemColumnProperties prop = properties.get(i);
			
			if(prop.dynamicCellName != null)
			{
				GridCellReference cellRef = new GridCellReference(newItem, globalIndex, prop.dataType);
				cells.put(prop.dynamicCellName, cellRef);
				itemCells.put(globalIndex, cellRef);
			}
			
			if(prop instanceof ItemColumnPusher)
			{
				globalIndex += ((ItemColumnPusher) prop).columnSpan;
				continue;
			}
			
			/* column span */
			newItem.setColumnSpan(globalIndex, prop.columnSpan);
			
			/* row span */
			newItem.setRowSpan(globalIndex, prop.rowSpan);
			
			/* text */
			newItem.setText(globalIndex, prop.text);
			
			/* bold font */
			switch(prop.fontStyle)
			{
			case SWT.DEFAULT:
				usingBoldFont = false;
				break;
				
			case SWT.INHERIT_FORCE:
				if(usingBoldFont)
				{
					newItem.setFont(boldFont);
				}
				break;
				
			case SWT.BOLD:
				usingBoldFont = true;
				newItem.setFont(boldFont);
				break;
				
			default:
				usingBoldFont = false;
				break;
					
			}
			
			/* font color */
			switch(prop.fontColorKey)
			{
			case SWT.DEFAULT:
				lastFontColor = null;
				break;
				
			case SWT.INHERIT_FORCE:
				break;
				
			default:
				lastFontColor = SWTResourceManager.getColor(prop.fontColorKey);
				break;
				
			}
			
			if(lastFontColor != null)
			{
				newItem.setForeground(globalIndex, lastFontColor);
			}
			
			
			/* font size */
			switch(prop.fontSize)
			{
				case SWT.DEFAULT:
					break;
					
				case SWT.INHERIT_FORCE:
					if(lastFontSized != null)
					{
						newItem.setFont(lastFontSized);
					}
					break;
					
				default:
					lastFontSized = SWTResourceManager.changeFontHeight(newItem.getFont(), 
							prop.fontSize);
					break;
			}
			
			/* background color */
			switch(prop.backGroundColorKey)
			{
			case SWT.DEFAULT:
				lastBackgroundColor = null;
				break;
				
			case SWT.INHERIT_FORCE:
				break;
				
			default:
				lastBackgroundColor = cellColors.get(prop.backGroundColorKey);
				break;
				
			}
			
			if(lastBackgroundColor != null)
			{
				newItem.setBackground(globalIndex, lastBackgroundColor);
			}
			
			
			globalIndex = globalIndex + prop.columnSpan + 1;	
		}
		
		return newItem;
	}
	
	private void createColumsForGrid(Grid grid) 
	{
		for(SheetColumnInformation info : columnsInformation)
		{
			GridColumn col = new GridColumn(grid, info.checkable ? SWT.CHECK : SWT.NONE);
			
			col.setCellRenderer(new SheetCellRender());
			
			col.setWidth(info.width);
			col.setAlignment(SWT.CENTER);
			col.setWordWrap(true);
			col.setCellSelectionEnabled(info.cellSelectionEnabled);
		}
	}
	protected void activateEditor(EditorType type, Rectangle bounds, GridCellReference reference)
	{
		editors.activate(type, bounds, reference);
	}
	
	protected void deactivateEditor()
	{
		editors.deactivate();
	}
	
	protected boolean userCanEditSheet() {
		return true;
	}
	
	protected void showEditor(Grid grid) 
	{
		if(grid.getSelectionCount() == 1 &&
				userCanEditSheet())
		{
			GridItem item = grid.getSelection()[0];
			Point selectedCellCoordinates = grid.getCellSelection()[0];
			
			if(item.getData("cells_refs") != null &&
					item.getData("cells_refs") instanceof Map<?, ?>)
			{
				@SuppressWarnings("unchecked")
				Map<Integer, GridCellReference> references = 
						(Map<Integer, GridCellReference>) item.getData("cells_refs");
				
				GridCellReference ref = references.get(selectedCellCoordinates.x);
				
				if(ref != null && ref.isEditable())
				{
					switch(ref.getDataType())
					{
					case INPUT_BOOLEAN:
						deactivateEditor();
						grid.setFocusColumn(grid.getColumn(selectedCellCoordinates.x));
						grid.setFocusItem(item);
						grid.selectCell(selectedCellCoordinates);
						break;
						
					case INPUT_TEXT:
						Rectangle cellBounds = item.getBounds(selectedCellCoordinates.x);
						Composite gridBorder = grid.getParent();
						
						cellBounds.y += grid.getBounds().y + gridBorder.getBounds().y;
						cellBounds.x += grid.getBounds().x + gridBorder.getBounds().x;
						
						activateEditor(columnsInformation[selectedCellCoordinates.x].editorType, cellBounds, ref);
						break;
						
						default:
							deactivateEditor();
							break;
					}
				}
			}
		}
	}

	private void commitSheetLine() 
	{
		//TODO: commit sheet line
	}

	private boolean moveToTheNextCell(Grid grid, int keyCode) 
	{
		
		
		final int actualCol = grid.getCellSelection()[0].x;
		final int actualRow = grid.getCellSelection()[0].y;
		
		GridItem item = grid.getItem(actualRow);
		Map<Integer, GridCellReference> itemCells = getCellsReferences(item);
		
		GridCellReference ref = itemCells.get(actualCol);	
		
		int maxRow = grid.getItemCount()-1;
		int maxCol = grid.getColumnCount()-1;
		
		int nextCol = -1;
		boolean nextColFound = false;
		
		for(Integer key : itemCells.keySet())
		{
			if(key > actualCol && !nextColFound)
			{
				
				nextCol = key;
				nextColFound = true;
			}
			
			maxCol = key;
		}
		
		
		int toRow = -1;
		int toCol = -1;
		
		int minRow = 0;
		int minCol = 0;

		switch (keyCode) {
		
		case SWT.ARROW_UP:
			
			toRow = actualRow-1 < minRow ? minRow: actualRow-1;
			toCol = actualCol;
			break;
		
		case SWT.ARROW_DOWN:
			toRow = actualRow + 1 > maxRow ? maxRow : actualRow + 1;
			toCol = actualCol;
			break;
			
		case SWT.ARROW_LEFT:
			toRow = actualRow;
			toCol = actualCol-1 < minCol ? minCol : actualCol-1;
			break;
		
		case SWT.TAB:
		case SWT.KEYPAD_CR:
		case SWT.CR:	
		case SWT.ARROW_RIGHT:
			
			toRow = actualRow;
			nextCol = actualCol + ref.getItem().getColumnSpan(actualCol) + 1;
			toCol = nextCol > maxCol ? actualCol : nextCol;
			
			if(actualCol == toCol)
			{
				commitSheetLine(actualRow-minRow, grid.getItem(actualRow));
				//Save Line
				//Move to the next Row if any
				toCol = minCol;
				toRow = toRow + 1 > maxRow ? maxRow : toRow + 1;
			}
			break;
			
		default:
			break;
		}
		
		grid.deselectAllCells();
		
		if(toRow != -1 && toCol != -1)
		{
			grid.selectCell(new Point(toCol, toRow));
			grid.setFocusColumn(grid.getColumn(toCol));
			grid.setFocusItem(grid.getItem(toRow));
			showEditor(grid);
		}
		
		return true;
	}
	
	private void commitSheetLine(final int itemIndex, final GridItem item) 
	{
		if(!getCellsReferences(item).isEmpty())
		{
			BusyIndicator.showWhile(getDisplay(), new Runnable() {
				
				@Override
				public void run() {
					try
					{
						saveLine(itemIndex, getCellsReferences(item));
					}
					catch(ModelException e)
					{
						MessageDialog.openError(getShell(), "Error", e.getMessage());
						e.printStackTrace();
					}
				}
			});
		}
	}



	@SuppressWarnings("unchecked")
	protected Map<Integer, GridCellReference> getCellsReferences(GridItem item)
	{
		if(item.getData("cells_refs") != null &&
				item.getData("cells_refs") instanceof Map<?, ?>)
		{
			
			return (Map<Integer, GridCellReference>) item.getData("cells_refs");
		}
		
		return new HashMap<Integer, GridCellReference>();
	}

	protected abstract Point getSheetSize();
	protected abstract SheetColumnInformation[] generateColumnsInformation();
	protected abstract void createSheet();
	protected abstract void saveLine(int lineNumber, Map<Integer, GridCellReference> refs) throws ModelException;
}
