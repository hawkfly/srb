package com.pansoft.entity;


public class RightInfo {

	private String id;
	private String name;
	private String url;
	private String ico;
	private String pid;
	private String remark;
	private String ext1;
	private String ext2;
	private String rights;
	
	public void addRights(RightInfo right){
		String id = right.getId();
		String name = right.getName();
		String url = right.getUrl();
		String temp = id + "," + name +"," + url;
		if(rights == null || "".equals(rights)){
			rights = temp;
		}else{
			rights = rights + ";" + temp;
		}
		
	}
	
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
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
	
	
}
