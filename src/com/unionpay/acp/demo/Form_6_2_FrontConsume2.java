package com.unionpay.acp.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SecureUtil;

/**
 * 重要：联调测试时请仔细阅读注释！
 * 
 * 产品：跳转网关支付产品<br>
 * 交易：消费类交易：前台跳转消费，有前台通知应答和后台通知应答<br>
 * 日期： 2015-09<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码性能规范性等方面的保障<br>
 * 提示：该接口参考文档位置：open.unionpay.com帮助中心 下载  产品接口规范  《网关支付产品接口规范》，<br>
 *              《平台接入接口规范-第5部分-附录》（内包含应答码接口规范，全渠道平台银行名称-简码对照表），
 *              《全渠道平台接入接口规范 第3部分 文件接口》（对账文件格式说明）<br>
 * 测试过程中的如果遇到疑问或问题您可以：1）优先在open平台中查找答案：
 * 							        调试过程中的问题或其他问题请在 https://open.unionpay.com/ajweb/help/faq/list 帮助中心 FAQ 搜索解决方案
 *                             测试过程中产生的6位应答码问题疑问请在https://open.unionpay.com/ajweb/help/respCode/respCodeList 输入应答码搜索解决方案
 *                          2） 咨询在线人工支持： open.unionpay.com注册一个用户并登陆在右上角点击“在线客服”，咨询人工QQ测试支持。
 *                          3）  测试环境测试支付请使用测试卡号测试， FAQ搜索“测试卡号”
 *                          4） 切换生产环境要点请FAQ搜索“切换”
 * 交易说明:以后台通知或交易状态查询交易确定交易成功
 */
public class Form_6_2_FrontConsume2 extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		/**
		 * 请求银联接入地址，获取证书文件，证书路径等相关参数初始化到SDKConfig类中
		 * 在java main 方式运行时必须每次都执行加载
		 * 如果是在web应用开发里,这个方法可使用监听的方式写入缓存,无须在这出现
		 */
		//这里已经将加载属性文件的方法挪到了web/AutoLoadServlet.java中
		//SDKConfig.getConfig().loadPropertiesFromSrc(); //从classpath加载acp_sdk.properties文件
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String merId = req.getParameter("merId");
		String txnAmt = req.getParameter("txnAmt");
		
		/**设置请求参数------------->**/
		Map<String, String> requestData = new HashMap<String, String>();
		String orderId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", DemoBase.version);   //版本号 全渠道默认值
		requestData.put("encoding", DemoBase.encoding); //字符集编码 可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", "01");            //签名方法 目前只支持01：RSA方式证书加密
		requestData.put("txnType", "01");               //交易类型 01：消费，02：预授权
		requestData.put("txnSubType", "01");            //交易子类型 01：自助消费，03：分期付款
		requestData.put("bizType", "000201");           //业务类型 B2C网关支付，手机wap支付
		requestData.put("channelType", "07");           //渠道类型 07：PC,08：移动端
		
		/***商户接入参数***/
		requestData.put("merId", merId);    //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		requestData.put("accessType", "0");             //接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
		requestData.put("orderId",orderId);             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", txnTime);            //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", txnAmt);              //交易金额，单位分，不要带小数点
		requestData.put("reqReserved", "透传字段");        //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", DemoBase.frontUrl);
		
		//后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", DemoBase.backUrl);
		
		
		/*** 特殊用法1,实现PC端商户界面点击银行网银图标直接跳转到银行网银支付的功能***/
		/***实现该功能需：1）上送issInsCode字段，该字段的值参考《平台接入接口规范-第5部分-附录》（全渠道平台银行名称-简码对照表）  2）联系银联业运开通了商户号的网银前置权限***/
		//requestData.put("issInsCode", "ABC");                 //发卡机构代码
		
		/*** 特殊用法2，实现对身份证和姓名的回显和校验，也可以与特殊用法3合并***/
		/*** 实现该功能需 1)上送customerInfo 2）联系银联业运开通了商户号的证件和姓名回显校验权限***/
		//requestData.put("customerInfo", getCustomer("UTF-8")); //银行卡验证信息及身份信息
		
		/***特殊用法3,商户直接上送卡号并在银联页面锁定该卡号支付***/
		/*** 实现该功能需 上送以下2个字段 ***/
		//requestData.put("accNo", "6221558812340000");          //卡号
		//requestData.put("reserved", "{cardNumberLock=1}");     //银联定义的系统保留域
		
		//有特殊需求的时候才上送
		//data.put("frontFailUrl", "http://商户地址:端口/ACPSample_PCGate/frontRcvResponse");//支付失败跳转页面
		//data.put("orderTimeoutInterval", "3000");//订单接收超时时间（防钓鱼使用）
		
		/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面------------->**/
		Map<String, String> submitFromData = DemoBase.signData(requestData); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		
		//多证书使用方法：如果有多个商户号接入银联,每个商户号对应不同的证书可以使用此方法:传入私钥证书和密码(并且在acp_sdk.properties中 配置 acpsdk.singleMode=false) 
		//Map<String, String> submitFromData = DemoBase.signData(data,"D:\\certs\\PM_700000000000001_acp.pfx", "000000");
		
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();//获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = DemoBase.createHtml(requestFrontUrl, submitFromData);//生成自动跳转的Html表单
		
		LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
		
		resp.getWriter().write(html);//将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/**
	 * 功能：组装银行卡验证信息及身份信息域
	 * 域格式说明：customerInfo域的格式为base64({key1=value2&key2=value2})，没有value的key不要出现
	 * @param encoding
	 * @return
	 */
	public static String getCustomer(String encoding) {
		
		//以下验证要素必须对应相应的卡号
		String certifTp = "01";// 证件类型
		String certifId = "510265790128303";// 证件号码
		String customerNm = "互联网";// 姓名
		
		StringBuffer sf = new StringBuffer("{");
		sf.append("certifTp=" + certifTp + SDKConstants.AMPERSAND);
		sf.append("certifId=" + certifId + SDKConstants.AMPERSAND);
		sf.append("customerNm=" + customerNm);
		sf.append("}");
		
		String customerInfo = sf.toString();
		try {
			return new String(SecureUtil.base64Encode(sf.toString().getBytes(encoding)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return customerInfo;
	}
	
}
