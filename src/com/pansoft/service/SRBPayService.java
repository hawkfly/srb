/**
 * 
 */
package com.pansoft.service;

import java.sql.SQLException;
import java.util.UUID;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.pansoft.jbsf.timer.AccountUnLockTimer;
import com.pansoft.jbsf.util.Utils;

/**
 * @author hawkfly
 */
public class SRBPayService {
	/**
	 * 支付前置动作
	 * 主要将订单编号，时间，用户插入到订单购买历史表中，进行留档
	 */
	public boolean preAction(final String cstmname, final String orderid, final String pdtid, final String createtime, final String accmoney, final String cjmoney){
		if(Utils.isNull(cstmname) || Utils.isNull(orderid) || Utils.isNull(createtime) || Utils.isNull(pdtid) ||Utils.isNull(cjmoney)||Utils.isNull(accmoney)){
			return false;
		}
		final float faccmoney = Float.parseFloat(accmoney);
		boolean isok =  Db.tx(new IAtom(){

			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				int isinsert  = Db.update("insert into SRB_ORDEREQ_HISTORY(oh_id, oh_orderid, oh_pdtid, oh_createtime, oh_status, oh_cstmname, oh_accmoney, oh_cjmoney) values(?, ?, ?, ?, ?, ?, ?, ?)", UUID.randomUUID().toString(), orderid, pdtid, createtime,"0", cstmname, accmoney, cjmoney);
				//支付前为账户加锁，账户锁定后只许进不许出
				int islock = 1;
				if(faccmoney != 0){
					islock = Db.update("update SRB_ACCOUNTINFO set acc_status = '0' where acc_username = ?", cstmname);
				}
				if(isinsert > 0 && islock > 0){
					return true;
				}else{
					return false;
				}
		}});
		//30分钟后对账户自动解锁
		AccountUnLockTimer unlockTimer = new AccountUnLockTimer();
		unlockTimer.timeVoid(cstmname, orderid);
		return isok;
		
	}
}
