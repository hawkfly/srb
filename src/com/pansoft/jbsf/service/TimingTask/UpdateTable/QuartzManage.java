package com.pansoft.jbsf.service.TimingTask.UpdateTable;

import java.text.ParseException;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManage {  
    private static SchedulerFactory sf = new StdSchedulerFactory();  
    private static String JOB_GROUP_NAME = "group";  
    private static String TRIGGER_GROUP_NAME = "trigger";  
  
    public static void startJob(String jobName, Job job, String time)  
            throws SchedulerException, ParseException {  
        Scheduler sched = sf.getScheduler();  
  
        JobDetail jobDetail = new JobDetail();  
        jobDetail.setName(jobName);  
        jobDetail.setGroup(JOB_GROUP_NAME);  
        jobDetail.setJobClass(job.getClass());  
  
        CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);  
        trigger.setCronExpression(time);  
        sched.scheduleJob(jobDetail, trigger);  
  
        if (!sched.isShutdown()) {  
            sched.start();  
        }  
    }  
  
    /** 
     * 从Scheduler 移除当前的Job,修改Trigger 
     *  
     * @param jobDetail 
     * @param time 
     * @throws SchedulerException 
     * @throws ParseException 
     */  
    public static void modifyJobTime(JobDetail jobDetail, String time)  
            throws SchedulerException, ParseException {  
        Scheduler sched = sf.getScheduler();  
        Trigger trigger = sched.getTrigger(jobDetail.getName(),  
                TRIGGER_GROUP_NAME);  
        if (trigger != null) {  
            CronTrigger ct = (CronTrigger) trigger;  
            // 移除当前进程的Job  
            sched.deleteJob(jobDetail.getName(), jobDetail.getGroup());  
            // 修改Trigger  
            ct.setCronExpression(time);  
            System.out.println("CronTrigger getName " + ct.getJobName());  
            // 重新调度jobDetail  
            sched.scheduleJob(jobDetail, ct);  
        }  
    }  
  
}