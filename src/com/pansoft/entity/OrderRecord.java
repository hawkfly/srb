package com.pansoft.entity;

import java.util.Date;
import java.util.List;

public class OrderRecord {

	private String id;
	private String yz_id;
	private String order_name;
	private Date order_date;
	private Double order_money;
	private String address;
	private String consignee;
	private String phone;
	private List<BuyInfo> buyinfos;
	private String remark;
	private String ext1;
	private String ext2;
	
	public List<BuyInfo> getBuyinfos() {
		return buyinfos;
	}
	public void setBuyinfos(List<BuyInfo> buyinfos) {
		this.buyinfos = buyinfos;
	}
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
	public String getOrder_name() {
		return order_name;
	}
	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public Double getOrder_money() {
		return order_money;
	}
	public void setOrder_money(Double order_money) {
		this.order_money = order_money;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
