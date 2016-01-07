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

public class IVManagerCheck implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String executeSeverAction(ReportRequest req,
			IComponentConfigBean arg1, List<Map<String, String>> list,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		String userid = req.getRequest().getSession().getAttribute("userid").toString();
		String username = req.getRequest().getSession().getAttribute("usercode").toString();
		String checkSM = arg3.get("key");//页面提交的审批说明
		String status = arg3.get("status");//通过还是不通过，【状态】
		String sqbh = list.get(0).get("F_SQBH");//申请编号
		String sh = list.get(0).get("F_SH");//税号
		String dwbh = list.get(0).get("F_DWBH");//单位编号
		String dwmc = list.get(0).get("F_DWMC");//单位名称
		String sqrbh = list.get(0).get("F_SQRBH");//申请人编号
		String sqrxm = list.get(0).get("F_SQRXM");//申请人姓名
		String sqsm = list.get(0).get("F_SQSM");//申请说明
		String tyrbh = list.get(0).get("F_TYRBH");//替用人编号
		String tyrxm = list.get(0).get("F_TYRXM");//替用人姓名
		Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式
		String nowTime="";
		nowTime= df.format(dt);
		List ls = Db.find("select F_MANA from iv_user_dw where F_DWBH=? and F_YHBH=?",dwbh,tyrbh);
		Record  rec = (Record)ls.get(0);
		String isMana = rec.getStr("F_MANA");
		if(!"1".equals(isMana))
		{
			req.getWResponse().getMessageCollector().error("审核失败！替用人不合法！",false);
			return "审核失败！替用人不合法！";
		}
		List lls = Db.find("select F_SQZT from iv_manasq where F_SQBH=?",sqbh);
		Record re = (Record)lls.get(0);
		String zt = re.getStr("F_SQZT");
		if(!"0".equals(zt))
		{
			req.getWResponse().getMessageCollector().error("审核失败！重复审核！",false);
			return "审核失败！重复审核！";
		}
		if("pass".equals(status))
		{
			Db.update("update iv_user_dw set F_MANA='0' where F_DWBH=? and F_YHBH=?",dwbh,tyrbh);
			Db.update("update iv_user_dw set F_MANA='1' where F_DWBH=? and F_YHBH=?",dwbh,sqrbh);
			Db.update("update iv_manasq set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
			req.getWResponse().getMessageCollector().success("审核已提交",false);
		}
		else if("fail".equals(status))
		{
			Db.update("update iv_manasq set F_SQZT='2',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
			req.getWResponse().getMessageCollector().success("审核已提交",false);
		}
		else
		{
			req.getWResponse().getMessageCollector().error("审核失败",false);
		}
		return null;
	}

}
