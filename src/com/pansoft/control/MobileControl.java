package com.pansoft.control;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pansoft.service.MobileService;


@Controller
@RequestMapping(value = "/m") 
public class MobileControl {

	@Resource
	private MobileService ms;
	
	@RequestMapping(value = "/static/goods.do")
	public @ResponseBody Map<String,String> creatGoodsPage(@RequestParam("id") String id){
		String path = this.getClass().getResource("/").getPath();
		path = path.substring(1, path.indexOf("WEB-INF/classes"));
		
		String result = ms.creatGoodsPage(path,id);
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", result);
		return map;
	}
	
	
	@RequestMapping(value = "/static/retails.do")
	public @ResponseBody Map<String,String> creatRetailsPage(@RequestParam("id") String id){
		String path = this.getClass().getResource("/").getPath();
		path = path.substring(1, path.indexOf("WEB-INF/classes"));
		
		String result = ms.creatRetailsPage(path,id);
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", result);
		return map;

	}
}
