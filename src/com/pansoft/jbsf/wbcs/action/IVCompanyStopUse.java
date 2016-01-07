package com.pansoft.jbsf.wbcs.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVCompanyStopUse implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String executeSeverAction(ReportRequest req,
			IComponentConfigBean arg1, List<Map<String, String>> list,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		String sqbh = list.get(0).get("F_SQBH");//申请编号
		String dwbh = list.get(0).get("F_DWBH");
		String userid = req.getRequest().getSession().getAttribute("userid").toString();
		String username = req.getRequest().getSession().getAttribute("usercode").toString();
		Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式
		String nowTime="";
		nowTime= df.format(dt);
		String checkSM = arg3.get("key");//页面提交的审批说明
		String status = arg3.get("status");//通过还是不通过，【状态】
		List lis = Db.find("select F_SQLX from iv_dwsq where F_SQBH=?",sqbh);
		Record rec = (Record)lis.get(0);
		String type = rec.getStr("F_SQLX");
		if("S".equals(type))
		{
			if("pass".equals(status))
			{
				Db.update("update iv_yhsq_dw set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
				Db.update("update iv_dwzd set F_SYZT='0' where F_DWBH=?",dwbh);
				req.getWResponse().getMessageCollector().success("审核已提交",false);
			}
			else if("fail".equals(status))
			{
				Db.update("update iv_yhsq_dw set F_SQZT='2',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
				req.getWResponse().getMessageCollector().success("审核已提交",false);
			}
			else
			{
				req.getWResponse().getMessageCollector().error("审核失败",false);
			}
		}
		else if("O".equals(type))
		{
			if("pass".equals(status))
			{
				Db.update("update iv_yhsq_dw set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
				Db.update("update iv_dwzd set F_SYZT='1' where F_DWBH=?",dwbh);
				req.getWResponse().getMessageCollector().success("审核已提交",false);
			}
			else if("fail".equals(status))
			{
				Db.update("update iv_yhsq_dw set F_SQZT='2',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
				req.getWResponse().getMessageCollector().success("审核已提交",false);
			}
			else
			{
				req.getWResponse().getMessageCollector().error("审核失败",false);
			}
		}
		return null;
	}

}
