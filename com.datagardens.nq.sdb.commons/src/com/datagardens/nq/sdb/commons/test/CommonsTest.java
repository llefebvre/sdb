package com.datagardens.nq.sdb.commons.test;

import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;



public class CommonsTest {

	public static void main(String[] args) 
	{
		try 
		{
//			NQDataModel.initialize();
			
			for(Job job : NQDataModel.getAllJobs())
			{
				System.out.println(job.getId());
			}
		} 
		catch (ModelException e)
		{
			e.printStackTrace();
		}
	}
}
