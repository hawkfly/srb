package com.pansoft.jbsf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	
	//组织返回json
	public static JSONObject OrangeJson(List<Map> listrec, int sumsize, int pageNum, int pageSize){
		
		int total=(int) Math.ceil(sumsize/pageSize)+1;//向上取整
		
        JSONObject   json = new JSONObject();
        JSONObject   clidjson = new JSONObject();
        JSONArray   array = new JSONArray();

        json.put("page", pageNum);
        json.put("total", total);
        json.put("records", sumsize);
        
        for(int i=0;i<listrec.size();i++){
        	Map map=listrec.get(i);
        	clidjson = new JSONObject();
        	

        	clidjson.put("GFSH",map.get("GFSH")==null?"":map.get("GFSH"));
        	clidjson.put("FPDM",map.get("FPDM")==null?"":map.get("FPDM"));
        	clidjson.put("FPHM",map.get("FPHM")==null?"":map.get("FPHM"));
        	clidjson.put("KPRQ",map.get("KPRQ")==null?"":map.get("KPRQ"));
        	clidjson.put("XHSH",map.get("XHSH")==null?"":map.get("XHSH"));
        	
        	clidjson.put("WSJE",map.get("WSJE")==null?"":map.get("WSJE"));
        	clidjson.put("SL",map.get("SL")==null?"":map.get("SL"));
        	clidjson.put("SE",map.get("SE")==null?"":map.get("SE"));
        	clidjson.put("FPMW",map.get("FPMW")==null?"":map.get("FPMW"));
        	clidjson.put("CZR",map.get("CZR")==null?"":map.get("CZR"));
        	
        	clidjson.put("HZMW",map.get("HZMW")==null?"":map.get("HZMW"));
        	clidjson.put("GFMC",map.get("GFMC")==null?"":map.get("GFMC"));
        	clidjson.put("GFDM",map.get("GFDM")==null?"":map.get("GFDM"));
        	clidjson.put("XHMC",map.get("XHMC")==null?"":map.get("XHMC"));
        	clidjson.put("XHDM",map.get("XHDM")==null?"":map.get("XHDM"));
        	
        	if(map.get("HZFWBZ")==null){
            	clidjson.put("HZFWBZ","");
        	}else{
            	clidjson.put("HZFWBZ",map.get("HZFWBZ").toString().equals("0")?"非汉字防伪":"汉字防伪");
        	}
        	clidjson.put("content",map.get("content")==null?"":map.get("content"));
        	clidjson.put("DJBH",map.get("DJBH")==null?"":map.get("DJBH"));
        	
        	String[] rzzt={"未认证","认证不通过","认证通过"};
        	if(map.get("RZZT")==null){
            	clidjson.put("RZZT","");
        	}else{
            	clidjson.put("RZZT",rzzt[Integer.parseInt(map.get("RZZT").toString())]);
        	}
        	clidjson.put("RZR",map.get("RZR")==null?"":map.get("RZR"));
        	
        	clidjson.put("RZSJ",map.get("RZSJ")==null?"":map.get("RZSJ"));
        	clidjson.put("machine",map.get("machine")==null?"":map.get("machine"));
        	clidjson.put("MAC",map.get("MAC")==null?"":map.get("MAC"));
        	clidjson.put("IP",map.get("IP")==null?"":map.get("IP"));
        	
            Map sjly=new HashMap();
        	sjly.put("TXZH", "图形转换结构化数据");
        	sjly.put("FPDR", "发票导入");
        	sjly.put("WBXT", "外部系统写入");
        	sjly.put("DYJH", "打印机截获数据");

        	if(map.get("SJLY")==null||map.get("SJLY").toString().equals("")){
        		clidjson.put("SJLY","");
        	}else{
            	clidjson.put("SJLY",sjly.get(map.get("SJLY").toString()));
        	}
        	
        	
        	Map FPLX=new HashMap();
        	FPLX.put("01", "普通发票");
        	FPLX.put("02", "二维码发票");
        	FPLX.put("03", "运输发票");

        	if(map.get("FPLX")==null||map.get("FPLX").toString().equals("")){
        		clidjson.put("FPLX","");
        	}else{
            	clidjson.put("FPLX",FPLX.get(map.get("FPLX").toString()));
        	}      
        	if(map.get("F_ZHZT")==null){
            	clidjson.put("F_ZHZT","");
        	}else{
            	clidjson.put("F_ZHZT",map.get("F_ZHZT").toString().equals("1")?"1：已转换为图形":"0:未转换为图形");
        	}
        	 
        	Map DJZT=new HashMap();
        	DJZT.put("BZ", "编辑");
        	DJZT.put("CS", "已传输");
        	DJZT.put("RZ", "认证通过");
        	DJZT.put("SB", "认证失败");
        	DJZT.put("ZF", "作废");
        	if(map.get("DJZT")==null||map.get("DJZT").toString().equals("")){
        		clidjson.put("DJZT","");
        	}else{
            	clidjson.put("DJZT",DJZT.get(map.get("DJZT").toString()));
        	}  
        	array.add(clidjson);
        }
        json.put("rows", array);

		return json;
	}
}
