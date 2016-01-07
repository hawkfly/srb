package com.pansoft.jbsf.wbcs.action;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.document.fdfs.FastDFSManager;
import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.util.JbsfHelper;
import com.pansoft.service.sendfiles.Hclient;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVSendMx implements IAction{
	//本机的路径
	//private static String path = "D:/workspace/phpProject/code/prjowner/WebContent/test/";
	//测试的路径
	private static String path = "/usr/local/glassfish3/glassfish/domains/domain1/applications/prjowner/upload";
	//正式的路径
	//private static String path = "D:/workspace/phpProject/code/prjowner/WebContent/test/";
	//   
	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		String gfdm = arg2.get(0).get("key");
		
		boolean flag = true;
		System.err.println("start!");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String gfdw = arg2.get(0).get("key");
				//"130411567395138001";
		String rzsj = "05";
		//将要传送的数据都插入到 关系表
		List listinsert = Db.find("select * from invoice where GFDM="+gfdw+" and substring(RZSJ, 5, 2) = "+rzsj+"");
		for (int i = 0; i < listinsert.size(); i++) {
			Record rec = (Record) listinsert.get(i);
			List changeres = Db.find("select * from invoice_list_send where DJBH='"+rec.getStr("DJBH")+"'");
			//如果 关系表中 已经存在该单据  那么就不会执行插入动作
			if(changeres.size() == 0){
				Db.update("insert into invoice_list_send (ID,DJBH,STATUS,FIRSTIME,SUCCESSTIME,SENDCOUNT) values ('"+rec.getStr("DJBH")+"','"+rec.getStr("DJBH")+"','0','"+df.format(new Date())+"','"+df.format(new Date())+"','0')");
			}
		}
		while(flag){
			List xunhuan = Db.find("select * from invoice_list_send where STATUS = '0'");	
			//控制发送的次数
			if(xunhuan.size() != 0){
			//将第一条表中的数据中的 发票号码和发票代码取出 然后加工 发送
				List listfs = Db.find("select * from invoice a,invoice_list_send b where b.STATUS = '0' and a.DJBH = b.DJBH ");	
				Record recfs = (Record) listfs.get(0);
				String fpdm = recfs.getStr("FPDM");
				String fphm = recfs.getStr("FPHM");
				process(fpdm,fphm);
				Hclient hclient = new Hclient();
				String rstStr = hclient.sendfiles(path + fpdm + fphm+ ".zip");
				int result=rstStr.indexOf("成功!");
			    if(result >=0){
			       List list = Db.find("select * from invoice_list_send where STATUS = '0'");	
				   Record rec = (Record) list.get(0);
				   String djbh = rec.getStr("DJBH");
				   Db.update("update invoice_list_send set STATUS='1',SUCCESSTIME='"+df.format(new Date())+"' where DJBH ="+djbh+"");
			    }
			    //间隔多少毫秒发送
			    try {
			    	Thread.sleep(2000);
			    } catch (InterruptedException e) {
			    	e.printStackTrace();
			    }
			}else{
				flag = false;
			}
		}
		System.out.println("end");
		
		//Db.update("update iv_users set F_PASS = '"+MD5Pwd+"' where F_YHBH = '"+str+"'");
		return null;
	}

	public String executeSeverAction(ReportRequest req,
			IComponentConfigBean arg1, List<Map<String, String>> list,
			Map<String, String> arg3) {
	
		return null;
	}

	
	public static void process(String fpdm,String fphm){
		DecimalFormat dftwo = new DecimalFormat("#.00");
		DecimalFormat dffour = new DecimalFormat("#.0000");
		List list = Db.find("select * from invoice a left join invoice_list b on a.DJBH = b.DJBH where a.FPDM = "
						+ fpdm + " and a.FPHM=" + fphm + "");
		Record rec = (Record) list.get(0);
		// 创建文件夹 并且创建txt文件
		try {
			creatTxtFile(path + rec.getStr("FPDM") + rec.getStr("FPHM")+ ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BigDecimal sl = rec.getBigDecimal("rate");
		double yibai = 100;
		BigDecimal b1 = new BigDecimal(yibai);
		// 把得到的数*100
		BigDecimal finalsl = sl.multiply(b1);
		// 去掉后面的小数点
		String zssl = new DecimalFormat("0").format(finalsl);
		String str = "进项发票补录" + "\r\n" + rec.getStr("XHMC") + "\r\n"
				+ rec.getStr("product") + "\r\n" + rec.getStr("type") + "\r\n"
				+ rec.getStr("unit") + "\r\n"
				+ new DecimalFormat("0").format(rec.getBigDecimal("number"))
				+ "\r\n" + dffour.format(rec.getBigDecimal("price")) + "\r\n"
				+ dftwo.format(rec.getBigDecimal("amount")) + "\r\n" + zssl
				+ "\r\n" + dftwo.format(rec.getBigDecimal("Rate_amount"))
				+ "\r\n" + "货物抵扣(01)" + "\r\n"
				+ "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n"
				+ "USERMODIFIEDUSERMODIFIED" + "\r\n";
		// 将文本写入txt文件
		try {
			writeTxtFile(str, path + rec.getStr("FPDM") + rec.getStr("FPHM")+ ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 创建tp文件
		try {
			creatTxtFile(path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".fp");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String strfp = "\r\n" + rec.getStr("GFMC") + "\r\n"
				+ rec.getStr("FPDM") + "\r\n" + rec.getStr("FPHM") + "\r\n"
				+ rec.getStr("KPRQ").substring(2, 4)
				+ rec.getStr("KPRQ").substring(5, 7)
				+ rec.getStr("KPRQ").substring(8, 10) + "\r\n"
				+ rec.getStr("GFSH") + "\r\n" + rec.getStr("XHSH") + "\r\n"
				+ dftwo.format(rec.getBigDecimal("amount")) + "\r\n"
				+ dftwo.format(rec.getBigDecimal("Rate_amount")) + "\r\n"
				+ rec.getStr("RZSJ").substring(0, 4) + "-"
				+ rec.getStr("RZSJ").substring(4, 6) + "-"
				+ rec.getStr("RZSJ").substring(6, 8) + "\r\n"
				+ rec.getStr("FPLX") + "\r\n";
		try {
			writeTxtFile(strfp, path + rec.getStr("FPDM") + rec.getStr("FPHM")+ ".fp");
		} catch (IOException e) {
			e.printStackTrace();
		}

		List listimg = Db.find("select * from invoice a,invoice_image b where a.FPDM = "
						+ fpdm + " and a.FPHM=" + fphm + " and a.DJBH = b.DJBH");
		Record recimg = (Record) listimg.get(0);

		String IPUrl = JbsfHelper.getversion("fastdfsurl");// 获取地址
		String Port = JbsfHelper.getversion("fastdfsport");// 获取端口号
		String group = recimg.getStr("GROUP0");
		String image = recimg.getStr("IMAGE");
		FastDFSManager manager = null;
		try {
			manager = new FastDFSManager(IPUrl, Integer.parseInt(Port));
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		InputStream downLoadFile;
		File file = null;
		file = new File(path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".jpg");
		byte[] bytes = null;
		try {
			bytes = manager.downLoadFile(group, image);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		downLoadFile = new ByteArrayInputStream(bytes);
		try {
			inputstreamtofile(downLoadFile, file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 把图片下载下来 转换成jpg格式
		changeImge(file);
		// 将文件压缩黑白处理，并且并后缀名改为gif
		compressPic(path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".jpg",
				path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".gif");
		String[] strs = new String[3];
		strs[0] = path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".fp";
		strs[1] = path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".txt";
		strs[2] = path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".gif";
		try {
			writeZip(strs, path + rec.getStr("FPDM") + rec.getStr("FPHM")+ ".zip");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File filef = new File(path + rec.getStr("FPDM") + rec.getStr("FPHM") + ".jpg");
		filef.delete();
		// 删除文件
		try {
			deletefile(strs);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException, Exception {
		/*
		 * creatTxtFile("120014413"+"005993913"); String content =
		 * "GFMC"+"\r\n"+ "GFwwwC"+"\r\n"+ "GFnnnC"+"\r\n";
		 * writeTxtFile(content);
		 * 
		 * ZipCompressor zca = new
		 * ZipCompressor("D:\\120014413"+"005993913.zip");
		 * zca.compress("D:\\120014413"+"005993913.txt");
		 * 
		 * File file = new File("D:\\120014413"+"005993913.txt");
		 * if(file.exists()){ file.delete(); }
		 */
		/*
		 * String IPUrl = "192.168.248.162";//获取地址 String Port = "22122";//获取端口号
		 * String group="group1"; String
		 * path="M00/00/01/wKj4o1TYfreAMKZ1AAjiA2ldbP82157987"; FastDFSManager
		 * manager = new FastDFSManager(IPUrl,Integer.parseInt(Port));
		 * InputStream downLoadFile; File file = null; file =new
		 * File("D:\\HUAHUA.jpg"); byte[] bytes =
		 * manager.downLoadFile("group1",path); downLoadFile = new
		 * ByteArrayInputStream(bytes); inputstreamtofile(downLoadFile,file );
		 * changeImge(file); compressPic("D:/HUAHUA.jpg", "D:/ssss.gif");
		 */

	}

	/**
	 * 创建文件
	 * 
	 * @throws IOException
	 */
	public static boolean creatTxtFile(String filename) throws IOException {
		boolean flag = false;
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
			flag = true;
		}
		return flag;
	}

	/**
	 * 写文件
	 * 
	 * @param newStr
	 *            新内容
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String newStr, String filename)
			throws IOException {
		// 先读取原有文件内容，然后进行写入操作
		boolean flag = false;
		String filein = newStr + "\r\n";

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// 文件路径
			File file = new File(filename);
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			buf.append(filein);

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			flag = true;
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			throw e1;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}

	/**
	 * 将流转换成文件
	 * 
	 * @throws IOException
	 */
	public static void inputstreamtofile(InputStream ins, File file)
			throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	/**
	 * 压缩图片
	 * 
	 * @throws IOException
	 */
	public static boolean compressPic(String srcFilePath, String descFilePath) {
		File file = null;
		BufferedImage src = null;
		FileOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		// 指定写图片的方式为 jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
				null);
		// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
		// 这里指定压缩的程度，参数qality是取值0~1范围内，
		imgWriteParams.setCompressionQuality((float) 0.06);
		imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();

		// 指定压缩时使用的色彩模式
		imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
				colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		try {
			if (StringUtils.isBlank(srcFilePath)) {
				return false;
			} else {
				file = new File(srcFilePath);
				src = ImageIO.read(file);
				out = new FileOutputStream(descFilePath);

				imgWrier.reset();
				// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
				// OutputStream构造
				imgWrier.setOutput(ImageIO.createImageOutputStream(out));
				// 调用write方法，就可以向输入流写图片
				imgWrier.write(null, new IIOImage(src, null, null),
						imgWriteParams);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * * 转换图片 * *
	 */
	public static void changeImge(File img) {
		try {
			Image image = ImageIO.read(img);
			int srcH = image.getHeight(null);
			int srcW = image.getWidth(null);
			BufferedImage bufferedImage = new BufferedImage(srcW, srcH,
					BufferedImage.TYPE_3BYTE_BGR);
			bufferedImage.getGraphics()
					.drawImage(image, 0, 0, srcW, srcH, null);
			bufferedImage = new ColorConvertOp(
					ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(
					bufferedImage, null);
			FileOutputStream fos = new FileOutputStream(img);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			encoder.encode(bufferedImage);
			fos.close();
			// System.out.println("转换成功...");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("图片转换出错！", e);
		}
	}

	public static void writeZip(String[] strs, String zipname)
			throws IOException {
		OutputStream os = new BufferedOutputStream(
				new FileOutputStream(zipname));
		ZipOutputStream zos = new ZipOutputStream(os);
		byte[] buf = new byte[8192];
		int len;
		for (int i = 0; i < strs.length; i++) {
			File file = new File(strs[i]);
			if (!file.isFile())
				continue;
			ZipEntry ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while ((len = bis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
		}
		zos.setEncoding("GBK");
		zos.closeEntry();
		zos.close();

		for (int i = 0; i < strs.length; i++) {
			System.out.println("------------" + strs[i]);
			File file = new File(strs[i]);
			file.delete();
		}
	}

	public static void deletefile(String[] strs) throws IOException {
		for (int i = 0; i < strs.length; i++) {
			File file = new File(strs[i]);
			file.delete();
		}
	}
}
