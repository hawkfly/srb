/**
 * 
 */
package com.pansoft.jbsf.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jf.aop.Before;
import com.jf.core.Controller;
import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.jf.plugin.activerecord.tx.Tx;
import com.pansoft.jbsf.bean.AccountInfo;
import com.pansoft.jbsf.bean.bankcardinfo;
import com.pansoft.jbsf.bean.zjmx;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;

/**
 * @author hawkfly
 */
public class RegisterController extends Controller {
/*	public void index(){
	String rtnMsg = "0";
		int r = 0;
		String username = getPara("username"), phone = getPara("phone"), tjrPhone = getPara("tjrPhone");
		String yhname = getPara("bankcityname");
		if(yhname != null){
			r = Db.update("insert into SRB_YHKINFO(id, yh_name) values(?,?)", UUID.randomUUID().toString(), yhname);
		}
		if(username != null){
			List<Record> lst = Db.find("select userid, username, realname from jbsf_user where phone = ?", tjrPhone);
			Record record = lst.get(0);
			r = Db.update("insert into SRB_CUSTOMER(id, cstm_name, cstm_phone, cstm_gx_gwid, cstm_gx_gwname, cstm_gx_gwrealname) values(?,?,?,?,?,?)",UUID.randomUUID().toString(), username, phone, record.get("userid"), record.get("username"), record.get("realname"));
		}
		if(r > 0){
			rtnMsg = "1";
		}
		
		renderJson(rtnMsg);
	}*/
   //用户注册,查询用户名、手机号是否已经存在	
	public void registerquery(){
		String rtnMsg="0";
		String username_n="0";
		String tel_n="0";

		String username= getPara("usernamekey");
		String userphone=getPara("userphonekey");
		String passwd=getPara("passwdkey");
		
		//用户名是否唯一
		List<Record> ust=Db.find("select * from SRB_CUSTOMER where cstm_name=?",username);
		List<Record> tst=Db.find("select * from SRB_CUSTOMER where cstm_phone=?",userphone);
		if(ust.size()!=0)
		{
			rtnMsg="1";//用户名已存在，返回1
			
		}
		else if(tst.size()!=0)
		{
			rtnMsg="2";
			//用户手机号已存在，返回2
		}
		System.out.println("msg:"+rtnMsg);
		renderJson(rtnMsg);	
	}
	
	  //用户注册
		@Before(Tx.class)
		public void register(){
			String rtnMsg="0";
			String username_n="0";
			String tel_n="0";
			int n=0;//插入custromer表成功，变为1
			int m=0;//插入accout表成功，变为1
			String yzm="";
			String username= getPara("usernamekey");
			String userphone=getPara("userphonekey");
			String passwd=getPara("passwdkey");
			String tjrphone=getPara("recommendkey");
			String yanzm=getPara("yanzmkey");
			System.out.println("yanzm:"+yanzm);
			System.out.println("usernamekey:"+username);System.out.println("userphonekey:"+userphone);System.out.println("passwd:"+passwd);
			   List<Record> ylst=Db.find("select * from SRB_YZM where usertel=?",userphone);
			   if(ylst.size()!=0)
			   {
				   yzm=ylst.get(0).getStr("yanzm");
			   }
			    
			    System.out.println("yzm:"+yzm);
			    if(yzm.equals(yanzm))//验证码输入正确
			    {
			    	String userid=UUID.randomUUID().toString();
					String mydate=Utils.getDefaultTime();
			    	//插入账户表
					m=Db.update("insert into SRB_ACCOUNTINFO(acc_id,acc_user,acc_username,acc_balance,acc_status,acc_opentime) values(?,?,?,?,?,?)",UUID.randomUUID().toString(),userid,username,0,1,mydate);
					List<Record> alst=Db.find("select * from SRB_ACCOUNTINFO where acc_username=?",username);System.out.println("更新账户表"+m);
					String accid=alst.get(0).getStr("acc_id");System.out.println("accid:"+accid);
					//插入顾客表
					List<Record> lst=Db.find("select id,username,realname from jbsf_user where phone= ?",tjrphone );//查询推荐人手机号是否存在
					if(lst.size() !=0)//如果推荐人手机号存在,将推荐人信息一块插入
					{   System.out.println("推荐人存在:");
						Record record=lst.get(0);
						n=Db.update("insert into SRB_CUSTOMER(cstm_id,cstm_name,cstm_password,cstm_phone,cstm_gx_gwid,cstm_gx_gwname,cstm_gx_gwrealname,cstm_accountid) values(?,?,?,?,?,?,?,?)",userid,username,passwd,userphone,record.get("id"),record.get("username"),record.get("realname"),accid);				
						System.out.println("插入账户表是否成功"+n);
					}
					else
					{//如果推荐人手机号不存在，则推荐人信息为空
						System.out.println("推荐人手机号不存在:");
						n=Db.update("insert into SRB_CUSTOMER(cstm_id,cstm_name,cstm_password,cstm_phone,cstm_accountid) values(?,?,?,?,?)", userid,username,passwd,userphone,accid);
						System.out.println("插入用户表是否成功:"+n);
					}										
					if(n>0 && m>0)
					{	rtnMsg="1";}//注册成功，返回1					
			    }
			    else
			    {
			    	if("".equals(yzm))
			    	{
			    		rtnMsg="3";//当验证码表里此手机号的验证码为空，说明用户没有获取验证码
			    	}
			    	else
			    	{
			    		rtnMsg="2";//当验证码输入不正确，返回2；
			    	}
			    	
			    }						 
			    System.out.println("rtnmsg:"+rtnMsg);
				renderJson(rtnMsg);
		
		}
	//登录
	public void login(){
		String m="0";
		String username=getPara("user_name"),password=getPara("pass_word");
		List<Record> ust=Db.find("select * from SRB_CUSTOMER where cstm_name = ? and cstm_password= ?",username,password);
		if(ust.size()!=0)
		{
			m="1";
		}
		renderJson(m);
	}
	//轮播图片显示
	public void lbpicture(){
		List<Record> pst=Db.find("select * from SRB_AD order by ad_fbxh desc");
		System.out.print(pst);
		renderJson(pst);
	}
	
