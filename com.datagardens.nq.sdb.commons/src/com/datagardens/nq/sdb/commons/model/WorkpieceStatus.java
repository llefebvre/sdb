package com.datagardens.nq.sdb.commons.model;

import java.util.HashMap;
import java.util.Map;

public enum WorkpieceStatus 
{
	INCOMPLETE(2), COMPLETE(1), SCRAPPED(3), REWORKED(4);
	
	private static Map<Integer, WorkpieceStatus> map;
	
	static
	{
		map = new HashMap<Integer, WorkpieceStatus>();
		for(WorkpieceStatus ws : values())
		{
			map.put(ws.code, ws);
		}
	}
	
	public static WorkpieceStatus get(int code)
	{
		return map.get(code);
	}
	
	private int code;
	private WorkpieceStatus(int code) 
	{
		this.code = code;
	}
	
	public int getCode() {
		return code;
	} 
	
	@Override
	public String toString() 
	{
		return code + "";
	}
}