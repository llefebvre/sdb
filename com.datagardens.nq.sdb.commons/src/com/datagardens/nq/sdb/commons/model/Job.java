/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/14/2012
 *
 * @author Francisco Berridi
 */
package com.datagardens.nq.sdb.commons.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Job extends NQModelObject
{	
	private Date releaseDate;
	private int quantity;
	private String partNumber;
	private String drawingNumber;
	private String partDesc;
	
	private SawSheet sawSheet;
	private Map<Integer, ShiftLogSheet> shiftLogSheets;

	public Job(String id, Date releaseDate, int quantity, String partNumber,
			   String drawingNumber, String partDesc)
	{
		super(id);
		this.id = id;
		this.releaseDate = releaseDate;
		this.quantity = quantity;
		this.partNumber = partNumber;
		this.drawingNumber = drawingNumber;
		this.partDesc = partDesc;
		
		shiftLogSheets = new HashMap<Integer, ShiftLogSheet>();
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public String getDrawingNumber() {
		return drawingNumber;
	}
	public String getPartDesc() {
		return partDesc;
	}
	
	public int getNumberOfOperations() 
	{
		return 1 + shiftLogSheets.size();
	}
	
	public void setSawSheet(SawSheet sawSheet) {
		this.sawSheet = sawSheet;
	}
	
	public SawSheet getSawSheet() {
		return sawSheet;
	}
	
	public Map<Integer, ShiftLogSheet> getShiftLogSheets() {
		return shiftLogSheets;
	}
	
	public void putShiftLogSheet(ShiftLogSheet sheet)
	{
		shiftLogSheets.put(sheet.getOperationNumber(), sheet);
	}
	
	public ShiftLogSheet getShiftLogSheet(int operationNumber)
	{
		return shiftLogSheets.get(operationNumber);
	}
}