	//显示推荐产品列表：选择已推荐的并且是否发布为1的产品
	public void querytjPtlist(){
		
		List<Record> plst=Db.find("select pt_id,pt_name,pt_yqnh,pt_tzqx,pt_xmgm,pt_ytbl,pt_ktje,pt_icon,pt_sfwb from SRB_PDCT where  pt_sffb=? order by pt_sftj desc, pt_xh desc,pt_fbsj desc,pt_sfwb asc, pt_ytbl desc limit 2","1");
    	int j=1;
    	List<Record> lst =  new ArrayList<Record>();
		for(int i=0;i<plst.size();i++)
    	{
			if(j++==3)
			 break;
    		Record pt=plst.get(i); 
    		lst.add(pt);
    	}
		renderJson(lst);
		
	}
	//显示所有产品列表：选择是否发布为1的产品
		public void queryPtlist(){
			
			List<Record> plst=Db.find("select pt_id,pt_name,pt_yqnh,pt_tzqx,pt_xmgm,pt_ytbl,pt_ktje,pt_icon,pt_sfwb from SRB_PDCT where pt_sffb=? order by pt_sfwb asc,pt_xh desc,pt_fbsj desc,pt_ytbl desc","1");
	    	renderJson(plst);
			
		}
	//产品详情页面
	public void queryPtinfo(){
		String id=getPara("ptid");	
		List<Record> ptlst=Db.find("select * from SRB_PDCT  where pt_id=?",id);
		renderJson(ptlst);
	}
	//购买产品时先判断此产品此用户是否已经购买
	public void whept(){
		String id=getPara("ptid");
		String username=getPara("username");
		String msg="0";
		List<Record> polst=Db.find("select * from SRB_USER_PDCT where upral_pdctid=? and upral_cstmname=?",id,username);
        if(polst.size()!=0)
        {
        	msg="1";//说明此用户已经购买过此产品
        	
        }
        renderJson(msg);
	}
	 /**
	  * 购买产品 
	  * 此方法不会被外部网址调用，只会在执行银联付款成功后回写的时候调用，所以将其改造成普通方法，供外部JAVA代码调用
	  * 一发起支付申请，就先把账户余额冻结，等支付完成后再解除锁定状态
	  */
	public boolean getPtinfo(final String orderid, final String id){
		
		return Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
			public boolean run() throws SQLException{
				/*String id=getPara("ptid");
				
				String orderid=getPara("orderid");//获取银联付款成功后的订单号
				*/
				String cjsj=Utils.getCurrentTime();//成交时间
				String mydate=Utils.getDefaultTime();				
				int m=0,n=0,l=0,s=0,p=0,t=0;
				float ktje=0,ytbl=0,cjje=0,xmgm=0;//可投金额，已投比例,投资金额
				float nktje=0,nytbl=0;//修改以后的可投金额、已投比例
				boolean isok = false;
				float newbalance=0;
				//通过查询SRB_ORDEREQ_HISTORY表得到用户名
				String username = Db.findFirst("select oh_cstmname from SRB_ORDEREQ_HISTORY where oh_orderid = ?", orderid).getStr("oh_cstmname");
				//通过查询SRB_ORDEREQ_HISTORY表得到扣除的账户金额
				float kcje = Db.findFirst("select oh_accmoney from SRB_ORDEREQ_HISTORY where oh_orderid = ?", orderid).getBigDecimal("oh_accmoney").floatValue();
				//通过查询SRB_ORDEREQ_HISTORY表得到投资金额
				cjje = Db.findFirst("select oh_cjmoney from SRB_ORDEREQ_HISTORY where oh_orderid = ?", orderid).getBigDecimal("oh_cjmoney").floatValue();
								
				//查询产品信息表
				List<Record> ptlst=Db.find("select pt_name,pt_yqnh,pt_tzqx,pt_xmgm,pt_shfs,pt_qtje,pt_ytbl,pt_ktje,pt_hkfs,pt_shjz,pt_xmxq,pt_qixx,pt_bzcs,pt_icon from SRB_PDCT  where pt_id=?",id);
				Record pst=ptlst.get(0);
				ktje=pst.getBigDecimal("pt_ktje").floatValue();
				ytbl=pst.getBigDecimal("pt_ytbl").floatValue();
				xmgm=pst.getBigDecimal("pt_xmgm").floatValue();
				//插入投资数据
				String ddid=UUID.randomUUID().toString();//订单ID
				String coraltype="in";
				String iswb="0";//产品是否完毕字段
				String wbsj="";//完毕时间字段，默认为空，如果产品已经完毕，获取当前时间
				nktje=ktje-cjje ;//新的可投金额的值变为：原可投金额-本次投资金额
				nytbl=(xmgm*ytbl+cjje)/xmgm;//计算新的已投比例
				if(nktje==0)
				{
					iswb="1";
					wbsj=Utils.getDefaultTime();
				}
					
				//查询账户余额
				List<Record> acclst=Db.find("select acc_balance from SRB_ACCOUNTINFO where acc_username=?",username);
				Record acc=acclst.get(0);
				float oldbalance=acc.getBigDecimal("acc_balance").floatValue();	
				if(oldbalance>=kcje)
				{
					newbalance=oldbalance-kcje;//将账户余额去掉购买产品扣掉的金额
					m=Db.update("insert into SRB_ORDER_PDCTS values(?,?,?,?,?)", UUID.randomUUID().toString(),ddid,id,mydate,cjje);//插入产品订单表
					List<Record> ulst=Db.find("select cstm_id,cstm_name,cstm_realname from SRB_CUSTOMER where cstm_name=?", username);
					Record ptrecord=ptlst.get(0);
					if(ulst.size()!=0)
					{
					     Record record=ulst.get(0);//插入用户订单表
					     n=Db.update("insert into SRB_USER_ORDER(coral_id,coral_cstmid,coral_cstmname,coral_cstmrealname,coral_orderid,coral_okdatetime,coral_type,coral_cjje,coral_realdatetime) values(?,?,?,?,?,?,?,?,?)",
					     UUID.randomUUID().toString(),record.get("cstm_id"),record.get("cstm_name"),record.get("cstm_realname"),ddid,mydate,coraltype,cjje,mydate);
				         //插入用户产品表
						 l=Db.update("insert into SRB_USER_PDCT(upral_id,upral_pdctid,upral_name,upral_cstmid,upral_cstmname,upral_cstmrealname,upral_gmrq,upral_cjje,upral_yscjje,upral_tzqx,upral_year,upral_month,upral_orderid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				         UUID.randomUUID().toString(),id,ptrecord.get("pt_name"),record.get("cstm_id"),record.get("cstm_name"),record.get("cstm_realname"),mydate,cjje,cjje,ptrecord.get("pt_tzqx"),Utils.getYear(),Utils.getMonth(),ddid);
					     //修改产品表已投比例、可投 金额、是否完毕，完毕时间字段，如果新的可投金额变为0，是否完毕字段变为1，插入完毕时间为当前时间，否则是否完毕字段为0，完毕时间为""
						 s=Db.update("update SRB_PDCT set pt_ytbl=? ,pt_ktje=?,pt_sfwb=?,pt_wbsj=? where pt_id=?",nytbl,nktje,iswb,wbsj,id);						 
						//插入订单历史表
						 p=Db.update("update SRB_ORDEREQ_HISTORY set oh_status=? ,oh_cjtime=? where oh_orderid=?","1",cjsj,orderid);
						
																			 
						 if(kcje==0)//如果扣除的原有账户余额为0，不更新账户表和插入账户出账表
						 {
							 t=1;
							 p=1;
						 }
						 else
						 {  
							//修改账户表余额、解除冻结状态
							 p=Db.update("update SRB_ACCOUNTINFO set acc_balance=?,acc_status=? where acc_username=?",newbalance,"1",username);
							 //插入账户支付表 
							 t=Db.update("insert into SRB_PAYRECORDS(prd_id,prd_cstmname,prd_cstmrealname,prd_cstmid,prd_paymoney,prd_datetime,prd_type,prd_orderid,prd_pdtid) values(?,?,?,?,?,?,?,?,?)",UUID.randomUUID().toString(),record.get("cstm_name"),record.get("cstm_realname"),record.get("cstm_id"),kcje,mydate,"1",ddid,id);
						 }
						 
						 if(m>0 && n>0 && l>0 && s>0 && p>0 && t>0)
					     {
					    	 
					    	 isok = true;
					     }
					}
				}
  				return isok;   				
    			  }				
			});
		
