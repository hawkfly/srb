package com.pansoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Free {
	
	static Log log =LogFactory.getLog(Free.class);
	
	public static void  template(String path , String templatename , String filename,Map<?,?> root ){
		try {
			Configuration config = new Configuration();
			//设置要解析的末班所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(new File(path));
			//设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());
			//获取模板并设置编码方
			Template template = config.getTemplate(templatename, "utf-8");
			log.info("模板生成器 Free 准备完毕&");
			//合并数据模型和模板
			FileOutputStream fos = new FileOutputStream(path+"/"+filename);
			Writer out  = new OutputStreamWriter(fos, "utf-8");
			template.process(root, out);
			log.info("模板生成器 Free 输出数据完成&"+filename);
			out.flush();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch(TemplateException  e){
			e.printStackTrace();
		}
	}
	
}
