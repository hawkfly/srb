package com.pansoft.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pansoft.dao.NoticeDao;
import com.pansoft.entity.Notice;
import com.pansoft.util.Consts;

@Service
public class NoticeService {

	@Resource
	private NoticeDao nd;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public List<Notice> getPublicNotice() {
		// TODO Auto-generated method stub
        List<Notice> notices = nd.getPublicNotice();

        for (Notice n : notices){
            n.setSend_date_str(sdf.format(n.getSend_date()));
        }
		return notices;
	}

	/**
	 * 分页获取 全体公告
	 * @param page
	 * @return
	 */
	public List<Notice> getPublicNoticePage(String page) {
		// TODO Auto-generated method stub
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Notice> list = nd.getPublicNotice();
		List<Notice> temp = new ArrayList<Notice>();
		int start = Integer.valueOf(page)*Consts.notice_num_per_page;
		int end = (Integer.valueOf(page)+1)*Consts.notice_num_per_page;
		if(end > list.size()){
			end = list.size();
		}
		for(;start<end;start++){
			Notice n = list.get(start);
			n.setExt1(format.format(n.getSend_date()));
			n.setContent(n.getContent()==null?"":n.getContent());
			temp.add(n);
		}
		return temp;
	}

	public void addNotice(Notice notice) {
		// TODO Auto-generated method stub
		nd.addNotice(notice);
	}

	public int delNotice(String id) {
		// TODO Auto-generated method stub
		return nd.delNotice(id);
	}

	public int upNotice(String id) {
		// TODO Auto-generated method stub
		return nd.upNotice(id);
	}

	public int downNotice(String id) {
		// TODO Auto-generated method stub
		return nd.downNotice(id);
	}
	
	public String getWdNotice(String user_id){
		String wd = nd.getWdNotice(user_id);
		return wd;
	}
	
	public void WdToYd(String user_id){
		nd.WdToYd(user_id);
	}
}
