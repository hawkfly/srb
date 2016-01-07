/**
 * 
 */
package com.pansoft.jbsf.msg;

/**
 * @author hawkfly
 */
public class DefaultRtnMsg<T> implements ICRtnMsg<T> {
	private static final long serialVersionUID = 1L;
	private boolean isPass = false;
    private String msg = "";
    private T content;
	/* (non-Javadoc)
	 * @see vis.wblw.pub.jbsf.msg.IRtnMsg#isPass()
	 */
	public boolean isPass() {
		// TODO Auto-generated method stub
		return isPass;
	}
	
	public DefaultRtnMsg setPass(boolean isPass){
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
	
	public DefaultRtnMsg setMsg(String msg){
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

	public T getContent() {
		return content;
	}

	public DefaultRtnMsg setContent(T content) {
		this.content = content;
		return this;
	}

}
