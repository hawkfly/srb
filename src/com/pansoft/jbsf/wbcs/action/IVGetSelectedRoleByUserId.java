package com.pansoft.jbsf.wbcs.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVGetSelectedRoleByUserId implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		String str="";
		if(arg2.size() == 0)return str;
		String userid = arg2.get(0).get("key");
		List list = Db.find("select roleid from jbsf_user_roles where userid=?",userid);
		Record rec;
		for(int i = 0;i<list.size();i++)
		{
			rec = (Record) list.get(i);
			String roleid = rec.getStr("roleid");
			if(i==0)
			{
				str = roleid;
			}
			else
			{
				str = str + "," + roleid;
			}
		}
		return str;
	}

	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
