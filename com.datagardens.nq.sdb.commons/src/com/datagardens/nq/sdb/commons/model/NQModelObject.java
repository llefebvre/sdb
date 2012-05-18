package com.datagardens.nq.sdb.commons.model;



public abstract class NQModelObject 
{
	
	protected String id;
	
	public NQModelObject(String id)
	{
		this.id = id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}
