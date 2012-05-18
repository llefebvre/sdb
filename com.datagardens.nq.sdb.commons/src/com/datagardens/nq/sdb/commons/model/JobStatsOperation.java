package com.datagardens.nq.sdb.commons.model;

public class JobStatsOperation 
{
	private String jobId;
	private int operationNumber;
	

	/* jobStatsOperation */
	private float opSetUp;
	private float opCycle;
	private float opNetCycleTime;
	private float opMeanCycleTime;

			
	public JobStatsOperation(String jobId, int operationNumber,
			float opSetup, float opCycle, float opNetCycleTime,float opMeanCycleTime) {
		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.opSetUp = opSetup;
		this.opCycle = opCycle;
		this.opNetCycleTime = opNetCycleTime;
		this.opMeanCycleTime = opMeanCycleTime;
	}
	
	
	
	@Override
	public String toString() {
		return "JobStatsOperation [jobId=" + jobId + ", operationNumber="
				+ operationNumber + ", opSetUp=" + opSetUp + ", opCycle="
				+ opCycle + ", opNetCycleTime=" + opNetCycleTime
				+ ", opMeanCycleTime=" + opMeanCycleTime + "]";
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

	public void setOpSetUp(float opSetUp) {
		this.opSetUp = opSetUp;
	}
	
	public void setOpCycle(float opCycle) {
		this.opCycle = opCycle;
	}
	
	public void setOpNetCycleTime(float opNetCycleTime) {
		this.opNetCycleTime = opNetCycleTime;
	}
	
	public void setOpMeanCycleTime(float opMeanCycleTime) {
		this.opMeanCycleTime = opMeanCycleTime;
	}

	public float getOpSetUp() {
		return this.opSetUp;
	}
		
	public float getOpCycle() {
		return this.opCycle;
	}
	
	public float getOpNetCycleTime() {
		return this.opNetCycleTime;
	}
	
	public float getOpMeanCycleTime() {
		return this.opMeanCycleTime;
	}


}
