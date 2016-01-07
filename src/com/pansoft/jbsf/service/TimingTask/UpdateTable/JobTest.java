package com.pansoft.jbsf.service.TimingTask.UpdateTable;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pansoft.jbsf.sync.DBUpdateTable;

public class JobTest implements Job {


	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			new DBUpdateTable().executeProcessService();
			System.out.println("同步成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
