package com.pansoft.jbsf.wbcs.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVSubmitCheck implements IAction{

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
		String checkSM = arg3.get("key");
		String status = arg3.get("status");
		String sqbh = list.get(0).get("F_SQBH");
		String sh = list.get(0).get("F_SH");
		String dwbh = list.get(0).get("F_DWBH");
		String dwmc = list.get(0).get("F_DWMC");
		String sqrbh = list.get(0).get("F_SQRBH");
		String sqrxm = list.get(0).get("F_SQRXM");
		Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式
		String nowTime="";
		nowTime= df.format(dt);
		List lls = Db.find("select F_SQZT from iv_yhsq_dw where F_SQBH=?",sqbh);
		Record re = (Record)lls.get(0);
		String isCheck = re.getStr("F_SQZT");
		//检测 该单位有没有管理员 
		List ills = Db.find("select F_YHBH from iv_user_dw where F_DWBH=? and F_MANA ='1' ",dwbh);
		Record ire = (Record)ills.get(0);
		String isCh = ire.getStr("F_YHBH");
		//如果查出来用户编号为空，说明是没有登录申请的企业，该企业的管理员为空，然后把第一个申请的改为管理员
		if("".equals(isCh)){
			Db.update("update iv_yhsq_dw set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
			Db.update("update iv_yhsq_dw set F_SQRBH=?,F_SQRXM=? where F_DWBH=? AND F_SQLX='DW'",sqrbh,sqrxm,dwbh);
			Db.update("update iv_user_dw set F_YHBH=? , F_NAME =?,F_CHDATE=?,F_SPRBH=?,F_SPRXM=? where F_DWBH = ? and F_MANA = '1' ",sqrbh,sqrxm,nowTime,userid,username,dwbh);
			req.getWResponse().getMessageCollector().success("审核已提交",false);
			return "审核已提交";
		}
		if(!"0".equals(isCheck))
		{
			req.getWResponse().getMessageCollector().error("审核失败!重复审核！",false);
			return null;
		}
		if("pass".equals(status))
		{
			//update iv_yhsq_dw(,F_SPRBH=#{userid},F_SPRXM=#{username},F_SPSJ=#{crtime},@{F_SPSM}) where F_SQBH =  @{F_SQBH};
			//insert into iv_user_dw(F_YHBH=@{F_SQRBH},F_NAME=@{F_SQRXM},@{F_SH},@{F_DWMC},@{F_DWBH},F_MANA='0',F_CRDATE=#{crtime},F_CHDATE=#{crtime},F_SPRBH=#{userid},F_SPRXM=#{username});
			Db.update("update iv_yhsq_dw set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
			Db.update("insert into iv_user_dw(F_YHBH,F_NAME,F_SH,F_DWMC,F_DWBH,F_MANA,F_CRDATE,F_CHDATE,F_SPRBH,F_SPRXM) values(?,?,?,?,?,'0',?,?,?,?)",sqrbh,sqrxm,sh,dwmc,dwbh,nowTime,nowTime,userid,username);
			req.getWResponse().getMessageCollector().success("审核已提交",false);
			return "审核已提交";
		}
		else if("fail".equals(status))
		{
			//update iv_yhsq_dw(F_SQZT='2',F_SPRBH=#{userid},F_SPRXM=#{username},F_SPSJ=#{chtime},@{F_SPSM}) where F_SQBH = @{F_SQBH};
			Db.update("update iv_yhsq_dw set F_SQZT='2',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,nowTime,checkSM,sqbh);
			req.getWResponse().getMessageCollector().success("审核已提交",false);
			return "审核已提交";
		}
		else
		{
			req.getWResponse().getMessageCollector().error("审核失败",false);
			return "审核失败";
		}
		
	}

}
