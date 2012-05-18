package com.datagardens.nq.sdb.client.views.sheets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.datagardens.nq.sdb.client.views.GridCellReference;
import com.datagardens.nq.sdb.client.views.GridCellReference.CellDataType;
import com.datagardens.nq.sdb.client.views.sheets.SheetColumnInformation.EditorType;
import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.SawSheet;

public class SawSheetView extends SheetView {

	public static int NUMBER_OF_LINES = 37;
	
	private Job selectedJob;
	private int shiftIndex;
	
	private Grid dataMgmtSection;
	private Grid operatorSection;
	
	private Map<Integer, IndividualCut> cuts;
	
	public SawSheetView(Composite parent, int style, int shiftIndex) 
	{
		super(parent, style, false, "Serialization Log Sheet", "Material Handlers");
		this.shiftIndex = shiftIndex;
		cuts = new HashMap<Integer, IndividualCut>();
	}
	
	@Override
	protected SheetColumnInformation[] generateColumnsInformation() 
	{
		SheetColumnInformation [] info = new SheetColumnInformation[10];
		
		info[0] = new SheetColumnInformation(70);
		info[1] = new SheetColumnInformation(70);
		info[2] = new SheetColumnInformation(70);
		info[3] = new SheetColumnInformation(70);
		info[4] = new SheetColumnInformation(70);
		info[5] = new SheetColumnInformation(70);
		info[6] = new SheetColumnInformation(70, EditorType.DATE);
		info[7] = new SheetColumnInformation(70);
		info[8] = new SheetColumnInformation(70);
		info[9] = new SheetColumnInformation(70);
		
		
		return info;
	}
	
	public int getShiftIndex() {
		return shiftIndex;
	}

	@Override
	protected void createSheet() 
	{
		dataMgmtSection = createSheetSection();
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Data Management; Prescribed Cuts", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GREEN));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Job #", 1, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Job Qty", 1));
		holder.put(2, new ItemColumnProperties("Qty Blanks", 1));
		holder.put(3, new ItemColumnProperties("Blank Length", 1));
		holder.put(4, new ItemColumnProperties("Yield / Blank", 1));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("managment_job_no", "", 1, 0, SWT.COLOR_BLUE, SWT.BOLD, SWT.DEFAULT, SWT.DEFAULT, CellDataType.TEXT));
		holder.put(1, new ItemColumnProperties("managment_job_qty", "", 1));
		holder.put(2, new ItemColumnProperties("managment_qty_blanks", "", 1, 0, SWT.DEFAULT, SWT.DEFAULT, CellDataType.INPUT_TEXT));
		holder.put(3, new ItemColumnProperties("managment_blank_length", "", 1, false, CellDataType.INPUT_TEXT));
		holder.put(4, new ItemColumnProperties("managment_yield_blank", "", 1, false, CellDataType.INPUT_TEXT));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Part #", 0, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("managment_part_no", "", 1, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(2, new ItemColumnProperties("Part Name", 0, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(3, new ItemColumnProperties("managment_part_name", "", 2, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(4, new ItemColumnProperties("Planned Net Yield", 1, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(5, new ItemColumnProperties("managment_planned_net_yield", "", 0, 0, SWT.DEFAULT, SWT.COLOR_RED, CellDataType.INPUT_TEXT));
		createGridItem(dataMgmtSection, holder);
		
