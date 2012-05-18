package com.datagardens.nq.sdb.commons.model;

import java.util.Map;

public class OpTallySheet 
{
	private String jobId;
	private int operationNumber;
	
	/* operator statistics */
	/*private OperatorStats operatorStats;*/
	private Map<Integer, OperatorStats> operatorStats;
	
	/* jobStatsOperation */
	private JobStatsOperation jobStatsOperation;

	/* jobStatsShiftLogLine */
	private JobStatsShiftLogLine jobStatsShiftLogLine;
		
	public OpTallySheet(String jobId, int operationNumber,
			Map<Integer, OperatorStats> operatorStats,
			JobStatsOperation jobStatsOperation,
			JobStatsShiftLogLine jobStatsShiftLogLine) {
		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.operatorStats = operatorStats;
		this.jobStatsOperation = jobStatsOperation;
		this.jobStatsShiftLogLine = jobStatsShiftLogLine;
	}
	
	
	

	@Override
	public String toString() {
		return "OpTallySheet [jobId=" + jobId + ", operationNumber="
				+ operationNumber + ", operatorStats=" + operatorStats
				+ ", jobStatsOperation=" + jobStatsOperation
				+ ", jobStatsShiftLogLine=" + jobStatsShiftLogLine + "]";
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

	public void setOperatorStats (Map<Integer, OperatorStats> operatorStats) {
		this.operatorStats = operatorStats;
	}
	
	public Map<Integer, OperatorStats> getOperatorStats() {
		return this.operatorStats;
	}
	public void setJobStatsOperation (JobStatsOperation jobStatsOperation) {
		this.jobStatsOperation = jobStatsOperation;
	}
	
	public JobStatsOperation getJobStatsOperation() {
		return this.jobStatsOperation;
	}
	
	public void setjobStatsShiftLogLine (JobStatsShiftLogLine jobStatsShiftLogLine) {
		this.jobStatsShiftLogLine = jobStatsShiftLogLine;
	}
	
	public JobStatsShiftLogLine getjobStatsShiftLogLine() {
		return this.jobStatsShiftLogLine;
	}
}
