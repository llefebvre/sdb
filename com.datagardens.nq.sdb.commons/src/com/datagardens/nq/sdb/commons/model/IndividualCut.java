package com.datagardens.nq.sdb.commons.model;


public class IndividualCut extends Cut 
{
	private int yieldCut;
	private int individualCutNumber;
	private int cutLength;
	
	
	
	public IndividualCut(
			int cutId,
			int individualCutNumber, 
			int serialNumberFirstCut,
			int serialNumberLastCut,
			int cutLength,
			String empNumber,
			String date, 
			String heatNumber, 
			String niNumber, 
			int yieldCut, 
			boolean signed) 
	{
		super(cutId, serialNumberFirstCut, serialNumberLastCut, empNumber, date, heatNumber, niNumber, signed);
		
		this.individualCutNumber = individualCutNumber;
		this.yieldCut = yieldCut;
		this.cutLength = cutLength;
	
	}
	
	public int getCutLength() {
		return cutLength;
	}
	
	public int getYieldCut() {
		return yieldCut;
	}

	public int getIndividualCutNumber() {
		return individualCutNumber;
	}
	

	@Override
	public String toString() {
		return "IndividualCut [yieldCut=" + yieldCut + ", individualCutNumber="
				+ individualCutNumber + ", cutLength=" + cutLength + ", id="
				+ id + ", serialNumberFirstCut=" + serialNumberFirstCut
				+ ", serialNumberLastCut=" + serialNumberLastCut
				+ ", empNumber=" + empNumber + ", date=" + date
				+ ", heatNumber=" + heatNumber + ", nI=" + nI + ", signed="
				+ signed + "]";
	}

	public void merge(IndividualCut cut) 
	{
		individualCutNumber = cut.individualCutNumber;
		serialNumberFirstCut = cut.serialNumberFirstCut;
		serialNumberLastCut = cut.serialNumberLastCut;
		empNumber = cut.empNumber;
		date = cut.date;
		heatNumber = cut.heatNumber;
		nI = cut.nI;
		yieldCut = cut.yieldCut;
		signed = cut.signed;
	}	
}
