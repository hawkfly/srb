package com.pansoft.plugins;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jf.kit.PathKit;
import com.jf.log.Logger;
import com.jf.plugin.IPlugin;
import com.jf.plugin.activerecord.Db;
import com.pansoft.service.ivcertification.plugins.impl.CtfPlugImpl;

public class AdapterPlugin implements IPlugin{
	private Logger logger = Logger.getLogger(CtfPlugin.class);
	public boolean start() {
		logger.debug("适配器扫描进行中...");
		try 
		{
			List<String> fileList = listFiles(new File(PathKit.getWebRootPath()+File.separator+"WEB-INF"),new ArrayList());
			Db.update("delete from iv_conf where F_PZFL like'%SPQ'");
			for (String str : fileList) 
			{
				//实例化获取的适配器类，并获取getName方法——得到相应适配器的名称
				Class<?> entranceClass=Class.forName(str);
				Method method=entranceClass.getMethod("getName");
				String adapterName=(String) method.invoke(entranceClass.newInstance());
				//区分默认适配器
				if(str.indexOf("jt.slof")>0)
				{
					Db.update("insert into iv_conf(F_PZFL,F_PZKEY,F_PZMC,F_PZZ,F_PZSM) values(?,?,?,?,?)","MSPQ",str,adapterName,adapterName,"默认适配器");
				}
				else
				{
					Db.update("insert into iv_conf(F_PZFL,F_PZKEY,F_PZMC,F_PZZ,F_PZSM) values(?,?,?,?,?)","SPQ",str,adapterName,adapterName,"适配器");
				}
			}
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("适配器扫描已完成！");
		return true;
	}
	public static List<String> listFiles(File rootFile,List<String> fileList)throws Exception
	{
		File[] allFiles = rootFile.listFiles(); 
		for(File file : allFiles)
		{
			 if(file.isDirectory())
			 {
				 listFiles(file, fileList);
			 }
			 else
			 {
				 String path = file.getCanonicalPath();
				 //确定要扫描的适配器目录的位置
				 if(path.indexOf("ivjtsender.jar")>0)
				 {
					 System.out.println("---path:"+path);
					 List<String> list=getClassNameByJar(path,true);
					 for(int i=0;i<list.size();i++)
					 {
	                    String realpath=list.get(i);
	                    if(realpath.indexOf(".class")>0&&realpath.indexOf("service/ivsender/jt")>0&&realpath.indexOf("IAdapter.class")<0&&realpath.indexOf("ISendAdapter.class")<0)
	                    {
	                    	String str = realpath.replace(".class", "");
	                    	str = str.replace("/", ".");
	                    	fileList.add(str);
	                    }
	                 }
				 }
			 }
		}
		return fileList;
	}
	public boolean stop() {
		
		return true;
	}
	
	/**
	 * 判断类是否函数类.<br>
	 * 首先,类不能是抽象的,其次,类必须实现函数接口
	 * @param c 类
	 * @return 是否是函数类
	 */
	public static boolean isFunction(Class<?> c) {
		if (c == null) {
			return false;
		}
		if (c.isInterface()) {
			return false;
		}
		if (Modifier.isAbstract(c.getModifiers())) {
			return false;// 抽象
		}
		return CtfPlugImpl.class.isAssignableFrom(c);
	}
	
	/** 
     * 从jar获取某包下所有类 
     * @param jarPath jar文件路径 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
	private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {  
        List<String> myClassName = new ArrayList<String>();  
  
        try {  
            JarFile jarFile = new JarFile(jarPath);  
            Enumeration<JarEntry> entrys = jarFile.entries();  
            while (entrys.hasMoreElements()) {  
                JarEntry jarEntry = entrys.nextElement();  
                String entryName = jarEntry.getName();  
                if (entryName.endsWith(".class")) {  
                    if (childPackage) {  
                        myClassName.add(entryName);  
                    } else {  
                        int index = entryName.lastIndexOf("/");  
                        String myPackagePath;  
                        if (index != -1) {  
                            myPackagePath = entryName.substring(0, index);  
                        } else {  
                            myPackagePath = entryName;  
                        }                    
                        myClassName.add(entryName);  

                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return myClassName;  
    } 
}
