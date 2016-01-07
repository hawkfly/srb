package com.pansoft.entity;

import java.util.List;

public class PayInfos {
	private String build_id;
	private String unit_id;
	private int floor_num;
	private List<CommunityBuild> units;
	private List<CommunityBuild> pays;
	
	
	public List<CommunityBuild> getPays() {
		return pays;
	}
	public void setPays(List<CommunityBuild> pays) {
		this.pays = pays;
	}
	public String getBuild_id() {
		return build_id;
	}
	public void setBuild_id(String build_id) {
		this.build_id = build_id;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public int getFloor_num() {
		return floor_num;
	}
	public void setFloor_num(int floor_num) {
		this.floor_num = floor_num;
	}
	public List<CommunityBuild> getUnits() {
		return units;
	}
	public void setUnits(List<CommunityBuild> units) {
		this.units = units;
	}
	
	
}
