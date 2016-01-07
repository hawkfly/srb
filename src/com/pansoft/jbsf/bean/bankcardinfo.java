package com.pansoft.jbsf.bean;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class bankcardinfo
 */
public class bankcardinfo {
	private String msg;
	private String realname;
	private String tel;
	private float balance;
	private String accstatus;
	private String bankno;
	private String bankname;
	private String zfmm;
	private String rtmsg;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRealname() {
		return realname;
	}
	public void setAccstatus(String accstatus) {
		this.accstatus = accstatus;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getBankname() {
		return bankname;
	}
	public String getAccstatus() {
		return accstatus;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getZfmm() {
		return zfmm;
	}
	public void setZfmm(String zfmm) {
		this.zfmm = zfmm;
	}
	public String getRtmsg() {
		return rtmsg;
	}
	public void setRtmsg(String rtmsg) {
		this.rtmsg = rtmsg;
	}
	
}