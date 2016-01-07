package com.pansoft.jbsf.service.TimingTask.UserRemind;


import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.JbsfHelper;


public class UserRemind {  
    public static void start() throws Exception,  
            ParseException {  
    	System.out.println("TimingTask:userRemind task has start.");
        /* 
         * 此进程为主进程，触发了quartz对Job的调度 因此启动Job之后，在该进程修改调度，是没有效果的 
         */
    	
    	Connection con;//Declare a Connection object  
        String driver=JbsfHelper.getversion("jdbc.driver");// the MySQL driver  
        String url=JbsfHelper.getversion("jdbc.url");// URL points to destination database to manipulate  
        String user=JbsfHelper.getversion("jdbc.user");//user name for the specified database  
        String pwd=JbsfHelper.getversion("jdbc.password");//the corresponding password  
        Class.forName(driver);
        con=DriverManager.getConnection(url,user,pwd);
    	Statement st = con.createStatement();
    	ResultSet rs = st.executeQuery("select * from iv_remind");
    	
        JobTest job = new JobTest();
        String hour;//小时
        String min;//分钟
        String name;//用户名
        String week="";//星期
        String tmp;
        while(rs.next())
        {
        	String tm = rs.getString("F_TXSJ");//提醒时间字符串
        	hour = tm.substring(0,2);
        	min = tm.substring(2,tm.length());
        	name = rs.getString("F_YHBH");//用户编号
        	tmp = rs.getString("F_MONDAY");
        	if("1".equals(tmp))week += "MON,";
        	tmp = rs.getString("F_TUESDAY");
        	if("1".equals(tmp))week += "TUE,";
        	tmp = rs.getString("F_WEDNESDAY");
        	if("1".equals(tmp))week += "WED,";
        	tmp = rs.getString("F_THURSDAY");
        	if("1".equals(tmp))week += "THU,";
        	tmp = rs.getString("F_FRIDAY");
        	if("1".equals(tmp))week += "FRI,";
        	tmp = rs.getString("F_SATURDAY");
        	if("1".equals(tmp))week += "SAT,";
        	tmp = rs.getString("F_SUNDAY");
        	if("1".equals(tmp))week += "SUN,";
        	if(week.length()>0)
        	{
        		week = week.substring(0,week.length()-1);
        	}
        	String time = "00 "+min+" "+hour+" ? * ";
        	String str = time +week;
        	QuartzManage.startJob(name, job,str);//秒 分 时 日 月 星 年
        }
        //关闭连接
        rs.close();
        st.close();
        con.close();
    }  
}  