package com.pansoft.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pansoft.service.LoginService;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.entity.UserInfo;
import com.pansoft.service.NoticeService;
import com.pansoft.service.PayService;

/**
 * Created by zhang_000 on 13-12-24.
 */
@Controller
public class IndexControl {

	@Resource
	private PayService ps;
	@Resource
	private NoticeService ns;
    @Resource
    private LoginService ls;
	
    @RequestMapping(value = "/mindex.do")
    public ModelAndView getIndexInfo(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mv = new ModelAndView("/phone/index");
        HttpSession session = request.getSession(false);
        UserInfo user = (UserInfo)session.getAttribute("yz_user");
        if(user==null)
            try {
                response.sendRedirect("/community/mlogin.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
       
        mv.addObject("user",user);
        return mv;
    }
    
    @RequestMapping(value="/getNum.do")
    public @ResponseBody JSONPObject getNum(@RequestParam String uid,@RequestParam String callback){
    	 Map<String, String> map = new HashMap<String, String>();
    	
    	 String wjf = ps.getYzWjf(uid);
         String wd = ns.getWdNotice(uid);

         map.put("wjf",wjf);
         map.put("wd",wd);
         
         return new JSONPObject(callback, map);
    }
    
    @RequestMapping(value = "/myzinfo.do")
    public ModelAndView getyzInfo(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mv = new ModelAndView("/phone/yzinfo");
        HttpSession session = request.getSession();
        UserInfo ui = (UserInfo)session.getAttribute("yz_user");
        if(ui==null)
            try {
                response.sendRedirect("/community/mlogin.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }

        mv.addObject("user",ui);
        return mv;
    }

    @RequestMapping(value = "/myzinfojson.do")
    public @ResponseBody JSONPObject getyzInfoJson(@RequestParam String uid,@RequestParam String callback){


        UserInfo ui = new UserInfo();
        ui.setId(uid);
        ui = ls.getUserInfoById(ui);
        
        /*测试*/
        
        if(ui==null){
        	ui = new UserInfo();
/*        	ui.setUsername("11101");
        	ui.setName("张三");
        	ui.setPhone("123123");*/
        }

        return new JSONPObject(callback, ui);
    }
}
