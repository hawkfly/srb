/**
 * 
 */
package com.pansoft.jbsf.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.pansoft.jbsf.msg.IRtnMsg;

public class Utils {

	public static final String propath = System.getProperty("user.home") + File.separator + "jsglmap.properties";
	public static final String activexWordDocId = "Word.Document";
	public static final String activexApplication = "Word.Application";
	public static final String WJ_SAVE_PATH = "C://WINDOWS//TEMP";// 文件保存路径
	private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
	private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
	private static final double MAX_VALUE = 9999999999999.99D;


	/*
	 * public static boolean isNullAlert(String dlbh, String errormsg){ if(dlbh
	 * == null || "".equals(dlbh)){ JClientFrameKit.ShowMessage(errormsg);
	 * return true; }else{ return false; } }
	 */

	public static boolean isNull(String param) {
		if (param == null || "".equals(param))
			return true;
		else
			return false;
	}

	public static boolean isEqualsVal(String srcval, String aimval) {
		if (aimval == null)
			return false;
		if (srcval == null)
			return false;
		if (aimval.trim().equals(srcval.trim()))
			return true;
		else
			return false;
	}
	
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static String[] getYearMonthDay() {
		Calendar now = Calendar.getInstance();
		String year = "" + now.get(Calendar.YEAR);
		String month = "" + (now.get(Calendar.MONTH) + 1);
		String day = "" + now.get(Calendar.DAY_OF_MONTH);
		return new String[] { year, month, day };
	}
	
