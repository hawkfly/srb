/**
 * 
 */
package com.pansoft.jbsf.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hawkfly
 */
public class JbsfException extends Exception {
	private static Log log=LogFactory.getLog(JbsfException.class);
	public JbsfException(){}
	public JbsfException(String msg){
		super(msg);
		log.error(msg);
	}
}
