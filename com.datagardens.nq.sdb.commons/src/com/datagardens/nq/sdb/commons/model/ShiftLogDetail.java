package com.datagardens.nq.sdb.commons.model;

public class ShiftLogDetail 
{
	private String jobId;
	private int operationNumber;
	private int shiftLogNumber;
	private int shiftNumber;
	private int serialNumber;
	private int inspectorEmployeeNumber;
	private boolean confirms100Percent;
	private String iorNumbe;
	private boolean machinistSigned;
	private boolean firstStartedOnShift;
	private boolean firstCompletedOnShift;
	
	public ShiftLogDetail(String jobId, int operationNumber,
			int shiftLogNumber, int shiftNumber, int serialNumber,
			int inspectorEmployeeNumber, boolean confirms100Percent,
			String iorNumbe, boolean machinistSigned,
			boolean firstStartedOnShift, boolean firstCompletedOnShift) {
		super();
		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.shiftLogNumber = shiftLogNumber;
		this.shiftNumber = shiftNumber;
		this.serialNumber = serialNumber;
		this.inspectorEmployeeNumber = inspectorEmployeeNumber;
		this.confirms100Percent = confirms100Percent;
		this.iorNumbe = iorNumbe;
		this.machinistSigned = machinistSigned;
		this.firstStartedOnShift = firstStartedOnShift;
		this.firstCompletedOnShift = firstCompletedOnShift;
	}
	
	public ShiftLogDetail() {
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getInspectorEmployeeNumber() {
		return inspectorEmployeeNumber;
	}

	public void setInspectorEmployeeNumber(int inspectorEmployeeNumber) {
		this.inspectorEmployeeNumber = inspectorEmployeeNumber;
	}

	public boolean isConfirms100Percent() {
		return confirms100Percent;
	}

	public void setConfirms100Percent(boolean confirms100Percent) {
		this.confirms100Percent = confirms100Percent;
	}

	public String getIorNumber() {
		return iorNumbe;
	}

	public void setIorNumbe(String iorNumbe) {
		this.iorNumbe = iorNumbe;
	}

	public boolean isMachinistSigned() {
		return machinistSigned;
	}

	public void setMachinistSigned(boolean machinistSigned) {
		this.machinistSigned = machinistSigned;
	}

	public boolean isFirstStartedOnShift() {
		return firstStartedOnShift;
	}

	public void setFirstStartedOnShift(boolean firstStartedOnShift) {
		this.firstStartedOnShift = firstStartedOnShift;
	}

	public boolean isFirstCompletedOnShift() {
		return firstCompletedOnShift;
	}

	public void setFirstCompletedOnShift(boolean firstCompletedOnShift) {
		this.firstCompletedOnShift = firstCompletedOnShift;
	}
}
