package com.datagardens.nq.sdb.commons.model;


public class ShiftLogSheet 
extends NQModelObject {
	
	public static ShiftLogSheet getEmptySheet(String jobId, Operation operation, int sheetNumber)
	{
		return new ShiftLogSheet(jobId, operation, sheetNumber, 1, 0, false, 0);
	}
	
	private String jobId;
	private Operation operation;
	private int sheetNumber;
	private int numberOfShifts;
	private int piecesCut;
	private boolean firstOffInspectionRequired;
	private int inProcessInspectionFrequency;
	
	
	public ShiftLogSheet(
			String jobId, 
			Operation operation,
			int sheetNumber,
			int numberOfShifts,
			int piecesCut,
			boolean firstOffInspectionRequired,
			int inProcessInspectionFrequency) 
	{	
		super("");
		this.jobId = jobId;
		this.operation = operation;
		this.sheetNumber = sheetNumber;
		this.numberOfShifts = numberOfShifts;
		this.piecesCut = piecesCut;
		this.firstOffInspectionRequired = firstOffInspectionRequired;
		this.inProcessInspectionFrequency = inProcessInspectionFrequency;
	}

	public String getJobId() {
		return jobId;
	}

	public int getOperationNumber() {
		return operation.getOperationNumber();
	}

	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}
	
	public int getSheetNumber() {
		return sheetNumber;
	}
	
	public void setNumberOfShifts(int numberOfShifts) {
		this.numberOfShifts = numberOfShifts;
	}

	public int getNumberOfShifts() {
		return numberOfShifts;
	}
	
	public int getPiecesCut() {
		return piecesCut;
	}
	
	public void setPiecesCut(int piecesCut) {
		this.piecesCut = piecesCut;
	}


	public boolean isFirstOffInspectionRequired() {
		return firstOffInspectionRequired;
	}


	public void setFirstOffInspectionRequired(boolean firstOffInspectionRequired) {
		this.firstOffInspectionRequired = firstOffInspectionRequired;
	}


	public int getInProcessInspectionFrequency() {
		return inProcessInspectionFrequency;
	}

	public void setInProcessInspectionFrequency(int inProcessInspectionFrequency) {
		this.inProcessInspectionFrequency = inProcessInspectionFrequency;
	}
	
	public Operation getOperation() {
		return operation;
	}
	
	/*private void addLine(ShiftLogLine line)
	{
		lines.put(line.getShiftLogLineNumber(), line);
	}
	
	public Map<Integer, ShiftLogLine> getLines() {
		return lines;
	}
	
	public ShiftLogTallySheet getShiftLogTally(Job job)
	{
		return new ShiftLogTallySheet(job, null, 0,0);
	}
	
	public void modifyLine(ShiftLogLine line)
	{
		ShiftLogLine curretLine = lines.get(line.getShiftLogLineNumber());
		if(curretLine == null)
		{
			addLine(line);
		}
		else
		{
			curretLine.merge(line);
		}
	}*/
}