		/*renderJson(inmsg);*/
	}
	
	//使用余额进行购买产品，余额足够，则先冻结账户资金，再往投资的几个表里插入数据
		@Before(Tx.class)
		public void buybybalance(){
			String inmsg="0";
			boolean isSuccess = Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
				public boolean run() throws SQLException{
					int m=0,n=0,l=0,s=0,p=0,t=0;
					int r = 0,q=0;
					float ktje=0,ytbl=0,cjje=0,xmgm=0;//可投金额，已投比例,投资金额
					float nktje=0,nytbl=0;//修改以后的可投金额、已投比例
					boolean isok = false; 
					String id=getPara("ptid");
					String username=getPara("username");
					String mydate=Utils.getDefaultTime();	
					String amout=getPara("amout");//投资金额
					cjje=Float.parseFloat(amout);//转换成float类型
					
				//查询产品信息表
					List<Record> ptlst=Db.find("select pt_name,pt_yqnh,pt_tzqx,pt_xmgm,pt_shfs,pt_qtje,pt_ytbl,pt_ktje,pt_hkfs,pt_shjz,pt_xmxq,pt_qixx,pt_bzcs,pt_icon from SRB_PDCT  where pt_id=?",id);
					Record pst=ptlst.get(0);
					ktje=pst.getBigDecimal("pt_ktje").floatValue();
					ytbl=pst.getBigDecimal("pt_ytbl").floatValue();
					xmgm=pst.getBigDecimal("pt_xmgm").floatValue();
					//插入投资数据
					String ddid=UUID.randomUUID().toString();//订单ID
					String coraltype="in";
					String iswb="0";//产品是否完毕字段
					String wbsj="";//完毕时间字段，默认为空，如果产品已经完毕，获取当前时间
					nktje=ktje-cjje;//新的可投金额的值变为：原可投金额-本次投资金额
					nytbl=(xmgm*ytbl+cjje)/xmgm;//计算新的已投比例
					if(nktje==0)
					{
						iswb="1";
						wbsj=Utils.getDefaultTime();
					}
					System.out.println("ktje"+ktje);
					System.out.println("ytbl"+ytbl);
					System.out.println("amout:"+cjje);
					System.out.println("nktje:"+nktje);
					System.out.println("nytbl"+nytbl);
					if(amout!=null || amout!="")
					{
						//查询账户余额
						List<Record> acclst=Db.find("select acc_balance from SRB_ACCOUNTINFO where acc_username=?",username);
						Record acc=acclst.get(0);
						float oldbalance=acc.getBigDecimal("acc_balance").floatValue();
						if(oldbalance>=cjje)
						{
							float newbalance=oldbalance-cjje;//将账户余额去掉购买产品扣掉的金额
							r=Db.update("update SRB_ACCOUNTINFO set acc_status=?,acc_locktime=? where acc_username=?","0",mydate,username);//冻结余额
							m=Db.update("insert into SRB_ORDER_PDCTS values(?,?,?,?,?)", UUID.randomUUID().toString(),ddid,id,mydate,cjje);//插入产品订单表
							List<Record> ulst=Db.find("select cstm_id,cstm_name,cstm_realname from SRB_CUSTOMER where cstm_name=?", username);
							Record ptrecord=ptlst.get(0);
							if(ulst.size()!=0)
							{
							     Record record=ulst.get(0);//插入用户订单表
							     n=Db.update("insert into SRB_USER_ORDER(coral_id,coral_cstmid,coral_cstmname,coral_cstmrealname,coral_orderid,coral_okdatetime,coral_type,coral_cjje,coral_realdatetime) values(?,?,?,?,?,?,?,?,?)",
							     UUID.randomUUID().toString(),record.get("cstm_id"),record.get("cstm_name"),record.get("cstm_realname"),ddid,mydate,coraltype,cjje,mydate);
						         //插入用户产品表
								 l=Db.update("insert into SRB_USER_PDCT(upral_id,upral_pdctid,upral_name,upral_cstmid,upral_cstmname,upral_cstmrealname,upral_gmrq,upral_cjje,upral_yscjje,upral_tzqx,upral_year,upral_month,upral_orderid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
						         UUID.randomUUID().toString(),id,ptrecord.get("pt_name"),record.get("cstm_id"),record.get("cstm_name"),record.get("cstm_realname"),mydate,cjje,cjje,ptrecord.get("pt_tzqx"),Utils.getYear(),Utils.getMonth(),ddid);
								//修改产品表已投比例、可投 金额、是否完毕，完毕时间字段，如果新的可投金额变为0，是否完毕字段变为1，插入完毕时间为当前时间，否则是否完毕字段为0，完毕时间为""
								 s=Db.update("update SRB_PDCT set pt_ytbl=? ,pt_ktje=?,pt_sfwb=?,pt_wbsj=? where pt_id=?",nytbl,nktje,iswb,wbsj,id);						 
								 //修改账户表余额、解除资金冻结状态
								 p=Db.update("update SRB_ACCOUNTINFO set acc_balance=?,acc_status=? where acc_username=?",newbalance,"1",username);
								 //插入账户支付表
								 t=Db.update("insert into SRB_PAYRECORDS(prd_id,prd_cstmname,prd_cstmrealname,prd_cstmid,prd_paymoney,prd_datetime,prd_type,prd_orderid,prd_pdtid) values(?,?,?,?,?,?,?,?,?)",UUID.randomUUID().toString(),record.get("cstm_name"),record.get("cstm_realname"),record.get("cstm_id"),cjje,mydate,"1",ddid,id);
								 if(m>0 && n>0 && l>0 && s>0 && p>0 && t>0)
							     {
							    	 
							    	 isok = true;
							     }
							}
						}
					
					}
	    				return isok;   				
	    			  }				
				});
			if(isSuccess)inmsg = "1";
			
			renderJson(inmsg);
		}
	//获取账户余额
	public void getaccbanlance(){
		float accbalance=0;
		String username=getPara("username");
		List<Record> balance=Db.find("select acc_balance from SRB_ACCOUNTINFO where acc_username=?", username);
		if(balance.size()!=0)
		{
			Record accamout=balance.get(0);
			accbalance=accamout.getBigDecimal("acc_balance").floatValue();		
		}
		renderJson(accbalance);
	}
	//获取账户表余额、状态
	public void getye(){
		String username=getPara("username");
		List<Record> balance=Db.find("select acc_balance, acc_status from SRB_ACCOUNTINFO where acc_username=?", username);
		
		renderJson(balance);
	}
	//我的交易页面
	public void queryMytrans(){
		String username=getPara("username");
		float accbalance=0;
		float ljfl=0;
		String realname="";
		//获取用户真实姓名
		List<Record> rlst=Db.find("select cstm_realname from SRB_CUSTOMER where cstm_name =?",username);
		if(rlst.size()!=0)
		{
			Record real1=rlst.get(0);
			realname=real1.get("cstm_realname");
		}
		//获取账户余额
		List<Record> balance=Db.find("select acc_balance from SRB_ACCOUNTINFO where acc_username=?", username);
		if(balance.size()!=0)
		{
			Record accamout=balance.get(0);
			accbalance=accamout.getBigDecimal("acc_balance").floatValue();
		
		}
		//获取累计返利金额:用户产品表中此用户下所有产品的累计返利金额的和
		List<Record> ljflje=Db.find("select sum(upral_ljflje) as ljflje from SRB_USER_PDCT group by upral_cstmname having upral_cstmname=?",username);
		if(ljflje.size()!=0)
		{
			Record lj=ljflje.get(0);
		    ljfl=lj.getBigDecimal("ljflje").floatValue();		
		}
		//获取待收本息：没结束的购买时的预期收益-已返利金额
		List<Record> dlst=Db.find("select p.pt_yqnh ,up.upral_pdctid,up.upral_cjje,up.upral_tzqx,upral_ljflje from SRB_USER_PDCT up,SRB_PDCT p where up.upral_pdctid = p.pt_id and upral_cstmname=? and upral_sfjs=?",username,"0");
		float dsbx = 0;
		for( int i=0;i<dlst.size();i++)
		{
			Record r = dlst.get(i);
			float yqnh = r.getBigDecimal("pt_yqnh").floatValue();
			float cjje = r.getBigDecimal("upral_cjje").floatValue();
			int tzqx = r.getInt("upral_tzqx");
			float ljfanl=r.getBigDecimal("upral_ljflje").floatValue();//累计返利金额
			float pdtsy = cjje * ((yqnh/12) * tzqx);//总的预期收益
			float dsbx1=pdtsy-ljfanl;//总的预期收益-累计返利金额=待收利息
			dsbx = dsbx + dsbx1+cjje;//代收本息
		}
		AccountInfo accountinfo = new AccountInfo();
		accountinfo.setRealname(realname);accountinfo.setDsbx(dsbx);
		accountinfo.setZhye(accbalance); accountinfo.setLjsy(ljfl);
		renderJson(accountinfo);
	}
	//绑定银行卡页面
	public void addbank(){
		String username=getPara("username");
		String yhh=getPara("yhcode");
		String ssyh=getPara("ssyh");
		String yhkh=getPara("yhkh");
		String khcs=getPara("khcs");
		String khyh=getPara("khyh");
		String ckr=getPara("ckr");
		int status=0;
		String sjh=getPara("sjh");
		int n=0;
		String msg="0";
		List<Record> blst=Db.find("select * from SRB_YHKINFO where yh_cstmname=? and yh_status=?",username,1);//查询是否已经有默认卡，有默认卡，则插入的新卡为非默认卡，否则，插入的卡为默认卡
		if(blst.size()!=0)
		{
			status=0;
		}
		else
		{
			status=1;
		}
		
        List<Record> ulst=Db.find("select cstm_id,cstm_realname from SRB_CUSTOMER where cstm_name=?",username);
        Record user=ulst.get(0);  
		
        n=Db.update("insert into SRB_YHKINFO(id,yh_code,yh_name,yh_city,yh_khh,yh_kh,yh_ylphone,yh_cstmid,yh_cstmname,yh_cstmrealname,yh_status) values(?,?,?,?,?,?,?,?,?,?,?)",
		  UUID.randomUUID().toString(),yhh,ssyh,khcs,khyh,yhkh,sjh,user.get("cstm_id"),username,ckr,status);
		if(n>0)
		{
			msg="1";
		}
		renderJson(msg);
			
	}
	//查询银行卡信息,如果存在返回1
	public void querybankinfo(){
		String username=getPara("username");
		
		String msg="0";
			List<Record> blst=Db.find("select * from SRB_YHKINFO where yh_cstmname=?",username);
			if(blst.size()>0)
			{
	            msg="1";
			}
		
		renderJson(msg);
  }
	//按照银行卡号查询银行信息表，如果此银行卡号已存在，不允许用户绑定此银行卡，返回1
	public void querybankinfobyno(){
		String bankno=getPara("bankno");
		String msg="0";
		
			List<Record> nlst=Db.find("select * from SRB_YHKINFO where yh_kh=?",bankno);
			if(nlst.size()!=0)
				{msg="1";}
			renderJson(msg);
		
  }
	//查询出用户所有的银行卡
	public void getbankinfo(){
		String username=getPara("username");
		List<Record> banklst=Db.find("select * from SRB_YHKINFO where yh_cstmname=? order by yh_status desc",username);
		
			renderJson(banklst);
	}
	//查询某个银行卡的详细信息，银行卡详情页面
	public void getbankdetail(){
		String id=getPara("bankid");System.out.println("id:"+id);
		List<Record> bklst=Db.find("select * from SRB_YHKINFO where id=?",id);
		renderJson(bklst);
		
	}
	//删除银行卡信息
	public void deletebankinfo(){
		String yhkid=getPara("bankid");
		int m=0;
		String rmsg="0";
		m=Db.update("delete from SRB_YHKINFO where id=?",yhkid);
		if(m>0)
		{
			rmsg="1";			
		}
		renderJson(rmsg);
	}
	//设为默认银行卡
	@Before(Tx.class)
	public void setbankdefault(){
		String yhkid=getPara("bankid");
		String username=getPara("username");System.out.println(username);
		int m=0;
		int n=0;
		String rmsg="0";
		m=Db.update("update SRB_YHKINFO set yh_status=? where yh_status=? and yh_cstmname=?",0,1,username);//将原来的默认卡状态变为0；
		n=Db.update("update SRB_YHKINFO set yh_status=? where id=?",1,yhkid);//设为默认卡
		if(n>0)
		{
			rmsg="1";
		}
		renderJson(rmsg);
	}
	//获取客户表信息
	public void getcustinfo(){
		String username=getPara("username");		
		List<Record> cuslst=Db.find("select * from SRB_CUSTOMER where cstm_name=?",username);
		renderJson(cuslst);
        
	}
	//实名认证页面
	public void smrz(){
		String username=getPara("username");
		String realname=getPara("realname");
		String sfzh=getPara("sfzh");
		String sex=getPara("sex");
		int n=0;
		String rtmsg="0";
		n=Db.update("update SRB_CUSTOMER set cstm_realname=? ,cstm_identity=? ,cstm_sex=? where cstm_name=?",realname,sfzh,sex,username);
		if(n!=0)
		{
			rtmsg="1";
		}
		renderJson(rtmsg);
	}
	//修改客户手机号
	public void modtel(){
		String username=getPara("username");
		String newtel=getPara("newtel");		
		int n=0;
		String rtmsg="0";
		n=Db.update("update SRB_CUSTOMER set cstm_phone=? where cstm_name=?",newtel,username);
		if(n!=0)
		{
			rtmsg="1";
		}
		renderJson(rtmsg);
	}
	//修改支付密码
	public void mdzfmm(){
		String username=getPara("username");
		String newmm=getPara("newmm");
		int n=0;
		String rtmsg="0";
		n=Db.update("update SRB_ACCOUNTINFO set acc_password=? where acc_username=?",newmm,username);
		if(n!=0)
		{
			rtmsg="1";
		}
		renderJson(rtmsg);
	}
	
	
	//提现页面信息显示
	public void getmymoney(){
		String username=getPara("username");
		String msg="0";
		float accbalance=0;
		List<Record> mlst=Db.find("select * from SRB_YHKINFO where yh_cstmname=?",username);//判断是否绑定银行卡，如果已绑定，返回1，否则0；
		if(mlst.size()!=0)
		{
			msg="1";
		}
		
		List<Record> banklst=Db.find("select acc_balance,acc_status,yh_name,yh_kh,yh_cstmrealname,yh_ylphone from SRB_YHKINFO,SRB_ACCOUNTINFO where SRB_YHKINFO.yh_cstmname=SRB_ACCOUNTINFO.acc_username and acc_username=? and yh_status=?",username,1);
        
    	Record bankinfo=banklst.get(0);
    	accbalance=bankinfo.getBigDecimal("acc_balance").floatValue(); 
    	String accstatus=bankinfo.getStr("acc_status");
    	String bankname=bankinfo.getStr("yh_name");
    	String bankno=bankinfo.getStr("yh_kh");
    	String realname=bankinfo.getStr("yh_cstmrealname");
    	String tel=bankinfo.getStr("yh_ylphone");  
        
    	bankcardinfo bank=new bankcardinfo();
        bank.setBalance(accbalance);
        bank.setAccstatus(accstatus);
        bank.setBankname(bankname);
        bank.setBankno(bankno);
        bank.setRealname(realname);
        bank.setTel(tel);
        
        renderJson(bank);
	}
	//取现页面，获取支付密码，返回前台
	public void getamout(){
		String username=getPara("username");//用户名
    	String zfmm;//支付密码
		List<Record> alst=Db.find("select acc_balance,acc_password from SRB_ACCOUNTINFO where acc_username=?",username);//从账户表查询支付密码
		Record m=alst.get(0);
		zfmm=m.get("acc_password");System.out.println("zfmm"+zfmm);
		renderJson(zfmm);
	}
	//支付密码输入正确，则执行取现操作
	@Before(Tx.class)
	public void quxian(){
		String username=getPara("username");//用户名
		String h=getPara("txje");//提现金额
		float txje=Float.parseFloat(h);
		String rmsg="0";
		String zfmmyes=getPara("zfmmyes"); 
        String mydate=Utils.getDefaultTime();
        
        List<Record> ulst=Db.find("select cstm_id,cstm_realname from SRB_CUSTOMER where cstm_name=?",username);
        Record user=ulst.get(0); 
        List<Record> alst=Db.find("select acc_balance,acc_password from SRB_ACCOUNTINFO where acc_username=?",username);//从账户表查询支付密码
		Record m=alst.get(0);
        if("1".equals(zfmmyes))//如果前台传入的值为1，说明支付密码输入正确，则执行取现操作
        {       	      	
        		List<Record> banklst=Db.find("select * from SRB_YHKINFO where yh_cstmname=?",username);
            	Record bankinfo=banklst.get(0);
            	//插入取现申请表
        		int acccount=Db.update("insert into SRB_QXSQ(qx_id,qx_cstmid,qx_username,qx_realname,qx_je,qx_sqsj,qx_yhmc,qx_yhk,qx_khh) values(?,?,?,?,?,?,?,?,?)",
        				UUID.randomUUID().toString(),user.get("cstm_id"),username,user.get("cstm_realname"),txje,mydate,bankinfo.get("yh_name"),bankinfo.get("yh_kh"),bankinfo.get("yh_khh"));
        		if(acccount!=0)
        		{    //插入取现记录表
        			int tradcount=Db.update("insert into SRB_PAYRECORDS(prd_id,prd_cstmname,prd_cstmrealname,prd_cstmid,prd_paymoney,prd_datetime,prd_type) values(?,?,?,?,?,?,?)",
        					      UUID.randomUUID().toString(),username,user.get("cstm_realname"),user.get("cstm_id"),txje,mydate,"0");
        			int n=Db.update("update SRB_ACCOUNTINFO set acc_status=?,acc_locktime=? where acc_username=?","0",mydate,username);//锁定账号余额
        			if(tradcount>0 && n>0)
        			{
        				rmsg="1";//如果插入成功，传1
        			}
        		}	
        	        	
        }
       renderJson(rmsg);
	}
	public void qxlist(){
		String username=getPara("username");
		String rtmsg="0";
		//根据用户名取出此用户的用户id，真实姓名
		List<Record> userlst=Db.find("select cstm_id,cstm_realname from SRB_CUSTOMER where cstm_name=?",username);		
		String userid=userlst.get(0).getStr("cstm_id");
		String realname=userlst.get(0).getStr("cstm_realname");
		String ptname="";
		List<Record> txlst=Db.find("select * from SRB_PAYRECORDS where prd_cstmname=? order by prd_datetime desc" ,username);
		
		
		if(txlst.size()!=0)
		{			
				for(Record record:txlst)
				{
					String qxtype=record.getStr("prd_type");
					String ptid=record.getStr("prd_pdtid");
					if("1".equals(qxtype))
					{
						List<Record> ptlst=Db.find("select pt_icon from SRB_PDCT where pt_id=?",ptid);
						if(ptlst.size()!=0)
						{
							ptname=ptlst.get(0).getStr("pt_icon");System.out.println("ptname:"+ptname);
							record.remove("prd_pdtid");
							record.set("prd_pdtid", ptname);
						}																		
					}
					
				}
				renderJson(txlst);								
		}
		else
		{
			renderJson(rtmsg);
		}						
	}
	//查询我的投资列表
	public void querytzinfo(){
		String username=getPara("username");System.out.println("username:"+username);
		String msg="0";//查询出在用户产品表中upral_sfjs为0的值
		List<Record> tzlst=Db.find("select pt_icon,upral_orderid,upral_pdctid,pt_tzqx,pt_yqnh,upral_gmrq,upral_jsrq,upral_ljflje,upral_cjje,upral_jsyy from SRB_USER_PDCT uorder,SRB_PDCT pt where uorder.upral_pdctid=pt_id and upral_cstmname=? and upral_sfjs=? order by upral_gmrq desc",username,"0");
		if(tzlst.size()!=0)
		{
			for (Record record : tzlst) {
				String orderid=record.getStr("upral_orderid");//查询用户订单表中此订单id的购入类型为out,真实交易时间为空的记录，通过这些查询是否存在处于赎回审核状态的项目
				List<Record> olst=Db.find("select * from SRB_USER_ORDER  where coral_orderid=? and coral_type=? and coral_realdatetime=?",orderid,"out","");
				if(olst.size()!=0)
				{
					record.remove("upral_jsyy");
					record.set("upral_jsyy", "0");//将此字段作为项目的状态字段，如果处于审核状态，则将jsyy的值设为0
				}
				else
				{
					record.remove("upral_jsyy");
					record.set("upral_jsyy", "1");//将此字段作为项目的状态字段，如果处于投资状态，则将jsyy的值设为1
				}
				String gmrq = record.getStr("upral_gmrq");System.out.println("购买日期:"+gmrq);
				int tzqx = record.getInt("pt_tzqx");
				if(gmrq == null || "".equals(gmrq))continue;//购买日期只取前面的日期，去掉时间
				String[] gmrqs = gmrq.trim().split(" ");
				String jsrq = Utils.getDate(gmrq, tzqx);//根据开始日期，计算结束日期
				record.remove("upral_jsrq");
				record.remove("upral_gmrq");
				
				record.set("upral_gmrq", gmrqs[0]);
				record.set("upral_jsrq", jsrq);			
			}
			
		}
		renderJson(tzlst);	
	}

	//查询已经赎回的产品列表
		public void shptlst(){
			String username=getPara("username");
			//查询出在用户产品表中upral_sfjs为1的值
			List<Record> shlst=Db.find("select pt_icon,upral_orderid,upral_pdctid,pt_tzqx,pt_yqnh,upral_gmrq,upral_jsrq,upral_ljflje,upral_yscjje from SRB_USER_PDCT uorder,SRB_PDCT pt where uorder.upral_pdctid=pt_id and upral_cstmname=? and upral_sfjs=? order by upral_jsrq desc",username,"1");
			if(shlst.size()!=0)
			{
				for (Record record : shlst) {
					String gmrq = record.getStr("upral_gmrq");
					int tzqx = record.getInt("pt_tzqx");
					if(gmrq == null || "".equals(gmrq))continue;//购买日期只取前面的日期，去掉时间
					String[] gmrqs = gmrq.trim().split(" ");				
					record.remove("upral_gmrq");					
					record.set("upral_gmrq", gmrqs[0]);
					String jsrq = record.getStr("upral_jsrq");
					System.out.println("结束日期："+jsrq);
					if(jsrq == null || "".equals(jsrq))continue;//购买日期只取前面的日期，去掉时间
					String[] jsrqs = jsrq.trim().split(" ");				
					record.remove("upral_jsrq");
					
					record.set("upral_jsrq", jsrqs[0]);
						
				}			
			}
			renderJson(shlst);	
		}
	//赎回投资页面信息显示
	public void shuhui() throws ParseException{
		/*String username=getPara("username");
		String ptid=getPara("ptid");*/
		String orderid=getPara("orderid");
		List<Record> ptlst=Db.find("select pt_yqnh,pt_tzqx,pt_icon,pt_shfs,upral_gmrq,upral_cjje,upral_jsrq from SRB_PDCT pt,SRB_USER_PDCT upt where upt.upral_pdctid=pt.pt_id and upral_orderid=?",orderid);
		Record pt=ptlst.get(0);
		int tzqx=pt.getInt("pt_tzqx");
		String gmrq=pt.getStr("upral_gmrq");
		String dqrq=Utils.getDate(gmrq, tzqx);//计算到期日期
		pt.remove("upral_jsrq");
		pt.set("upral_jsrq", dqrq);
		/*
		String mydate=Utils.getDefaultTime();
		String gmrq=pt.getStr("upral_gmrq");
		long days=Utils.DateDifferent(gmrq, mydate);如果在未到期前赎回，计算截止今日应收到的利息。
		pt.remove("pt_tzqx");
		pt.set("pt_tzqx", days);*/
		
		renderJson(ptlst);	
	}
	
	//确认赎回动作
	@Before(Tx.class)
	public void qrsh(){
		String username=getPara("username");
		String shje1=getPara("shje");//赎回金额
		float shje=Float.parseFloat(shje1);
		String orderid=getPara("orderid");//订单id
		String msg="0";
		String mydate=Utils.getDefaultTime();//当前日期
		
		List<Record> ptlst=Db.find("select cstm_id,cstm_realname from SRB_CUSTOMER where cstm_name=?",username);
		Record pt=ptlst.get(0);		
		String custmid=pt.getStr("cstm_id");
		String cstrealname=pt.getStr("cstm_realname");System.out.println("realname:"+cstrealname);
		
		List<Record> shlst=Db.find("select * from SRB_USER_ORDER where coral_orderid=? and coral_type=?",orderid,"out");
		 if(shlst.size()!=0)//此订单的赎回是否已经提交
		 {
			 msg="3";//如果已经提交过，返回3，提示不能重复申请
			 
		 }
		 else
		 { System.out.println("订单赎回没提交");
		 	String coral_id=UUID.randomUUID().toString();
			//插入用户订单表一条赎回数据，其中的订单号就是前面查询出的订单号
			int m=Db.update("insert into SRB_USER_ORDER(coral_id,coral_cstmid,coral_cstmname,coral_cstmrealname,coral_orderid,coral_okdatetime,coral_type,coral_cjje) values(?,?,?,?,?,?,?,?)",coral_id,custmid,username,cstrealname,orderid,mydate,"out",shje);
			//插入流程表
			int n=Db.update("insert into SRB_FLOW_HISTORY(id,h_cflowid,h_cflowstatus,h_coralid,h_startdatetime,h_note,h_isok) values(?,?,?,?,?,?,?)",UUID.randomUUID().toString(),"1","ready",coral_id,mydate,"赎回申请","0");
			if(m>0 && n>0)
			{
				msg="1";
			}
			
		 }
		 System.out.println(msg);
		 renderJson(msg);
		
	}
	//显示我的交易记录
	public void translist(){
		String username=getPara("username");
		String msg="0";
		List<Record> tlst=Db.find("select upral_pdctid,upral_name,coral_realdatetime,coral_cjje,coral_type from SRB_USER_ORDER user,SRB_USER_PDCT pt where user.coral_orderid=pt.upral_orderid and coral_cstmname=? order by coral_okdatetime desc",username);
		if(tlst.size()!=0)
		{
			for(Record record: tlst)
			{
				String ptid=record.getStr("upral_pdctid");
				List<Record> alst=Db.find("select * from SRB_PDCT where pt_id=?",ptid);
				if(alst.size()!=0)
				{
					String pticon=alst.get(0).getStr("pt_icon");
					record.remove("upral_name");
					record.set("upral_name", pticon);
				}
				
			}
			renderJson(tlst);
		}
		else
		{
			renderJson(msg);
		}		
	}
	//显示资金明细
	public void moneydetail(){
		String username=getPara("username");
		
		List<Record> balst=Db.find("select acc_balance from SRB_ACCOUNTINFO where acc_username= ?",username);
		Record yue=balst.get(0);
		float zhye=yue.getBigDecimal("acc_balance").floatValue();//用户余额
		
		float yhsy=0;//已获收益
		float dssy=0;//待收收益
		float zhzc=0;//资产总额
		float ytje=0;//已投金额
		float dsbj=0;//待收本金
		float ytje1=0;
		float yqsy=0;//预期收益
		List<Record> ptlst=Db.find("select pt_yqnh,pt_tzqx,upral_cjje,upral_ljflje from SRB_PDCT pt,SRB_USER_PDCT upt where pt.pt_id=upt.upral_pdctid and upral_cstmname=? and upral_sfjs=?",username,"0");
		for(int i=0;i<ptlst.size();i++)
		{
			dsbj+=(ptlst.get(i).getBigDecimal("upral_cjje")).floatValue();
			
			yqsy=(ptlst.get(i).getBigDecimal("upral_cjje").floatValue())*(ptlst.get(i).getBigDecimal("pt_yqnh").floatValue())/12*(ptlst.get(i).getInt("pt_tzqx"));//计算所有产品的代收收益
			dssy+=yqsy-(ptlst.get(i).getBigDecimal("upral_ljflje").floatValue());//待收收益=预期收益-累计返利金额
		}
		List<Record> fl=Db.find("select sum(upral_ljflje) as ljfl,sum(upral_yscjje) as ytje from (select * from SRB_USER_PDCT where upral_cstmname=?)a",username);
		if(fl.size()!=0)
		{
			BigDecimal ljflbd = fl.get(0).getBigDecimal("ljfl");
			if(ljflbd != null)yhsy=ljflbd.floatValue();//累计返利
			BigDecimal ytjebd = fl.get(0).getBigDecimal("ytje");
			if(ytjebd != null)ytje1=ytjebd.floatValue();//获取投的所有本金
		}
		
		zhzc=dsbj+zhye;//账户资产=待收本金+账户余额
		
		zjmx zjmx=new zjmx();
		zjmx.setDsbj(dsbj);zjmx.setDssy(dssy);
		zjmx.setYhsy(yhsy);zjmx.setYtje(ytje1);
		zjmx.setZhye(zhye);zjmx.setZhzc(zhzc);
		
		renderJson(zjmx);		
	}
	//设置支付密码
	public void setzzmm(){
		String username=getPara("username");
		String zfmm=getPara("zfmm");
		String msg="0";
		int m=0;
		/*List<Record> mm=Db.find("select acc_password from SRB_ACCOUNTINFO where acc_username=?",username);
		
		if(mm.get(0).getStr("acc_password")!= "")
		{
			msg="1";
		}*/
		m=Db.update("update SRB_ACCOUNTINFO set acc_password=? where acc_username=?",zfmm,username);
		if(m>0)
		{
			msg="1";
		}
		renderJson(msg);
	}
	//修改登录密码
	public void modifypasswd(){
		String username=getPara("username");
		String ymm=getPara("ymm");//原密码
		String xmm=getPara("xmm");//新密码		
		String msg="0";
		int m=0;
		List<Record> ymlst=Db.find("select cstm_password from SRB_CUSTOMER where cstm_name=?",username);
		String ymm1=ymlst.get(0).getStr("cstm_password");
		if(ymm1.equals(ymm))
		{			
			m=Db.update("update SRB_CUSTOMER set cstm_password=? where cstm_name=?",xmm,username);	
			System.out.println(ymm1);
			if(m>0)
			{
				msg="1";
			}
		}
		renderJson(msg);		
	}
	//显示系统消息
	public void sysmsg(){
		String username=getPara("username");
				
		List<Record> syslst=Db.find("select id,h_startdatetime,h_note,h_isok,h_cflowstatus from SRB_FLOW_HISTORY h,SRB_USER_ORDER o where h.h_coralid=o.coral_id and h_cflowid=? and o.coral_cstmname =?","0",username);
		renderJson(syslst);				
	}
	//修改消息的状态
	public void modmess(){
		String id=getPara("id");
		int m=0;
	    String mydate=Utils.getDefaultTime();
		if(id!="" ||id!=null)
		{
			m=Db.update("update SRB_FLOW_HISTORY set h_isok=? and h_enddatetime=? where id=?","1",mydate,id);
		}
		
	}
	//app升级
	public void appupdate(){
		String appid=getPara("appid");
		String os=getPara("os");
		String version=getPara("version");
		List<Record> ulst=Db.find("select * from SRB_UPDATE where os=?",os);
		
		renderJson(ulst);
	}
	//问题反馈
	public void feedback(){
		String username=getPara("username");
		String nr=getPara("nr");
		int m=0;
		String rtnmsg="0";
		m=Db.update("insert into SRB_FEEDBACK values(?,?,?)",UUID.randomUUID().toString(),nr,username);
		if(m!=0)
		{
			rtnmsg="1";
		}
		renderJson(rtnmsg);
	}
	//借款
	public void jkuan(){
		String username=getPara("username");
		String realname=getPara("realname");
		String tel=getPara("tel");
		String amout=getPara("amout");
		String jkqx=getPara("jkqx");
		String yzm=getPara("yzm");
		/*String tjrtel=getPara("tjrtel");*/
		String yanzm="";
		String mydate=Utils.getDefaultTime();
		int m=0;
		String rtmsg="0";
		/*String recomid="";
		String recomname="";*/
		List<Record> ylst=Db.find("select  yanzm from SRB_YZM where usertel=?",tel);
		if(ylst.size()!=0)
		{
			yanzm=ylst.get(0).getStr("yanzm");
		}
		if(yanzm.equals(yzm))//输入的验证码和数据库的验证码相同
		{
			/*if(tjrtel!=null && tjrtel!="")//如果推荐人手机号存在，取出推荐人id、姓名信息
			{
				List<Record> rlst=Db.find("select id,realname from jbsf_user where phone=?",tjrtel);
				if(rlst.size()!=0)
				{
					recomid=rlst.get(0).getStr("id");
					recomname=rlst.get(0).getStr("realname");
				}				
			}//插入借款信息表
*/			m=Db.update("insert into SRB_JKAPPLY(jkrq_id,jkrq_name,jkrq_phone,jkrq_jkje,jkrq_qx,jkrq_sqsj) values(?,?,?,?,?,?)",UUID.randomUUID().toString(),realname,tel,amout,jkqx,mydate);			
			if(m!=0)
			{
				rtmsg="1";
			}			
		}
		else
		{
			rtmsg="2";
		}
		renderJson(rtmsg);		
	}
	//查询此用户的手机号是否存在
	public void querytel()
	{
		String tel=getPara("tel");
		String msg="0";
		List<Record> ulst=Db.find("select * from SRB_CUSTOMER where cstm_phone=?",tel);
		if(ulst.size()!=0)
		{
			msg="1";
		}
		renderJson(msg);
	}
	//忘记密码
	public void forgetpasswd()
	{
		String tel=getPara("tel");
		String yzm=getPara("yzm");
		String passwd=getPara("passwd");
		String msg="0";
		//判断手机号是否存在
		List<Record> ulst=Db.find("select * from SRB_CUSTOMER where cstm_phone=?",tel);
		if(ulst.size()==0)//手机号不存在，返回1
		{
			msg="1";
			renderJson(msg);
		}
		//判断输入的验证码是否正确
		List<Record> ylst=Db.find("select * from SRB_YZM where usertel=?",tel);
		
		if(ylst.size()!=0)
		{
			String yanzm=ylst.get(0).getStr("yanzm");
			if(yanzm.equals(yzm)==false)
			{
				msg="2";
				renderJson(msg);//输入的验证码不正确，返回2
			}
			else
			{
				int m=Db.update("update SRB_CUSTOMER set cstm_password=? where cstm_phone=?",passwd,tel);
				if(m>0)
				{
					msg="3";
					renderJson(msg);
				}
			}
			
		}
		
	}
	public void queryHtuser(){
		String tjrPhone = getPara("tjrPhone");
		List<Record> lst = Db.find("select id, username, realname from jbsf_user where phone = ?", tjrPhone);
		renderJson(lst);
	}
	
	@Before(Tx.class)
	public void txtest(){
		//update1
		//update2
		//insert3
	}
}
