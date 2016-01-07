package com.pansoft.dao;

import com.pansoft.entity.Problem;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class ProblemDao extends SqlMapClientDaoSupport {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public List<Problem> getProblems(){
		List<Problem> list = this.getSqlMapClientTemplate().queryForList("problem.problemList");
        for (Problem pr : list)
            pr.setRe_date(formatter.format(pr.getRecord_date()));
		return list;
	}

    public List<Problem> getProblemsByYz(String yz_id){
        List<Problem> list = this.getSqlMapClientTemplate().queryForList("problem.problemListByYz",yz_id);
        for (Problem pr : list)
            pr.setRe_date(formatter.format(pr.getRecord_date()));
        return list;
    }

    public Problem getProblem(int id){
        Problem cp = (Problem)this.getSqlMapClientTemplate().queryForObject("problem.problemOne", String.valueOf(id));
        cp.setRe_date(formatter.format(cp.getRecord_date()));
        return cp;
    }
    
    public List<Problem> getProblemReplies(int pid){
    	List<Problem> list = this.getSqlMapClientTemplate().queryForList("problem.problemReplies",	String.valueOf(pid));
        for (Problem pr : list)
            pr.setRe_date(formatter.format(pr.getRecord_date()));
    	return list;
    }

	public List<Problem> getProblemsWithReply(){
		
		List<Problem> list = null;
		return list;
	}
	
	public void addProblem(Problem problem){
		this.getSqlMapClientTemplate().insert("problem.insertProblem", problem);
	}

    public void updateProblemStatus(String id,String ext)
    {
        Map<String,String> map = new HashMap<String,String>();
        map.put("ext2", ext);
        map.put("id", id);
        this.getSqlMapClientTemplate().update("problem.updateProblemStatus",map);
    }

    public int newProblemCount()
    {
        return (Integer)this.getSqlMapClientTemplate().queryForObject("problem.existNewProblem");
    }
}
