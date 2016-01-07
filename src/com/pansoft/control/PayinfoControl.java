package com.pansoft.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pansoft.entity.*;
import com.pansoft.util.Consts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.service.PayService;

@Controller
public class PayinfoControl {

	@Resource
	private PayService ps;
	
	
	
	@RequestMapping(value = "/mYzJfxx.do") 
	public @ResponseBody List<PayInfo> getYzJfxx(@RequestParam String uid,@RequestParam String page)
    {
		String yz_id = uid;
		List<PayInfo> list = ps.getYzJfxx(yz_id,page);
		return list;
	}
	
	
	@RequestMapping(value = "/payinfo.do") 
	public ModelAndView initPayInfo(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/payinfo");
		String sq_id = "";
		HttpSession session = request.getSession(false);
		if(session != null){
			sq_id = (String) session.getAttribute("sq_id");
		}
		/**
		 * 测试
		 */
		if(sq_id==null || sq_id.equals("") || sq_id.equals("null")){
			sq_id = "1";
		}
		
		List<Paytype> types = ps.getPayType(sq_id);
		//获取时间
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		//获取楼号，单元
		List<CommunityBuild> builds = ps.getBuildsInfo(sq_id);
		
		mv.addObject("cur_year", year);
		mv.addObject("cur_month", month);
		mv.addObject("types", types);
		mv.addObject("builds", builds);
		return mv;
	}

    @RequestMapping(value = "/payed.do")
         public ModelAndView getPayedInfo(HttpServletRequest request, @ModelAttribute PayFilterParam filter){
        ModelAndView mv = new ModelAndView("/payed");
        String sq_id = "";
        HttpSession session = request.getSession(false);
        if(session != null){
            sq_id = (String) session.getAttribute("sq_id");
        }
        /**
         * 测试
         */
        if(sq_id==null || sq_id.equals("") || sq_id.equals("null")){
            sq_id = "1";
        }

        int page = 0;
        if(filter.getPage() != null)
        {
            page = Integer.parseInt(filter.getPage());
        }

        List<Paytype> types = ps.getPayType(sq_id);
        Paytype pt = new Paytype();
        pt.setId("0");
        pt.setName("请选择");
        types.add(0,pt);
        List<PayInfo> payedinfo = ps.getPayedinfo(filter);

        List<PayInfo> payRet = new ArrayList<PayInfo>();
        int start = page* Consts.payinfo_num_per_page;
        int end = (page+1)*Consts.payinfo_num_per_page;
        if(payedinfo.size()<end)
            end = payedinfo.size();
        for(;start<end;start++)
        {
            payRet.add(payedinfo.get(start));
        }


        mv.addObject("types", types);
        mv.addObject("payedinfo",payRet);
        mv.addObject("pmax", payedinfo.size());
        mv.addObject("page", page);
        mv.addObject("startDate",filter.getStartDate());
        mv.addObject("endDate",filter.getEndDate());
        mv.addObject("type",filter.getPaytype());

        return mv;
    }

    @RequestMapping(value = "/unpay.do")
    public ModelAndView getUnPayInfo(HttpServletRequest request, @ModelAttribute PayFilterParam filter){
        ModelAndView mv = new ModelAndView("/unpay");
        String sq_id = "";
        HttpSession session = request.getSession(false);
        if(session != null){
            sq_id = (String) session.getAttribute("sq_id");
        }
        /**
         * 测试
         */
        if(sq_id==null || sq_id.equals("") || sq_id.equals("null")){
            sq_id = "1";
        }

        int page = 0;
        if(filter.getPage() != null)
        {
            page = Integer.parseInt(filter.getPage());
        }

        List<Paytype> types = ps.getPayType(sq_id);
        Paytype pt = new Paytype();
        pt.setId("0");
        pt.setName("请选择");
        types.add(0,pt);
        List<PayInfo> payedinfo = ps.getUnPayinfo(filter);

        List<PayInfo> payRet = new ArrayList<PayInfo>();
        int start = page* Consts.payinfo_num_per_page;
        int end = (page+1)*Consts.payinfo_num_per_page;
        if(payedinfo.size()<end)
            end = payedinfo.size();
        for(;start<end;start++)
        {
            payRet.add(payedinfo.get(start));
        }


        mv.addObject("types", types);
        mv.addObject("payedinfo",payRet);
        mv.addObject("pmax", payedinfo.size());
        mv.addObject("page", page);
        mv.addObject("startDate",filter.getStartDate());
        mv.addObject("endDate",filter.getEndDate());
        mv.addObject("type",filter.getPaytype());

        return mv;
    }
	
