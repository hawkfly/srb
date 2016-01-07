/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.jbsf.dao.JBSFDaoTemplate;
import com.wbcs.jbsf.util.Wbcs4JBSFUtil;
import com.wbcs.jbsf.util.WbcsUtils;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.assistant.EditableReportAssistant;
import com.wbcs.system.buttons.EditableReportSQLButtonDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportParamBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportUpdateDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.InsertSqlActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.UpdateSqlActionBean;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;

/**
 * @author hawkfly
 *
 */
@SuppressWarnings("unchecked")
public class ItVerifySh extends AbsInterceptorDefaultAdapter {

	int flowid = -1;
	public int doSavePerAction(ReportRequest rrequest, ReportBean rbean, Map mRowData, Map mParamValues, AbsEditActionBean actionbean, AbsEditableReportEditDataBean editbean)
    {
		Map<String, String> msql = new HashMap<String, String>();
		msql.put("sql1", "coral_realfhje"); msql.put("sql2", "update SRB_FLOW_HISTORY"); msql.put("sql3", "insert into SRB_FLOW_HISTORY");
		msql.put("sql4", "coral_realdatetime"); msql.put("sql5", "update SRB_USER_PDCT"); msql.put("sql6", "update SRB_ACCOUNTINFO");
    	
    	JBSFDaoTemplate daoTemplate = new JBSFDaoTemplate(false);
		Connection conn = rrequest.getConnection();
		String rolename = Wbcs4JBSFUtil.getRolenames(rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_ROLE)); 
		Object userid = rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_USERID);
		Record record = Db.findFirst("select flow_id, flow_status from SRB_CONFIG_FLOW where flow_userid = ?", userid);
		flowid = record.getInt("flow_id"); String flowstatus = record.getStr("flow_status");
		
		String sql = ""; List<EditableReportParamBean> lstParamBean = null;
		Object clickBtn = mParamValues.get("whatbtn");
		if(actionbean instanceof UpdateSqlActionBean){
			UpdateSqlActionBean updatesqlbean = (UpdateSqlActionBean)actionbean;
			sql = updatesqlbean.getSql();
			lstParamBean = updatesqlbean.getLstParamBeans();
		}else if(actionbean instanceof InsertSqlActionBean){
			InsertSqlActionBean insertsqlbean = (InsertSqlActionBean)actionbean;
			sql = insertsqlbean.getSql();
			lstParamBean = insertsqlbean.getLstParamBeans();
		}
		
		int rtnval = WX_RETURNVAL_SKIP;
		if(editbean instanceof EditableReportUpdateDataBean || editbean instanceof EditableReportSQLButtonDataBean){
			if(Consts.BTN_PASS.equals(clickBtn)&&Consts.FLOWCHECK.equals(flowstatus)){
				//执行动作
				rtnval = doCNodeBtnpassService(sql, msql, mRowData,mParamValues)? WX_RETURNVAL_SUCCESS: WX_RETURNVAL_SKIP;
				
			}else if(Consts.BTN_PASS.equals(clickBtn)&&Consts.FLOWFINAL.equals(flowstatus)){
				
				rtnval = doFNodeBtnpassService(sql, msql, mRowData,mParamValues)? WX_RETURNVAL_SUCCESS: WX_RETURNVAL_SKIP;
				
			}else if(Consts.BTN_NO.equals(clickBtn)&&Consts.FLOWCHECK.equals(flowstatus)){
				
				rtnval = doCNodeBtnnoService(sql, msql, mRowData,mParamValues)? WX_RETURNVAL_SUCCESS: WX_RETURNVAL_SKIP;
				
			}else if(Consts.BTN_NO.equals(clickBtn)&&Consts.FLOWFINAL.equals(flowstatus)){
				rrequest.getWResponse().getMessageCollector().error("本节点暂不允许退回！", false);
				rtnval = doFNodeBtnnoService(sql, msql, mRowData,mParamValues)? WX_RETURNVAL_SUCCESS: WX_RETURNVAL_SKIP;
			}
		}
		if(rtnval == WX_RETURNVAL_SUCCESS)
			return EditableReportAssistant.getInstance().doSavePerAction(rrequest, rbean, mRowData, mParamValues, actionbean, editbean);
		else
			return WX_RETURNVAL_SKIP;
    }
	
	/**
	 * 审核节点通过按钮业务逻辑
	 * @param sql1
	 * @param sql2
	 * @param sql3
	 * @return
	 */
	private boolean doCNodeBtnpassService(String sql, Map<String, String> msql, Map mRowData, Map mParamValues){
		Map<String, String> srowData = (Map<String, String>)mRowData;
		if(sql.indexOf(msql.get("sql1"))!=-1 || sql.indexOf(msql.get("sql2"))!=-1 ){//sql1和sql2原生态执行
			return true;
		}else if(sql.indexOf(msql.get("sql3"))!=-1 ){//sql3加工后执行
			mParamValues.remove("cflowid");
			mParamValues.remove("cflowstatus");
			mParamValues.remove("note");
			int iflowid = flowid + 1;
			
			mParamValues.put("cflowid",iflowid+"");
			mParamValues.put("cflowstatus","ready");
			mParamValues.put("note","");
			return true;
		}else{//其他不执行
			return false;			
		}
	}
	
	/**
	 * 审批节点不通过按钮业务逻辑
	 * @param sql1
	 * @param sql2
	 * @param sql3
	 * @return
	 */
	private boolean doCNodeBtnnoService(String sql, Map<String, String> msql,Map mRowData, Map mParamValues){
		return true;
	}
	
	/**
	 * 结束节点通过业务逻辑
	 * @param sql2
	 * @param sql4
	 * @param sql5
	 * @return
	 */
	private boolean doFNodeBtnpassService(String sql, Map<String, String> msql,Map mRowData, Map mParamValues){
		Map<String, String> srowData = (Map<String, String>)mRowData;
		if(sql.indexOf(msql.get("sql2"))!=-1 || sql.indexOf(msql.get("sql4"))!=-1 ){//sql1和sql2原生态执行
			return true;
		}else if(sql.indexOf(msql.get("sql5"))!=-1 ){//sql3加工后执行
			//成交金额  = 原始成交金额 - 原始赎回金额
			String cjje_old = srowData.get("cjje");
			String shje_old = srowData.get("shje");
			int cjje_new = Integer.parseInt(cjje_old) - Integer.parseInt(shje_old);
			mParamValues.remove("cjje");
			mParamValues.remove("jsrq");
			
			mParamValues.put("cjje", cjje_new+"");
			mParamValues.put("jsrq", WbcsUtils.getDefaultTime());
			
			Object sfjsobj = mParamValues.get("sfjs");
			if(cjje_new > 0){//部分赎回
				mParamValues.remove("sfjs");
				mParamValues.put("sfjs", "0");
			}
			
			return true;
		}else if(sql.indexOf(msql.get("sql6")) != -1){
			//账户余额 = 原来账户金额  + 真实赎回金额
			String userid = srowData.get("coral_cstmid");
			String realfhje = srowData.get("coral_realfhje");
			Record r = Db.findFirst("select acc_balance from SRB_ACCOUNTINFO where acc_user = ?", userid);
			float balance = r.getBigDecimal("acc_balance").floatValue();
			balance = balance + Float.parseFloat(realfhje);
			mParamValues.remove("balance");
			
			mParamValues.put("balance", balance+"");
			
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 结束节点不通过业务逻辑
	 * @param sql2
	 * @param sql4
	 * @param sql5
	 * @return
	 */
	private boolean doFNodeBtnnoService(String sql, Map<String, String> msql,Map mRowData, Map mParamValues){
		return false;
	}
}
