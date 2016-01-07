package com.pansoft.jbsf.service.TimingTask.UpdateUserInfo;

import java.text.ParseException;
import java.util.List;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.pansoft.jbsf.util.JbsfHelper;
import com.wbcs.config.Config;


public class UpdateUserInfo {  
    public static void start() throws SchedulerException,ParseException {
        /* 
         * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
         */  
    	System.out.println("TimingTask:updateUserInfo task has start.");
        JobTest job = new JobTest();
        String hour;
        String min;
        hour = JbsfHelper.getversion("hour");
        min = JbsfHelper.getversion("minute");
        QuartzManage.startJob("updateUserInfo", job,"00 "+min+" "+hour+" ? * *");//秒 分 时 日 月 星 年
    }  
}  