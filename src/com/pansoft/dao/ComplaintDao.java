package com.pansoft.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.pansoft.entity.Complaint;

@Component
@SuppressWarnings("unchecked")
public class ComplaintDao extends SqlMapClientDaoSupport {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public List<Complaint> getComplaints(String type){
        List<Complaint> list = this.getSqlMapClientTemplate().queryForList("complaint.complaintList");
        for (Complaint cp : list)
            cp.setRe_date(formatter.format(cp.getRecord_date()));
		return list;
	}

    public List<Complaint> getComplaintsByYz(String yz_id){
        List<Complaint> list = this.getSqlMapClientTemplate().queryForList("complaint.complaintListByYz",yz_id);
        for (Complaint cp : list)
            cp.setRe_date(formatter.format(cp.getRecord_date()));
        return list;
    }

    public List<Complaint> getComplaintsByType(String type){
        List<Complaint> list = this.getSqlMapClientTemplate().queryForList("complaint.complaintListByType",type);
        for (Complaint cp : list)
            cp.setRe_date(formatter.format(cp.getRecord_date()));
        return list;
    }

    public Complaint getComplaint(int id){
        Complaint cp = (Complaint)this.getSqlMapClientTemplate().queryForObject("complaint.complaintOne", String.valueOf(id));
        cp.setRe_date(formatter.format(cp.getRecord_date()));

        return cp;
    }
    
    public List<Complaint> getComplaintReplies(int pid){
    	List<Complaint> list = this.getSqlMapClientTemplate().queryForList("complaint.complaintReplies",	String.valueOf(pid));
        for (Complaint cp : list)
            cp.setRe_date(formatter.format(cp.getRecord_date()));
    	return list;
    }

	public List<Complaint> getComplaintsWithReply(){
		
		List<Complaint> list = null;
		return list;
	}
	
	public void addComplaint(Complaint complaint){
		this.getSqlMapClientTemplate().insert("complaint.insertComplaint", complaint);
	}

    public void updateComplaintStatus(String id,String ext)
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("ext2", ext);
        map.put("id", id);
        this.getSqlMapClientTemplate().update("complaint.updateComplaintStatus",map);
    }

    public int newCompaintCount()
    {
        return (Integer)this.getSqlMapClientTemplate().queryForObject("complaint.existNewComplaint");
    }
}
