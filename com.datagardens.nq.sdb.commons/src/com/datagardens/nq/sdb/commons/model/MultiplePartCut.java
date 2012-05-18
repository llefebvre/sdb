package com.datagardens.nq.sdb.commons.model;

public class MultiplePartCut extends Cut 
{
	public MultiplePartCut(int id, Integer serialNumberFirstCut,
			Integer serialNumberLastCut, String empNumber, String date,
			String heatNumber, String nI, boolean signed) {
		super(id, serialNumberFirstCut, serialNumberLastCut, empNumber, date,
				heatNumber, nI, signed);
	}

	private Integer yieldCut;
}
