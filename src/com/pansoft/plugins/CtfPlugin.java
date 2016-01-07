package com.pansoft.plugins;

import java.io.File;
import java.io.IOException;
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

public class CtfPlugin implements IPlugin{
	private Logger logger = Logger.getLogger(CtfPlugin.class);
	public boolean start() {
		try {
			logger.debug("插件信息读取中...");
			List<String> list=listFiles(new File(PathKit.getWebRootPath()+File.separator+"WEB-INF"),new ArrayList());
			Db.update("delete from iv_conf where F_PZFL='RZFW'");
			for(String str:list){
				str=str.replaceAll("/", ".");
				StringBuffer resultstr = new StringBuffer();
				Class<?> entranceClass=Class.forName(str);
				Method method=entranceClass.getMethod("returnplugname");  			
				resultstr.append(str);
				resultstr.append("_sendInvoice");
				String plugname=(String) method.invoke(entranceClass.newInstance());
				Db.update("insert into iv_conf(F_PZFL,F_PZKEY,F_PZMC,F_PZZ,F_PZSM) values(?,?,?,?,?)"
						,"RZFW",resultstr.toString(),plugname,plugname,"请不要使用分类编号为RZFW（认证服务），否则可能会被删除。");

			}
			logger.debug("插件信息读取完毕 ！");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return true;
	}

	public boolean stop() {
		return true;
	}

	/** 
     * 获取所有文件列表 .class文件 和.jar文件
     * @param rootFile 
     * @param fileList 
     * @throws IOException 
     */  
    public static  List<String> listFiles(File rootFile,List<String> fileList) throws Exception{  
        File[] allFiles = rootFile.listFiles();  
        for(File file : allFiles){  
            if(file.isDirectory()){  
                listFiles(file, fileList);  
            }else{
                String path = file.getCanonicalPath();  

            	//不查找Class文件
                /*if(path.indexOf(".class")>0){
                	String realpath=path.substring(path.indexOf("classes")+8);
                	if(isFunction(Class.forName(realpath.replaceAll("\\\\", ".").replaceAll(".class", "")))){
                        fileList.add(realpath.replaceAll("\\\\", ".").replaceAll(".class", ""));  
                	}
                }*/
                //暂不处理Jar
                if(path.indexOf("InvoiceAuthenticate.jar")>0){
                	System.out.println(path);
                	List<String> list=getClassNameByJar(path,true);
                	for(int i=0;i<list.size();i++){
                    	String realpath=list.get(i);
                    	if(realpath.startsWith("/")){
                    		realpath=realpath.substring(1);
                    	}
                		try {
							if(isFunction(Class.forName(realpath.replaceAll("/", ".").replaceAll(".class", "")))){
							    fileList.add(list.get(i).replace("//", ".").substring(0,list.get(i).lastIndexOf(".")));  
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
                	}
                }
            }  
        }  
        return fileList;  
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
	public static void main(String[] args) throws IOException {  
		try {
			List<String> list=listFiles(new File(PathKit.getWebRootPath()+File.separator+"WEB-INF"),new ArrayList());
			
			StringBuffer resultstr = new StringBuffer();
			for(String str:list){
				str=str.replaceAll("/", ".");
				System.out.println(str);
				Class<?> entranceClass=Class.forName(str);
				Method method=entranceClass.getMethod("returnplugname");  
				resultstr.append((String) method.invoke(entranceClass.newInstance()));
				resultstr.append(":");
				resultstr.append(str);
				resultstr.append("_sendInvoice;");
			}
			System.out.println(resultstr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		List<String> list=getClassNameByJar(PathKit.getWebRootPath()+File.separator+"WEB-INF"+File.separator+"lib"+File.separator+"InvoiceAuthenticate.jar",true);//asm-3.3.1
		for(String str:list){
			System.out.println("--"+str);
		}
    }
}
