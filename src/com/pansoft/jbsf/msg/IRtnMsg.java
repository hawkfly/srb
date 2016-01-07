/**
 * 
 */
package com.pansoft.jbsf.msg;

import java.io.Serializable;

/**
 * 返回值消息体
 * @author hawkfly
 */
public interface IRtnMsg extends Serializable{
	boolean isPass();
	String getMsg();
	IRtnMsg init(String ...params);
}
