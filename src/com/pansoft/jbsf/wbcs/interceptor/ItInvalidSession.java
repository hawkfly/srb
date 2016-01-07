/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import com.wbcs.config.Config;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.intercept.AbsPageInterceptor;

/**
 * 会话失效后，跳转回登录页面
 * @author hawkfly
 */
public class ItInvalidSession extends AbsPageInterceptor {
	public void doStart(ReportRequest rrequest)
    {
		Object PAGEID = rrequest.getAttribute("PAGEID");
		if("loginPageClt".equals(PAGEID))return;
		Object roles = rrequest.getRequest().getSession().getAttribute("roles");
		if(roles == null){
			String prjpath = rrequest.getRequest().getContextPath();
			String showreporturl = Config.showreport_url;
			String loginpath = showreporturl + "?PAGEID=loginPageClt";
			rrequest.getWResponse().println(
					"<script>" +
					"if(parent){" +
					"	parent.location.href = '"+loginpath+"'" +
					"}else{" +
					"	location.href = '"+loginpath+"'" +
					"}" +
					"</script>");
			rrequest.getWResponse().getMessageCollector().alert("会话已失效！", true);
		}
    }
}
