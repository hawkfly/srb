package com.pansoft.service;

import com.pansoft.dao.ProblemDao;
import com.pansoft.entity.Complaint;
import com.pansoft.entity.Problem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProblemService {

	@Resource
	private ProblemDao cd;
	
	
	/**
	 * 分页获取提问
	 * @param page
	 * @return
	 */
	public List<Problem> getProblems(int page,int size) {
		// TODO Auto-generated method stub
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Problem> list = cd.getProblems();
		List<Problem> temp = new ArrayList<Problem>();
		int start = page*size;
		int end = (page+1)*size;
		if(end > list.size()){
			end = list.size();
		}
		for(;start<end;start++){
			Problem n = list.get(start);
			//n.setExt1(format.format(n.getSend_date()));
			n.setContent(n.getContent()==null?"":n.getContent());
			temp.add(n);
		}
		return temp;
	}

    public List<Problem> getProblemsByYz(String yz_id) {
        // TODO Auto-generated method stub
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Problem> list = cd.getProblemsByYz(yz_id);

        return list;
    }


	
	public void addReply(Problem cp,String status)
	{
		cd.addProblem(cp);
        cd.updateProblemStatus(cp.getPid(),status);
	}

    public void newProblem(Problem cp)
    {
        cd.addProblem(cp);
    }
	
	public Problem getProblemWithReply(int id)
    {
        Problem cp = cd.getProblem(id);
        cp.setReplies(cd.getProblemReplies(id));
        return cp;
    }

    public List<Problem> getProblemReplies(int id) {
        return cd.getProblemReplies(id);
    }

    public boolean existNewProblem()
    {
        return cd.newProblemCount()>0;
    }
}
