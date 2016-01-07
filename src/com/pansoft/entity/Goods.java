package com.pansoft.entity;

public class Goods {

	private String id;
	private String sj_id;
	private String name;
	private String introduction;
	private String pics;
	private Double price;
	private Double o_price;
	private String goods_order;
	private Double discount;
	private String remark;
	private String ext1;
	private String ext2;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSj_id() {
		return sj_id;
	}
	public Double getO_price() {
		return o_price;
	}
	public void setO_price(Double o_price) {
		this.o_price = o_price;
	}
	public String getGoods_order() {
		return goods_order;
	}
	public void setGoods_order(String goods_order) {
		this.goods_order = goods_order;
	}
	public void setSj_id(String sj_id) {
		this.sj_id = sj_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
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
