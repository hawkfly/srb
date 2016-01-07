package com.pansoft.jbsf.wbcs.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVGetID implements IAction {

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		Db.update(" insert into iv_swyh(F_SH,F_MC)(select F_SH,F_MC from (select F_SH,F_MC from (select F_SH,min(F_DWBH) F_BH,F_DWMC F_MC from iv_dwzd where  F_SH >'0' group by F_SH) h) t where  not exists (select F_SH,F_MC from iv_swyh where t.F_SH=F_SH))");
		return null;
	}

	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		System.out.println(22);
		return null;
	}

}
