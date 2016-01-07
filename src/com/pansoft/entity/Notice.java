package com.pansoft.entity;

import java.util.Date;

public class Notice {

	private String id;
	private String receiver;
	private String title;
	private String content;
	private String index_pic;
	private Date send_date;
    private String send_date_str;
	private String isread;
	private String remark;
	private String ext1;
	private String ext2;
	private String top;
	
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIndex_pic() {
		return index_pic;
	}
	public void setIndex_pic(String index_pic) {
		this.index_pic = index_pic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSend_date() {
		return send_date;
	}
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	public String getIsread() {
		return isread;
	}
	public void setIsread(String isread) {
		this.isread = isread;
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


    public String getSend_date_str() {
        return send_date_str;
    }

    public void setSend_date_str(String send_date_str) {
        this.send_date_str = send_date_str;
    }
}
