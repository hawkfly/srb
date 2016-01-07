package com.pansoft.jbsf.msg;

public interface ICRtnDetailMsg<T> extends IRtnMsg{
	public ICRtnDetailMsg<T> setPass(boolean isPass);
	public ICRtnDetailMsg<T> setMsg(String msg);
	public ICRtnDetailMsg<T> setErrorDetail(T t);
	public T getErrorDetail();
	public ICRtnDetailMsg<T> setSuccessDetail(T t);
	public T getSuccessDetail();
}
