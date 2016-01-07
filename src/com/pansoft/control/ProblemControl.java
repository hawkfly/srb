package com.pansoft.control;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pansoft.entity.RightInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.entity.Problem;
import com.pansoft.service.*;

@Controller
public class ProblemControl {

	@Resource
	private ProblemService cs;
	
	@RequestMapping(value = "/problem.do")
         public ModelAndView getProblem(){
        final int page = 0;
        final int size = 20;
        ModelAndView mv = new ModelAndView("/problem");
        List<Problem> list = cs.getProblems(page, size);

        mv.addObject("problems",list);
        mv.addObject("pmax", list.size());
        mv.addObject("page", 0);
        return mv;
    }

    @RequestMapping(value = "/mproblem.do")
    public ModelAndView getProblemMobile(@RequestParam String uid){
        String loginid = uid;

        final int page = 0;
        final int size = 20;
        ModelAndView mv = new ModelAndView("/phone/mprlist");
        List<Problem> list = cs.getProblemsByYz(loginid);
        for(Problem cp : list)
        {
            cp.setReplies(cs.getProblemReplies(Integer.parseInt(cp.getId())));
        }

        mv.addObject("id",uid);
        mv.addObject("complaints",list);
        return mv;
    }

    @RequestMapping(value = "/problempartial.do")
    public @ResponseBody Problem getProblemPartial(){
        final int page = 0;
        final int size = 2000;
        List<Problem> list = cs.getProblems(page, size);
        Problem cp = new Problem();
        cp.setReplies(list);

        return cp;
    }

    @RequestMapping(value = "/prpage.do")
    public @ResponseBody List<Problem> publicProblemPage(@RequestParam("page") String page){
        List<Problem> list = cs.getProblems(Integer.parseInt(page),20);
        return list;
    }
    @RequestMapping(value = "/prjson.do")
    public @ResponseBody Problem getProblemWithReplies(@RequestParam("id") String id){
        Problem cp = cs.getProblemWithReply(Integer.parseInt(id));
        return cp;
    }

    @RequestMapping(value = "/mprdetail.do")
    public ModelAndView getComplaintWithRepliesMoblie(@RequestParam("id") String id,@RequestParam String uid){
        Problem cp = cs.getProblemWithReply(Integer.parseInt(id));
        ModelAndView mv = new ModelAndView("/phone/mprdetail");
        mv.addObject("id",uid);
        mv.addObject("cp",cp);
        return mv;
    }

    //回复
    @RequestMapping(value = "/replyProblem.do")
    public void replyProblem(HttpServletRequest request,@ModelAttribute Problem cp){
        String loginid = "0";
        HttpSession session = request.getSession();
        Object sessionId = session.getAttribute("loginid");
        if(sessionId != null)
            loginid = (String)sessionId;

        cp.setRecord_date(new Date());
        cp.setYz_id(loginid);
        cp.setExt1("1");
        cs.addReply(cp,"1");
    }

    //追问
    @RequestMapping(value = "/addProblem.do")
    public @ResponseBody String addProblem(@ModelAttribute Problem cp,@RequestParam String uid){
        cp.setRecord_date(new Date());
        cp.setYz_id(uid);
        cp.setExt1("0");
        cs.addReply(cp, "0");

        return "success";
    }

    //新报修
    @RequestMapping(value = "/newProblem.do")
    public @ResponseBody String newProblem(@ModelAttribute Problem cp,@RequestParam String uid){
        cp.setRecord_date(new Date());
        cp.setYz_id(uid);
        cp.setExt1("0");
        cp.setExt2("0");
        cp.setPid("0");
        cs.newProblem(cp);

        return "success";
    }

    @RequestMapping(value = "/mpradd.do")
    public ModelAndView mcpAdd(@RequestParam String uid){
        ModelAndView mv = new ModelAndView("/phone/mpradd");
        mv.addObject("id",uid);
        return mv;
    }

    @RequestMapping(value = "/existNewPr")
    public @ResponseBody String existNewProblem(HttpServletRequest request)
    {
        List<RightInfo> rightinfos = (List<RightInfo>)request.getSession().getAttribute("roleInfos");
        boolean hasRight = false;
        for(RightInfo ri : rightinfos)
        {
            if(ri.getName().equals("物业答复"))
            {   hasRight=true;
                break;
            }
        }
        if(!hasRight ) return "0";
        if (cs.existNewProblem())
            return "1";
        return "0";
    }
}
