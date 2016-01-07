/**
 * 
 */
package com.pansoft.jbsf.bean;

import java.io.Serializable;

/**
 * @author hawkfly
 */
public class MenuTreeNode /*extends AbsReportDataPojo*/ implements Serializable {

	/*public MenuTreeNode(ReportRequest rrequest, ReportBean rbean) {
		super(rrequest, rbean);
		// TODO Auto-generated constructor stub
	}*/
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String code;
	public String pId;
	public String getPId() {
		return pId;
	}
	public void setPId(String pId) {
		this.pId = pId;
	}
	public String name;
	public String uri;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
