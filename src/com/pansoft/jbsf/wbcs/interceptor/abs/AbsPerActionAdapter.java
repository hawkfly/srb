package com.pansoft.jbsf.wbcs.interceptor.abs;

import java.util.Map;

import com.pansoft.jbsf.exception.JbsfException;
import com.pansoft.jbsf.itface.ISQL;
import com.pansoft.jbsf.util.WbcsHelper;
import com.pansoft.jbsf.wbcs.interceptor.ItQxcash.QxSQL;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;

public abstract class AbsPerActionAdapter extends AbsInterceptorDefaultAdapter {
	private ISQL isql;
	public AbsPerActionAdapter(ISQL isql){
		this.isql = isql;
	}
	@Override
	public int doSavePerAction(ReportRequest rrequest, ReportBean rbean,
			Map<String, String> mRowData, Map<String, String> mParamValues,
			AbsEditActionBean actionbean, AbsEditableReportEditDataBean editbean) {
		
		try {
			
			int rtnval = WbcsHelper.dofilterSql(WbcsHelper.getSql(actionbean),isql, new Object[]{rrequest,rbean,mRowData,mParamValues,actionbean,editbean},
						new Class[]{ReportRequest.class, ReportBean.class, Map.class, Map.class, AbsEditActionBean.class, AbsEditableReportEditDataBean.class});
			if(rtnval==Consts.PER_SUCCESS){
				return super.doSavePerAction(rrequest, rbean, mRowData, mParamValues,actionbean, editbean);
			}else{
				return rtnval;
			}
			
		} catch (JbsfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//抛出异常也会执行原生态默认动作
		return super.doSavePerAction(rrequest, rbean, mRowData, mParamValues,actionbean, editbean);
	}
}
