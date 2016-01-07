/**
 * 
 */
package com.pansoft.jbsf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排除指定的默写属性的注解
 * @author hawkfly
 * @date 2014-10-13下午3:30:40
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excludes {
	String[] vals();
}
