package com.datagardens.nq.sdb.commons.model;

import java.util.Map;

public class Operation extends NQModelObject
{
	
	private String jobId;
	private int operationNumber;
	private float setup;
	private float cycle;
	private int operationSheets;
	private Map<Integer, Float> operationTimes;
	
	public Operation(String jobId, int operationNumber, float setup, float cycle)
	{
		super(operationNumber + "");
		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.setup = setup;
		this.cycle = cycle;
	}	

	@Override
	public String toString() {
		return "Operation [jobId=" + jobId + ", operationNumber="
				+ operationNumber + ", setup=" + setup + ", cycle=" + cycle
				+ ", operationSheets="
				+ operationSheets + "]";
	}

	public void setOperationSheets(int operationSheets) {
		this.operationSheets = operationSheets;
	}
	
	public void setOperationTimes(Map<Integer, Float> operationTimes) {
		this.operationTimes = operationTimes;
	}
	
	public Map<Integer, Float> getOperationTimes() {
		return operationTimes;
	}
	
	public Float getOperationTimeForEmployee(Integer employeeNumber)
	{
		return operationTimes.get(employeeNumber);
	}
	
	public String getJobId() 
	{
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

	public float getSetup() {
		return setup;
	}

	public void setSetup(float setup) {
		this.setup = setup;
	}

	public float getCycle() {
		return cycle;
	}

	public void setCycle(float cycle) {
		this.cycle = cycle;
	}
	
	//////////////////////////////////////////////
	// Methods
	//////////////////////////////////////////////
	public Integer getOperationSheets(Integer quantity) {
	
		Double total_time = (double)(cycle * quantity + setup);
		
		operationSheets = (int) java.lang.Math.ceil(total_time/12);
		
		return operationSheets;
	}
}