	@RequestMapping(value = "/getUnit.do")
	public @ResponseBody List<CommunityBuild> getUnit(@RequestParam("id") String id){
		List<CommunityBuild> build = ps.getBuildsInfo(id);
		return build;
	}
	
	@RequestMapping(value = "/searchPayinfo.do")
	public @ResponseBody PayInfos searchPayinfo(@RequestParam("build") String build,@RequestParam("unit") String unit,
			@RequestParam("year") String year,@RequestParam("month") String month,@RequestParam("type") String type,HttpServletRequest request){
		String sq_id = "";
		HttpSession session = request.getSession(false);
		if(session != null){
			sq_id = (String) session.getAttribute("sq_id");
		}
		/**
		 * 测试
		 */
		if(sq_id==null || sq_id.equals("") || sq_id.equals("null")){
			sq_id = "1";
		}
		PayInfos pay = new PayInfos();
		pay.setBuild_id(build);
		pay.setUnit_id(unit);
		//获取楼层
		CommunityBuild b = ps.getBuild(build);
		pay.setFloor_num(b.getTotal_num());
		pay.setBuild_id(b.getBuild_id());
		//出来时间
		String date = "";
		if(month == null || month.equals("")){
			date = String.valueOf(Integer.valueOf(year)+1) + "-1-1";
		}else{
			date = year + "-"+month +"-1";
		}
		//获取单元信息
		List<CommunityBuild> units;
		if(unit==null || unit.equals("")){
			units = ps.getBuildsInfo(build);
			
			List<CommunityBuild> qf = ps.getPayinfo(type,date ,sq_id,b.getBuild_id());
			pay.setPays(qf);
		}else{
			units = new ArrayList<CommunityBuild>();
			CommunityBuild u = ps.getBuild(unit);
			units.add(u);
			pay.setUnit_id(u.getBuild_id());
			//缴费信息
			List<CommunityBuild> qf = ps.getPayinfo(type,date ,sq_id,b.getBuild_id(),u.getBuild_id());
			pay.setPays(qf);
		}
		pay.setUnits(units);
		return pay;
	}
	
	
	@RequestMapping(value = "/getPayinfo.do")
	public ModelAndView getPayInfo(@RequestParam("name") String name,@RequestParam("id") String id,HttpServletRequest request){
		ModelAndView mv = new ModelAndView("/phone/lifepayinfo");
		HttpSession session = request.getSession(false);
		String yz_id = null,sq_id = null ;
		UserInfo userinfo = null;
		if(session != null){
			yz_id = (String) session.getAttribute("loginid");
			userinfo = (UserInfo) session.getAttribute("yz_user");
//			sq_id = userinfo.getSq_id();
		}
		
		//测试
		if(sq_id==null||yz_id==null){
			userinfo = new UserInfo();
			userinfo.setLouhao("11");
			userinfo.setName("zhang");
			sq_id="1";
			yz_id="1";
		}
		
		
		//获取收费标准
//		Paytype paytype = ps.getPayTypeByName(sq_id,name);
		Paytype paytype = ps.getPayTypeById(name);
		String pay_id="";
		if(paytype != null){
			pay_id = paytype.getId();
		}
		List<PayInfo> list = new ArrayList<PayInfo>();
		if(pay_id!=null && !pay_id.equals("")){
			list = ps.getPayinfo(yz_id,pay_id);
		}
		mv.addObject("userinfo",userinfo);
		mv.addObject("name", name);
		mv.addObject("list", list);
		mv.addObject("paytype",paytype);
		return mv;
	}
}
