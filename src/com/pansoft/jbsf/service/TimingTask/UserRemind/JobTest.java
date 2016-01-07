package com.pansoft.jbsf.service.TimingTask.UserRemind;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.MailSenderInfo;
import com.pansoft.jbsf.util.SimpleMailSender;
import com.shcm.bean.BalanceResultBean;
import com.shcm.bean.ReplyBean;
import com.shcm.bean.SendResultBean;
import com.shcm.bean.SendStateBean;
import com.shcm.send.DataApi;
import com.shcm.send.OpenApi;


public class JobTest implements Job {  
    static int a = 0;  
    Date now = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy"+"年"+"MM"+"月"+"dd"+"日");//可以方便地修改日期格式
    String time = dateFormat.format( now ); 
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	//判断是否达到应该提醒的条件，再根据条件执行不同的代码
    	JobDetail jobDetail = context.getJobDetail();
    	String userid = jobDetail.getName();//当前任务所属用户的ID
    	System.err.println("start!");
    	//查询'待处理数量’,'待处理金额'
    	List list = Db.find("select count(1) COUNTNUM ,sum(WSJE)/10000 ZJE from invoice where GFDM in (select F_DWBH from iv_user_dw where F_YHBH = '"+userid+"') and DJZT in ('CS','SB')");
    	Record rec = (Record)list.get(0);
    	//1.根据ID获取该用户当前的'待处理数量’,'待处理金额'
    	int curCount=rec.getLong("COUNTNUM").intValue();
    	double curMoney=rec.getBigDecimal("ZJE").doubleValue();

