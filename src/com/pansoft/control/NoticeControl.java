package com.pansoft.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.entity.Complaint;
import com.pansoft.entity.Notice;
import com.pansoft.entity.Order;
import com.pansoft.entity.Retails;
import com.pansoft.entity.UserInfo;
import com.pansoft.service.ComplaintService;
import com.pansoft.service.NoticeService;
import com.pansoft.service.RetailsService;
import com.pansoft.util.Consts;

@Controller
public class NoticeControl {

	@Resource
	private NoticeService ns;
	
	@Resource
	private ComplaintService cs;
	
	@Resource
	private RetailsService rs;
	
	/**
	 * 进入网站首页
	 * @return
	 */
	@RequestMapping(value = "/index.do") 
	public ModelAndView getPublicNotice(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/index"); 
		HttpSession session = request.getSession();
		if(session == null){
			return mv;
		}
		String user_type = (String) session.getAttribute("user_type");	//用户类别
		if(user_type == null){
			user_type = "wy";
		}
		if(user_type.equals("wy")){
			List<Notice> list = ns.getPublicNotice();
			int pmax = list.size();
			if(pmax > Consts.notice_num_per_page){
				List<Notice> temp = new ArrayList<Notice>();
				for(int i=0; i<Consts.notice_num_per_page; i++){
					temp.add(list.get(i));
				}
				list = temp;
			}
			//获取需要答复的业主提问
			final int page = 0;
			final int size = 20;
			List<Complaint> list_c = cs.getComplaints(page, size,"0");
			
			mv.addObject("complaints",list_c);
			
			mv.addObject("notices", list);
			mv.addObject("pmax", pmax);
			mv.addObject("page", 0);
		}else if(user_type.equals("sj")){
			Retails retail = (Retails)session.getAttribute("user");
			
			List<Order> orders = rs.getOrderOfSj(retail.getId());
			mv.addObject("pmax", 0);
			mv.addObject("page", 0);
			mv.addObject("orders", orders);
		}
		
		mv.addObject("user_type", user_type);
		return mv;
	}
	
	
	/**
	 * 进入网站首页
	 * @return
	 */
	@RequestMapping(value = "/main.do") 
	public ModelAndView mainPage(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/mainpage"); 
		HttpSession session = request.getSession();
		if(session == null){
			return mv;
		}
		String user_type = (String) session.getAttribute("user_type");	//用户类别
		if(user_type == null){
			user_type = "wy";
		}
		if(user_type.equals("wy")){
			List<Notice> list = ns.getPublicNotice();
			int pmax = list.size();
			if(pmax > Consts.notice_num_per_page){
				List<Notice> temp = new ArrayList<Notice>();
				for(int i=0; i<Consts.notice_num_per_page; i++){
					temp.add(list.get(i));
				}
				list = temp;
			}
			
			mv.addObject("notices", list);
			mv.addObject("pmax", pmax);
			mv.addObject("page", 0);
		}else if(user_type.equals("sj")){
			Retails retail = (Retails)session.getAttribute("user");
			
			List<Order> orders = rs.getOrderOfSj(retail.getId());
			mv.addObject("pmax", 0);
			mv.addObject("page", 0);
			mv.addObject("orders", orders);
		}
		
		mv.addObject("user_type", user_type);
		return mv;
	}

	@RequestMapping(value = "/editNote.do") 
	public ModelAndView editNote(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/editNote"); 
		List<Notice> list = ns.getPublicNotice();
		int pmax = list.size();
		if(pmax > Consts.notice_num_per_page){
			List<Notice> temp = new ArrayList<Notice>();
			for(int i=0; i<Consts.notice_num_per_page; i++){
				temp.add(list.get(i));
			}
			list = temp;
		}
		
		mv.addObject("notices", list);
		mv.addObject("pmax", pmax);
		mv.addObject("page", 0);
		return mv;
	}
	
	@RequestMapping(value = "/npage.do")
	public @ResponseBody List<Notice> publicNoticePage(@RequestParam("page") String page){

		List<Notice> list = ns.getPublicNoticePage(page);
		return list;
	}
	
	@RequestMapping(value = "/test.do",method=RequestMethod.GET)
	public @ResponseBody JSONPObject Test(@RequestParam("page") String page,@RequestParam String callback){
		List<Notice> list = ns.getPublicNoticePage(page);
//		return list;
		return new JSONPObject(callback, list); 
	}
	
	@RequestMapping(value = "/addNotice.do")
	public void addNotice(@ModelAttribute Notice notice){
		ns.addNotice(notice);
	}
	
	@RequestMapping(value = "/delNotict.do")
	public @ResponseBody Map<String,String> delNotice(@RequestParam("id") String id){
		int num = ns.delNotice(id);
		Map<String,String> map = new HashMap<String,String>();
		map.put("num", String.valueOf(num));
		return map;
	}
	
	@RequestMapping(value = "/upNotict.do")
	public @ResponseBody Map<String,String> upNotice(@RequestParam("id") String id){
		int num = ns.upNotice(id);
		Map<String,String> map = new HashMap<String,String>();
		map.put("num", String.valueOf(num));
		return map;
	}
	
	@RequestMapping(value = "/downNotict.do")
	public @ResponseBody Map<String,String> downNotice(@RequestParam("id") String id){
		int num = ns.downNotice(id);
		Map<String,String> map = new HashMap<String,String>();
		map.put("num", String.valueOf(num));
		return map;
	}


    @RequestMapping(value = "/mnotice.do")
    public ModelAndView getNoticeMobile(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mv = new ModelAndView("/phone/notice");

        HttpSession session = request.getSession();
        UserInfo ui = (UserInfo)session.getAttribute("yz_user");
        String user_id = (String)session.getAttribute("loginid");
        if(ui==null)
            try {
                response.sendRedirect("/community/mindex.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        ns.WdToYd(user_id);
        mv.addObject("user",ui);

        List<Notice> list = ns.getPublicNotice();
        int pmax = list.size();
        if(pmax > Consts.notice_num_per_page){
            List<Notice> temp = new ArrayList<Notice>();
            for(int i=0; i<Consts.notice_num_per_page; i++){
                temp.add(list.get(i));
            }
            list = temp;
        }

        mv.addObject("notices", list);
        mv.addObject("pmax", pmax);
        mv.addObject("page", 0);


        return mv;
    }


    @RequestMapping(value = "/mnoticejson.do")
    public @ResponseBody JSONPObject getNoticeMobileJson(@RequestParam String uid,@RequestParam String callback){


        ns.WdToYd(uid);

        List<Notice> list = ns.getPublicNotice();
        int pmax = list.size();
        if(pmax > Consts.notice_num_per_page){
            List<Notice> temp = new ArrayList<Notice>();
            for(int i=0; i<Consts.notice_num_per_page; i++){
                temp.add(list.get(i));
            }
            list = temp;
        }


        return new JSONPObject(callback, list);
    }
}
