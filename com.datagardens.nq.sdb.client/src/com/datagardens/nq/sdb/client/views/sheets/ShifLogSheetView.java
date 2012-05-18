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
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogDetail;
import com.datagardens.nq.sdb.commons.model.ShiftLogLine;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;
import com.datagardens.nq.sdb.commons.model.WorkpieceStatus;

public class ShifLogSheetView extends SheetView
{
	private Grid dataMgmtSection;
	private Grid qcSection;
	private Grid operatorSection;
	private Grid adminSection;
	
	private Job selectedJob;
	private ShiftLogSheet selectedSheet;
	private int shiftIndex;

	private Map<Integer, ShiftLogLine> cuts;
	
	
	public ShifLogSheetView(Composite parent, int style, int shiftIndex) 
	{
		super(parent, style,
				true, 
				"Serialization Log Sheet", 
				"Operational");
		cuts = new HashMap<Integer, ShiftLogLine>();
		this.shiftIndex = shiftIndex;
	}
	public int getShiftIndex() {
		return shiftIndex;
	}
	
	@Override
	protected void createSheet() 
	{
		
		dataMgmtSection = createSheetSection();
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Data Managment", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GREEN));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Date", 1, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Job #", 1));
		holder.put(2, new ItemColumnProperties("Job Qty", 1));
		holder.put(3, new ItemColumnProperties("Pieces Cut", 1));
		holder.put(4, new ItemColumnProperties("Operation #", 1));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("managment_date", "", 1));
		holder.put(1, new ItemColumnProperties("managment_job_no", "", 1,0, SWT.COLOR_BLUE, SWT.BOLD, SWT.DEFAULT, SWT.DEFAULT, CellDataType.TEXT));
		holder.put(2, new ItemColumnProperties("managment_job_qty", "", 1));
		holder.put(3, new ItemColumnProperties("managment_pieces_cut", "", 1));
		holder.put(4, new ItemColumnProperties("managment_operation_no", "", 1,0, SWT.COLOR_RED, SWT.BOLD, 16, SWT.DEFAULT, CellDataType.TEXT));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Part #", 0, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("managment_part_no", "", 1, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(2, new ItemColumnProperties("Dwg #",  0, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(3, new ItemColumnProperties("managment_dwg", "", 1,0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(4, new ItemColumnProperties("Part Name: ", 1, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(5, new ItemColumnProperties("managment_part_name", "", 1, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		createGridItem(dataMgmtSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("First Off Inspection Required? (Y/N):", 3,0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		holder.put(1, new ItemColumnProperties("managment_first_insp_rqd", "", 0,0, SWT.DEFAULT, SWT.DEFAULT, CellDataType.INPUT_BOOLEAN));
		holder.put(2, new ItemColumnProperties("In Process Inspection Frequency:", 3,0, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		holder.put(3, new ItemColumnProperties("managment_insp_frec", "", 0, 0, SWT.DEFAULT, SWT.DEFAULT, CellDataType.INPUT_TEXT));
		createGridItem(dataMgmtSection, holder);
		
		qcSection = createSheetSection();
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("QC, First Off Disposition", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_BLUE));
		createGridItem(qcSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Serial Number", 1, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Inspector Emp #", 1));
		holder.put(2, new ItemColumnProperties("100% Conforms (Y/N)", 1));
		holder.put(3, new ItemColumnProperties("IOR #", 1));
		holder.put(4, new ItemColumnProperties("Machinist Initial", 1, 0, SWT.DEFAULT, SWT.COLOR_YELLOW));
		createGridItem(qcSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("qc_serial_number", "", 1, true, CellDataType.INPUT_TEXT));
		holder.put(1, new ItemColumnProperties("qc_insp_emp_no","", 1, true, CellDataType.INPUT_TEXT));
		holder.put(2, new ItemColumnProperties("qc_100_conforms","", 1, true, CellDataType.INPUT_BOOLEAN));
		holder.put(3, new ItemColumnProperties("qc_ior_no","", 1, true, CellDataType.INPUT_TEXT));
		holder.put(4, new ItemColumnProperties("qc_machinist_init","", 1, true, CellDataType.INPUT_BOOLEAN));
		createGridItem(qcSection, holder);
		
		
		operatorSection = createSheetSection();
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Operator", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_YELLOW));
		createGridItem(operatorSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("The First Part was Started on my shift (Y/N)", 3, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		holder.put(1, new ItemColumnProperties("opr_first_part_started", "", 0, 0, false, CellDataType.INPUT_BOOLEAN));
		holder.put(2, new ItemColumnProperties("The First Part was Completed on my shift (Y/N)", 3, 0, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		holder.put(3, new ItemColumnProperties("opr_first_part_completed", "", 0, 0, false, CellDataType.INPUT_BOOLEAN));
		createGridItem(operatorSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("S/N", 0, 1, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Started (Emp#)", 0, 1));
		holder.put(2, new ItemColumnProperties("Workpiece Status", 2, 0));
		holder.put(3, new ItemColumnProperties("Date (mo/day)", 0, 1));
		holder.put(4, new ItemColumnProperties("Self QC Initials", 0, 1));
		holder.put(5, new ItemColumnProperties("In Process Inspection", 1, 0, SWT.DEFAULT, SWT.COLOR_CYAN));
		holder.put(6, new ItemColumnProperties("IOR #", 0, 1));
		createGridItem(operatorSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(2));
		holder.put(1, new ItemColumnProperties("Incomp.", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_GRAY));
		holder.put(2, new ItemColumnProperties("Comp.", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_GRAY));
		holder.put(3, new ItemColumnProperties("Scrap", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_GRAY));
		holder.put(4, new ItemColumnPusher(2));
		holder.put(5, new ItemColumnProperties("QC Emp #", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_CYAN));
		holder.put(6, new ItemColumnProperties("Initials", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_CYAN));
		createGridItem(operatorSection, holder);
		
		for(int i=0; i<33; i++)
		{
			holder = new HashMap<Integer, ItemColumnProperties>();
			for(int j=0; j<columnsInformation.length; j++)
			{
				CellDataType dataType = CellDataType.INPUT_TEXT;
				
				switch(j)
				{
				case 0:
					dataType = CellDataType.INPUT_NUMBER;
					break;
					
				case 1:
				case 7:
					dataType = CellDataType.INPUT_EMPLOYEE_NUMBER;
					break;
					
				case 6:
				case 8:
					dataType = CellDataType.INPUT_BOOLEAN;
					break;
					
				case 5:
					dataType = CellDataType.INPUT_DATE;
					break;
					
				default:
					break;
				}
				
				holder.put(j, new ItemColumnProperties(i + "_"+ j, "", 0, 0, false, 
						dataType));
			}			
			createGridItem(operatorSection, holder);
		}
		
		adminSection = createSheetSection();
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Administrator", 9, 0, SWT.COLOR_DARK_RED, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GRAY));
		createGridItem(adminSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("Labour Hours", 1, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Completed Parts", 1));
		holder.put(2, new ItemColumnProperties("Net Cycle Time for Op.", 1));
		holder.put(3, new ItemColumnProperties("Avrg Cycle Time / Op.", 1));
		holder.put(4, new ItemColumnProperties("Parts Scrapped", 1, 0));
		createGridItem(adminSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("admin_labor_hours", "", 1));
		holder.put(1, new ItemColumnProperties("admin_completed_parts", "", 1));
		holder.put(2, new ItemColumnProperties("admin_net_cycle_time", "", 1));
		holder.put(3, new ItemColumnProperties("admin_avrg_cycle_time", "", 1));
		holder.put(4, new ItemColumnProperties("admin_parts_scrapped", "", 1));
		createGridItem(adminSection, holder);
		
	}

	@Override
	protected Point getSheetSize() {
		return new Point(620, 1000);
	}
	
	
	public void fillWithJobAndSheet(Job job, ShiftLogSheet sheet) 
	{
		selectedJob = job;
		selectedSheet = sheet;
		fillSawSheetInformation();
	}
	
	private void fillSawSheetInformation() 
	{
		GridItem item = dataMgmtSection.getItem(2);
		Map<Integer, GridCellReference> refs = getCellsReferences(item);
		
		refs.get(0).setValue("2012-02-02");
		refs.get(2).setValue(selectedJob.getId());
		refs.get(4).setValue(selectedJob.getQuantity());
		refs.get(6).setValue(selectedSheet.getPiecesCut());
		refs.get(8).setValue(selectedSheet.getOperationNumber());
		
		item = dataMgmtSection.getItem(3);
		refs = getCellsReferences(item);
		
		refs.get(1).setValue(selectedJob.getPartNumber());
		refs.get(4).setValue(selectedJob.getDrawingNumber());
		refs.get(8).setValue(selectedJob.getPartDesc());
		
		item = dataMgmtSection.getItem(4);
		refs = getCellsReferences(item);
		
		refs.get(4).setValue(selectedSheet.isFirstOffInspectionRequired());
		refs.get(9).setValue(selectedSheet.getInProcessInspectionFrequency());
		
		updateShiftLogQCData();
	}
	
	public void updateShiftLogQCData()
	{
		try
		{
			System.out.println("getting shif log detaisl for index" + shiftIndex);
			ShiftLogDetail detail = NQDataModel.getShiftLogDetials(selectedSheet, shiftIndex);
			GridItem item = qcSection.getItem(2);
			
			Map<Integer, GridCellReference> refs = getCellsReferences(item);
			refs.get(0).setValue(detail.getSerialNumber());
			refs.get(2).setValue(detail.getInspectorEmployeeNumber());
			refs.get(4).setValue(detail.isConfirms100Percent());
			refs.get(6).setValue(detail.getIorNumber());
			refs.get(8).setValue(detail.isMachinistSigned());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
			
			System.out.println("getting shif log CUTS for index" + shiftIndex);
			cuts = NQDataModel.getCutsForShiftLogShift(selectedSheet, shiftIndex);
			
			int initialRow = 3; 
			for(int i=0; i<30; i++)
			{
				GridItem item = operatorSection.getItem((initialRow + i));
				Map<Integer, GridCellReference> refs = 	getCellsReferences(item);
				
				if (refs == null)
				{
					continue;
				}
				
				setCellValue(refs, 0, "");
				setCellValue(refs, 1, "");
				setCellValue(refs, 2, "");
				setCellValue(refs, 3, "");
				setCellValue(refs, 4, "");
				setCellValue(refs, 5, "");
				setCellValue(refs, 6, "");
				setCellValue(refs, 7, "");
				setCellValue(refs, 8, "");
				setCellValue(refs, 9, "");
			}
			
			for(ShiftLogLine line : cuts.values())
			{
				System.out.println(">> line " + line);
				setSheetLogSheetLine(line);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			MessageDialog.openError(getShell(), "Saw Sheet", e.getMessage());
		}
	}
	
	private void setSheetLogSheetLine(ShiftLogLine line) 
	{
		GridItem item = operatorSection.getItem((4+line.getShiftLogLineNumber()));
		Map<Integer, GridCellReference> refs = 	getCellsReferences(item);
		
		setCellValue(refs, 0, line.getSerialNumber());
		setCellValue(refs, 1, line.getStartedEmployeeNumber());
		setCellValue(refs, 5, line.getDate());
		setCellValue(refs, 6, line.isSelfQCSigned());
		setCellValue(refs, 7, line.getQcEmployeeNumber());
		setCellValue(refs, 8, line.isQcSigned());
		setCellValue(refs, 9, line.getIORNumber());
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
		setCellValue(refs, index, value ? GridCellReference.YES : GridCellReference.NO);
	}
	
	public void fillWithSheet(Job job, ShiftLogSheet shiftLog) 
	{
		/*selectedShiftLogSheet = shiftLog;
		GridItem manageItem = dataMgmtSection.getItem(2);
		Map<Integer, GridCellReference> refs = getCellsReferences(manageItem);

//		refs.get(0).setValue(shiftLog.getDate());
		refs.get(2).setValue(job.getId());
		refs.get(4).setValue(job.getQuantity());
		refs.get(6).setValue(shiftLog.getPiecesCut());
		refs.get(8).setValue(shiftLog.getOperationNumber());
		
		manageItem = dataMgmtSection.getItem(3);
		refs = getCellsReferences(manageItem);
		
		refs.get(1).setValue(job.getPartNumber());
		refs.get(4).setValue(job.getDrawingNumber());
		refs.get(8).setValue(job.getPartDesc());
		
		manageItem = dataMgmtSection.getItem(4);
		refs = getCellsReferences(manageItem);
		
		refs.get(4).setValue(shiftLog.isFirstOffInspectionRequired()?"YES":"");
		refs.get(9).setValue(shiftLog.getInProcessInspectionFrequency());
		
		
		manageItem = qcSection.getItem(2);
		refs = getCellsReferences(manageItem);
		
		refs.get(0).setValue(shiftLog.getSerialNumber());
		refs.get(2).setValue(shiftLog.getEmployeeNumber());
		refs.get(4).setValue(shiftLog.isHundredPercent()?"YES":"");
		refs.get(6).setValue(shiftLog.getIORNumber());
		refs.get(8).setValue(shiftLog.isMachinistSigned()?"YES" : "");
		
		
		
		manageItem = operatorSection.getItem(1);
		refs = getCellsReferences(manageItem);
		
		refs.get(4).setValue(shiftLog.isTheFirstPartWasStartedOnMyShift()?"YES":"");
		refs.get(9).setValue(shiftLog.isTheFirstPartWasCompletedOnMyShift()?"YES":"");
		
		manageItem = adminSection.getItem(2);
		refs = getCellsReferences(manageItem);
		
		refs.get(0).setValue(shiftLog.getLabourHours());
		refs.get(2).setValue(shiftLog.getCompleteParts());
		refs.get(4).setValue(shiftLog.getNetCycleTimePerOperation());
		refs.get(6).setValue(shiftLog.getAverageCycleTimePerOperation());
		refs.get(8).setValue(shiftLog.getPartsScrapped());*/
	}
	
	@Override
	protected SheetColumnInformation[] generateColumnsInformation() {
		
		SheetColumnInformation [] columnsInformation = 
				new SheetColumnInformation [10];
		
		columnsInformation[0]  = new SheetColumnInformation(60);
		columnsInformation[1]  = new SheetColumnInformation(60);
		columnsInformation[2]  = new SheetColumnInformation(60);
		columnsInformation[3]  = new SheetColumnInformation(60);
		columnsInformation[4]  = new SheetColumnInformation(60);
		columnsInformation[5]  = new SheetColumnInformation(60);
		columnsInformation[6]  = new SheetColumnInformation(60);
		columnsInformation[7]  = new SheetColumnInformation(60);
		columnsInformation[8]  = new SheetColumnInformation(60);
		columnsInformation[9]  = new SheetColumnInformation(60);
		
		return columnsInformation;
	}

	@Override
	protected void saveLine(int lineNumber, Map<Integer, GridCellReference> refs)
	throws ModelException 
	{
		
		if(refs.size() <= 9)
		{
			switch(lineNumber)
			{
			case 1:
				saveShiftLogDetail();
				break;
				
			case 2:
				saveShiftLogDetail();
				break;
				
			case 4:
				selectedSheet.setFirstOffInspectionRequired(Boolean.parseBoolean(refs.get(4).getValue()));
				selectedSheet.setInProcessInspectionFrequency(Integer.parseInt(refs.get(9).getValue()));
				NQDataModel.saveShiftLogSheet(selectedSheet);
				break;
			}
			
		}
		else
		{
//			This are the lines
			lineNumber = lineNumber - 4;
			ShiftLogLine line = new ShiftLogLine(
					selectedJob.getId(), 
					selectedSheet.getOperationNumber(), 
					selectedSheet.getSheetNumber(),
					shiftIndex,
					lineNumber,
					Integer.parseInt(refs.get(0).getValue()),
					Integer.parseInt(refs.get(1).getValue()),
					WorkpieceStatus.COMPLETE,
					refs.get(5).getValue(), 
					Boolean.parseBoolean(refs.get(6).getValue()), 
					Integer.parseInt(refs.get(7).getValue()),
					Boolean.parseBoolean(refs.get(8).getValue()),
					refs.get(9).getValue());
			
			NQDataModel.saveShiftLogLine(line);
			
			/*ShiftLogLine line = ShiftLogLine.getEmptyLine(selectedShiftLogSheet, lineNumber);
			
			line.setSerialNumber(Integer.parseInt(refs.get(0).getValue()));
			line.setStartedEmployeeNumber(refs.get(1).getValue());
			line.setWorkpieceStatus(WorkpieceStatus.COMPLETE); //TODO: fix
			line.setDate(refs.get(5).getValue());
			line.setSelfQCSigned(Boolean.parseBoolean(refs.get(6).getValue()));
			line.setQcEmployeeNumber(refs.get(7).getValue());
			line.setQcSigned(Boolean.parseBoolean(refs.get(8).getValue()));
			line.setIORNumber(refs.get(9).getValue());*/
			
//			selectedShiftLogSheet.modifyLine(line);
		}
		
//		NQDataModel.saveShiftLogSheet(selectedShiftLogSheet);
	}
	
	private void saveShiftLogDetail()
	throws ModelException 
	{
		ShiftLogDetail detail = new ShiftLogDetail();
		detail.setJobId(selectedJob.getId());
		detail.setOperationNumber(selectedSheet.getOperationNumber());
		detail.setShiftLogNumber(selectedSheet.getSheetNumber());
		detail.setShiftNumber(shiftIndex);
		
		GridItem item = qcSection.getItem(2);
		Map<Integer, GridCellReference> refs = getCellsReferences(item);
		
		detail.setSerialNumber(Integer.parseInt(refs.get(0).getValue()));
		detail.setInspectorEmployeeNumber(Integer.parseInt(refs.get(2).getValue()));
		detail.setConfirms100Percent(Boolean.parseBoolean(refs.get(4).getValue()));
		detail.setIorNumbe(refs.get(6).getValue());
		detail.setMachinistSigned(Boolean.parseBoolean(refs.get(8).getValue()));
		
		
		item = operatorSection.getItem(1);
		refs = getCellsReferences(item);
		
		detail.setFirstStartedOnShift(Boolean.parseBoolean(refs.get(4).getValue()));
		detail.setFirstCompletedOnShift(Boolean.parseBoolean(refs.get(9).getValue()));
		
		NQDataModel.saveShiftLogDetail(detail);
	}
}
