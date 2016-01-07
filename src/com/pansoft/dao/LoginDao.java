package com.pansoft.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.pansoft.entity.Community;
import com.pansoft.entity.Retails;
import com.pansoft.entity.RightInfo;
import com.pansoft.entity.RoleInfo;
import com.pansoft.entity.UserInfo;
import com.pansoft.entity.WyUser;

@Component
@SuppressWarnings("unchecked")
public class LoginDao extends SqlMapClientDaoSupport{
	
	public String checkPhMm(UserInfo user){
		String res = (String) this.getSqlMapClientTemplate().queryForObject("user.checkphuser", user);
		return res;
	}
	public String checkWyMm(WyUser com){
		String res = (String) this.getSqlMapClientTemplate().queryForObject("user.checkwyuser", com);
		return res;
	}
	public String checkSjMm(Retails ret){
		String res = (String) this.getSqlMapClientTemplate().queryForObject("user.checkpsjser", ret);
		return res;
	}
	
	public void XgPhMM(UserInfo user){
		this.getSqlMapClientTemplate().update("user.xgphmm",user);
	}
	public void XgWyMM(WyUser com){
		this.getSqlMapClientTemplate().update("user.xgwymm",com);
	}
	public void XgSjMM(Retails ret){
		this.getSqlMapClientTemplate().update("user.xgsjmm",ret);
	}
	
	public List<Community> getUser(Community c) {
		// TODO Auto-generated method stub
		List<Community> list = this.getSqlMapClientTemplate().queryForList("user.getUserByObj", c);
		return list;
	}

	public void logindate(Community c) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("user.logindata", c);
	}

    public void logindate(UserInfo ui) {
        // TODO Auto-generated method stub
        this.getSqlMapClientTemplate().insert("user.yzlogindata", ui);
    }
    
    public void logindate(WyUser c) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("user.wylogindata", c);
	}

    public List<UserInfo> getYzuser(UserInfo ui){
        List<UserInfo> list = this.getSqlMapClientTemplate().queryForList("user.getYzUserObj",ui);
        return list;
    }

    public List<UserInfo> getYzuserById(UserInfo ui){
        List<UserInfo> list = this.getSqlMapClientTemplate().queryForList("user.getYzUserObjById",ui);
        return list;
    }

	public List<Retails> getRetailsUser(Retails c) {
		// TODO Auto-generated method stub
		List<Retails> list = this.getSqlMapClientTemplate().queryForList("user.getSjUserObj",c);
        return list;
	}

	public void logindate(Retails retails) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("user.sjlogindata", retails);
	}
	public List<WyUser> getWyUser(WyUser c) {
		// TODO Auto-generated method stub
		List<WyUser> list = this.getSqlMapClientTemplate().queryForList("user.getWyUser",c);
        return list;
	}
	
	public List<RoleInfo> getRoleInfos(String roles){
		List<RoleInfo> list = this.getSqlMapClientTemplate().queryForList("user.getRoleInfos", roles);
		return list;
	}
	
	public List<RightInfo> getRightInfos(String ids){
		List<RightInfo> list = this.getSqlMapClientTemplate().queryForList("user.getRightInfos", ids);
		return list;
	}
	
}
