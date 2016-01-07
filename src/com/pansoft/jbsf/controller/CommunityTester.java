/**
 * 
 */
package com.pansoft.jbsf.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;

import com.jf.core.Controller;
import com.pansoft.jbsf.util.Utils;

/**
 * 社区测试控制器
 * @author hawkfly
 */
public class CommunityTester extends Controller {
	public void index(){
		Map<String, String> mdatas = new HashMap<String, String>();
		String paraname = getPara("tabname");
		String callback = getPara("callback");
		String billcode = getPara("billCode");
		if("首页".equals(paraname)){
			renderHtml(callback+"({res:"+"\"<table width='200' border='0'><tr><td width='133' ><img src='../img/home_bx.png' width='60' height='60'/><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td width='43'><img  src='../img/home_gg.png'  width='60' height='60' /><br/><span style='text-align:center; width: 100%;'>sss</span></td><td width='10'><img  src='../img/home_grxx.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "</tr><tr><td><img  src='../img/home_jf.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td><td><img src='../img/home_mytiwen.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td><img src='../img/jf_1.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td></tr></table>\"})");
		}else if("社区商城".equals(paraname)){
			renderHtml(callback+"({res:"+"\"<table width='200' border='0'><tr><td width='133' ><img src='../img/_.jpg' width='60' height='60'/><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td width='43'><img  src='../img/_1.jpg'  width='60' height='60' /><br/><span style='text-align:center; width: 100%;'>sss</span></td><td width='10'><img  src='../img/_1.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "</tr><tr><td><img  src='../img/_1.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td><td><img src='../img/_1.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td><img src='../img/_.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td></tr></table>\"})");
		}else if("生活缴费".equals(paraname)){
			renderHtml(callback+"({res:\"sdfsdffffffffffffffffffffffffffffffffffffsljfdlsajfdlsjdflsjdflssdfjsad水电费水电费撒阿斯蒂芬是范德萨地方山东方式飞倒萨发送到方式阿道夫啊方式\"})");
		}else if("物业答复".equals(paraname)){
			renderHtml(callback+"({res:"+"\"<table width='200' border='0'><tr><td width='133' ><img src='../img/sfgl.jpg' width='60' height='60'/><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td width='43'><img  src='../img/sfzl.jpg'  width='60' height='60' /><br/><span style='text-align:center; width: 100%;'>sss</span></td><td width='10'><img  src='../img/jfgl.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "</tr><tr><td><img  src='../img/tingdian.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td><td><img src='../img/tingshui.jpg' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td><img src='../img/wy_ico_sdm.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td></tr></table>\"})");
		}else if("社区互助".equals(paraname)){
			renderHtml(callback+"({res:"+"\"<table width='200' border='0'><tr><td width='133' ><img src='../img/wy_ico_chewei.png' width='60' height='60'/><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td width='43'><img  src='../img/wy_ico_huji.png'  width='60' height='60' /><br/><span style='text-align:center; width: 100%;'>sss</span></td><td width='10'><img  src='../img/wy_ico_jisheng.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "</tr><tr><td><img  src='../img/wy_ico_nuanqi.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td><td><img src='../img/wy_ico_ruxue.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td><img src='../img/wy_ico_yangquan.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td></tr></table>\"})");
		}else{
			renderHtml(callback+"({res:"+"\"<table width='200' border='0'><tr><td width='133' ><img src='../img/home_bx.png' width='60' height='60'/><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td width='43'><img  src='../img/home_gg.png'  width='60' height='60' /><br/><span style='text-align:center; width: 100%;'>sss</span></td><td width='10'><img  src='../img/home_grxx.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "</tr><tr><td><img  src='../img/home_jf.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td><td><img src='../img/home_mytiwen.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td>"
					+ "<td><img src='../img/jf_1.png' width='60' height='60' /><br/><span style='text-align:center;'>sss</span></td></tr></table>\"})");
		}
		try {
			File file = readFile();
			System.out.println(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		this.createToken();
//		renderHtml(callback+"({\"louhao\":\"1号楼\"})");
	}
	
	public void imgslistTester(){
		List<String> imgs = new ArrayList<String>();
		imgs.add("http://192.168.250.147:8080/community/images/jfgl.jpg");
		imgs.add("http://192.168.250.147:8080/community/images/sfgl.jpg");
		imgs.add("http://192.168.250.147:8080/community/images/sfzl.jpg");
		imgs.add("http://192.168.250.147:8080/community/images/tingdian.jpg");
		imgs.add("http://192.168.250.147:8080/community/images/tingshui.jpg");
		renderJson(imgs);
	}
	
	public void getversion(){
		renderText("1.0");
	}
	
	public void testsendjt(){
		String a = this.getAttr("content");
		String b = this.getPara("content");
		try {
			InputStream in = this.getRequest().getInputStream();
			String content = Utils.getStreamString(in);
			System.out.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderText("testsendjt...");
	}
	
	/**
	 * 执行誉能增值税发票webservice数据接口测试
	 * @DATE 2014-10-11下午4:44:48
	 */
	/*public void testYNInvoice(){
		try {
			WSClientAxis4.newInstance().sendInvoice();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderHtml("invoice");
	}
	
	public void testYNtwodcInvoice(){
		try {
			WSClientAxis4.newInstance().sendTwodcInvoice();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderHtml("twodc Invoice");
	}*/
	
	/**
	 * 读取PDF文件测试
	 * @DATE 2014-10-11下午4:44:38
	 */
	@SuppressWarnings("unused")
	private File readFile() throws Exception{
		ServletInputStream inStream;
		inStream = this.getRequest().getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();   
		byte[] buff = new byte[100]; //ff用于存放循环读取的临时数据   
		int rc = 0;   
		while ((rc = inStream.read(buff, 0, 100)) > 0) {   
		  swapStream.write(buff, 0, rc);
		}   
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
		return Utils.getFile(in_b, "C://WINDOWS//TEMP", UUID.randomUUID().toString());
	}
}
