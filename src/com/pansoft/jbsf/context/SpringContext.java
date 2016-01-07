/**
 * 
 */
package com.pansoft.jbsf.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author hawkfly
 */
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		this.context = arg0;
	}
	
	public static ApplicationContext getContext(){
		return context;
	}

}
