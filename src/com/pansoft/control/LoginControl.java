package com.pansoft.control;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pansoft.entity.Community;
import com.pansoft.entity.LoginedInfo;
import com.pansoft.entity.Retails;
import com.pansoft.entity.RightInfo;
import com.pansoft.entity.UserInfo;
import com.pansoft.entity.WyUser;
import com.pansoft.service.LoginService;

@Controller
public class LoginControl {

	@Resource
	private LoginService ls;
	
	@RequestMapping(value = "/xgmm.do")
	public @ResponseBody Map<String,String> xgmm(@RequestParam("username") String username,
			@RequestParam("password") String password,@RequestParam("rpassword") String rpassword,@RequestParam("type") String type,HttpServletRequest request){
		Map<String, String> res = new HashMap<String, String>();
		res.put("res", "error");
		
		if("ph".equals(type)){
			UserInfo user = new UserInfo();
			user.setUsername(username);
			user.setPassword(password);
			String jg = ls.checkPhMm(user);
			if("1".equals(jg)){
				user.setPassword(rpassword);
				ls.XgPhMM(user);
				 res.put("res", "success");
			}else{
				return res;
			}
			
		}else if("sj".equals(type)){
			Retails user = new Retails();
			user.setUsername(username);
			user.setPassword(password);
			String jg = ls.checkSjMm(user);
			if("1".equals(jg)){
				user.setPassword(rpassword);
				ls.XgSjMM(user);
				 res.put("res", "success");
			}else{
				return res;
			}
			
		}else if("wy".equals(type)){
			WyUser user = new WyUser();
			user.setUsername(username);
			user.setPassword(password);
			String jg = ls.checkWyMm(user);
			if("1".equals(jg)){
				user.setPassword(rpassword);
				ls.XgWyMM(user);
				 res.put("res", "success");
			}else{
				return res;
			}
		}
		
		
		
		return res;
	}
	
	@RequestMapping(value = "/login.do")
	public @ResponseBody Map<String,String> login(@RequestParam("username") String username,
			@RequestParam("password") String password,HttpServletRequest request) throws IOException{
		WyUser c  = new WyUser();
		c.setUsername(username);
		c.setPassword(password);
		List<WyUser> list = ls.getWyUser(c);
		Map<String,String> map = new HashMap<String,String>();
		if(list == null || list.size() == 0){
			//继续查询店铺
			Retails retails = new Retails();
			retails.setUsername(username);
			retails.setPassword(password);
			List<Retails> sj = ls.getRetailsUser(retails);
			if(sj == null || sj.size() == 0){
				map.put("code", "failed");
			}else{
				map.put("code", "success");
				retails = sj.get(0);
				HttpSession session = request.getSession();
				session.setAttribute("user", retails);
				session.setAttribute("user_type", "sj");
				//写记录表
				retails.setLogindate(Calendar.getInstance().getTime());
				ls.logindata(retails);
			}
			
		}else{
			map.put("code", "success");
			c = list.get(0);
			HttpSession session = request.getSession();
//			session.setAttribute("sq_id", c.getId());
			session.setAttribute("org_id", c.getOrg_id());
			session.setAttribute("sq_id", c.getOrg_id());
            session.setAttribute("loginid",c.getId());
			session.setAttribute("user", c);
			session.setAttribute("user_type", "wy");
			//获取用户权限
			String roles = c.getRoles();
			List<RightInfo> roleInfos = ls.getRightInfos(roles);
			
			session.setAttribute("roleInfos", roleInfos);
			
			
			//写记录表
			c.setLogindate(Calendar.getInstance().getTime());
			ls.logindata(c);
		}
		
		return map;
	}
	
	@RequestMapping("/leave")
	public @ResponseBody Map<String,String> leave(HttpServletRequest req, HttpServletResponse rsp){
		try{
			HttpSession httpSession = req.getSession(false);
			if (httpSession == null){
				//http session 已经超长失效
			}else{
				httpSession.invalidate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//repo
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "");
		return map;
	}


	@RequestMapping(value = "/mlogin.do")
    public @ResponseBody Map<String,String> moblielogin(@RequestParam("username") String username,
                                                  @RequestParam("password") String password,HttpServletRequest request){

        //for test
//        username = "11101";
//        password = "1";

    	HttpSession httpSession = request.getSession(false);
		if (httpSession == null){
			//http session 已经超长失效
		}else{
			httpSession.invalidate();
		}
		
        UserInfo ui  = new UserInfo();
        ui.setUsername(username);
        ui.setPassword(password);

        UserInfo logined = ls.getUserInfo(ui);
        Map<String,String> map = new HashMap<String,String>();
        if(logined == null){
            map.put("code", "failed");
        }else{
            map.put("code", "success");
            HttpSession session = request.getSession();
            session.setAttribute("loginid", logined.getId());
            session.setAttribute("yz_user", logined);
            //写记录表
            logined.setLogindate(Calendar.getInstance().getTime());
            ls.logindata(logined);
        }

        return map;
    }

    @RequestMapping(value = "/mloginjson.do")
    public @ResponseBody
    JSONPObject moblieloginjson(@RequestParam("username") String username,
                                @RequestParam("password") String password,String callback){

        //for test
//        username = "11101";
//        password = "1";

        UserInfo ui  = new UserInfo();
        ui.setUsername(username);
        ui.setPassword(password);

        LoginedInfo info = new LoginedInfo();
        info.id = "-1";
        info.sq_id="-1";

        UserInfo logined = ls.getUserInfo(ui);
        Map<String,String> map = new HashMap<String,String>();
        if(logined != null){
            //写记录表
            logined.setLogindate(Calendar.getInstance().getTime());
            ls.logindata(logined);

            info.id = logined.getId();
            info.sq_id = logined.getSq_id();
        }

        return new JSONPObject(callback, info);
    }
}