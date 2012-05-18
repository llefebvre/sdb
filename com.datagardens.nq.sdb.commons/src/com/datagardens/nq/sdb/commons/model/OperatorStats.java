package com.datagardens.nq.sdb.commons.model;

public class OperatorStats 
{
	private Employee employee;
	
	private int partsComplete;
	private int partsScrapped;
	private int partsReworked;
	private float operatorTime;
	private float meanCycle;
	private float scoreRouted;
	private float scoreToPeer;

	public OperatorStats(Employee employee,int complete, int scrapped,int reworked,
			float operatorTime,float meanCycle,float routed, float toPeer) 
	{
		
		this.employee = employee;
		this.partsComplete = complete;
		this.partsScrapped = scrapped;
		this.partsReworked = reworked;
		this.operatorTime = operatorTime;
		this.meanCycle = meanCycle;
		this.scoreRouted = routed;
		this.scoreToPeer = toPeer;
	}
	
	
	
	@Override
	public String toString() {
		return "OperatorStats [employee=" + employee + ", partsComplete="
				+ partsComplete + ", partsScrapped=" + partsScrapped
				+ ", partsReworked=" + partsReworked + ", operatorTime="
				+ operatorTime + ", meanCycle=" + meanCycle + ", scoreRouted="
				+ scoreRouted + ", scoreToPeer=" + scoreToPeer + "]";
	}



	public int getEmployeeNumber()
	{
		return employee.getEmployeeNumber();
	}
	
	public String getEmployeeName()
	{
		return employee.getName();
	}
	
	public String getEmployeeLastName()
	{
		return employee.getLastName();
	}
	
	public Employee getEmployee()
	{
		return employee;
	}
		
	public int partsCompleted()
	{
		return partsComplete;
	}
	
	public int partsScrapped()
	{
		return partsScrapped;
	}
	
	public int partsReworked()
	{
		return partsReworked;
	}
	
	public float getOperatorTime() {
		return operatorTime;
	}
	
	public float getMeanCycle()
	{
		return meanCycle;
	}
	
	public float getRouted()
	{
		return scoreRouted;
	}
	
	public float getToPeer()
	{
		return scoreToPeer;
	}
		
}