		operatorSection = createSheetSection();
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Operator", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_YELLOW));
		createGridItem(operatorSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Cut Log Actual (to be filled inthe Material Handlers)",
				6, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		holder.put(1, new ItemColumnProperties("Total Actual Cut Yield", 1, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(2, new ItemColumnProperties("operator_total_actual_yield", "", 0, 0, false, CellDataType.INPUT_NUMBER));
		createGridItem(operatorSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Cut Length", 0, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Yield / Cut", 0, 0));
		holder.put(2, new ItemColumnProperties("S/N From:", 0, 0));
		holder.put(3, new ItemColumnProperties("S/N To:", 0, 0));
		holder.put(4, new ItemColumnProperties("Emp No.", 0, 0));
		holder.put(5, new ItemColumnProperties("MH Initial (parts mrkd)", 0, 0));
		holder.put(6, new ItemColumnProperties("Date (mo/day)", 0, 0, CellDataType.INPUT_DATE));
		holder.put(7, new ItemColumnProperties("NI#", 0, 0));
		holder.put(8, new ItemColumnProperties("Heat Number", 1, 0));
		createGridItem(operatorSection, holder).setHeight(42);
		
		for(int i=0; i<NUMBER_OF_LINES; i++)
		{
			holder = new HashMap<Integer, ItemColumnProperties>();
			for(int j=0; j<9; j++)
			{
				CellDataType dataType = CellDataType.INPUT_TEXT;
				
				switch(j)
				{
				case 0:
				case 1:
				case 2:
				case 3:
					dataType = CellDataType.INPUT_NUMBER;
					break;
					
				case 4:
					dataType = CellDataType.INPUT_EMPLOYEE_NUMBER;
					break;
					
				case 5:
					dataType = CellDataType.INPUT_BOOLEAN;
					break;
					
				case 6:
					dataType = CellDataType.INPUT_DATE;
					break;
				}
				
				holder.put(j, new ItemColumnProperties(i+"_"+j, "", j==8 ? 1 : 0,0, false, dataType));
			}
			
			createGridItem(operatorSection, holder);
		}
	}
	
	private void setCellValue(Map<Integer, GridCellReference> refs, int index, String value)
	{
		if(refs.get(index) != null)
		{
			refs.get(index).setValue(value);
		}
	}
	
	private void setCellValue(Map<Integer, GridCellReference> refs, int index, int value)
	{
		setCellValue(refs, index, value+"");
	}
	
	private void setCellValue(Map<Integer, GridCellReference> refs, int index, boolean value)
	{
		setCellValue(refs, index, value? GridCellReference.YES : 
			GridCellReference.NO);
	}
	
	@Override
	protected Point getSheetSize()
	{
		return new Point(708, 890);
	}
	
	
	public void fillWithJob(Job job) 
	{
		selectedJob = job;
		fillSawSheetInformation();
	}
	
	
	private void fillSawSheetInformation()
	{	
		SawSheet sheet = selectedJob.getSawSheet();
		
		GridItem item = dataMgmtSection.getItem(2);
		Map<Integer, GridCellReference> refs = 	getCellsReferences(item);
		
		setCellValue(refs, 0, selectedJob.getId());
		setCellValue(refs, 2, selectedJob.getQuantity());
		setCellValue(refs, 4, sheet.getQuantityBlanks());
		setCellValue(refs, 6, sheet.getBlankLength());
		setCellValue(refs, 8, sheet.getYieldPerBlank());

		item = dataMgmtSection.getItem(3);
		refs = getCellsReferences(item);
		
		setCellValue(refs, 1, selectedJob.getPartNumber());
		setCellValue(refs, 4, selectedJob.getPartDesc());
		setCellValue(refs, 9, sheet.getPlannedNetYield());
		
		GridItem totalYieldItem= operatorSection.getItem(1);
		getCellsReferences(totalYieldItem).get(9).setValue(sheet.getTotalActualCutYield());
	}
	
	
	public void updateShiftInformation() 
	{
		fillSawSheetInformation();
		
		try
		{
			SawSheet sheet = selectedJob.getSawSheet();
			
			if(sheet == null)
			{
				throw new ModelException("Indvalid Saw Sheet");
			}
			
			cuts = NQDataModel.getCutsForSawSheetShift(sheet, shiftIndex);
			
			int initialRow = 3;
			
			for(int i=0; i<NUMBER_OF_LINES; i++)
			{
				GridItem item = operatorSection.getItem((initialRow + i));
				Map<Integer, GridCellReference> refs = 	getCellsReferences(item);
				
				setCellValue(refs, 0, "");
				setCellValue(refs, 1, "");
				setCellValue(refs, 2, "");
				setCellValue(refs, 3, "");
				setCellValue(refs, 4, "");
				setCellValue(refs, 5, "");
				setCellValue(refs, 6, "");
				setCellValue(refs, 7, "");
				setCellValue(refs, 8, "");
			}
			
			for(int i=0; i<cuts.size(); i++)
			{
				IndividualCut cut = cuts.get(i);
				GridItem item = operatorSection.getItem((initialRow + i));
				Map<Integer, GridCellReference> refs = 	getCellsReferences(item);
				
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ut.getDate()= " + cut.getDate());
				setCellValue(refs, 0, cut.getCutLength());
				setCellValue(refs, 1, cut.getYieldCut());
				setCellValue(refs, 2, cut.getSerialNumberFirstCut());
				setCellValue(refs, 3, cut.getSerialNumberLastCut());
				setCellValue(refs, 4, cut.getEmpNumber());
				setCellValue(refs, 5, cut.isSigned());
				setCellValue(refs, 6, cut.getDate());
				setCellValue(refs, 7, cut.getnI());
				setCellValue(refs, 8, cut.getHeatNumber());
			}
		}
		catch(ModelException e)
		{
			e.printStackTrace();
			MessageDialog.openError(getShell(), "Saw Sheet", e.getMessage());
		}
		
	}
	
	
	@Override
	protected void saveLine(int lineNumber, Map<Integer, GridCellReference> refs) 
	throws ModelException 
	{
		
		SawSheet sheet = selectedJob.getSawSheet();
		
		if(refs.size() < 9)
		{
			//header
			switch(lineNumber)
			{
			
			case 1: 
				sheet.setTotalActualCutYield(Integer.parseInt(refs.get(9).getValue()));
				break;
				
			case 2:
				sheet.setQuantityBlanks(Integer.parseInt(refs.get(4).getValue()));
				sheet.setBlankLength(Integer.parseInt(refs.get(6).getValue()));
				sheet.setYieldPerBlank(Integer.parseInt(refs.get(8).getValue()));
				break;
				
			case 3:
				sheet.setPlannedNetYield(Integer.parseInt(refs.get(9).getValue()));
				break;
				
				default:
					break;
			}
		}
		else
		{
			int cutLineNumber = lineNumber - 3;
			
			//cuts
			IndividualCut newCutLine = new IndividualCut(
					shiftIndex, 
					cutLineNumber,
					Integer.parseInt(refs.get(2).getValue()),
					Integer.parseInt(refs.get(3).getValue()),
					Integer.parseInt(refs.get(0).getValue()),
					refs.get(4).getValue(),
					refs.get(6).getValue(),
					refs.get(8).getValue(),
					refs.get(7).getValue(), 
					Integer.parseInt(refs.get(1).getValue()), 
					refs.get(5).getBooleanValue());
			
			sheet.getCuts().clear();
			sheet.modifyCut(newCutLine);
		}
		
		NQDataModel.saveSawSheet(sheet);
	}
}
