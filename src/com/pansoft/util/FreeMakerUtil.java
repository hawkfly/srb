package com.pansoft.util;

import java.util.Map;

public class FreeMakerUtil {
	

	//默认生成的静态页面模板名称
	private static String template_page = "index.ftl";
	

	/**
	 * 创建商品静态页面
	 * @param path
	 * @param name
	 * @param map
	 */
	public static void creatGoodsPage(String path, String name, Map<?, ?> map){
		Free.template(path+"mobile/goods", template_page, name+".html",map);
	}
	
	/**
	 * 创建商店静态页面
	 * @param path
	 * @param name
	 * @param map
	 */
	public static void creatRetailsPage(String path, String name, Map<?, ?> map){
		Free.template(path+"mobile/retails", template_page, name+".html",map);
	}
	
}

