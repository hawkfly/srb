package com.pansoft.jbsf.service.TimingTask.mxsend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.chainsaw.Main;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.pansoft.jbsf.service.TimingTask.mxsend.JobTest;
import com.pansoft.jbsf.service.TimingTask.mxsend.QuartzManage;
import com.pansoft.jbsf.util.JbsfHelper;
import com.wbcs.config.Config;


public class MxSend {  
	 public static void start() throws Exception,ParseException {  
	System.out.println("TimingTask:userRemind task has start.");
	 /* 
	  * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
	  */
		JobTest job = new JobTest();
		String time ="00 27 16 ? * *";
	 	QuartzManage.startJob("MxSend", job,time);//秒 分 时 日 月 星 年
	 }  
}  