package com.datagardens.nq.sdb.client.views.sheets;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.datagardens.nq.sdb.client.views.GridCellReference;
import com.datagardens.nq.sdb.client.views.GridCellReference.CellDataType;
import com.datagardens.nq.sdb.client.views.sheets.SheetColumnInformation.EditorType;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.JobStatsOperation;
import com.datagardens.nq.sdb.commons.model.JobStatsShiftLogLine;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.OpTallySheet;
import com.datagardens.nq.sdb.commons.model.OperatorStats;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class JobTallySheetView extends SheetView {

	private Grid headerSection;
	private Grid stadisticsSection;
	private Grid inputSection;
	
	private Job tallyJob;
	private OpTallySheet tallyData;
	
	public JobTallySheetView(Composite parent, int style)
	{
		super(parent, style, false, "Serializtion Tally Sheet",
				"Operational Summary and Statistics");
	}

	@Override
	protected Point getSheetSize()
	{
		return new Point(750, 2500);
	}

	@Override
	protected SheetColumnInformation[] generateColumnsInformation() 
	{
		SheetColumnInformation [] info = new SheetColumnInformation [12];
		info[0]  = new SheetColumnInformation(60);
		info[1]  = new SheetColumnInformation(60);
		info[2]  = new SheetColumnInformation(60);
		info[3]  = new SheetColumnInformation(60);
		info[4]  = new SheetColumnInformation(60);
		info[5]  = new SheetColumnInformation(60);
		info[6]  = new SheetColumnInformation(60);
		info[7]  = new SheetColumnInformation(60, EditorType.DATE);
		info[8]  = new SheetColumnInformation(60);
		info[9]  = new SheetColumnInformation(60);
		info[10] = new SheetColumnInformation(60);
		info[11] = new SheetColumnInformation(60);
		
		return info;
	}

	@Override
	protected void createSheet() 
	{
		headerSection = createSheetSection();
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemEmptyColumn(0, 2));
		holder.put(1, new ItemColumnProperties("Job #", 1, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(2, new ItemColumnProperties("Job Qty", 1));
		holder.put(3, new ItemColumnProperties("Pieces Cut", 1));
		holder.put(4, new ItemColumnProperties("Total Parts Complete", 1));
		holder.put(5, new ItemColumnProperties("Operation #", 1));
		holder.put(6, new ItemEmptyColumn(0, 2));
		createGridItem(headerSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("header_job_no", "", 1, 0, SWT.COLOR_BLUE, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_YELLOW, CellDataType.TEXT));
		holder.put(2, new ItemColumnProperties("header_job_qty", "", 1));
		holder.put(3, new ItemColumnProperties("header_pieces_cut", "", 1, 0));
		holder.put(4, new ItemColumnProperties("header_total_parts_complete", "", 1));
		holder.put(5, new ItemColumnProperties("header_operation_number", "", 1, 0, SWT.COLOR_RED, SWT.COLOR_MAGENTA));
		createGridItem(headerSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Part #:", 0, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(2, new ItemColumnProperties("header_part_no", "", 1, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(3, new ItemColumnProperties("Dwg #:", 0, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(4, new ItemColumnProperties("header_dwg_no", "", 1, 0, SWT.COLOR_BLUE, SWT.DEFAULT));
		holder.put(5, new ItemColumnProperties("Part Name:", 1, 0, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(6, new ItemColumnProperties("header_part_name", "", 1, 0));
		createGridItem(headerSection, holder);
		
		
		stadisticsSection = createSheetSection();
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemEmptyColumn(0, 16));
		holder.put(1, new ItemColumnProperties("Job Statistics", 9, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GREEN));
		holder.put(2, new ItemEmptyColumn(0,16));
		createGridItem(stadisticsSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Routed Operation Time", 2, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.COLOR_DARK_BLUE));
		holder.put(2, new ItemColumnProperties("Mean Cycle Time", 0, 1));
		holder.put(3, new ItemColumnProperties("Total Parts", 2, 0));
		holder.put(4, new ItemColumnProperties("Rates", 2, 0));
		createGridItem(stadisticsSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Set-up", 0, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.COLOR_BLUE));
		holder.put(2, new ItemColumnProperties("Cycle", 0, 0));
		holder.put(3, new ItemColumnProperties("Net Cycle", 0, 0));
		holder.put(4, new ItemColumnPusher(1));
		holder.put(5, new ItemColumnProperties("Comp.", 0, 0));
		holder.put(6, new ItemColumnProperties("Scrapped", 0, 0));
		holder.put(7, new ItemColumnProperties("Reworked", 0, 0));
		holder.put(8, new ItemColumnProperties("Good", 0, 0));
		holder.put(9, new ItemColumnProperties("Scrap", 0, 0));
		holder.put(10, new ItemColumnProperties("Rework", 0, 0));
		createGridItem(stadisticsSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("statistics_set_up", "", 0, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, CellDataType.TEXT));
		holder.put(2, new ItemColumnProperties("statistics_cycle", "", 0, 0));
		holder.put(3, new ItemColumnProperties("statistics_net_cycle", "", 0, 0, SWT.DEFAULT, SWT.COLOR_GREEN));
		holder.put(4, new ItemColumnProperties("statistics_mean_cycle_time", "", 0, 0));
		holder.put(5, new ItemColumnProperties("statistics_comp", "", 0, 0));
		holder.put(6, new ItemColumnProperties("statistics_scrapped", "", 0, 0));
		holder.put(7, new ItemColumnProperties("statistics_reworked", "", 0, 0));
		holder.put(8, new ItemColumnProperties("statistics_good", "", 0, 0));
		holder.put(9, new ItemColumnProperties("statistics_scrap", "", 0, 0));
		holder.put(10, new ItemColumnProperties("statistics_rework", "", 0, 0));
		createGridItem(stadisticsSection, holder);
		
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Operator Statistics", 9, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_DARK_GREEN));
		createGridItem(stadisticsSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Operator", 2, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.COLOR_DARK_BLUE));
		holder.put(2, new ItemColumnProperties("Parts", 2, 0));
		holder.put(3, new ItemColumnProperties("Operator Time", 1, 0));
		holder.put(4, new ItemColumnProperties("Relative Score", 1, 0));
		createGridItem(stadisticsSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(1));
		holder.put(1, new ItemColumnProperties("Emp #", 0, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.COLOR_BLUE));
		holder.put(2, new ItemColumnProperties("Name", 1, 0));
		holder.put(3, new ItemColumnProperties("Complete", 0, 0));
		holder.put(4, new ItemColumnProperties("Scrapped", 0, 0));
		holder.put(5, new ItemColumnProperties("Reworked", 0, 0));
		holder.put(6, new ItemColumnProperties("Total", 0, 0));
		holder.put(7, new ItemColumnProperties("Mean Cycle", 0, 0));
		holder.put(8, new ItemColumnProperties("Routed", 0, 0));
		holder.put(9, new ItemColumnProperties("To Peer", 0, 0));
		createGridItem(stadisticsSection, holder);
		
		for(int i=0; i<10; i++)
		{
			holder = new HashMap<Integer, ItemColumnProperties>();
			holder.put(0, new ItemColumnPusher(1));
			holder.put(1, new ItemColumnProperties("statistics_emp_no_" + i, "", 0, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, CellDataType.TEXT));
			holder.put(2, new ItemColumnProperties("statistics_name_" + i, "", 1, 0, SWT.DEFAULT, SWT.COLOR_GREEN));
			holder.put(3, new ItemColumnProperties("statistics_complete_" + i, "", 0, 0));
			holder.put(4, new ItemColumnProperties("statistics_scrapped_" + i, "", 0, 0));
			holder.put(5, new ItemColumnProperties("statistics_reworked_" + i, "", 0, 0));
			holder.put(6, new ItemColumnProperties("statistics_total_" + i, "", 0, 0, SWT.DEFAULT, SWT.DEFAULT));
			holder.put(7, new ItemColumnProperties("statistics_mean_cycle_" + i, "", 0, 0, SWT.DEFAULT, SWT.COLOR_GREEN));
			holder.put(8, new ItemColumnProperties("statistics_routed_" + i, "", 0, 0, SWT.DEFAULT, SWT.COLOR_RED));
			holder.put(9, new ItemColumnProperties("statistics_to_peer_" + i, "", 0, 0));
			createGridItem(stadisticsSection, holder);
		}
		
		
		inputSection = createSheetSection();
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnProperties("S/N", 0, 1, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_GRAY));
		holder.put(1, new ItemColumnProperties("Adjusted S/N", 0, 1));
		holder.put(2, new ItemColumnProperties("Started (Emp #)", 0, 1));
		holder.put(3, new ItemColumnProperties("WC", 0, 1));
		holder.put(4, new ItemColumnProperties("Workpiece Status", 2, 0));
		holder.put(5, new ItemColumnProperties("Date (mo/day)", 0, 1));
		holder.put(6, new ItemColumnProperties("Heat No.", 0, 1));
		holder.put(7, new ItemColumnProperties("In Process Inspection", 1, 0, SWT.DEFAULT, SWT.COLOR_BLUE));
		holder.put(8, new ItemColumnProperties("IOR #", 0, 1));
		createGridItem(inputSection, holder);
		
		holder = new HashMap<Integer, ItemColumnProperties>();
		holder.put(0, new ItemColumnPusher(4));
		holder.put(1, new ItemColumnProperties("Comp.", 0, 0, SWT.DEFAULT, SWT.DEFAULT, 10, SWT.COLOR_GRAY));
		holder.put(2, new ItemColumnProperties("Scrap", 0, 0));
		holder.put(3, new ItemColumnProperties("Reworked", 0, 0));
		holder.put(4, new ItemColumnPusher(2));
		holder.put(5, new ItemColumnProperties("QC Emp #", 0, 0, SWT.DEFAULT, SWT.BOLD, SWT.DEFAULT, SWT.COLOR_BLUE));
		holder.put(6, new ItemColumnProperties("Initials", 0, 0));
		createGridItem(inputSection, holder);
		
		for(int i=0; i<100; i++)
		{
			holder = new HashMap<Integer, ItemColumnProperties>();
			for(int j=0; j<11; j++)
			{
				holder.put(j, new ItemColumnProperties(j+"_"+i, "", 0, 0, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, SWT.DEFAULT, CellDataType.INPUT_TEXT));
			}
			createGridItem(inputSection, holder);
		}
	}

	@Override
	protected void saveLine(int lineNumber, Map<Integer, GridCellReference> refs)
	throws ModelException 
	{
		
	}

	public void updateTallyInformation(final Job job, final int operationNumber) 
	{
		try
		{
			
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
			dialog.run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) 
						throws InvocationTargetException, InterruptedException 
				{
					try
					{
						monitor.beginTask("Building Tally Sheet...", 100);
						monitor.worked(20);
						
						monitor.subTask("Getting job information.");
						tallyJob = NQDataModel.findJobById(job.getId());
						monitor.worked(30); //50
						
						monitor.subTask("Analysing operation data.");
						tallyData = NQDataModel.getOperationTallyForJob(tallyJob.getShiftLogSheet(operationNumber));
						monitor.worked(40);
						monitor.done();
					}
					catch(Exception e)
					{
						throw new InvocationTargetException(e);
					}
				}
			});
			
			
			System.out.println("######################################### TALLY ## " + tallyData);
			
			ShiftLogSheet sheet = tallyJob.getShiftLogSheet(operationNumber);
			
			GridItem item = headerSection.getItem(1);
			Map<Integer, GridCellReference> refs = getCellsReferences(item);

			refs.get(1).setValue(tallyJob.getId());
			refs.get(3).setValue(tallyJob.getQuantity());
			refs.get(5).setValue(sheet.getPiecesCut());
			refs.get(7).setValue("00000");
			refs.get(9).setValue(sheet.getOperationNumber());
		
			item = stadisticsSection.getItem(3);
			refs = getCellsReferences(item);
			
			JobStatsOperation op = tallyData.getJobStatsOperation();
			refs.get(1).setValue(op.getOpSetUp());
			refs.get(2).setValue(op.getOpCycle());
			refs.get(3).setValue(op.getOpNetCycleTime());
			refs.get(4).setValue(op.getOpMeanCycleTime());
			
			JobStatsShiftLogLine opLine = tallyData.getjobStatsShiftLogLine();
			if(opLine != null)
			{
				refs.get(5).setValue(opLine.getPartsComplete());
				refs.get(6).setValue(opLine.getPartsScrapped());
				refs.get(7).setValue(opLine.getPartsReworked());
				refs.get(8).setValue(opLine.getPercentGood());
				refs.get(9).setValue(opLine.getPercentScrap());
				refs.get(10).setValue(opLine.getPercentRework());
			}
			
			if( tallyData.getOperatorStats() != null)
			{
				int index = 7;
				for(OperatorStats ops : tallyData.getOperatorStats().values())
				{
					item = stadisticsSection.getItem(index);
					refs = getCellsReferences(item);
					
					refs.get(1).setValue(ops.getEmployeeNumber());
					refs.get(2).setValue(ops.getEmployeeName() + " " + ops.getEmployeeLastName());
					refs.get(4).setValue(ops.partsCompleted());
					refs.get(5).setValue(ops.partsScrapped());
					refs.get(6).setValue(ops.partsReworked());
					refs.get(7).setValue(ops.getOperatorTime());
					refs.get(8).setValue(ops.getMeanCycle());
					refs.get(9).setValue(ops.getRouted());
					refs.get(10).setValue(ops.getToPeer());
					index++;
				}
			}
			
		}
		catch(InvocationTargetException ie)
		{
			ie.printStackTrace();
			MessageDialog.openError(getShell(), "Problem Ocurred", 
					"Cannot create tally sheet: " + ie.getCause().getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			MessageDialog.openError(getShell(), "Problem Ocurred", 
					"Cannot create tally sheet: " + e.getMessage());
		}
		
		
	}

}
