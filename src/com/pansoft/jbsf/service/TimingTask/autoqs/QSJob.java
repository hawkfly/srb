/**
 * 
 */
package com.pansoft.jbsf.service.TimingTask.autoqs;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;
import com.wbcs.jbsf.util.WbcsUtils;

/**
 * 每天24时执行账户自动清算动作
 * @author hawkfly
 */
public class QSJob implements Job {

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//查询所有未结束的用户产品，包含购买时间，产品周期，购买金额，年化收益
		List<Record> lstpdcts = Db.find("select up.*, p.pt_yqnh,p.pt_id, p.pt_name, p.pt_fxfs from SRB_USER_PDCT up, SRB_PDCT p where up.upral_sfjs = 0");
		//计算当前时间，是否等于购买时间+购买周期的时间
		final String curtime = WbcsUtils.getDefaultTime();
		String curdate = "";
		if(curtime != null){
			curdate = curtime.split(" ")[0];
		}
		for (Record record : lstpdcts) {
			final String gmrq = record.getStr("upral_gmrq");
			final int tzqx = record.getInt("upral_tzqx");
			final String upralid = record.getStr("upral_id");
			final String cstmid = record.getStr("upral_cstmid");
			final String cstmname = record.getStr("upral_cstmname");
			final String realname = record.getStr("upral_cstmrealname");
			final String ptid = record.getStr("pt_id");
			final String ptname = record.getStr("pt_name");
			final String orderid = record.getStr("upral_orderid");
			
			final float cjje = record.getBigDecimal("upral_cjje").floatValue();
			final float yqnh = record.getBigDecimal("pt_yqnh").floatValue();
			
			String zqhrq = Utils.getDate(gmrq, tzqx);
			//是则执行清帐动作，否则继续检查下一条记录
			if(zqhrq.equals(curdate)){
				//清帐动作，用户订单状态标示为结束状态及结束日期，用户产品状态标示为结束状态及结束日期，向用户产品表回写收益，向账户表累加本次本金及收益
				final float sy = cjje * ((tzqx/12)*yqnh);
				
				boolean isok = Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
					public boolean run() throws SQLException {
						// TODO Auto-generated method stub
						int isupok = Db.update("update SRB_USER_PDCT set upral_sfjs = 1, upral_jsrq = ?, upral_preflsq= ?, upral_preflje = ?, upral_ljflje = ?, upral_jsyy = '正常理财周期结束！' where upral_id = ?", curtime, upralid, sy, sy, upralid);
						Record r = Db.findFirst("select acc_balance from SRB_ACCOUNTINFO where acc_user = ? for update", cstmid);
						float balanceold = r.getBigDecimal("acc_balance").floatValue();
						float balancenew = balanceold + cjje + sy;
						int isaccok = Db.update("update SRB_ACCOUNTINFO set acc_balance = ? where acc_user = ?", balancenew, cstmid);
						int isrcdok = Db.update("insert SRB_SQRECORDS(sq_id, sq_cstmid, sq_cstmname, sq_cstmrealname, sq_pdctid, sq_pdctname, sq_orderid, sq_pdctzq, sq_yqnh, sq_cjje, sq_sy, sq_time, sq_gmrq) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", UUID.randomUUID().toString(), cstmid, cstmname, realname, ptid, ptname, orderid, tzqx, yqnh, cjje, sy, curtime,gmrq);
						if(isupok > 0 && isaccok > 0 && isrcdok > 0)return true;
						else return false;
					}});
				
			}else{
				continue;
			}
		}
	}

}
