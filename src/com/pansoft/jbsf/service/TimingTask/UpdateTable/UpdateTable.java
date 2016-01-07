package com.pansoft.jbsf.service.TimingTask.UpdateTable;


import java.text.ParseException;

import org.quartz.SchedulerException;




public class UpdateTable{
	public static void start() throws SchedulerException, ParseException  {  
		System.out.println("TimingTask:UpdateTable task has start.");
		/* 
		 * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
		 */
		String str="0 0 0 0 2 ?";     //秒 分 时 日 月 周       年（可不写） quartz 时间配置规则
		JobTest job = new JobTest();
		QuartzManage.startJob("abc", job,str);
	}
}