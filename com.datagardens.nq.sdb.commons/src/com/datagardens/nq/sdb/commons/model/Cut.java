package com.datagardens.nq.sdb.commons.model;


public abstract class Cut {

	protected int id;
	protected Integer serialNumberFirstCut;
	protected Integer serialNumberLastCut;
	protected String empNumber;
	protected String date;
	protected String heatNumber;
	protected String nI;
	protected boolean signed;
	
	public Cut(
			int id,
			Integer serialNumberFirstCut,
			Integer serialNumberLastCut, 
			String empNumber,
			String date,
			String heatNumber,
			String nI, 
			boolean signed) 
	{
		super();
		this.id = id;
		this.serialNumberFirstCut = serialNumberFirstCut;
		this.serialNumberLastCut = serialNumberLastCut;
		this.empNumber = empNumber;
		this.date = date;
		this.heatNumber = heatNumber;
		this.nI = nI;
		this.signed = signed;
	}

	public int getId() {
		return id;
	}

	public Integer getSerialNumberFirstCut() {
		return serialNumberFirstCut;
	}

	public Integer getSerialNumberLastCut() {
		return serialNumberLastCut;
	}

	public String getEmpNumber() {
		return empNumber;
	}

	public String getDate() {
		return date;
	}

	public String getHeatNumber() {
		return heatNumber;
	}

	public String getnI() {
		return nI;
	}

	public boolean isSigned() {
		return signed;
	}
}
