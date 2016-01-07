package com.unionpay.acp.demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 
 * 
 * 名称： 第一卷 商户卷 第1/6部分 手机支付 ——跳转网关支付产品/手机网页支付产品<br>
 * 功能： 6.7.1　预授权交易<br>
 * 前台交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form_6_7_1_AuthDeal_Front extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		/**
		 * 参数初始化
		 * 在java main 方式运行时必须每次都执行加载
		 * 如果是在web应用开发里,这个方写在可使用监听的方式写入缓存,无须在这出现
		 */
		//SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件

		/**
		 * 组装请求报文
		 */
		Map<String, String> data = new HashMap<String, String>();
		// 版本号
		data.put("version", "5.0.0");
		// 字符集编码 默认"UTF-8"
		data.put("encoding", "UTF-8");
		// 签名方法 01 RSA
		data.put("signMethod", "01");
		// 交易类型 02-预授权
		data.put("txnType", "02");
		// 交易子类型 01:自助消费 02:订购 03:分期付款
		data.put("txnSubType", "01");
		// 业务类型
		data.put("bizType", "000201");
		// 渠道类型，07-PC，08-手机
		data.put("channelType", "07");
		// 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		data.put("accessType", "0");
		
		//***********************要调通交易以下字段必须修改填写********************************************
		// 商户号码，请改成自己的商户号
	    data.put("merId", "888888888888888");
		// 前台通知地址 ，前台点击“返回商户”按钮的使用调用 
		data.put("frontUrl", "http://localhost:8080/ACPSample_PCGate/frontRcvResponse");
		// 后台通知地址，支付成功后银联会自动将异步结果报文post到商户上送的改地址上，商户需要上送这样一个接受通知的外网能访问的地址
		data.put("backUrl", "http://222.222.222.222:8080/ACPSample_PCGate/frontRcvResponse");
	    //***********************要调通交易以上字段必须修改填写********************************************
				

		// 商户订单号，8-40位数字字母
		data.put("orderId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 订单发送时间，取系统时间
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		// 交易金额，单位分
		data.put("txnAmt", "1000");
		// 交易币种
		data.put("currencyCode", "156");
		// 请求方保留域，透传字段，查询、通知、对账文件中均会原样出现
		// data.put("reqReserved", "透传信息");
		// 订单描述，可不上送，上送时控件中会显示该信息
		// data.put("orderDesc", "订单描述");

		Map<String, String> submitFromData = DemoBase.signData(data);
		//如果有多个商户号接入银联,每个商户号对应不同的证书可以使用此方法:传入私钥证书和密码(并且在acp_sdk.properties中 配置 acpsdk.singleMode=false) 
		//Map<String, String> submitFromData = DemoBase.signData(data,"D:\\certs\\PM_700000000000001_acp.pfx", "000000");
		
		// 交易请求url 从配置文件读取
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
		
		/**
		 * 创建表单
		 */
		String html = DemoBase.createHtml(requestFrontUrl, submitFromData);
		System.out.println(html);
		
		resp.getWriter().write(html);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(req, resp);
	}
}