package com.pansoft.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.entity.BuyInfo;
import com.pansoft.entity.Goods;
import com.pansoft.entity.OrderRecord;
import com.pansoft.entity.Retails;
import com.pansoft.entity.UserInfo;
import com.pansoft.service.RetailsService;

@Controller
public class RetailsControl {

	@Resource
	private RetailsService rs;
	
	
	@RequestMapping(value="/androidtest.do")
	public void test(@RequestParam("num") String num,HttpServletResponse res){
		
		res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        
        List<Map<String, Object>> lr = rs.getAndroidRetails(num);
        String t = JSONArray.fromObject(lr).toString();
        try {
			res.getWriter().write(t);
//			res.getOutputStream().write("真的很奇怪,日本人！".getBytes("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@RequestMapping(value="/androidgoods.do")
	public void getAndroidGodds(@RequestParam("num") String num,HttpServletResponse res){
		
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		List<Map<String, Object>> lr = rs.getAndroidGoods(num);
		String t = JSONArray.fromObject(lr).toString();
		try {
			res.getWriter().write(t);
//			res.getOutputStream().write("真的很奇怪,日本人！".getBytes("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/showRetails.do") 
	public ModelAndView showRetails(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/phone/retails");
		String sq_id = "";
		HttpSession session = request.getSession(false);
		if(session != null){
			UserInfo userinfo = (UserInfo) session.getAttribute("yz_user");
			sq_id = userinfo.getSq_id();
		}
		/**
		 * 测试
		 */
		if(sq_id==null || "".equals(sq_id) || "null".equals(sq_id)){
			sq_id = "1";
		}
		List<Retails> retails = rs.getRetails(sq_id);
		mv.addObject("retails", retails);
		return mv;
	}
	
	@RequestMapping(value="/getRetail.do",method=RequestMethod.GET)
	public @ResponseBody JSONPObject getRetail(@RequestParam String callback,HttpServletRequest request){
		List<Retails> res = new ArrayList<Retails>();
		String sq_id = "";
//		HttpSession session = request.getSession(false);
//		if(session != null){
//			UserInfo userinfo = (UserInfo) session.getAttribute("yz_user");
//			sq_id = userinfo.getSq_id();
//		}
//		/**
//		 * 测试
//		 */
//		if(sq_id==null || "".equals(sq_id) || "null".equals(sq_id)){
//			sq_id = "1";
//		}
		sq_id="1";
		res = rs.getRetails(sq_id);
		
		
		return new JSONPObject(callback, res);
	}
	
	
	@RequestMapping(value = "/showGoods.do") 
	public ModelAndView showGoods(@RequestParam("sj_id") String sj_id,@RequestParam("name") String name,@RequestParam("id") String id){
		ModelAndView mv = new ModelAndView("/phone/goods");
		List<Goods> goods = rs.getGoods(sj_id);
		Retails r = rs.getRetailsByid(sj_id);
		mv.addObject("goods", goods);
		mv.addObject("sj_name", r.getName());
		return mv;
	}
	
	@RequestMapping(value = "/viewGoods.do") 
	public ModelAndView viewGood(@RequestParam("id") String id,@RequestParam("lid") String lid){
		ModelAndView mv = new ModelAndView("/phone/goodinfo");
		Goods goods = rs.getGoodsById(id);
		mv.addObject("id", lid);
		mv.addObject("goods", goods);
		return mv;
	}
	
	@RequestMapping(value = "/getOrderUser.do") 
	public @ResponseBody UserInfo getOrderUser(HttpServletRequest request){
		UserInfo userinfo = null;
		HttpSession session = request.getSession(false);
		if(session != null){
			userinfo = (UserInfo) session.getAttribute("yz_user");
		}
		if(userinfo == null){
			userinfo = new UserInfo();
			userinfo.setName("测试名称");
			userinfo.setRemark("测试送货地址");
			userinfo.setExt1("111111111111111111");
			userinfo.setExt2("222222222222222222");
		}
		return userinfo;
	}
	
	@RequestMapping(value = "/updateOrder.do",method = RequestMethod.POST)
	public @ResponseBody Map<String,String> updateOrder(@RequestBody OrderRecord order,HttpServletRequest req){
		Map<String,String> map = new HashMap<String,String>();
		
		String yz_id = (String) req.getSession().getAttribute("loginid");
		
		rs.addOrderRecord(order,yz_id);
		map.put("code", "success");
		return map;
	}
	
	@RequestMapping(value = "/updateOrderState.do")
	public @ResponseBody Map<String,String> updateOrderState(@RequestParam("id") String id,@RequestParam("state") String state){
		Map<String,String> map = new HashMap<String,String>();
		BuyInfo buyinfo = new BuyInfo();
		buyinfo.setId(id);
		buyinfo.setState(state);
		rs.updateOrderState(buyinfo);
		map.put("code", "success");
		return map;
	}
	
	@RequestMapping(value = "/getOrderOfUser.do")
	public @ResponseBody JSONPObject getOrderOfUser(@RequestParam String uid,@RequestParam String callback){
		List<OrderRecord> list = new ArrayList<OrderRecord>();

			list = rs.getOrderOfUser(uid);
		return new JSONPObject(callback, list);
	}
	
	@RequestMapping(value = "/updateOrderAllState.do")
	public @ResponseBody JSONPObject updateOrderState(@RequestParam String ddid,@RequestParam String uid,@RequestParam String callback){
		Map<String,String> map = new HashMap<String,String>();
		rs.updateOrderState(ddid);
		map.put("code", "success");
		return new JSONPObject(callback, map);
	}
	
	
//	public  String listmap_to_json_string(List<Map<String, Object>> list)
//	{       
//	  /*  for (Map<String, Object> map : list) {
////	        JSONObject json_obj=new JSONObject();
////	        for (Map.Entry<String, Object> entry : map.entrySet()) {
////	            String key = entry.getKey();
////	            Object value = entry.getValue();
////	            try {
////	                json_obj.put(key,value);
////	            } catch (Exception e) {
////	                e.printStackTrace();
////	            }                           
////	        }
////	        json_arr.add(json_obj);
////	    }*/
//	    
//	    for(int i=0;i<list.size();i++){
//	    	Map<String, Object> tmp = list.get(i);
//	    	Iterator ite=tmp.entrySet().iterator();
//	    	 JSONObject json_obj=new JSONObject();
//	    	while(ite.hasNext()){//只遍历一次,速度快
//	    	Map.Entry e=(Map.Entry)ite.next();
//	    	System.out.println(e.getKey()+"="+e.getValue());
//	    	json_obj.put(e.getKey(), e.getValue());
//	    }
//	    json_obj.to
//	    
//	    }
//	    
//	    return json_arr.toString();
//	}

}
