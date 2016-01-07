/**
 * 
 */
package com.pansoft.jbsf.service.TimingTask.orderclear;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.controller.RegisterController;
import com.pansoft.jbsf.util.JbsfLogUtils;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;
import com.unionpay.acp.demo.DemoBase;
import com.unionpay.acp.sdk.SDKConfig;

/**
 * 每天24时执行账户自动清算动作
 * @author hawkfly
 */
public class OCJob implements Job {

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//查询所有未结束的用户产品，包含购买时间，产品周期，购买金额，年化收益
		List<Record> lstdatas = Db.find("select oh_id, oh_orderid, oh_pdtid, oh_createtime, oh_cstmname,oh_accmoney from SRB_ORDEREQ_HISTORY where TIMESTAMPDIFF(HOUR, oh_createtime, oh_cjtime) > 1 and (oh_status = '0' or oh_status = '2')");
		
		for (int i = 0; i < lstdatas.size(); i++) {
			Record record = lstdatas.get(i);
			queryOrder(record);
		}
	}
	
	public boolean queryOrder(Record record){
		
		final String orderId = record.getStr("oh_orderid");
		String txnTime = record.getStr("oh_createtime");
		String id = record.getStr("oh_id");
		String pdtid = record.getStr("oh_pdtid");
		final String username = record.getStr("oh_cstmname");
		final float faccmoeny = record.getBigDecimal("oh_accmoney").floatValue();
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", DemoBase.version);                 //版本号
		data.put("encoding", DemoBase.encoding);               //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "00");                             //交易类型 00-默认
		data.put("txnSubType", "00");                          //交易子类型  默认00
		data.put("bizType", "000201");                         //业务类型 B2C网关支付，手机wap支付
		
		/***商户接入参数***/
		data.put("merId", "777290058110097");                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改
		
		/***要调通交易以下字段必须修改***/
		data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
		data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> submitFromData = DemoBase.signData(data);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

		//多证书使用方法：如果有多个商户号接入银联,每个商户号对应不同的证书可以使用此方法:传入私钥证书和密码(并且在acp_sdk.properties中 配置 acpsdk.singleMode=false) 
		//Map<String, String> submitFromData = DemoBase.signData(data,"D:\\certs\\PM_700000000000001_acp.pfx", "000000");
		
		String url = SDKConfig.getConfig().getSingleQueryUrl();// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl

		//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		Map<String, String> resmap = DemoBase.submitUrl(submitFromData, url);
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		boolean isnetok = true;
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(Utils.isNull(resmap.get("respCode"))){//没有正确返回200编码，可能是网络异常
			isnetok = false;
			JbsfLogUtils.info(Consts.NETWORKERROR, "订单号为: "+orderId+", 网络可能存在异常！一小时后会重新推送！当前时间为：" + Utils.getDefaultTime());
		}else if("00".equals(resmap.get("respCode"))){//如果查询交易成功
			//处理被查询交易的应答码逻辑
			if("00".equals(resmap.get("origRespCode"))){
				//交易成功，更新商户订单状态
				//TODO
				boolean isok = new RegisterController().getPtinfo(orderId, pdtid);
				if(isok){
					JbsfLogUtils.info(Consts.QUERYORDERSUCESS, "订单号为: "+orderId + ", 更新时间："+Utils.getDefaultTime());
				}
			}else if("03".equals(resmap.get("origRespCode")) ||
					 "04".equals(resmap.get("origRespCode")) ||
					 "05".equals(resmap.get("origRespCode"))){
				//需再次发起交易状态查询交易 
				//TODO
				JbsfLogUtils.info(Consts.QUERYORDERERROR345, "订单号为: "+orderId + ", 更新时间："+Utils.getDefaultTime());
			}else{
				//其他应答码为失败请排查原因
				//TODO
				Db.update("update SRB_ORDEREQ_HISTORY set oh_status = '3' where oh_orderid = ?", orderId);
				JbsfLogUtils.info(Consts.QUERYORDERERROROTHER, "订单号为: "+orderId + ", 更新时间："+Utils.getDefaultTime());
			}
		}else{//查询交易本身失败，或者未查到原交易，检查查询交易报文要素
			//TODO
			Db.tx(new IAtom(){
				@Override
				public boolean run() throws SQLException {
					// TODO Auto-generated method stub
					int i = Db.update("update SRB_ORDEREQ_HISTORY set oh_status = '-1' where oh_orderid = ?", orderId);
					int j = 1;
					if(faccmoeny != 0){
						j = Db.update("update SRB_ACCOUNTINFO set acc_status = '1' where acc_username = ?", username);
					}
					if(i>0 && j>0){
						return true;
					}else{
						return false;
					}
				}});
			
			JbsfLogUtils.info(Consts.QUERYORDERERROROTHER, "订单号为: "+orderId + ", 更新时间："+Utils.getDefaultTime());
		}
		
		return isnetok;
	}

}
