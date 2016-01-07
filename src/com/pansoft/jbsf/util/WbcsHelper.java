/**
 * 
 */
package com.pansoft.jbsf.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pansoft.jbsf.exception.JbsfException;
import com.pansoft.jbsf.itface.ISQL;
import com.pansoft.util.Consts;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditSqlActionBean;

/**
 * wbcs框架快速开发接口支持
 * @author hawkfly
 */
public class WbcsHelper {
	
	public static String getSql(AbsEditActionBean actionbean){
		String sql = "";
		
		if(actionbean instanceof AbsEditSqlActionBean){
			AbsEditSqlActionBean sqlactionbean = (AbsEditSqlActionBean)actionbean;
			sql = sqlactionbean.getSql();
		}
		
		return sql;
	}

	/**
	 * 执行SQL过滤。并根据指定的泛型类执行同名方法
	 * @param sql
	 * @param isql
	 */
	public static int dofilterSql(String sql, ISQL isql, Object[] args, Class[] argtypes) throws JbsfException{
		// TODO Auto-generated method stub
		if(sql == null)throw new JbsfException("参数sql为空！");
		int status = Consts.PER_SUCCESS;//默认情况下都不匹配按照原生态SQL执行
		Class<? extends ISQL> enumclass = isql.getClass();
		ISQL[] ss = enumclass.getEnumConstants();
		/*Class[] argsclass = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			argsclass[i] = args[i].getClass();
		}*/
		for (ISQL item : ss) {
			if(sql.indexOf(item.toString()) != -1){
				try {
					Method method = isql.getClass().getMethod(item.toString(), argtypes);
					Object rtnObj = method.invoke(isql, args);
					status = (Integer)rtnObj;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return status;
	}
	
	public static void errorMsg(ReportRequest rrequest, String msg, boolean isTerminate){
		rrequest.getWResponse().getMessageCollector().error(msg, isTerminate);
	}
}
