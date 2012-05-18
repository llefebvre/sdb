package com.datagardens.nq.sdb.commons.model;

public class JobStatsShiftLogLine 
{
	private String jobId;
	private int operationNumber;
	
	/* jobStatsShiftLogLine */
	private int partsCut;
	private int partsComplete;
	private int partsScrapped;
	private int partsReworked;
	private float percentGood;
	private float percentScrap;
	private float percentRework;
		
	public JobStatsShiftLogLine(String jobId, int operationNumber, int partsCut, int partsComplete,int partsScrapped, int partsReworked,
			float percentGood, float percentScrap, float percentRework ) {
		this.jobId = jobId;
		this.operationNumber = operationNumber;
		this.partsCut = partsCut;
		this.partsComplete = partsComplete;
		this.partsScrapped = partsScrapped;
		this.partsReworked = partsReworked;
		this.percentGood = percentGood;
		this.percentScrap = percentScrap;
		this.percentRework = percentRework;
		
	}
	
	
	
	@Override
	public String toString() {
		return "JobStatsShiftLogLine [jobId=" + jobId + ", operationNumber="
				+ operationNumber + ", partsCut=" + partsCut
				+ ", partsComplete=" + partsComplete + ", partsScrapped="
				+ partsScrapped + ", partsReworked=" + partsReworked
				+ ", percentGood=" + percentGood + ", percentScrap="
				+ percentScrap + ", percentRework=" + percentRework + "]";
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

	public int getPartsCut () {
		return this.partsCut;
	}
	public int getPartsComplete() {
		return this.partsComplete;
	}
	public int getPartsScrapped() {
		return this.partsScrapped;
	}
	public int getPartsReworked() {
		return this.partsReworked;
	}
	
	public float getPercentGood() {
		return this.percentGood;
	}
	public float getPercentScrap() {
		return this.percentScrap;
}
	public float getPercentRework() {
		return this.percentRework;
		
	}

}
