package com.pansoft.jbsf.wbcs.interceptor;

import java.util.List;
import java.util.Map;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.Config;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.assistant.EditableReportAssistant;
import com.wbcs.system.buttons.EditableReportSQLButtonDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportInsertDataBean;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;

public class ItprModifyRoleId extends AbsInterceptorDefaultAdapter{
	public int doSavePerRow(ReportRequest rrequest,ReportBean rbean,Map<String,String> mRowData,Map<String,String> mParamValues,
            AbsEditableReportEditDataBean editbean)
    {	/**
		如果执行删除动作
		if(editbean instanceof EditableReportDeleteDataBean)
		如果执行更新...
		if(editbean instanceof EditableReportUpdateDataBean)
		*/
		
		//如果执行插入操作则执行下面的if语句		
		if(editbean instanceof EditableReportInsertDataBean || editbean instanceof EditableReportSQLButtonDataBean )
		{
			String num="";//记录同一个用户创建的角色ID后的编号,以便获取最大值
			String userid = mParamValues.get("crid");//当前登录的用户的ID
			String username = mParamValues.get("crname");//当前登录的用户名
			String sql = "select max(id) curmaxid from jbsf_roles where id like '"+userid+"%' and pid='"+userid+"' and isghost='0'";
			String defaultdatasource = Config.getInstance().getDefault_datasourcename();
			boolean ismysql = false;
			if("ds_mysql".equals(defaultdatasource)){
				sql = "select max(ceil(id)) curmaxid from jbsf_roles where id like '"+userid+"%' and pid='"+userid+"' and isghost='0'";
				ismysql = true;
			}
			List listrec = Db.find(sql);
			Record rec = (Record) listrec.get(0);
			String maxid = "0";
			if(ismysql){
				num = maxid = rec.getDouble("curmaxid")+"";
			}else{
				maxid = rec.getStr("curmaxid");//获取当前用户创建的角色中ID最大的那个ID号
				if(maxid==null||"".equals(maxid))
				{
					num = "0";//如果没有取到任何值，说明表中没有该用户创建的角色，则num初始化值开始自增
				}
				else
				{
					num = maxid.replaceFirst(userid, "");//最大ID的最后的数字编号
				}
			}
	
			int numid = (int)Double.parseDouble(num);
			numid +=1;//把最大编号+1
			String newid = userid+numid;//生成新的最大ID
			mRowData.put("id",newid);//把新的ID赋给前台页面中
			mRowData.put("pid",userid);
		}
	        return EditableReportAssistant.getInstance().doSaveRow(rrequest,rbean,mRowData,mParamValues,editbean);
    }

}
