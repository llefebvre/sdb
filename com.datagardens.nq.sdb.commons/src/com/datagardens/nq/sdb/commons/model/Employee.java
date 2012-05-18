package com.datagardens.nq.sdb.commons.model;

public class Employee extends NQModelObject {

	private int employeeNumber;
	private String name;
	private String lastName;
	
	public Employee(int id, String name, String lastName) {
		super(id + "");
		this.employeeNumber = id;
		this.name = name;
		this.lastName = lastName;
	}
	
	
	
	@Override
	public String toString() {
		return "Employee [employeeNumber=" + employeeNumber + ", name=" + name
				+ ", lastName=" + lastName + "]";
	}



	public String getName() {
		return name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public int getEmployeeNumber() {
		return employeeNumber;
	}
}
