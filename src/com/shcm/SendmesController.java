package com.shcm;

import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import com.jf.plugin.activerecord.Record;
import com.jf.core.Controller;
import com.jf.plugin.activerecord.Db;
import com.pansoft.jbsf.util.Utils;
import com.shcm.bean.BalanceResultBean;
import com.shcm.bean.SendResultBean;
import com.shcm.send.DataApi;
import com.shcm.send.OpenApi;

public class SendmesController extends Controller {
	public void index(){
		
	}
	public void sendsms() throws Exception{
		String usertel=getPara("usertel");
		String rtnmsg="1";
		int m=0;
		String mydate=Utils.getDefaultTime();
		Random random=new Random();
		int telcode=random.nextInt(899999)+100000;
				
		System.out.println("验证码："+telcode);
		String telcode1=String.valueOf(telcode);//验证码
		String smscontent1="您的验证码为："+telcode1+"。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
		String smscontent2 = new String(smscontent1.toString().getBytes("UTF-8"));  
	    String smscontent = URLEncoder.encode(smscontent2, "UTF-8");  //转成utf8格式
	
		System.out.println("utf8格式："+smscontent);
		String sopenurl="http://smsapi.c123.cn/OpenPlatform/OpenApi";
		String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
		/*String accout="1001@501231510001";// 接口帐号
*/		String accout="1001@501240900001";// 接口帐号
		/*String authkey = "84189424C6852BBCEC736C9AD63AEC49";// 接口密钥
*/		String authkey = "317796D1A7EDFA978F5F8F74E25C12F1";// 接口密钥
		int cgid = 52;// 通道组编号
		int csid = 0;// 默认使用的签名编号(未指定签名编号时传此值到服务器)
		// 发送参数
		OpenApi.initialzeAccount(sopenurl, accout, authkey, cgid, csid);
		
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, accout, authkey);
		List<SendResultBean> listItem=sendOnce(usertel,smscontent1);System.out.println("listItem:"+listItem);
		if(listItem != null)
		{
			for(SendResultBean t:listItem)
			{
				if(t.getResult()== 1)
				{
					System.out.println("t.getResult()"+t.getResult());	
					List<Record> telst=Db.find("select * from SRB_YZM where usertel=?",usertel);
					if(telst.size()!=0)//如果此手机号以前存在验证码，则更新
					{
						m=Db.update("update SRB_YZM set yanzm=?,senddatetime=? where usertel=?",telcode1,mydate,usertel);
					}
					else
					{//如果之前没有存在验证码，则插入验证码记录
						 m=Db.update("insert into SRB_YZM values(?,?,?,?)",UUID.randomUUID().toString(),usertel,telcode1,mydate);
					}
					
					if(m>0) rtnmsg="1";
				}								
			}
			
		}
		renderJson(rtnmsg);
	}
	
	public static List<SendResultBean> sendOnce(String mobile, String content) throws Exception
	{
		
		// 发送短信
		return OpenApi.sendOnce(mobile, content, 52, 0, null);
		
	}
	
	
	}	

