package com.pansoft.entity;

import java.util.Date;

public class PayInfo {

	private String id;
	private String yz_id;
	private String pay_id;
	private Date plan_date;
	private Date start_date;
	private Date end_date;
	private Double plan_pay;
	private String start_num;
	private String end_num;
	private String pay_org;
	private String pay_code;
	private Date real_date;
	private Double real_pay;
	private String pay_type;
	private String remark;
	private String ext1;
	private String ext2;

    private String yh_pay;
    private String yz_name;
    private String pay_str;
	
	
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
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public Date getPlan_date() {
		return plan_date;
	}
	public void setPlan_date(Date plan_date) {
		this.plan_date = plan_date;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Double getPlan_pay() {
		return plan_pay;
	}
	public void setPlan_pay(Double plan_pay) {
		this.plan_pay = plan_pay;
	}
	public String getStart_num() {
		return start_num;
	}
	public void setStart_num(String start_num) {
		this.start_num = start_num;
	}
	public String getEnd_num() {
		return end_num;
	}
	public void setEnd_num(String end_num) {
		this.end_num = end_num;
	}
	public String getPay_org() {
		return pay_org;
	}
	public void setPay_org(String pay_org) {
		this.pay_org = pay_org;
	}
	public String getPay_code() {
		return pay_code;
	}
	public void setPay_code(String pay_code) {
		this.pay_code = pay_code;
	}
	public Date getReal_date() {
		return real_date;
	}
	public void setReal_date(Date real_date) {
		this.real_date = real_date;
	}
	public Double getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Double real_pay) {
		this.real_pay = real_pay;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
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


    public String getYh_pay() {
        return yh_pay;
    }

    public void setYh_pay(String yh_pay) {
        this.yh_pay = yh_pay;
    }

    public String getYz_name() {
        return yz_name;
    }

    public void setYz_name(String yz_name) {
        this.yz_name = yz_name;
    }

    public String getPay_str() {
        return pay_str;
    }

    public void setPay_str(String pay_str) {
        this.pay_str = pay_str;
    }
}
