package com.datagardens.nq.sdb.commons.model;

import java.util.ArrayList;
import java.util.List;

public class OperatorStatistics 
{
	private Employee employee;
	private List<ShiftLogLine> cuts;
	private float totalOperatorTime;
	private ShiftLogTallySheet tallySheet;
	
	public OperatorStatistics(ShiftLogTallySheet tallySheet,
			Employee employee, float totalOperatorTime)
	{
		this.tallySheet = tallySheet;
		this.employee = employee;
		this.totalOperatorTime = totalOperatorTime;
		cuts = new ArrayList<ShiftLogLine>();
	}
	
	
	public void addCut(ShiftLogLine line)
	{
		cuts.add(line);
	}
	
	public void setTotalOperatorTime(float totalOperatorTime) {
		this.totalOperatorTime = totalOperatorTime;
	}
	
	
	public int getEmployeeNumber()
	{
		return employee.getEmployeeNumber();
	}
	
	public String getName()
	{
		return employee.getId();
	}
	
	public int partsCompleted()
	{
		return countCutsPerStatus(WorkpieceStatus.COMPLETE);
	}
	
	public int partsScrapped()
	{
		return countCutsPerStatus(WorkpieceStatus.SCRAPPED);
	}
	
	public int partsReworked()
	{
		return countCutsPerStatus(WorkpieceStatus.REWORKED);
	}
	
	public float getTotalOperatorTime() {
		return totalOperatorTime;
	}
	
	public float getMeanCycle()
	{
		return getTotalOperatorTime() / partsCompleted();
	}
	
	public float getScoreRouted()
	{
		return (tallySheet.getNetCycle() - getMeanCycle()) / tallySheet.getNetCycle();
	}
	
	public float getScoreToPeer()
	{
		return (tallySheet.getMeanCycleTime() - getMeanCycle()) / tallySheet.getMeanCycleTime();
	}
	
	private int countCutsPerStatus(WorkpieceStatus status)
	{
		int i = 0;
		for(ShiftLogLine line : cuts)
		{
			if(line.getWorkpieceStatus() == status)
			{
				i++;
			}
		}
		return i;
	}
}
