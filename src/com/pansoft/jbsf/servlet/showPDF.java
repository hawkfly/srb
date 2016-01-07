package com.pansoft.jbsf.servlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;

/**
 * Servlet implementation class showPDF
 */
public class showPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public showPDF() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		byte[] pdfbytes = Db.findFirst("select * from share_invoice where SFIELD0=?","06035141").getBytes("SDATA");
		File pdf = null;
		try {
		   String path = this.getServletContext().getRealPath("/");
		   String pdfname = UUID.randomUUID().toString()+".pdf";
		   pdf = getFile(pdfbytes, path+"pansoftjbsf/",pdfname);
		   response.setContentType("application/pdf"); // MIME
		   InputStream inputStream = new FileInputStream(pdf); //放到webroot下，新建一个data文件夹，文件大小不能超过1024B
		   byte[] b = new byte[1024];
		   int len = -1;
		   OutputStream outputStream = response.getOutputStream();
		   while ((len = inputStream.read(b)) != -1) {
		    outputStream.write(b, 0, len);  //读取字节，从0到len长度
		   }
		   //String ss = path+"/pansoftjbsf/"+pdfname;
		   outputStream.close();
		   inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			pdf.delete();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/** 
     * 根据byte数组，生成文件 
     */  
    public static File getFile(byte[] bfile, String filePath,String fileName)throws Exception {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath); 
            boolean a = !dir.exists();
            boolean b = dir.isDirectory();
            if(!dir.exists()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"\\"+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        }finally {  
            if (bos != null) {  
               bos.close();  
            }  
            if (fos != null) {  
               fos.close();  
            }  
        }
        return file;
    }

}
