/**
 * 
 */
package com.pansoft.jbsf.msg;

/**
 * @author hawkfly
 *
 */
public class DefaultDetailRtnMsg<T> implements ICRtnDetailMsg<T> {

	private static final long serialVersionUID = 1L;
	private boolean isPass = false;
    private String msg = "";
    private T errorDetail;
    private T successDetail;
	/* (non-Javadoc)
	 * @see vis.wblw.pub.jbsf.msg.IRtnMsg#isPass()
	 */
	public boolean isPass() {
		// TODO Auto-generated method stub
		return isPass;
	}
	
	public DefaultDetailRtnMsg<T> setPass(boolean isPass){
		this.isPass = isPass;
		return this;
	}

	/* (non-Javadoc)
	 * @see vis.wblw.pub.jbsf.msg.IRtnMsg#getMsg()
	 */
	public String getMsg() {
		// TODO Auto-generated method stub
		return msg;
	}
	
	public DefaultDetailRtnMsg<T> setMsg(String msg){
		this.msg = msg;
		return this;
	}

	/* (non-Javadoc)
	 * @see vis.wblw.pub.jbsf.msg.IRtnMsg#init(java.lang.String[])
	 */
	public IRtnMsg init(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	public ICRtnDetailMsg<T> setErrorDetail(T errorDetail) {
		// TODO Auto-generated method stub
		this.errorDetail = errorDetail;
		return this;
	}

	public T getErrorDetail() {
		// TODO Auto-generated method stub
		return this.errorDetail;
	}

	public ICRtnDetailMsg<T> setSuccessDetail(T successDetail) {
		// TODO Auto-generated method stub
		this.successDetail = successDetail;
		return this;
	}

	public T getSuccessDetail() {
		// TODO Auto-generated method stub
		return this.successDetail;
	}

}
