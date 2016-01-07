package com.pansoft.plugins;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 基本配置
 * @author Administrator
 */
public class PropertyUtil {
	private static final Log log = LogFactory.getLog(PropertyUtil.class);
	private static Properties pros = new Properties();
	static {
		try {
			InputStream in = PropertyUtil.class.
					getClassLoader().
					getResourceAsStream("conf/set.properties");
			pros.load(in);
		} catch (Exception e) {
			log.error("load configuration error", e);
		}
	}
	/**
	 * 获取值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return pros.getProperty(key);
	}
}
