package com.pansoft.jbsf.service.TimingTask.autoqs;

import java.text.ParseException;


public class QSQuartz {  
	 public static void start() throws Exception,ParseException {  
	System.out.println("TimingTask:userRemind task has start.");
	 /* 
	  * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
	  */
		QSJob job = new QSJob();
		String time ="0 0 12 * * ?";
	 	QSManage.startJob("QSQuartz", job,time);//秒 分 时 日 月 星 年
	 }  
}  