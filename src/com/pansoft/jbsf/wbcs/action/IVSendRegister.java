package com.pansoft.jbsf.wbcs.action;

import java.io.IOException;  
import java.util.HashMap;  
import java.util.List;
import java.util.Map;  

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.HttpException;  
import org.apache.commons.httpclient.HttpStatus;  
import org.apache.commons.httpclient.NameValuePair;  
import org.apache.commons.httpclient.methods.PostMethod;  
import org.apache.commons.httpclient.params.HttpClientParams;  
import org.apache.commons.httpclient.params.HttpMethodParams;  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVSendRegister implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		String str = arg2.get(0).get("key");
		IVSendRegister t = new IVSendRegister();  
        String res = t.sendData(str);  
        if(res.length() > 5){
	        String before = res.substring(0, 4);
	        String after = res.substring(5);
	        if("True".equals(before) &&!"".equals(after)){
	        	Db.update("update gzyn_registration set ZCM = '"+after+"', SQZT = '1' where ID = '"+str+"'");
	        	System.out.println("注册码已获取！");
	        	return "注册码已获取！";
	        }else{
	        	Db.update("update gzyn_registration set  SQZT = '2' where ID = '"+str+"'");
	        	System.out.println("获取注册码失败！");
	        	return "获取注册码失败！";
	        }
        }else{
        	Db.update("update gzyn_registration set  SQZT = '2' where ID = '"+str+"'");
        	System.out.println("没有返回数据，获取注册码失败！");
        	return "获取注册码失败！";
        }
	}

	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

		static Log log = LogFactory.getLog(IVSendRegister.class);  
	    private String Url;  
	    // 初始化数据  
	    public IVSendRegister() {  
	    	//http://www.ycansoft.com:83/PlUploadRegCode.jsp
	        Url = "http://www.taxserver.cn/request_registration?";  
	    }  
	  //http://192.168.2.132/provide_registration?taxno=370502090682163&kpjh=0
	    public String sendData(String sqbh) {  
	        String receivedData = null;  
	        List lis = Db.find("select * from gzyn_registration where ID=?",sqbh);
			Record rec = (Record)lis.get(0);
			String dwsh = rec.getStr("DWSH");
			String dwmc = rec.getStr("DWMC");
	        try {  
	            Map<String, String> paramsData = new HashMap<String, String>();  
	            paramsData.put("taxno", dwsh);
	            paramsData.put("kpjh", "0");
	            paramsData.put("name", dwmc);
	            paramsData.put("jqm", "");
	            //http://192.168.2.132/provide_registration?taxno=370502090682163&name=huahua&kpjh=0
	            receivedData = send(Url, paramsData);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return receivedData;  
	    }  
	  
	    public static String send(String url, Map<String, String> paramsMap) {  
	        String result = null;  
	        PostMethod postMethod = null;  
	        HttpClient httpClient = new HttpClient();  
	  
	        httpClient.getParams().setParameter(  
	                HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");  
	        postMethod = new PostMethod(url);
	        

	        

	        if (paramsMap != null && paramsMap.size() > 0) {  
	            NameValuePair[] datas = new NameValuePair[paramsMap.size()];  
	            int index = 0;  
	            for (String key : paramsMap.keySet()) {  
	                datas[index++] = new NameValuePair(key, paramsMap.get(key));  
	            }  
	            postMethod.setRequestBody(datas);  
	  
	        }  
	  
	        HttpClientParams httparams = new HttpClientParams();  
	        httparams.setSoTimeout(60000);  
	        postMethod.setParams(httparams);  
	        
	        postMethod.getParams().toString();
	  
	        try {  
	            int statusCode = httpClient.executeMethod(postMethod);  
	            if (statusCode == HttpStatus.SC_OK) {  
	                result = postMethod.getResponseBodyAsString();  
	                log.info("发送成功！");  
	            } else {  
	                log.error(" http response status is " + statusCode);  
	            }  
	  
	        } catch (HttpException e) {  
	            log.error("error url=" + url, e);  
	        } catch (IOException e) {  
	            log.error("error url=" + url, e);  
	        } finally {  
	            if (postMethod != null) {  
	                postMethod.releaseConnection();  
	            }  
	        }  
	  
	        return result;  
	    } 
	    public static void main(String[] args) {  
	    	IVSendRegister t = new IVSendRegister();  
	        String res = t.sendData("9A575DE1BF467DA05BC9BE85B5F31603");  
	        String before = res.substring(0, 4);
	        String after = res.substring(5);
	        System.out.println(before);
	        System.out.println(after);
	      
	    }  
}
