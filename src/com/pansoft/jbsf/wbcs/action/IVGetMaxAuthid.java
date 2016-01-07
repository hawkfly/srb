package com.pansoft.jbsf.wbcs.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.Utils;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVGetMaxAuthid implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		String sesid = arg0.getSession().getAttribute("userid").toString();
		String uuid = Utils.getUUID();
		String result="";
		String id = "";
		String pid = arg2.get(0).get("key");
		List list = Db.find("select id from jbsf_authorize where pid = ? group by id order by id desc",pid);
		if(list.size()!=0)
		{
			Record rec = (Record) list.get(0);
			id = rec.getStr("id");
			if(id.length()<=4)
			{
				int num = Integer.parseInt(id);
				num++;
				String authid = num+"";
				Db.update("insert into jbsf_authorize(id,pid,name,description) values(?,?,?,?)",authid,pid,"新权限","新权限");
				if(!"0".equals(sesid))
				{
					Db.update("insert into jbsf_roles_authes values(?,?,?,?)",uuid,sesid,authid,"write");	
				}
				return authid;
			}
			else
			{
				id = id.substring(0,pid.length()+3);
				String endNum = id.substring(id.length()-3,id.length());
				String startNum = id.substring(0,id.length()-3);
				int num = Integer.parseInt(endNum);
				num++;
				if(num<10)
				{
					result = "00"+num;
				}
				else
				{
					result = "0"+num;
				}
				String authid = startNum + result;
				Db.update("insert into jbsf_authorize(id,pid,name,description) values(?,?,?,?)",authid,pid,"新权限","新权限");
				//若当前用户是最高级管理员，则不显示此页面在管理员界面
				
				if(!"0".equals(sesid))
				{
					Db.update("insert into jbsf_roles_authes values(?,?,?,?)",uuid,sesid,authid,"write");	
				}
				return authid;
			}
		}
		else
		{
			String authid = pid+"001";
			Db.update("insert into jbsf_authorize(id,pid,name,description) values(?,?,?,?)",authid,pid,"新权限","新权限");
			if(!"0".equals(sesid))
			{
				Db.update("insert into jbsf_roles_authes values(?,?,?,?)",uuid,sesid,authid,"write");
			}
			return authid;
		}
	}
	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}	


}
