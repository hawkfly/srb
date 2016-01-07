/**
 * 
 */
package com.pansoft.jbsf.timer;

import java.util.Timer;
import java.util.TimerTask;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.service.TimingTask.orderclear.OCJob;
import com.pansoft.jbsf.util.JbsfLogUtils;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;

/**
 * 每隔30分钟执行一次账户解锁操作
 * 银联支付默认20分钟失效！
 * @author hawkfly
 */
public class AccountUnLockTimer {
	 public static void main(String[] args) { 
		 AccountUnLockTimer tTask=new AccountUnLockTimer();
         tTask.timeVoid("abcc", "order1");
     }
     
     public void timeVoid(String username, String orderid){
         Timer timer = new Timer();
         
         
         TimerTask tt=new UnlockAccountTimerTask(username, orderid, timer);
         
         
         long milliseconds = 30 * 60 * 1000; //30分钟
         timer.schedule(tt, milliseconds);
     }
     
     public class UnlockAccountTimerTask extends TimerTask{
    	private String username;
    	private Timer timer;
		private String orderid;
    	public UnlockAccountTimerTask(String username,String orderid, Timer timer){
    		this.username = username;
    		this.orderid = orderid;
    		this.timer = timer;
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Record record = Db.findFirst("select acc_status from SRB_ACCOUNTINFO where acc_username = ?", username);
			String status = record.getStr("acc_status");
			if(Consts.ACTIVI.equals(status))return;
			OCJob job = new OCJob();
			Record record2 = Db.findFirst("select oh_id, oh_orderid, oh_pdtid, oh_createtime, oh_cstmname, oh_accmoney from SRB_ORDEREQ_HISTORY where oh_orderid = ?", orderid);
			boolean isnetok = job.queryOrder(record2);
			JbsfLogUtils.info("acountunlocktimer", "30分钟后执行" + isnetok + "  " + orderid + "  " + username + "   " + Utils.getCurrentTime());
			/*if(job.queryOrder(record2)){//网络正常
				//对指定的账户执行锁定接触动作
				int r = Db.update("update SRB_ACCOUNTINFO set acc_status = '1' where acc_username = ?", username);
				if(r>0){
					JbsfLogUtils.info(Consts.UNLOCKACCOUNTSUCCESS, "30分钟后执行账户解锁成功！");
				}else{
					JbsfLogUtils.info(Consts.UNLOCKACCOUNTERROR, "30分钟后账户解锁失败，可能是用户名不正确造成的！");
				}
			}*/
			
			timer.cancel();
		}
    	 
     }
}