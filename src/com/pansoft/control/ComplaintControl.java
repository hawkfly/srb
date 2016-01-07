package com.pansoft.control;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pansoft.entity.RightInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.pansoft.entity.Complaint;
import com.pansoft.service.ComplaintService;
import com.pansoft.util.Consts;

@Controller
public class ComplaintControl {

	@Resource
	private ComplaintService cs;
	
	@RequestMapping(value = "/complaint.do")
         public ModelAndView getComplaint(){
        final int page = 0;
        final int size = 20;
        ModelAndView mv = new ModelAndView("/complaint");
        List<Complaint> list = cs.getComplaints(page, size,"0");

        mv.addObject("complaints",list);
        mv.addObject("pmax", list.size());
        mv.addObject("page", 0);
        return mv;
    }

    @RequestMapping(value = "/mcomplaint.do")
    public ModelAndView getComplaint(@RequestParam String uid){
        String loginid = uid;

        final int page = 0;
        final int size = 20;
        ModelAndView mv = new ModelAndView("/phone/mcplist");
        List<Complaint> list = cs.getComplaintsByYz(loginid);
        for(Complaint cp : list)
        {
            cp.setReplies(cs.getComplaintReplies(Integer.parseInt(cp.getId())));
        }

        mv.addObject("id",uid);
        mv.addObject("complaints",list);
        return mv;
    }

    @RequestMapping(value = "/mcomplaintt.do")
    public ModelAndView getComplaint(HttpServletRequest request,@RequestParam("type") String type,@RequestParam("id") String id){
        String loginid = "0";
        HttpSession session = request.getSession();
        Object sessionId = session.getAttribute("loginid");
        if(sessionId != null)
            loginid = (String)sessionId;

        final int page = 0;
        final int size = 20;
        ModelAndView mv = new ModelAndView("/phone/mcplistt");
        List<Complaint> list = cs.getComplaintsByType(type);
        for(Complaint cp : list)
        {
            cp.setReplies(cs.getComplaintReplies(Integer.parseInt(cp.getId())));
        }
        
       String typename = "";//Consts.getType(type);
        
       mv.addObject("id",id);
        mv.addObject("complaints",list);
        mv.addObject("typename",typename);
        return mv;
    }

    @RequestMapping(value = "/complaintpartial.do")
    public @ResponseBody Complaint getComplaintPartial(){
        final int page = 0;
        final int size = 2000;
        List<Complaint> list = cs.getComplaints(page, size, "0");
        Complaint cp = new Complaint();
        cp.setReplies(list);

        return cp;
    }

    @RequestMapping(value = "/cppage.do")
    public @ResponseBody List<Complaint> publicComplaintPage(@RequestParam("page") String page){
        List<Complaint> list = cs.getComplaints(Integer.parseInt(page),20,"0");
        return list;
    }

    @RequestMapping(value = "/cpjson.do")
    public @ResponseBody Complaint getComplaintWithReplies(@RequestParam("id") String id){
        Complaint cp = cs.getComplaintWithReply(Integer.parseInt(id));
        return cp;
    }

    @RequestMapping(value = "/mcpdetail.do")
    public ModelAndView getComplaintWithRepliesMoblie(@RequestParam("id") String id,@RequestParam String uid){
        Complaint cp = cs.getComplaintWithReply(Integer.parseInt(id));
        ModelAndView mv = new ModelAndView("/phone/mcpdetail");
        mv.addObject("id",uid);
        mv.addObject("cp",cp);
        return mv;
    }

    @RequestMapping(value = "/mcpdetailnr.do")
    public ModelAndView getComplaintWithRepliesMoblieNr(@RequestParam("id") String id,@RequestParam("lid") String lid){
        Complaint cp = cs.getComplaintWithReply(Integer.parseInt(id));
        ModelAndView mv = new ModelAndView("/phone/mcpdetailnr");
        mv.addObject("cp",cp);
        mv.addObject("id",lid);
        return mv;
    }

    //回复
    @RequestMapping(value = "/replyComplaint.do")
    public void replyComplaint(HttpServletRequest request, @ModelAttribute Complaint cp){
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
    @RequestMapping(value = "/addComplaint.do")
    public @ResponseBody String addComplaint( @ModelAttribute Complaint cp, @RequestParam String uid){
        cp.setRecord_date(new Date());
        cp.setYz_id(uid);
        cp.setExt1("0");
        cs.addReply(cp,"0");

        return "success";
    }

    //新提问
    @RequestMapping(value = "/newComplaint.do")
    public @ResponseBody String newComplaint(@ModelAttribute Complaint cp,@RequestParam String uid){

        cp.setRecord_date(new Date());
        cp.setYz_id(uid);
        cp.setExt1("0");
        cp.setExt2("0");
        cp.setPid("0");
        cs.newComplaint(cp);

        return "success";
    }

    @RequestMapping(value = "/mcpindex.do")
    public ModelAndView mcpIndex(){
        ModelAndView mv = new ModelAndView("/phone/mcpindex");
        return mv;
    }

    @RequestMapping(value = "/mcpadd.do")
    public ModelAndView mcpAdd(@RequestParam String uid){
        ModelAndView mv = new ModelAndView("/phone/mcpadd");
        mv.addObject("id",uid);
        return mv;
    }

    @RequestMapping(value = "/existNewCp")
    public @ResponseBody String existNewComplaint(HttpServletRequest request)
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

        if (cs.existNewComplaint())
            return "1";
        return "0";
    }
}
