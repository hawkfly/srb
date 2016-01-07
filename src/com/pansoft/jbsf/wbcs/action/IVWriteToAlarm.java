package com.pansoft.jbsf.wbcs.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVWriteToAlarm implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
			//笛卡尔积算法
		//检验被插入表中是否有数据
		List list = Db.find("select * from iv_set_message");
		System.out.println(list+":"+list.size());
		if(list.size()<1)
		{
			Db.update("insert into iv_set_message(F_YHBH,F_FLBH,F_FLMC,F_SFKQ)(select F_YHBH,F_FLBH,F_FLMC,F_SFKQ from (select F_YHBH from iv_users where F_SYZT='1') p,(select F_FLBH,F_FLMC,F_SFKQ from iv_xxfl where F_SYZT='1') q)");
		}
		else
		{
		Db.update("insert into iv_set_message(F_YHBH,F_FLBH,F_FLMC,F_SFKQ)(select distinct F_YHBH,F_FLBH,F_FLMC,F_SFKQ from (select F_FLBH,F_FLMC,F_SFKQ from iv_set_message) x,(select F_YHBH from iv_users where  F_SYZT='1' and F_YHBH not in(select F_YHBH from iv_set_message)) y)");
		Db.update("insert into iv_set_message(F_YHBH,F_FLBH,F_FLMC,F_SFKQ)(select distinct F_YHBH,F_FLBH,F_FLMC,F_SFKQ from (select F_YHBH from iv_set_message) m,(select F_FLBH,F_FLMC,F_SFKQ from iv_xxfl where F_SYZT='1' and F_FLBH not in(select F_FLBH from iv_set_message)) n)");
		}
		return "写入成功";
	}

	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
