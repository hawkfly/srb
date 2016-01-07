/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jf.plugin.activerecord.Db;
import com.wbcs.config.component.application.report.ColBean;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.config.component.other.ButtonsBean;
import com.wbcs.jbsf.dao.IDataModal;
import com.wbcs.jbsf.dao.JBSFDaoTemplate;
import com.wbcs.jbsf.util.Wbcs4JBSFUtil;
import com.wbcs.system.CacheDataBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.assistant.EditableReportAssistant;
import com.wbcs.system.assistant.ReportAssistant;
import com.wbcs.system.buttons.AbsButtonType;
import com.wbcs.system.buttons.EditableReportSQLButtonDataBean;
import com.wbcs.system.buttons.WbcsButton;
import com.wbcs.system.component.application.report.EditableFormReportType;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsEditableReportEditDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.DeleteSqlActionBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportDeleteDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportInsertDataBean;
import com.wbcs.system.component.application.report.configbean.editablereport.EditableReportUpdateDataBean;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;
import com.wbcs.system.intercept.IInterceptor;
import com.wbcs.util.Consts;

/**
 * 山东传媒网新闻编辑功能报表拦截器
 * @author hawkfly
 */
public class Ref_ItprSDCMNewsEditor extends AbsInterceptorDefaultAdapter
{
    @Override
	public void doStart(ReportRequest rrequest,ReportBean rbean)
    {   
        String rolename = Wbcs4JBSFUtil.getRolenames(rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_ROLE));
        //String tmp = rrequest.getStringAttribute(rbean.getId()+"_ACCESSMODE");
        if("add".equals(rrequest.getStringAttribute(rbean.getId()+"_ACCESSMODE"))){
            rrequest.getWResponse().addOnloadMethod("loadUploader","{ACCESSMODE: 'add'}",false);
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "publishCstmBtn", "display", "false");
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "publishandtoimpttnewsCstmBtn", "display", "false");
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "publishandtohomesiteCstmBtn", "display", "false");
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "gobackCstmBtn", "display", "false");
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "gobackdelCstmBtn", "display", "false");
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "revokePublishCstmBtn", "display", "false");
            if(rolename.indexOf("编审") != -1){
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "craftCstmBtn", "display", "false");
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "checkCstmBtn", "display", "false");
            }else{
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "publish4bsRoleCstmBtn", "display", "false");
            }
        }else if("update".equals(rrequest.getStringAttribute(rbean.getId()+"_ACCESSMODE"))){
            //String rolename = rrequest.getRequest().getParameter("rolename");
            //改为在session中直接获取
            rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "publish4bsRoleCstmBtn", "display", "false");
            if(rolename.indexOf("编辑") != -1){
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "craftCstmBtn", "display", "false");
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "checkCstmBtn", "display", "false");
            }
            if(rolename.indexOf("编审") != -1){
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "craftCstmBtn", "display", "false");
                rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "checkCstmBtn", "display", "false");
            }
        }else{
            String accessmode = rrequest.getRequest().getParameter(rbean.getId()+"_ACCESSMODE");
            if(Consts.READONLY_MODE.equals(accessmode)){//是只读模式
                if("编辑".equals(rolename)){//推送要闻显示
                    
                }else if("编审".equals(rolename)){//推送主站显示
                    
                }else{//将CstmBtn结尾的按钮全部隐藏
                    ButtonsBean bsb = rbean.getButtonsBean();
                    if(bsb!=null){
                        List<AbsButtonType> lstButs = bsb.getAllDistinctButtonsList();
                        for(AbsButtonType btnitem:lstButs)
                        {
                            if(btnitem instanceof WbcsButton){
                               if(btnitem.getName().lastIndexOf("CstmBtn") > 0){
                                    rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, btnitem.getName(), "display", "false");
                               }
                            }
                        }
                    }
                }
            }else{//为newslist 追加动态参数txtxwzt
                if(rolename.indexOf("记者") != -1){
                    rrequest.getRequest().getSession().setAttribute("txtxwzt","2");
                }
                if(rolename.indexOf("编辑") != -1){
                   Object lmids = rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_LMIDS);
                   String newlmids = String.valueOf(lmids).trim().replace(" ",",");
                   rrequest.getRequest().getSession().setAttribute("lmids",newlmids);
                }
                if(rolename.indexOf("编审") != -1){
                    rrequest.getRequest().getSession().setAttribute("txtpublish","2");
                }
            }
        }
    }
    
    /* 根据登录角色对新闻列表的数据进行过滤
     * @see com.wbcs.system.intercept.AbsInterceptorDefaultAdapter#beforeLoadData(com.wbcs.system.ReportRequest, com.wbcs.config.component.application.report.ReportBean, java.lang.Object, java.lang.String)
     */
    @Override
	public Object beforeLoadData(ReportRequest rrequest,ReportBean rbean,Object typeObj,String sql)
    {
        String rolename = Wbcs4JBSFUtil.getRolenames(rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_ROLE));
        if(rolename.indexOf("记者") != -1 && (rolename.indexOf("编辑") == -1 || rolename.indexOf("编审") == -1)){//如果是记者角色则将发布状态的新闻过滤掉
            StringBuffer tmpsqlBuf = new StringBuffer();
            if(sql.startsWith("select count(*) from (")){//数据统计语句
                tmpsqlBuf.append("select count(*) from (");
                String innersql = sql.substring(tmpsqlBuf.length(), sql.indexOf(") )  wx_tabletemp"));
                tmpsqlBuf.append(innersql).append(" and XWZT <> to_char(2)").append(") )  wx_tabletemp");
                sql = tmpsqlBuf.toString();
            }else if(sql.startsWith("SELECT * FROM(")){//数据查询语句
                tmpsqlBuf.append("SELECT * FROM(");
                String innersql = sql.substring(tmpsqlBuf.length(), sql.indexOf(")  wx_temp_tbl2"));
                tmpsqlBuf.append(innersql).append(" and XWZT <> to_char(2)").append(")  wx_temp_tbl2");
                sql = tmpsqlBuf.toString();
            }
        }
        return sql;
    }
    
    /* 数据相关的权限控制（适用于表单）
     * @see com.wbcs.system.intercept.AbsInterceptorDefaultAdapter#afterLoadData(com.wbcs.system.ReportRequest, com.wbcs.config.component.application.report.ReportBean, java.lang.Object, java.lang.Object)
     */
    @Override
	public Object afterLoadData(ReportRequest rrequest,ReportBean rbean,Object typeObj,Object dataObj)
    {
        if(typeObj instanceof EditableFormReportType)
        {
            String rolename = Wbcs4JBSFUtil.getRolenames(rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_ROLE));
            Object bsztObj = ReportAssistant.getInstance().getPropertyValue(((List)dataObj).get(0), "BSZT");
            //Object bsztObj = ReportDataAssistant.getInstance().getColValue(rrequest,rbean.getId(),"BSZT",((List)dataObj).get(0));
            //Object xwztObj = ReportDataAssistant.getInstance().getColValue(rrequest,rbean.getId(),"XWZT",((List)dataObj).get(0));
            Object xwztObj = ReportAssistant.getInstance().getPropertyValue(((List)dataObj).get(0), "XWZT");
            if("update".equals(rrequest.getStringAttribute(rbean.getId()+"_ACCESSMODE"))){
                if(rolename.indexOf("编审") != -1){
                    if("0".equals(bsztObj)){
                        rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "gobackdelCstmBtn", "display", "false");
                    }
                }
                
                if(rolename.indexOf("编辑") != -1 || rolename.indexOf("编审") != -1)
                {
                    if("2".equals(xwztObj))
                    {
                        rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "gobackCstmBtn", "display", "false");
                        rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "gobackdelCstmBtn", "display", "false");
                    }
                    
                    if("0".equals(xwztObj) || "1".equals(xwztObj))//编辑、编审角色数据为草稿或送审状态的新闻
                    {
                        rrequest.authorize(rbean.getId(), com.wbcs.util.Consts.BUTTON_PART, "revokePublishCstmBtn", "display", "false");
                    }
                }
            }
        }
        return dataObj;
    }
    
    @Override
	public int doSavePerAction(ReportRequest rrequest,ReportBean rbean,Map<String,String> mRowData,Map<String,String> mParamValues,
            AbsEditActionBean actionbean,AbsEditableReportEditDataBean editbean)
    {
        if(actionbean instanceof DeleteSqlActionBean){
            if("newslistPage".equals(rrequest.getPagebean().getId())||"themenewslistPage".equals(rrequest.getPagebean().getId()))
                return Wbcs4JBSFUtil.doSavePerAction(rrequest, rbean, mRowData, mParamValues, actionbean, editbean, "ZYXX", new String[]{"SYTP"});
            else
                return Wbcs4JBSFUtil.doSavePerAction(rrequest, rbean, mRowData, mParamValues, actionbean, editbean, "ZYXX", new String[]{"ZYLJ1"});
        }else{
        	return EditableReportAssistant.getInstance().doSavePerAction(rrequest,rbean,mRowData,mParamValues,actionbean,editbean);
        }
    }
    
    /*
     * report type=form 只适用于表单
     * 转换图集路径拼接字段为资源ID拼接字段再行保存 
     */
    @Override
	public int doSave(ReportRequest rrequest,ReportBean rbean,AbsEditableReportEditDataBean editbean)
    {
    	try{
	        Map<String, String> mcustom = rrequest.getMCustomizeEditData(rbean.getId());
	        CacheDataBean cdb=rrequest.getCdb(rbean.getId());
	        List<Map<String,String>> lstRowData=cdb.getLstEditedData(editbean);
	        JBSFDaoTemplate daoTemplate = new JBSFDaoTemplate(false);
	        if(editbean instanceof EditableReportInsertDataBean || editbean instanceof EditableReportSQLButtonDataBean ){
	            if(mcustom == null){
	               mcustom = lstRowData.get(0);
	            }
	            final String zyxxpathstr = mcustom.get("ZYXX");
	            List<Map<String, String>> lstInsertData = rrequest.getLstInsertedData(rbean.getId());
	            if(lstInsertData != null){
	                String ordertimestr = lstInsertData.get(0).get("ordertime");
	                ColBean ordertimeColBean = rbean.getDbean().getColBeanByColColumn("ordertime");
	                if(ordertimeColBean != null&&ordertimestr == null)
	                {
	                    lstInsertData.get(0).put("ordertime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	                }
	            }
	            String id = mcustom.get("ID");
	            //按照路径查询的编号和按照新闻ID查询zyxx(老数据)做交集(intersect)得到本次编辑保留的原有数据
	            /*select 
	            zylj
	            from t_resource_info
	            where id in
	            (
	                        SELECT id FROM T_RESOURCE_INFO where zylj in(select * from table (SELECT jbsfutilspkg.f_split('upload/20130903/77621378176426795.jpg,upload/20130903/37681378176424061.jpg,upload/20130903/4551378176425676.JPG,upload/20130903/6661378176426857.jpg', ',') FROM dual))
	                        intersect
	                        select * from table(select jbsfutilspkg.f_split(zyxx, ',') from t_news_details where id=1478)
	            )*/
	            //处理varchar2最大接受4000个字符的问题
	            List<String> zyxxpathlst = Wbcs4JBSFUtil.split4000(zyxxpathstr,new ArrayList<String>());
	            StringBuffer originalzyxxsqltmp = new StringBuffer("select zylj,id from t_resource_info where id in((");
	            for(int i=0;i<zyxxpathlst.size();i++)
	            {
	                originalzyxxsqltmp.append("SELECT id FROM T_RESOURCE_INFO where zylj in(select * from table (SELECT jbsfutilspkg.f_split(?,',') from dual)) ");
	                originalzyxxsqltmp.append(" union ");
	            }
	            String tmpsql = originalzyxxsqltmp.substring(0,originalzyxxsqltmp.lastIndexOf("union"));
	            StringBuffer originalzyxxsql = new StringBuffer(tmpsql);
	            originalzyxxsql.append(")");
	            originalzyxxsql.append(" intersect ");
	            originalzyxxsql.append("select * from table(select jbsfutilspkg.f_split(zyxx, ',') from t_news_details where id=?)");
	            originalzyxxsql.append(")");
	            String[] newzyxx = daoTemplate.select(originalzyxxsql.toString(),new IDataModal<String[]>(){
	                public String[] loadDatas(ResultSet rs,int ccount,ResultSetMetaData rmd,String[] dataModal) throws SQLException
	                {
	                    String[] rtnAry = new String[2];
	                    String newzyxxpathstr = zyxxpathstr;
	                    StringBuffer originalzyxxidstr = new StringBuffer("");
	                    while(rs.next()){
	                       //将查询到的未编辑的固有路径在zyxxpathstr中排除掉,剩余的为新增加路径
	                        newzyxxpathstr = newzyxxpathstr.replace(rs.getString(1),"");
	                        originalzyxxidstr = originalzyxxidstr.append(rs.getString(2)).append(",");
	                    }
	                    //0下标位置放固有的ID逗号拼接字符串
	                    rtnAry[0] = Wbcs4JBSFUtil.removeTailComma(originalzyxxidstr.toString());
	                    //1下标位置放新增资源的路径逗号拼接字符串
	                    rtnAry[1] = Wbcs4JBSFUtil.removeTailComma(newzyxxpathstr);
	                    return rtnAry;
	                }},zyxxpathlst, id);
	            //将新增加路径执行原有查询得到新增加路径对应的id,将固有id和新增加资源的id拼接起来即是新的zyxx
	            //String sql = "SELECT en_concat(id) id FROM T_RESOURCE_INFO where zylj in(select * from table (SELECT jbsfutilspkg.f_split(?, ',') FROM dual))";
	            List<String> newzyxxpathlst = new ArrayList<String>();
	            if(newzyxx[1] != null && newzyxx[1].length() > 4000){
	                newzyxxpathlst = Wbcs4JBSFUtil.split4000(zyxxpathstr,newzyxxpathlst);
	            }else{
	                newzyxxpathlst.add(newzyxx[1]);
	            }
	            StringBuffer sqlbuf = new StringBuffer("select zylj,id from t_resource_info where id in(");
	            for(int i=0;i<newzyxxpathlst.size();i++)
	            {
	                sqlbuf.append("SELECT id FROM T_RESOURCE_INFO where zylj in(select * from table (SELECT jbsfutilspkg.f_split(?, ',') FROM dual))");
	                sqlbuf.append(" union ");
	            }                               
	            sqlbuf.append("select * from table (SELECT jbsfutilspkg.f_split(?, ',') FROM dual)");
	            sqlbuf.append(")").append(Wbcs4JBSFUtil.createSqlwhereinOrder(zyxxpathstr,"zylj"));
	            String newzyxxidstr = daoTemplate.<String>select(sqlbuf.toString(),new IDataModal<String>(){
	                public String loadDatas(ResultSet rs,int ccount,ResultSetMetaData rmd,String dataModal) throws SQLException
	                {
	                    StringBuffer rtnstr = new StringBuffer();
	                    while(rs.next()){
	                        rtnstr.append(rs.getString("id")).append(",");
	                    }
	                    if(rtnstr.length() == 0)return rtnstr.toString();
	                    else return rtnstr.substring(0, rtnstr.lastIndexOf(","));
	                }
	             },newzyxxpathlst, newzyxx[0]);
	            mcustom.remove("ZYXX");
	            mcustom.put("ZYXX",newzyxxidstr);
	            //取作为索引图片的索引号，在zyxxidstr中取到id号转存之
	           /* String sytp = null;
	            String sytpindex = mcustom.get("SYTP");
	            if(sytpindex != null){
	                int sytpnum = Integer.parseInt(sytpindex);
	                sytp = zyxxidstr.split(",")[sytpnum-1];
	            }
	            mcustom.remove("SYTP");
	            mcustom.put("SYTP",sytp);*/
	            
	            rrequest.getWResponse().addOnloadMethod("loadUploader","{paths:'"+zyxxpathstr+"',ACCESSMODE:'add',savestatus:'success'}",false);
	            rrequest.getWResponse().addOnloadMethod("refreshParentComponent","{pageid: null}",false);
	        }else if(editbean instanceof EditableReportUpdateDataBean){
	            //取到SYTP字段，执行select in得到资源id的集合
	            if(mcustom != null){
	                String zyxxpathstr = mcustom.get("ZYXX");
	                List<Map<String, String>> lstUpdateParams = rrequest.getLstUpdatedParamValues(rbean);
	                String newsid = lstUpdateParams.get(0).get("newsid");
	                
	                String oldzyxxsql = "SELECT ID, ZYLJ FROM T_RESOURCE_INFO WHERE ID in(select * from table(select jbsfutilspkg.f_split(ZYXX,',') from T_NEWS_DETAILS where ID=?))";
	                Map<String, String> moldzyxx = daoTemplate.<Map<String, String>>select(oldzyxxsql,new IDataModal<Map<String, String>>(){
	                    public Map<String,String> loadDatas(ResultSet rs,int ccount,ResultSetMetaData rmd,Map<String,String> dataModal)
	                            throws SQLException
	                    {
	                        dataModal = new HashMap<String, String>();
	                        while(rs.next()){
	                            dataModal.put(rs.getString(2), rs.getString(1));
	                        }
	                        return dataModal;
	                    }},newsid);
	                
	                String[] zyxxpathary = zyxxpathstr.split(",");
	                List<String> lstaddzyxxpath = new ArrayList<String>();
	                List<String> lstoldzyxxpath = new ArrayList<String>();
	                for(int i=0;i<zyxxpathary.length;i++)
	                {
	                    if(!moldzyxx.containsKey(zyxxpathary[i].trim())){
	                        lstaddzyxxpath.add(zyxxpathary[i].trim());//原集合中没有的
	                    }else{
	                        lstoldzyxxpath.add(zyxxpathary[i].trim());//两个集合都有的
	                    }
	                }
	                String newaddzyxxstr = Wbcs4JBSFUtil.lst2dotStr(lstaddzyxxpath);
	                List<String> lstoldzyxxids = new ArrayList<String>();
	                for(String item : lstoldzyxxpath){
	                    lstoldzyxxids.add(moldzyxx.get(item));//编辑未变动的资源id
	                }
	                String oldzyxxidstr = Wbcs4JBSFUtil.lst2dotStr(lstoldzyxxids);
	                
	                String sql = "SELECT en_concat(id) id FROM T_RESOURCE_INFO where zylj in(select * from table (SELECT jbsfutilspkg.f_split(?, ',') FROM dual))";
	                String newzyxxidstr = daoTemplate.<String>select(sql,new IDataModal<String>(){
	                    public String loadDatas(ResultSet rs,int ccount,ResultSetMetaData rmd,String dataModal) throws SQLException
	                    {
	                        String rtnstr = null;
	                        while(rs.next()){
	                            rtnstr = rs.getString("id");
	                        }
	                        return rtnstr;
	                    }
	                 },newaddzyxxstr);
	                
	                //将资源id重新放入ZYXX字段中
	                String zyxxidstr = new StringBuffer(oldzyxxidstr).append(",").append(newzyxxidstr).toString();
	                mcustom.remove("ZYXX");
	                mcustom.put("ZYXX",zyxxidstr);
	                
	                //取作为索引图片的索引号，在zyxxidstr中取到id号转存之
	                String sytp = null;
	                String sytpindex = mcustom.get("SYTP");
	                if(sytpindex != null){
	                    int sytpnum = Integer.parseInt(sytpindex);
	                    sytp = zyxxidstr.split(",")[sytpnum-1];
	                }
	                mcustom.remove("SYTP");
	                mcustom.put("SYTP",sytp);
	                
	                rrequest.getWResponse().addOnloadMethod("loadUploader","{paths:'"+zyxxpathstr+"',ids:'"+zyxxidstr+"',sytp:'"+sytp+"'}",false);
	            }else{
	                rrequest.getWResponse().addOnloadMethod("loadUploader","",false);
	            }
	            rrequest.getWResponse().addOnloadMethod("refreshParentComponent","{pageid: '"+rrequest.getPagebean().getId()+"'}",false);
	        }else if(editbean instanceof EditableReportDeleteDataBean){
	            //尚不需处理
	        }
	        return super.doSave(rrequest,rbean,editbean);
    	}catch(SQLException e){
    		e.printStackTrace();
    		return IInterceptor.WX_RETURNVAL_TERMINATE;
    	}
    }
    
    @Override
	public int doSavePerRow(ReportRequest rrequest,ReportBean rbean,Map<String,String> mRowData,Map<String,String> mParamValues,
            AbsEditableReportEditDataBean editbean)
    {
        return EditableReportAssistant.getInstance().doSaveRow(rrequest,rbean,mRowData,mParamValues,editbean);
    }
}

