/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.util.List;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.intercept.AbsInterceptorDefaultAdapter;

/**
 * 根据角色及组织机构过去绩效数据
 * @author hawkfly
 *
 */
public class ItJxFilter extends AbsInterceptorDefaultAdapter {

	public Object beforeLoadData(ReportRequest rrequest,ReportBean rbean,Object typeObj,String sql)
    {
		//获取当前登录人角色
		//获取当前登录人组织结构编码
		//系统管理员、城市经理、综合管理主管(所有人员)、营业部长(本部门含团队经理)、团队经理(本部门不嗨营业部长)、其他(只查看本人)
		Object userid = rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_USERID);
		Object orgid = rrequest.getRequest().getSession().getAttribute(com.wbcs.jbsf.util.Consts.SESSION_ORGID);
		List<Record> lstroleids = Db.find("select ur.roleid,r.code from jbsf_user_roles ur, jbsf_roles r where ur.roleid = r.id and ur.userid = ?", userid);
		for (Record record : lstroleids) {
			String rolecode = record.getStr("code");
			if(Consts.ADMIN.indexOf(rolecode) != -1){//管理员查询所有
				return sql;
			}else if(Consts.CSJL.indexOf(rolecode) != -1){//城市经理
				return sql;
			}else if(Consts.ZHZG.indexOf(rolecode) != -1){//综合管理部主管
				return sql;
			}else if(Consts.YYBZ.indexOf(rolecode) != -1){//营业部长
				String appendsql = " and u.id in(select id from jbsf_user where orgid='"+orgid+"')";
				String rstsql = packsql(sql, appendsql);
				return rstsql;
			}else if(Consts.TDJL.indexOf(rolecode) != -1){//团队经理
				String appendsql = " and u.id in(select r.userid from jbsf_user u, (select ur.userid, ur.roleid,r.code,r.rolename from jbsf_user_roles ur, jbsf_roles r where ur.roleid = r.id) r where u.id = r.userid and u.orgid = '"+orgid+"' and r.code <> '"+Consts.YYBZ+"')";
				String rstsql = packsql(sql, appendsql);
				return rstsql;
			}else{//普通员工
				String appendsql = " and u.id = '" +userid+"'";
				String rstsql = packsql(sql, appendsql);
				return rstsql;
			}
		}
        return sql;
    }
	
	public String packsql(String orgisql,String appendsql){
		String[] sqls = Utils.splitWbcsSql(orgisql);
		String rstsql = sqls[0] + appendsql + sqls[1];
		return rstsql;
	}
	
}
