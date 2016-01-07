package com.pansoft.jbsf.wbcs.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.jbsf.dao.JBSFDaoTemplate;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsJavaEditActionBean;

public class CheckNotPass extends AbsJavaEditActionBean{

	@Override
	public void updateData(ReportRequest rrequest,ReportBean rbean,Map<String,String> mRowData,Map<String,String> mParamValues)
	{		
		
		 Object userid = mParamValues.get("userid");//当前登录的用户编号
		 Object username = mParamValues.get("username");//当前登录的用户名
		 Object sqbh = mParamValues.get("sqbh");//申请编号
		 Object spsm = mRowData.get("F_SPSM");//审批说明
		 if("".equals(spsm)||spsm==null)
		 {
			 rrequest.getWResponse().getMessageCollector().error("审核失败,审批说明不能为空！", false);
			 return;
		 }
		Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式 
		 Object crtime= df.format(dt);
		int count =  Db.update("update iv_yhsq_dw set F_SQZT='2',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,crtime,spsm,sqbh);
		if(count==1)
		{
			rrequest.getWResponse().getMessageCollector().success("审核已提交", false);
		}
		else
		{
			rrequest.getWResponse().getMessageCollector().error("审核失败", false);
		}
	}

}
