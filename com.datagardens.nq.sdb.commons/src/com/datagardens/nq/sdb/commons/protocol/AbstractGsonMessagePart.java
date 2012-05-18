/**
 * Copyright 2012 (c) DataGardens Inc. All rights reserved
 * Version 1.0
 * March/13/2012
 * @author Francisco Berridi
 */
 package com.datagardens.nq.sdb.commons.protocol;

import com.google.gson.Gson;

public abstract class AbstractGsonMessagePart 
{
	protected static Gson gson = new Gson();
	
	@Override
	public String toString() 
	{
		return gson.toJson(this);
	}
}
