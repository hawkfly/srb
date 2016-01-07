package com.pansoft.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.pansoft.entity.Notice;

@Component
@SuppressWarnings("unchecked")
public class NoticeDao extends SqlMapClientDaoSupport {

	public List<Notice> getPublicNotice() {
		// TODO Auto-generated method stub
		List<Notice> list = this.getSqlMapClientTemplate().queryForList("notice.publicNotice");
		return list;
	}

	public void addNotice(Notice notice) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("notice.insertNotice", notice);
	}

	public int delNotice(String id) {
		// TODO Auto-generated method stub
		int num = this.getSqlMapClientTemplate().delete("notice.delNotice", id);
		return num;
	}

	public int upNotice(String id) {
		// TODO Auto-generated method stub
		int num = this.getSqlMapClientTemplate().update("notice.upNotice", id);
		return num;
	}

	public int downNotice(String id) {
		// TODO Auto-generated method stub
		int num = this.getSqlMapClientTemplate().update("notice.downNotice", id);
		return num;
	}
	
	public String getWdNotice(String user_id){
		String wd = (String) this.getSqlMapClientTemplate().queryForObject("notice.getWdNotice", user_id);
		return wd;
	}
	
	public void WdToYd(String user_id){
		this.getSqlMapClientTemplate().insert("notice.WdToYd", user_id);
	}
	
}
