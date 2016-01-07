/**
 * 
 */
package testcase;

import java.util.List;
import java.util.Map;

import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.assistant.EditableReportAssistant;
import com.wbcs.system.component.application.report.abstractreport.AbsReportType;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;
import com.wbcs.system.intercept.ColDataByInterceptor;
import com.wbcs.system.intercept.RowDataByInterceptor;

/**
 * 连接器调试实例
 * @author hawkfly
 */
public class MyItprTest extends AbsInterceptorDefaultAdapter {

    public void doStart(ReportRequest reportrequest, ReportBean reportbean)
    {
    }

    public int doSave(ReportRequest rrequest, ReportBean rbean, AbsEditableReportEditDataBean editbean)
    {
        return EditableReportAssistant.getInstance().doSaveReport(rbean, rrequest, editbean);
    }

    public int doSavePerRow(ReportRequest rrequest, ReportBean rbean, Map mRowData, Map mParamValues, AbsEditableReportEditDataBean editbean)
    {
        return EditableReportAssistant.getInstance().doSaveRow(rrequest, rbean, mRowData, mParamValues, editbean);
    }

    public int doSavePerAction(ReportRequest rrequest, ReportBean rbean, Map mRowData, Map mParamValues, AbsEditActionBean actionbean, AbsEditableReportEditDataBean editbean)
    {
    	
		//return super.doSavePerAction(rrequest,rbean,mRowData,mParamValues,actionbean,editbean);
        return EditableReportAssistant.getInstance().doSavePerAction(rrequest, rbean, mRowData, mParamValues, actionbean, editbean);
    }

    public void doEnd(ReportRequest reportrequest, ReportBean reportbean)
    {
    }

    public Object beforeLoadData(ReportRequest rrequest, ReportBean rbean, Object typeObj, String sql)
    {
        return sql;
    }

    public Object afterLoadData(ReportRequest rrequest, ReportBean rbean, Object typeObj, Object dataObj)
    {
        return dataObj;
    }

    public RowDataByInterceptor beforeDisplayReportDataPerRow(AbsReportType reportTypeObj, ReportRequest rrequest, int rowindex, int i, List list)
    {
        return null;
    }

    public ColDataByInterceptor beforeDisplayReportDataPerCol(AbsReportType reportTypeObj, ReportRequest rrequest, Object displayColBean, int i, String s)
    {
        return null;
    }
}
