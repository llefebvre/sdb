package com.datagardens.nq.sdb.commons.model.nio.rbac;

import com.datagardens.nq.sdb.commons.model.NQModelObject;


public class SDBUser extends NQModelObject
{
	private String user;
	private String password;
	private SDBRole role;
	
	public SDBUser(String user, String password)
	{
		this(user, password, SDBRole.NOT_ASSIGNED);
	}
	
	public SDBUser(String user, String password, SDBRole role)
	{
		super(user);
		this.user = user;
		this.password = password;
		this.role = role;
	}
	
	public void setUsername(String user) {
		this.user = user;
	}
	
	public String getUsername() {
		return user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public SDBRole getRole() {
		return role;
	}
	
	public void setRole(SDBRole role) {
		this.role = role;
	}
	
	public boolean hasLevel(SDBRole role)
	{
		return this.role.compareTo(role) >= 0;
	}
	
	public static enum SDBRole 
	implements ModelEnum
	{
		
		NOT_ASSIGNED("Not Assigned"),
		DATA_ENTRY("Data Entry"), 
		SALES_COORDINATOR("Sales Coordinator"),
		ADMINISTRATOR("Administrator"),
		ROOT("root");
		
		public static SDBRole parse(String string)
		{
			SDBRole role = Enum.valueOf(SDBRole.class, string);
			if(role == null)
			{
				role = NOT_ASSIGNED;
			}
			
			return role;
		}
		
		private String id;
		
		private SDBRole(String id)
		{
			this.id = id;
		}

		@Override
		public String getId() 
		{
			return id;
		}
	}
	
	public static interface ModelEnum
	{
		String getId();
	}
}
