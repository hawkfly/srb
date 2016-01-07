/**
 * 
 */
package com.pansoft.jbsf.msg;

/**
 * @author hawkfly
 *
 */
public interface ICRtnMsg<T> extends IRtnMsg {
	public ICRtnMsg<T> setPass(boolean isPass);
	public ICRtnMsg<T> setMsg(String msg);
	public ICRtnMsg<T> setContent(T t);
	public T getContent();
}