	public static String getYearMonthDayCN(){
		String[] ymd = getYearMonthDay();
		StringBuffer sb = new StringBuffer();
		sb.append(ymd[0]);
		sb.append("年");
		sb.append(ymd[1].length() == 1 ? "0"+ymd[1] : ymd[1]);
		sb.append("月");
		sb.append(ymd[2].length() == 1 ? "0"+ymd[2] : ymd[2]);
		sb.append("日");
		return sb.toString();
	}

	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
		if (a.size() != b.size())
			return false;
		Collections.sort(a);
		Collections.sort(b);
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}

	public static <T extends Map> T removeNullsOnMap(T t) {
		Set<?> set = t.keySet();
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object val = t.get(key);
			if (val == null || String.valueOf(val).equals(""))
				t.remove(key);
		}
		return t;
	}
	public static String getUUID()
	{
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		str = str.replaceAll("-","");
		return str;
	}
	public static String setunion(List a, List b) {
		for (int i = 0; i < b.size(); i++) {
			if (!a.contains(b.get(i)))
				a.add(b.get(i));
		}
		String str = new String();
		for (int i = 0; i < a.size(); i++) {
			str = str + a.get(i) + ",";
		}
		str = str.substring(0, str.length() - 1);
		return str;
	}

	public static boolean hadCollection(List<String> allCollection,
			List<String> hadCollection) {
		for (int i = 0; i < hadCollection.size(); i++) {
			if (!allCollection.contains(hadCollection.get(i)))
				return false;
		}
		return true;
	}

	public static String parseName(String qzzw) {
		if (Utils.isNull(qzzw))
			return "";
		if (qzzw.indexOf("{#") == -1)
			return "";
		if ((qzzw.indexOf("{#") + 2) >= qzzw.length())
			return "";
		return qzzw.substring(qzzw.indexOf("{#") + 2, qzzw.indexOf("#}"));
	}

	public static String getPropVal(String key) {
		Properties prop = new Properties();
		FileReader reader = null;
		try {
			File file = new File(propath);
			if (!file.exists())
				return null;
			reader = new FileReader(propath);
			prop.load(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String val = prop.getProperty(key);
		return val;
	}

	public static void setPropVal(String key, String val) {
		Properties prop = new Properties();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(propath, true));
			prop.setProperty(key, val);
			prop.store(writer, "author:hawkfly");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	public static void removePropVal(String key) {
		Properties prop = new Properties();
		FileReader reader = null;
		PrintWriter writer = null;
		try {
			reader = new FileReader(propath);
			prop.load(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		prop.remove(key);
		try {
			writer = new PrintWriter(propath);
			prop.store(writer, "author:hawkfly");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	public static File createFile(ByteArrayInputStream pBytesIn,
			String tmpFilePrefixName, String suffixName) {
		FileOutputStream fileoutStream = null;
		File file = null;
		try {
			file = File.createTempFile(tmpFilePrefixName, suffixName, new File(
					WJ_SAVE_PATH));
			fileoutStream = new FileOutputStream(file);
			byte[] pRead = new byte[1024];
			while (true) {
				int iRead = pBytesIn.read(pRead);
				if (iRead <= 0) {
					break;
				} else {
					fileoutStream.write(pRead, 0, iRead);
					fileoutStream.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileoutStream != null) {
				try {
					fileoutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				if (!newfile.getParentFile().exists()) {
					newfile.getParentFile().mkdirs();
				}
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ File.separator + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + File.separator + file[i], newPath
							+ File.separator + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 动态与逻辑
	 * 
	 * @param paramap
	 *            key 应该为客户端看到的中文名字
	 * @return 将所有动态值做逻辑与后的结果
	 */
	public static IRtnMsg dynamicAnd(Map<String, Boolean> paramap) {
		IRtnMsg rtnMsg = new IRtnMsg() {
			public boolean isPass() {
				// TODO Auto-generated method stub
				return true;
			}

			public String getMsg() {
				// TODO Auto-generated method stub
				return null;
			}

			public IRtnMsg init(String... params) {
				// TODO Auto-generated method stub
				return this;
			}
		};

		Set<String> keys = paramap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (!paramap.get(key)) {
				rtnMsg = new IRtnMsg() {
					private String msg;

					public boolean isPass() {
						// TODO Auto-generated method stub
						return false;
					}

					public String getMsg() {
						// TODO Auto-generated method stub
						return "请检查" + msg;
					}

					public IRtnMsg init(String... params) {
						// TODO Auto-generated method stub
						this.msg = params[0];
						return this;
					}
				}.init(key);
			}
		}
		return rtnMsg;
	}

	public static String linkParam(String src, String appendParam, String mark) {
		if (src == null || appendParam == null)
			return null;
		if (mark == null)
			mark = ",";
		src = src.trim();
		StringBuffer sb = new StringBuffer(src);
		if (!src.endsWith(mark) && !src.equals(""))
			sb.append(mark);
		sb.append(appendParam);
		return sb.toString();
	}

	public static String linkParam(String src, String appendParam) {
		return linkParam(src, appendParam, null);
	}

	public static String renderParamToStr(String srcStr, String... params) {
		int questionSignCount = counter(srcStr, '?');
		if (questionSignCount != params.length)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			sb.append("'");
			sb.append(params[i]);
			sb.append("'");
			srcStr = srcStr.replaceFirst("\\?", sb.toString());
			sb.delete(0, sb.length());
		}
		return srcStr;
	}

	private static int counter(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 获得指定文件的byte数组
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String filePath) throws Exception {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} finally {
			fis.close();
			bos.close();
		}
		return buffer;
	}

	public static byte[] linkByteArr(byte[] arr1, byte[] arr2) {
		byte[] result = null;
		if (arr1.length == 0 || arr2.length == 0) {
			result = arr2.length == 0 ? arr1 : arr2;
		}
		result = new byte[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, result, 0, arr1.length);
		System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
		return result;
	}

	/**
	 * 根据byte数组，生成文件
	 */
	public static File getFile(byte[] bfile, String filePath, String fileName)
			throws Exception {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
				
			}
			file = new File(filePath + "\\" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return file;
	}

	/**
	 * 将一个输入流转化为字符串
	 */
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 排数原始串srcStr中的包含在排除数组excludeStr中的所有元素，并返回排除后的有效字符串
	 * 
	 * @param srcStr
	 *            源串
	 * @param excludeStr
	 *            待排除元素数组
	 * @return 排除后的有效字符串
	 * @DATE 2014-10-13下午3:55:37
	 */
	public static String getExcludedStrByComma(String srcStr,String[] excludeStr) {
		for (int i = 0; i < excludeStr.length; i++) {
			srcStr = srcStr.replace(excludeStr[i].trim(), "");
		}
		String[] tmpStrs = srcStr.split(",");
		String newStr = "";
		for (int i = 0; i < tmpStrs.length; i++) {
			String item = tmpStrs[i].trim();
			if (!"".equals(item)) {
				newStr = linkParam(newStr, item);
			}
		}
		return newStr;
	}

	public static String getColumnsBySql(String sql) {
		return sql.substring(sql.indexOf("select") + 6, sql.indexOf("from"));
	}

	/**
	 * @Description: 根据一个接口返回该接口的所有类
	 * @param c 接口
	 * @return List<Class> 实现接口的所有类
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> getAllClassByInterface(Class c) {
		List returnClassList = new ArrayList<Class>();
		// 判断是不是接口,不是接口不作处理
		if (c.isInterface()) {
			String packageName = c.getPackage().getName(); // 获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);// 获得当前包以及子包下的所有类
				// 判断是否是一个接口
				for (int i = 0; i < allClass.size(); i++) {
					if (c.isAssignableFrom(allClass.get(i))) {
						if (!c.equals(allClass.get(i))) {
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return returnClassList;
	}

	/**
	 * 
	 * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
	 * @param pageName 包名
	 * @return List<Class> 包下所有类
	 */
	private static List<Class> getClasses(String packageName)throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClass(directory, packageName));
		}
		return classes;
	}

	private static List<Class> findClass(File directory, String packageName)throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClass(file,
						packageName + "." + file.getName()));
		} else if (file.getName().endsWith(".class")) {
			classes.add(Class.forName(packageName
						+ "."
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}
	
	/**
	 * 小写金额转大写
	 * @param v
	 * @return
	 */
	public static String change(double v) {
		if (v < 0 || v > MAX_VALUE)
			return "参数非法!";
		long l = Math.round(v * 100);
		if (l == 0)
			return "零元整";
		String strValue = l + "";
		// i用来控制数
		int i = 0;
		// j用来控制单位
		int j = UNIT.length() - strValue.length();
		String rs = "";
		boolean isZero = false;
		for (; i < strValue.length(); i++, j++) {
			char ch = strValue.charAt(i);
			if (ch == '0') {
				isZero = true;
				if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
					rs = rs + UNIT.charAt(j);
					isZero = false;
				}
			} else {
				if (isZero) {
					rs = rs + "零";
					isZero = false;
				}
				rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
			}
		}
		if (!rs.endsWith("分")) {
			rs = rs + "整";
		}
		rs = rs.replaceAll("亿万", "亿");
		return rs;
	}

	/**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }
    
	/**
	 * @author cn
	 * @param s 要截取的字符串
	 * @param length 要截取字符串的长度->是字节一个汉字2个字节
	 * return 返回length长度的字符串（含汉字）
	*/
    public static String bsubstring(String s, int length) throws Exception
    {

        byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++){
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1){
                n++; // 在UCS2第二个字节时n加1
            }else{
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0){
                    n++;
                }
            }
        }
        //将截一半的汉字要保留
        if (i % 2 == 1){
        	i = i + 1;
        }
        return new String(bytes, 0, i, "Unicode");
    }
    
    /**
     * 搜索meline最后一位或者紧跟其后一位是否为空格，如果不是则向前搜索meline中最后一个空格进行截取，如果均未找到空格则正常截取
     * @param substring0 正常截取的字符串
     * @param substring1 正常截取字符串向后加一位后的子串
     * @param separator  指定的分隔符
     * @return 新的截取值
     */
    public static String splitStrByAppoint(String substring0, String substring1, String separator){
    	if(substring1.endsWith(separator)){
    		return substring0;
    	}else if(substring0.indexOf(separator) == -1){
    		return substring0;
    	}else{
    		int lastSeparator = substring0.lastIndexOf(separator);
    		return substring0.substring(0, lastSeparator);
    	}
    }
    

    public static String getMd5(String inputStr) throws NoSuchAlgorithmException{
		String md5Str = inputStr;
		if (inputStr != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inputStr.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			md5Str = hash.toString(16);
			if ((md5Str.length() % 2) != 0) {
				md5Str = "0" + md5Str;
			}
		}
		return md5Str;
    }
    
    private static String hexString="0123456789ABCDEFGHIJKLMNOPQRSTUVWSYZabcdefghijklmnopqrstuvwxyz"; 
    /**
     * 字符串转16进制
     */
    public static String toHexString(String s) 
    { 
    	//根据默认编码获取字节数组 
    	byte[] bytes=s.getBytes(); 
    	StringBuilder sb=new StringBuilder(bytes.length*2); 
    	//将字节数组中每个字节拆解成2位16进制整数 
    	for(int i=0;i<bytes.length;i++) 
    	{ 
    	sb.append(hexString.charAt((bytes[i]&0xf0)>>4)); 
    	sb.append(hexString.charAt((bytes[i]&0x0f)>>0)); 
    	} 
    	return sb.toString(); 
    } 

	// 转化十六进制编码为字符串
	public static String toStringHex(String bytes) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
		//将每2位16进制整数组装成一个字节 
		for(int i=0;i<bytes.length();i+=2) 
		baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1)))); 
		return new String(baos.toByteArray()); 
	}
	public static String encodeStr(byte[] b){
		Base64 base64=new Base64();
		b=base64.encode(b);
		String s=new String(b);
		return s;
	}
	public static byte[] decodeStr(String encodeStr){
		byte[] b=encodeStr.getBytes();
		Base64 base64=new Base64();
		b=base64.decode(b);
		return b;
	}
	//AES加密
	public static byte[] encrypt(String content, String password) {  
        try {             
                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                kgen.init(128, new SecureRandom(password.getBytes()));  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                byte[] byteContent = content.getBytes("utf-8");  
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
                byte[] result = cipher.doFinal(byteContent);  
                return result; // 加密   
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
	}
	
	//AES解密
	public static byte[] decrypt(byte[] content, String password) {  
        try {  
                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                 kgen.init(128, new SecureRandom(password.getBytes()));  
                 SecretKey secretKey = kgen.generateKey();  
                 byte[] enCodeFormat = secretKey.getEncoded();  
                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
                byte[] result = cipher.doFinal(content);  
                return result; // 加密   
        } catch (NoSuchAlgorithmException e) {  
                e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
                e.printStackTrace();  
        } catch (InvalidKeyException e) {  
                e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
                e.printStackTrace();  
        } catch (BadPaddingException e) {  
                e.printStackTrace();  
        }  
        return null;  
	} 
    
	
	public static String getYear(){
		Calendar cal=Calendar.getInstance();//使用日历类
        int year=cal.get(Calendar.YEAR);//得到年
        return String.valueOf(year);
	}
	
	public static String getMonth(){
		Calendar cal=Calendar.getInstance();//使用日历类
        int month=cal.get(Calendar.MONTH)+1;//得到年
        return String.valueOf(month);
	}
	
	public static String getDefaultTime(){
		java.util.Date nowTime=new java.util.Date();
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        return time.format(nowTime);
	}
	
	public static String getDefaultDate(){
		java.util.Date nowTime=new java.util.Date();
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");
        
        return time.format(nowTime);
	}
	
	//获取当前时间
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
	}
      /** 
	   * 计算出离beginDate日期datas天的日期,若datas小于0表示当前日期之前datas天，若datas大于0表当前日期之后datas天 
	   * 
	   * @param 要计算的天数 
	   * @return 得到日期 
	   */ 
	  public static java.util.Date getDate(java.util.Date beginDate, int datas) { 
	    Calendar beginCal=Calendar.getInstance(); 
	    beginCal.setTime(beginDate); 
	    GregorianCalendar calendar = new GregorianCalendar(beginCal.get(Calendar.YEAR),beginCal.get(Calendar.MONTH),beginCal.get(Calendar.DATE));
	    calendar.add(GregorianCalendar.DATE, datas); 
	    String begin = new java.sql.Date(calendar.getTime().getTime()).toString(); 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	    java.util.Date endDate = null; 
	    try { 
	      endDate = sdf.parse(begin); 
	    } catch (ParseException e) { 
	      e.printStackTrace(); 
	    }
	    return endDate; 
	  }
	  
	/**
	 * 获取yyyy-MM-dd格式的日期
	 * @param beginDate String yyyy-MM-dd HH:mm:ss
	 * @param month int 以月为单位的周期
	 * @return 周期结束后的日期
	 */
	public static String getDate(String beginDate, int month){
		  String rtnDate = "";
		  if(month < 1)return rtnDate;
		  int days = month * 30;
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  try {
			 java.util.Date date = getDate(parseDate(beginDate, "yyyy-MM-dd HH:mm:ss"), days);
			 rtnDate = format.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return rtnDate;
	  }
	  
	/**
	 * 字符串时间转换为java.util.Date
	 * @param date 时间字符串
	 * @param format  "yyyy-MM-dd HH:mm:ss"
	 * @return java.util.Date
	 * @throws ParseException
	 */
	public static java.util.Date parseDate(String date, String format) throws ParseException{
		  SimpleDateFormat sdf = new SimpleDateFormat(format);
		  java.util.Date rtndate = sdf.parse(date);
		  return rtndate;
	}
	
	/**求两个日期的时间差
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static long DateDifferent(String startdate,String enddate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
	        java.util.Date d1 = null;
	        java.util.Date d2 = null;
	        long diffDays=0;
	        try {
	            d1 = format.parse(startdate);
	            d2 = format.parse(enddate);
	    
	            //毫秒ms
	            long diff = (long) (d2.getTime() - d1.getTime());    
	            long diffSeconds = diff / 1000 % 60;
	            long diffMinutes = diff / (60 * 1000) % 60;
	            long diffHours = diff / (60 * 60 * 1000) % 24;
	            diffDays = diff / (24 * 60 * 60 * 1000);   
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return diffDays;
    }
	
	public static String margeString(String...strs){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
		}
		return sb.toString();
	}
	
	public static String[] splitWbcsSql(String sql){
		String[] str = new String[2];
		int fj = sql.indexOf(") wbcs_temp_tbl");
		str[0] = sql.substring(0, fj);
		str[1] = sql.substring(fj);
		return str;
	}
}
