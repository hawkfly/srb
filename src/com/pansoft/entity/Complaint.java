package com.pansoft.entity;

import java.util.Date;
import java.util.List;

public class Complaint {

	private String id;
	private String yz_id;
	private String title;
	private String content;
	private Date record_date;
	private String re_content;
	private String re_date;
	private String pid;
	private String remark;
	private String ext1;
	private String ext2;
    private String type;
	private List<Complaint> replies;
    private String yzname;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYz_id() {
		return yz_id;
	}
	public void setYz_id(String yz_id) {
		this.yz_id = yz_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRecord_date() {
		return record_date;
	}
	public void setRecord_date(Date record_date) {
		this.record_date = record_date;
	}
	public String getRe_content() {
		return re_content;
	}
	public void setRe_content(String re_content) {
		this.re_content = re_content;
	}
	public String getRe_date() {
		return re_date;
	}
	public void setRe_date(String re_date) {
		this.re_date = re_date;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
    public List<Complaint> getReplies(){
    	return replies;
    }
    public void setReplies(List<Complaint> replies){this.replies=replies;}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYzname() {
        return yzname;
    }

    public void setYzname(String yzname) {
        this.yzname = yzname;
    }
}
