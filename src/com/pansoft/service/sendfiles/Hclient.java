package com.pansoft.service.sendfiles;
import java.io.File;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class Hclient{
	public static void main(String[] args) {
		
		String temp ="D:/workspace/phpProject/code/ivb2b/app/storage/tempZip/130012417000218017.zip";
		sendfiles(temp);
	}
	public Hclient(){
		
	}
	public static String sendfiles(String str){
		   String targetURL = null;// TODO 指定URL
		   File targetFile = null;// TODO 指定上传文件
		   String files[] = null;//初始化保存文件名的数组
		   
		   files = str.split("&");//把传过来的String类型 划分为数组里的数据
		   
		   targetURL = "http://121.28.3.229:8010/outside/outside/fileupload"; //servleturl
		   PostMethod filePost = new PostMethod(targetURL);
		   for (int i = 0; i < files.length; i++) {
		   System.out.println(files[i]);
		   targetFile = new File(files[i]);	
		   try
		   {
		 
		    //通过以下方法可以模拟页面参数提交
		    //filePost.setParameter("name", "中文");
		    //filePost.setParameter("pass", "1234");
		 
		   Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
		    
		   filePost.setRequestEntity(new MultipartRequestEntity(parts,filePost.getParams()));
		   filePost.getParams().setSoTimeout(10000);
		   HttpClient client = new HttpClient();
		    client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		    int status = client.executeMethod(filePost);
		    System.out.println(status);
			    if (status == HttpStatus.SC_OK){
			     Date nowTime = new Date(); 
			     System.out.println(nowTime);
			     System.out.println("上传成功"+",时间："+nowTime);
			     String rstStr = new String(filePost.getResponseBody(),"GBK");
					//JDAO.update("insert into stormtracker values(?,?)", System.currentTimeMillis(),"sended responsedatas : "+rstStr);
		            //String rstStr = Utils.getStreamString(in);
		           /*
		            String[] rtnstrs = rstStr.split(Utils.SPLIT);
		            lstRtn = Arrays.asList(rtnstrs);*/
			     System.out.println(rstStr);
			     return rstStr;
			     // 上传成功
			    }else{
			     System.out.println("上传失败");
			     // 上传失败
			     return "error";
			    }
		   }catch (Exception ex){
		    ex.printStackTrace();
		   }finally{
		    filePost.releaseConnection();
		   }
		   }
		   return "error";
		      
	}
	
}
