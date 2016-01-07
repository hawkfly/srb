package com.pansoft.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.pansoft.entity.Problem;
import org.springframework.stereotype.Service;

import com.pansoft.dao.ComplaintDao;
import com.pansoft.entity.Complaint;
import com.pansoft.entity.Notice;
import com.pansoft.util.Consts;

@Service
public class ComplaintService {

	@Resource
	private ComplaintDao cd;
	
	
	/**
	 * 分页获取提问
	 * @param page
	 * @return
	 */
	public List<Complaint> getComplaints(int page,int size ,String type) {
		// TODO Auto-generated method stub
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Complaint> list = cd.getComplaints(type);
		List<Complaint> temp = new ArrayList<Complaint>();
		int start = page*size;
		int end = (page+1)*size;
		if(end > list.size()){
			end = list.size();
		}
		for(;start<end;start++){
			Complaint n = list.get(start);
			//n.setExt1(format.format(n.getSend_date()));
			n.setContent(n.getContent()==null?"":n.getContent());
			temp.add(n);
		}
		return temp;
	}

    public List<Complaint> getComplaintsByYz(String yz_id) {
        // TODO Auto-generated method stub
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Complaint> list = cd.getComplaintsByYz(yz_id);

        return list;
    }

    public List<Complaint> getComplaintsByType(String type) {
        // TODO Auto-generated method stub
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Complaint> list = cd.getComplaintsByType(type);

        return list;
    }
	
	public void addReply(Complaint cp,String status)
	{
		cd.addComplaint(cp);
        cd.updateComplaintStatus(cp.getPid(),status);
	}

    public void newComplaint(Complaint cp)
    {
        cd.addComplaint(cp);
    }
	
	public Complaint getComplaintWithReply(int id)
    {
        Complaint cp = cd.getComplaint(id);
        cp.setReplies(cd.getComplaintReplies(id));
        return cp;
    }

    public List<Complaint> getComplaintReplies(int id)
    {
        return cd.getComplaintReplies(id);
    }

    public boolean existNewComplaint()
    {
        return cd.newCompaintCount()>0;
    }
}
