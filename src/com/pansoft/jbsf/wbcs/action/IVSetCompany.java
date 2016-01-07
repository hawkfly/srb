package com.pansoft.jbsf.wbcs.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;
import java.security.MessageDigest;
public class IVSetCompany implements IAction{

	/**
	 * 设置代理商
	 */
	
	String userid = "0liqinjun";
	String username = "liqinjun";
	/*String userid = req.getRequest().getSession().getAttribute("userid").toString();
	String username = req.getRequest().getSession().getAttribute("usercode").toString();*/
	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		String str = arg2.get(0).get("key");
		List list=Db.find("select * from iv_users where F_YHBH = '"+str+"' ");
		Record rec=(Record) list.get(0);
		
		String f_sh = fifteen();
		String f_dwbh = f_sh+"001";
		String f_dwmc = str+"代理公司";
		String uuid = uuid();
		
		//向用户和单位的 关系表插入的数据
		Record record_user_dw = new Record();
		record_user_dw.set("F_YHBH", rec.get("F_YHBH"));
		record_user_dw.set("F_NAME", rec.get("F_NAME"));
		record_user_dw.set("F_SH", f_sh);
		record_user_dw.set("F_DWBH", f_dwbh);
		record_user_dw.set("F_DWMC", f_dwmc);
		record_user_dw.set("F_MANA", "1");
		record_user_dw.set("F_CRDATE", nowDate());
		record_user_dw.set("F_CHDATE", nowDate());
		record_user_dw.set("F_SPRBH", userid);
		record_user_dw.set("F_SPRXM", username);
		
		//向单位字典插入的数据
		Record record_dwzd = new Record();
		record_dwzd.set("F_DWBH", f_dwbh);
		record_dwzd.set("F_DWMC", f_dwmc);
		record_dwzd.set("F_SH", f_sh);
		record_dwzd.set("F_DQBH", "0531");
		record_dwzd.set("F_JGBH", "1111");
		record_dwzd.set("F_TEL", rec.get("F_TEL"));
		record_dwzd.set("F_FR", "王某");
		record_dwzd.set("F_ADDR", "山东省济南市高新区");
		record_dwzd.set("F_START_DATE", nowDate());
		record_dwzd.set("F_SYZT", "1");
		record_dwzd.set("F_CHDATE", nowDate());
		record_dwzd.set("F_CHDATE", nowDate());
		record_dwzd.set("F_ISAGENT", "1");
		
		//向单位申请表里面添加数据
		Record record_dwsq = new Record();
		record_dwsq.set("F_SQBH", uuid);
		record_dwsq.set("F_SH", f_sh);
		record_dwsq.set("F_DWBH", f_dwbh);
		record_dwsq.set("F_DWMC", f_dwmc);
		record_dwsq.set("F_SQLX", "N");
		record_dwsq.set("F_TEL", rec.get("F_TEL"));
		record_dwsq.set("F_SQRXM", rec.get("F_NAME"));
		
		//向单位申请和用户关系表添加数据
		Record record_yhsq_dw = new Record();
		record_yhsq_dw.set("F_SQBH", uuid);
		record_yhsq_dw.set("F_SH", f_sh);
		record_yhsq_dw.set("F_DWBH", f_dwbh);
		record_yhsq_dw.set("F_DWMC", f_dwmc);
		record_yhsq_dw.set("F_SQLX", "DW");
		record_yhsq_dw.set("F_SQRBH", rec.get("F_YHBH"));
		record_yhsq_dw.set("F_SQRXM", rec.get("F_NAME"));
		record_yhsq_dw.set("F_SQSJ", nowDate());
		record_yhsq_dw.set("F_SQZT", "1");
		record_yhsq_dw.set("F_SPRBH", userid);
		record_yhsq_dw.set("F_SPRXM", username);
		record_yhsq_dw.set("F_SPSJ", nowDate());
		if("0".equals(rec.getStr("F_ISAGENT")) ){
			Db.save("iv_user_dw", record_user_dw);
			Db.save("iv_dwzd", record_dwzd);
			Db.save("iv_dwsq", record_dwsq);
			Db.save("iv_yhsq_dw", record_yhsq_dw);
			Db.update("update iv_users set F_ISAGENT ='1' where F_YHBH = '"+str+"'");
		}
		
