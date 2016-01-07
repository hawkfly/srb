/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.application.report.ConditionBean;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.jbsf.auth.RegistReportID;
import com.wbcs.jbsf.util.JacksonMapper;
import com.wbcs.jbsf.util.WbcsUtils;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;

/**
 * 通用树转换处理类
 * @author hawkfly
 */
public class ItGetree extends AbsInterceptorDefaultAdapter {
	@Override
	public Object beforeLoadData(ReportRequest rrequest,ReportBean rbean,Object typeObj,String sql)
    {
		String reportid = rbean.getReportBean().getId();
		try{
			switch(RegistReportID.valueOf(reportid)){
			case authTreeRpt://根据当前用户所有角色的所有权限拼接其所有权限子树节点
				StringBuffer fsql = new StringBuffer();
				String newsql = "select distinct id, code, pid, name, url from jbsf_authorize";
				StringBuffer orwhere = new StringBuffer(" where ");
				//顶层权限列表
				List<Record> lstopauthes = Db.find(sql);
				for (int i = 0; i < lstopauthes.size(); i++) {
					Record record = lstopauthes.get(i);
					orwhere.append(" id like '");
					orwhere.append(record.getStr("ID")+"%' or");
				}
				
				fsql.append(newsql).append(orwhere.substring(0, orwhere.lastIndexOf("or")));
				fsql.append(" union ");
				fsql.append(sql);
		        return fsql.toString();
			case authSelectRpt:
				ConditionBean cb = rbean.getReportBean().getSbean().getConditionBeanByName("txtroleids");
				String ss = cb.getConditionValueForSP(rrequest);
				String tt = cb.getConditionValue(rrequest, -1);
				String rsql = WbcsUtils.parseString(sql);
				
				return rsql;
			default:
				return WbcsUtils.parseString(sql);
			}
		}catch(Exception e){
			return WbcsUtils.parseString(sql);
		}
    }
    @Override
	public Object afterLoadData(ReportRequest rrequest,ReportBean rbean,Object typeObj,Object dataObj)
    {
        ObjectMapper jsonMapper = JacksonMapper.getInstance();
        StringWriter sw = new StringWriter();
        JsonGenerator gen;
        try
        {
            gen=new JsonFactory().createJsonGenerator(sw);
            jsonMapper.writeValue(gen, dataObj); 
            gen.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        String datasStr = sw.toString().replace("pid","pId");
        StringBuffer paramsBuf=new StringBuffer();
        paramsBuf.append("{pageid:'"+rrequest.getPagebean().getId()+"'"); 
        paramsBuf.append(",reportid:'"+rbean.getId()+"'");
        paramsBuf.append(",datas:"+datasStr+"}");
        rrequest.getWResponse().addOnloadMethod("onloadTree",paramsBuf.toString(),false);
        
        //rrequest.getWResponse().setStatecode(Consts.STATECODE_NONREFRESHPAGE);
        return dataObj;
    }
}
