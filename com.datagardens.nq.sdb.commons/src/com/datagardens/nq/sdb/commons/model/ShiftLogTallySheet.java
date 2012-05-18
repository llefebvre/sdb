package com.datagardens.nq.sdb.commons.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftLogTallySheet 
{
	
	
	private Job job;
	private ShiftLogSheet shiftLogSheet;
	private Operation operation;
	private int piecesCut;
	private int totalPartsComplete;
	private Map<Integer, OperatorStatistics> operatorsStadistics;
	private List<ShiftLogLine> allLines;
	
	private int totalPartsCompletetedInAllShifts;
	private int totalPartsScrappedInAllShifts;
	private int totalPartsReworkedtedInAllShifts;


	public ShiftLogTallySheet(Job job, 
			ShiftLogSheet shiftLogSheet, 
			int totalPartsComplete) throws ModelException
	{
		super();
		this.job = job;
		this.shiftLogSheet = shiftLogSheet;
		this.operation = shiftLogSheet.getOperation();
		this.totalPartsComplete = totalPartsComplete;
		
		totalPartsCompletetedInAllShifts = -1;
		totalPartsScrappedInAllShifts = -1;
		totalPartsReworkedtedInAllShifts = -1;
		
		fillOperatorsStatistics();
		
	}
	
	private void fillOperatorsStatistics()
	throws ModelException 
	{
		allLines = new ArrayList<ShiftLogLine>();
		
		for(int i=0; i<shiftLogSheet.getNumberOfShifts(); i++)
		{
			for(ShiftLogLine line : NQDataModel.getCutsForShiftLogShift(shiftLogSheet, i).values())
			{
				piecesCut++;
				allLines.add(line);
			}
		}
		
		operatorsStadistics = new HashMap<Integer, OperatorStatistics>();
		
		for(ShiftLogLine line : allLines)
		{
			OperatorStatistics os = operatorsStadistics.get(line.getOperationNumber());
			if(os == null)
			{
				Employee employee = NQDataModel.findEmployeeById(line.getOperationNumber());
				
				os = new OperatorStatistics(this, employee, 
						operation.getOperationTimeForEmployee(employee.getEmployeeNumber()));
			}
			
			os.addCut(line);
		}
	}

	public int getPiecesCut() {
		return piecesCut;
	}
	
	public int getTotalPartsComplete() {
		return totalPartsComplete;
	}
	
	public String getJobNumber()
	{
		return job.getId();
	}
	
	public int getJobQuantity()
	{
		return job.getQuantity();
	}
	
	public int getOperationNumber()
	{
		return operation.getOperationNumber();
	}
	
	public String getPartNumber()
	{
		return job.getPartNumber();
	}
	
	public String getDrawingNumber()
	{
		return job.getDrawingNumber();
	}
	
	public String getPartName()
	{
		return job.getPartDesc();
	}
	
	public float getSetup()
	{
		return operation.getSetup();
	}
	
	public float getCycle()
	{
		return operation.getCycle();
	}
	
	public float getNetCycle()
	{
		return (getSetup()/getJobQuantity()) + getCycle(); 
	}
	
	public float getMeanCycleTime()
	{
		float time = 0f;
		for(OperatorStatistics os : operatorsStadistics.values())
		{
			time += os.getTotalOperatorTime();
		}
		
		return time/getTotalCutsCompleteInAllShifts();
	}
	
	public float getRatesGood()
	{
		return getTotalCutsCompleteInAllShifts() / getPiecesCut();
	}
	
	public float getRatesScrap()
	{
		return getTotalCutsScrappedInAllShifts() / getPiecesCut();
	}
	
	public float getRatesRework()
	{
		return getTotalCutsReworkedAllShifts() / getPiecesCut();
	}
	
	public int getTotalCutsCompleteInAllShifts()
	{
		if(totalPartsCompletetedInAllShifts != -1)
		{
			return totalPartsCompletetedInAllShifts; 
		}
		else
		{
			//TODO: query database
			return 1;
		}
	}
	
	public int getTotalCutsScrappedInAllShifts()
	{
		if(totalPartsScrappedInAllShifts != -1)
		{
			return totalPartsScrappedInAllShifts; 
		}
		else
		{
			//TODO: query database
			return 1;
		}
	}
	
	public int getTotalCutsReworkedAllShifts()
	{
		if(totalPartsReworkedtedInAllShifts != -1)
		{
			return totalPartsReworkedtedInAllShifts; 
		}
		else
		{
			//TODO: query database
			return 1;
		}
	}
}