    	//获取被发送者的信息
    	List listinfo = Db.find("select * from iv_remind where F_YHBH ='"+userid+"'");
    	Record recinfo = (Record)listinfo.get(0);
    	//2.获取用户提醒的最大限度 数量、金额是多少
    	int maxCount=recinfo.getInt("F_SL");
    	double maxMoney=recinfo.getBigDecimal("F_JE").doubleValue();
    	//3.判断是否达到提醒条件
    	if(curCount>=maxCount||curMoney>=maxMoney)
    	{
    		//4.若已达到提醒条件，获取提醒方式
    		String info ="亲爱的用户:\n您好！\n您现有"+curCount+"张发票未认证，总金额为"+curMoney+"万元。\n您该去《财务供应链集成服务平台》处理业务啦！\nhttp://www.taxserver.cn\n\n\n普联软件股份有限公司\n"+time+"";
    		String phoneInfo ="亲爱的用户,您好！您有"+curCount+"张发票未认证，总金额为"+curMoney+"元，该去《财务供应链集成服务平台》处理业务啦！";
    		String sfdh=recinfo.getStr("F_SFDH");//是否电话提醒？
    		String sfem=recinfo.getStr("F_SFML");//是否邮件提醒？
    		String tel=recinfo.getStr("F_TELE");//电话号码
    		String email=recinfo.getStr("F_MAIL");;//邮箱
    		if("1".equals(sfem))
    		{
    			sendEmail(email,info);
    		}
    		if("1".equals(sfdh))
    		{
    			try
    			{
					sendMessage(tel,phoneInfo);
				} 
    			catch (Exception e)
    			{
					e.printStackTrace();
				}
    		}
    	}
    }
    //发送短信的代码实现
    private static void sendMessage(String tel,String info) throws Exception
    {
    	System.out.println("短信已发送至:"+tel);
    	String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
    	String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
    	// 接口帐号
    	String account = "1001@501069340001";
    	// 接口密钥
    	String authkey = "2DB1ED52F44872513E4BB4E7567EA4B6";
    	// 通道组编号
    	int cgid = 52;
    	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
    	int csid = 0;
    	// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		
		// 取帐户余额
		BalanceResultBean br = OpenApi.getBalance();
		if(br.getResult() < 1)
		{
			System.out.println("获取可用余额失败: " + br.getErrMsg());
			return;
		}
		System.out.println("可用条数: " + br.getRemain());
		
		// 更新接口密钥
		//String sAuthKey = OpenApi.updateKey();
		//if(!sAuthKey.isEmpty())
		//{
		//	System.out.println("已成功更新密钥,新接口密钥为: " + sAuthKey);
		//}
		
		List<SendResultBean> listItem = sendOnce(tel, info);
		if(listItem != null)
		{
			for(SendResultBean t:listItem)
			{
				if(t.getResult() < 1)
				{
					System.out.println("发送提交失败: " + t.getErrMsg());
					return;
				}
				
				System.out.println("发送成功: 消息编号<" + t.getMsgId() + "> 总数<" + t.getTotal() + "> 余额<" + t.getRemain() + ">");
			}
		}
		
		// 循环获取状态报告和回复
		while(true)
		{
			List<SendStateBean> listSendState = DataApi.getSendState();
			if(listSendState != null)
			{
				for(SendStateBean t:listSendState)
				{
					System.out.println("状态报告 => 序号<" + t.getId() + "> 消息编号<" + t.getMsgId() + "> 手机号码<" + t.getMobile() + "> 结果<" + (t.getStatus() > 1 ? "发送成功" : t.getStatus() > 0 ? "发送失败" : "未知状态") + "> 运营商返回<" + t.getDetail() + ">");
				}
			}
			
			// 取回复
			List<ReplyBean> listReply = DataApi.getReply();
			if(listReply != null)
			{
				for(ReplyBean t:listReply)
				{
					System.out.println("回复信息 => 序号<" + t.getId() + "> 回复时间<" + t.getReplyTime() + "> 手机号码<" + t.getMobile() + "> 回复内容<" + t.getContent() + ">");
				}
			}
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    //发送邮件的代码实现
    private void sendEmail(String email,String info)
    {
    	System.out.println("邮件已发送至:"+email);
    	 MailSenderInfo mailInfo = new MailSenderInfo();    
         mailInfo.setMailServerHost("mail.pansoft.com");    
         mailInfo.setMailServerPort("25");    
         mailInfo.setValidate(true);    
         mailInfo.setUserName("paninvoice@pansoft.com");    
         mailInfo.setPassword("eb2cb2Og9");//您的邮箱密码    
         mailInfo.setFromAddress("paninvoice@pansoft.com");    
         mailInfo.setToAddress(email);    
         mailInfo.setSubject("财务供应链集成服务平台");    
         mailInfo.setContent(info);    
            //这个类主要来发送邮件   
         SimpleMailSender sms = new SimpleMailSender();   
             try {
				sms.sendTextMail(mailInfo);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//发送文体格式    
    }
    
    public static List<SendResultBean> sendOnce(String mobile, String content) throws Exception
	{
		// 发送短信
		return OpenApi.sendOnce(mobile, content, 0, 0, null);
	}
    
    public static void main(String[] args) throws Exception{   
    	//这个类主要是设置邮件   
    	/*Date now = new Date();
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy"+"年"+"MM"+"月"+"dd"+"日");//可以方便地修改日期格式
     String time = dateFormat.format( now ); 
     MailSenderInfo mailInfo = new MailSenderInfo();    
     mailInfo.setMailServerHost("mail.pansoft.com");    
     mailInfo.setMailServerPort("25");    
     mailInfo.setValidate(true);    
     mailInfo.setUserName("lixingchao@pansoft.com");    
     mailInfo.setPassword("89902973");//您的邮箱密码    
     mailInfo.setFromAddress("lixingchao@pansoft.com");    
     mailInfo.setToAddress("lixingchao@pansoft.com");    
     mailInfo.setSubject("财务供应链集成服务平台");   
     mailInfo.setContent("亲爱的用户:\n您好！\n您现有2张发票未认证，总金额为10W元。\n您该去《财务供应链集成服务平台》处理业务啦！\nhttp://www.taxserver.cn\n\n\n普联软件股份有限公司\n"+time+"");    
        //这个类主要来发送邮件   
     SimpleMailSender sms = new SimpleMailSender();   
         sms.sendTextMail(mailInfo);//发送文体格式    
         //sms.sendHtmlMail(mailInfo);//发送html格式   
    	 */   	

    }  
    
    
    
    
    
    
    
    
} 