/**
 * 
 */
package com.pansoft.jbsf.util;

import java.util.UUID;

import com.jf.plugin.activerecord.Db;

/**
 * @author hawkfly
 */
public class JbsfLogUtils {
	public static void info(String mkname, String content){
		String id = Utils.getUUID();
		String mark = "info";
		String time = Utils.getDefaultTime();
		Db.update("insert into jbsf_logs(log_id, log_type, log_content, log_time, log_mark) values(?,?,?,?,?)", id, mkname, content, time, mark);
	}
	
	public static void error(String mkname, String content){
		String id = Utils.getUUID();
		String mark = "error";
		String time = Utils.getDefaultTime();
		Db.update("insert into jbsf_logs(log_id, log_type, log_content, log_time, log_mark) values(?,?,?,?,?)", id, mkname, content, time, mark);
	}
}
