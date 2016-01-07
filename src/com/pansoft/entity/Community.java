package com.pansoft.entity;

import java.util.Date;

public class Community {

	private String id;
	private String sq_name;
	private String sq_address;
	private String wy_name;
	private String wy_address;
	private String wy_tel;
	private String username;
	private String password;
	private String remark;
	private String ext1;
	private String ext2;
	private Date logindate;
	private Date leavedate;
	
	public Date getLogindate() {
		return logindate;
	}
	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}
	public Date getLeavedate() {
		return leavedate;
	}
	public void setLeavedate(Date leavedate) {
		this.leavedate = leavedate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSq_name() {
		return sq_name;
	}
	public void setSq_name(String sq_name) {
		this.sq_name = sq_name;
	}
	public String getSq_address() {
		return sq_address;
	}
	public void setSq_address(String sq_address) {
		this.sq_address = sq_address;
	}
	public String getWy_name() {
		return wy_name;
	}
	public void setWy_name(String wy_name) {
		this.wy_name = wy_name;
	}
	public String getWy_address() {
		return wy_address;
	}
	public void setWy_address(String wy_address) {
		this.wy_address = wy_address;
	}
	public String getWy_tel() {
		return wy_tel;
	}
	public void setWy_tel(String wy_tel) {
		this.wy_tel = wy_tel;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
