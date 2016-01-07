/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.util.Map;

import com.pansoft.jbsf.itface.ISQL;
import com.pansoft.jbsf.util.WbcsHelper;
import com.pansoft.jbsf.wbcs.interceptor.abs.AbsPerActionAdapter;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;

/**
 * 取现申请拦截处理器
 * @author hawkfly
 */
public class ItQxcash extends AbsPerActionAdapter {
	public ItQxcash() {
		super(QxSQL.SRB_QXSQ);
		// TODO Auto-generated constructor stub
	}

	public enum QxSQL implements ISQL{
		SRB_QXSQ, SRB_ACCOUNTINFO;
		
		public int SRB_QXSQ(ReportRequest rrequest, ReportBean rbean,
				Map<String, String> mRowData, Map<String, String> mParamValues,
				AbsEditActionBean actionbean, AbsEditableReportEditDataBean editbean){
			
			System.out.println("this is SRB_QXSQ method!");
			return Consts.PER_SUCCESS;
		}
		
		public int SRB_ACCOUNTINFO(ReportRequest rrequest, ReportBean rbean,
				Map<String, String> mRowData, Map<String, String> mParamValues,
				AbsEditActionBean actionbean, AbsEditableReportEditDataBean editbean){
			String qxje = mRowData.get("qx_je");
			String balanceold = mRowData.get("acc_balance");
			try{
				float balance = Float.parseFloat(balanceold) - Float.parseFloat(qxje);
				mParamValues.remove("newbalance");
				mParamValues.put("newbalance", balance + "");
			}catch(Exception e){
				e.printStackTrace();
				WbcsHelper.errorMsg(rrequest, "金额计算错误！", true);
			}
			
			System.out.println("this is SRB_ACCOUNTINFO method!");
			return Consts.PER_SUCCESS;
		}
	}
	
}
