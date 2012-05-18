package com.datagardens.nq.sdb.commons.model;

public class ShiftLogLine
{
	///////////////////////////////////////////////////
	// CLASS
	///////////////////////////////////////////////////
	public static ShiftLogLine getEmptyLine(ShiftLogSheet sheet, int shiftLogLineNumber)
	{
		return /*new ShiftLogLine(sheet.getJobId(), sheet.getOpreationNumber(), sheet.getSheetNumber(),
				shiftLogLineNumber, 
				0, 
				"",
				WorkpieceStatus.COMPLETE, 
				"", 
				false, 
				"",
				false,
				"");*/null;
	}
	
	/////////////////////////////////////////////////////
	// INSTANCE
	/////////////////////////////////////////////////////
	
	private String jobId;
	private int operationNumber;
	private int shiftLogNumber;
	private int shiftLogLineNumber;
	private int shiftNumber;
	private int serialNumber;
	private int startedEmployeeNumber;
	private WorkpieceStatus workpieceStatus;
	private String date;
	private boolean selfQCSigned;
	private int qcEmployeeNumber;
	private boolean qcSigned;
	private String IORNumber;
	
	public ShiftLogLine(String jobId,
			int operationNumber,
			int shiftLogNumber,
			int shiftNumber,
			int shiftLogLineNumber, 
			int serialNumber,
			int startedEmployeeNumber,
			WorkpieceStatus workpieceStatus,
			String date, 
			boolean selfQCSigned,
			int qcEmployeeNumber,
			boolean qcSigned,
			String IORNumber)
	{

		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.shiftLogNumber = shiftLogNumber;
		this.shiftNumber = shiftNumber;
		this.shiftLogLineNumber = shiftLogLineNumber;
		this.serialNumber = serialNumber;
		this.startedEmployeeNumber = startedEmployeeNumber;
		this.workpieceStatus = workpieceStatus;
		this.date = date;
		this.selfQCSigned = selfQCSigned;
		this.qcEmployeeNumber = qcEmployeeNumber;
		this.qcSigned = qcSigned;
		this.IORNumber = IORNumber;
	}
	
	

	@Override
	public String toString() {
		return "ShiftLogLine [jobId=" + jobId + ", operationNumber="
				+ operationNumber + ", shiftLogNumber=" + shiftLogNumber
				+ ", shiftLogLineNumber=" + shiftLogLineNumber
				+ ", shiftNumber=" + shiftNumber + ", serialNumber="
				+ serialNumber + ", startedEmployeeNumber="
				+ startedEmployeeNumber + ", workpieceStatus="
				+ workpieceStatus + ", date=" + date + ", selfQCSigned="
				+ selfQCSigned + ", qcEmployeeNumber=" + qcEmployeeNumber
				+ ", qcSigned=" + qcSigned + ", IORNumber=" + IORNumber + "]";
	}



	public String getJobId() {
		return jobId;
	}
	
	public int getOperationNumber() {
		return operationNumber;
	}
	public void setOperationNumber(int operationNumber) {
		this.operationNumber = operationNumber;
	}

	public int getShiftLogNumber() {
		return shiftLogNumber;
	}

	public void setShiftLogNumber(int shiftLogNumber) {
		this.shiftLogNumber = shiftLogNumber;
	}
	
	public int getShiftNumber() {
		return shiftNumber;
	}
	
	public void setShiftNumber(int shiftNumber) {
		this.shiftNumber = shiftNumber;
	}

	public int getShiftLogLineNumber() {
		return shiftLogLineNumber;
	}

	public void setShiftLogLineNumber(int shiftLogLineNumber) {
		this.shiftLogLineNumber = shiftLogLineNumber;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getStartedEmployeeNumber() {
		return startedEmployeeNumber;
	}

	public void setStartedEmployeeNumber(int startedEmployeeNumber) {
		this.startedEmployeeNumber = startedEmployeeNumber;
	}

	public WorkpieceStatus getWorkpieceStatus() {
		return workpieceStatus;
	}

	public void setWorkpieceStatus(WorkpieceStatus workpieceStatus) {
		this.workpieceStatus = workpieceStatus;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isSelfQCSigned() {
		return selfQCSigned;
	}

	public void setSelfQCSigned(boolean selfQCSigned) {
		this.selfQCSigned = selfQCSigned;
	}

	public int getQcEmployeeNumber() {
		return qcEmployeeNumber;
	}

	public void setQcEmployeeNumber(int qcEmployeeNumber) {
		this.qcEmployeeNumber = qcEmployeeNumber;
	}

	public boolean isQcSigned() {
		return qcSigned;
	}

	public void setQcSigned(boolean qcSigned) {
		this.qcSigned = qcSigned;
	}

	public String getIORNumber() {
		return IORNumber;
	}

	public void setIORNumber(String iORNumber) {
		IORNumber = iORNumber;
	}
	
	
	public void merge(ShiftLogLine newLine)
	{
		shiftLogLineNumber = newLine.getShiftLogLineNumber();
		serialNumber = newLine.getSerialNumber();
		startedEmployeeNumber = newLine.getStartedEmployeeNumber();
		workpieceStatus = newLine.getWorkpieceStatus();
		date = newLine.getDate();
		selfQCSigned = newLine.isSelfQCSigned();
		qcEmployeeNumber = newLine.getQcEmployeeNumber();
		qcSigned = newLine.isQcSigned();
		IORNumber = newLine.getIORNumber();
	}
}
