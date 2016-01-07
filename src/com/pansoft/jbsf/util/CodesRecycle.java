/**
 * 
 */
package com.pansoft.jbsf.util;

import java.math.BigDecimal;
import java.util.List;

import com.jf.core.ActionKey;
import com.jf.core.Controller;
import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;

/**
 * 开发过程中代码回收
 * @author hawkfly
 */
public class CodesRecycle extends Controller{

	public void proprietorsController_getpprslevelnodes(){
		String level = getPara("lv");//id
		List<Record> dataList = null;
		if("0".equals(level)){//小区--->楼房
			dataList = Db.find("select distinct louhao id, louhao||'号楼' name, 'true' isParent from t_user_info group by sq_id,louhao having sq_id=?",getPara("id"));
		}else if("1".equals(level)){//楼房--->单元    sq_id（社区编号）应该在session中取得
			dataList = Db.find("select distinct danyan id, danyan||'单元' name, 'true' isParent from t_user_info group by sq_id, louhao, danyan having sq_id = '1' and louhao=?",getPara("id"));		
		}else if("2".equals(level)){//单元--->住户
			dataList = Db.find("select distinct fanghao id, danyan unitid, louhao buildingid, louhao||'-'||danyan||'-'||fanghao name from t_user_info group by sq_id, louhao, danyan, fanghao having sq_id='1' and louhao=? and danyan=? order by name",getPara("parentid"),getPara("id"));
		}
	}
	
	@ActionKey("/jf/batchpropertypayinfos")
	public void batchpropertypayinfos(){
		Object sq_id = getSessionAttr("sq_id");
		Record baserecord = Db.findFirst("SELECT id,price,chargingregion FROM t_pay_type where sq_id=? and name='物业费'", sq_id);
		String pay_id = baserecord.get("id"); BigDecimal price = baserecord.get("price"); BigDecimal chargingregion = baserecord.get("chargingregion");
		int updatecount = Db.update("insert into t_pay_info(id, yz_id, pay_id, plan_date, start_date, end_date, plan_pay, real_date, real_pay, pay_type) SELECT seq_sqwy.nextval id, fix.yz_id, fix.pay_id, fix.plan_date, fix.start_date, fix.end_date, ?*mianji*? plan_total, null real_date, '0' real_pay,'0' pay_type FROM (SELECT id yz_id,? pay_id, mianji, (select max(end_date)+1 from t_pay_info p, t_user_info u where p.yz_id=u.id and u.sq_id=? and p.pay_id=?) plan_date, (select max(end_date)+1 from t_pay_info p, t_user_info u where p.yz_id=u.id and u.sq_id=? and p.pay_id=?) start_date, add_months((select max(end_date)+1 from t_pay_info p, t_user_info u where p.yz_id=u.id and u.sq_id=? and p.pay_id=?), ?) end_date FROM t_user_info where sq_id=?)fix", chargingregion, price, pay_id, sq_id, pay_id, sq_id, pay_id, sq_id, pay_id, chargingregion, sq_id);
		renderJson(updatecount);
	}
}