		return null;
	}

	public String executeSeverAction(ReportRequest req,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		String userid = req.getRequest().getSession().getAttribute("userid").toString();
		String username = req.getRequest().getSession().getAttribute("usercode").toString();
		String str = arg2.get(0).get("key");
		List list=Db.find("select * from iv_users where F_YHBH = '"+str+"' ");
		Record rec=(Record) list.get(0);
		
		String f_sh = fifteen();
		String f_dwbh = f_sh+"001";
		String f_dwmc = str+"代理公司";
		String uuid = uuid();
		
		//向用户和单位的 关系表插入的数据
		Record record_user_dw = new Record();
		record_user_dw.set("F_YHBH", rec.get("F_YHBH"));
		record_user_dw.set("F_NAME", rec.get("F_NAME"));
		record_user_dw.set("F_SH", f_sh);
		record_user_dw.set("F_DWBH", f_dwbh);
		record_user_dw.set("F_DWMC", f_dwmc);
		record_user_dw.set("F_MANA", "1");
		record_user_dw.set("F_CRDATE", nowDate());
		record_user_dw.set("F_CHDATE", nowDate());
		record_user_dw.set("F_SPRBH", userid);
		record_user_dw.set("F_SPRXM", username);
		
		//向单位字典插入的数据
		Record record_dwzd = new Record();
		record_dwzd.set("F_DWBH", f_dwbh);
		record_dwzd.set("F_DWMC", f_dwmc);
		record_dwzd.set("F_SH", f_sh);
		record_dwzd.set("F_DQBH", "0531");
		record_dwzd.set("F_JGBH", "1111");
		record_dwzd.set("F_TEL", rec.get("F_TEL"));
		record_dwzd.set("F_FR", "王某");
		record_dwzd.set("F_ADDR", "山东省济南市高新区");
		record_dwzd.set("F_START_DATE", nowDate());
		record_dwzd.set("F_SYZT", "1");
		record_dwzd.set("F_CHDATE", nowDate());
		record_dwzd.set("F_CHDATE", nowDate());
		record_dwzd.set("F_ISAGENT", "1");
		
		//向单位申请表里面添加数据
		Record record_dwsq = new Record();
		record_dwsq.set("F_SQBH", uuid);
		record_dwsq.set("F_SH", f_sh);
		record_dwsq.set("F_DWBH", f_dwbh);
		record_dwsq.set("F_DWMC", f_dwmc);
		record_dwsq.set("F_SQLX", "N");
		record_dwsq.set("F_TEL", rec.get("F_TEL"));
		record_dwsq.set("F_SQRXM", rec.get("F_NAME"));
		
		//向单位申请和用户关系表添加数据
		Record record_yhsq_dw = new Record();
		record_yhsq_dw.set("F_SQBH", uuid);
		record_yhsq_dw.set("F_SH", f_sh);
		record_yhsq_dw.set("F_DWBH", f_dwbh);
		record_yhsq_dw.set("F_DWMC", f_dwmc);
		record_yhsq_dw.set("F_SQLX", "DW");
		record_yhsq_dw.set("F_SQRBH", rec.get("F_YHBH"));
		record_yhsq_dw.set("F_SQRXM", rec.get("F_NAME"));
		record_yhsq_dw.set("F_SQSJ", nowDate());
		record_yhsq_dw.set("F_SQZT", "1");
		record_yhsq_dw.set("F_SPRBH", userid);
		record_yhsq_dw.set("F_SPRXM", username);
		record_yhsq_dw.set("F_SPSJ", nowDate());
		if(rec.getStr("F_ISAGENT") == "0"){
			Db.save("iv_user_dw", record_user_dw);
			Db.save("iv_dwzd", record_dwzd);
			Db.save("iv_dwsq", record_dwsq);
			Db.save("iv_yhsq_dw", record_yhsq_dw);
			Db.update("update iv_users set F_ISAGENT ='1' where F_YHBH = '"+str+"'");
		}
		return null;
	}
	//随机生成13位的编号
	public static String fifteen(){
		String num ="";
		int a[] = new int[13];
	      for(int i=0;i<a.length;i++ ) {
	          a[i] = (int)(10*(Math.random()));
	          num+=a[i];
	      }
	   // System.out.println(num);
		return num+"DL";
	}
	//获取现在的时间
	public static String nowDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		return time;
	}
	//32位 uuid
	public static String uuid(){
		 String s = UUID.randomUUID().toString().toUpperCase(); 
	     //去掉“-”符号 
	     return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	}
	public static void main(String[] args) {
		fifteen();
		nowDate();
		System.out.println(uuid());
		
	}
}
