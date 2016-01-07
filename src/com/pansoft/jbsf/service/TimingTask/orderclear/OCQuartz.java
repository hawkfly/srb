package com.pansoft.jbsf.service.TimingTask.orderclear;

import java.text.ParseException;


public class OCQuartz {  
	 public static void start() throws Exception,ParseException {  
	System.out.println("TimingTask:userRemind task has start.");
	 /* 
	  * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
	  */
		OCJob job = new OCJob();
		String time ="0 0 */1 * * ?";
	 	OCManage.startJob("OCQuartz", job,time);//秒 分 时 日 月 星 年
	 }  
}  