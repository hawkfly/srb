/**
 * 
 */
package com.pansoft.jbsf.wbcs.interceptor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.Record;
import com.pansoft.util.Consts;
import com.wbcs.system.intercept.AbsFileUploadInterceptor;

/**
 * 图片上传前拦截，检测图片的宽度和高度是不是符合要求
 * @author hawkfly
 */
public class ItCheckImg extends AbsFileUploadInterceptor {
	public boolean beforeFileUpload(HttpServletRequest request,FileItem fileitemObj,Map<String,String> mFormAndConfigValues,PrintWriter out)
    {
		InputStream fileStream;
		try {
			fileStream = fileitemObj.getInputStream();
			BufferedImage bimg = ImageIO.read(fileStream);
			int sjwidth = bimg.getWidth(), sjheight = bimg.getHeight();
			List<Record> wandh = Db.find("select name, value from jbsf_config where type = ?", Consts.TYPE_INDEXIMG);
			int bzwidth = 0, bzheight = 0;
			for (Record record : wandh) {
				String name = record.get("name");
				if("width".equals(name))bzwidth = Integer.parseInt(record.getStr("value").trim());
				if("height".equals(name))bzheight = Integer.parseInt(record.getStr("value").trim());
			}
			if(bzwidth == 0 || bzheight == 0)out.write("图片标准宽度高度未初始化，请与管理员联系！");
			if(bzwidth == sjwidth && bzheight == sjheight)return true;
			else{
				out.write("上传的图片宽度高度必须为设定的标准宽度高度！");
				mFormAndConfigValues.put("isfailed", "1");
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
	
	public boolean beforeDisplayFileUploadPrompt(HttpServletRequest request,List lstFieldItems,Map<String,String> mFormAndConfigValues,
            String failedMessage,PrintWriter out)
    {
		String val = mFormAndConfigValues.get("isfailed");
		System.out.println(val);
		if(val != null && "1".equals(val))return false;
		else return true;
    }
}
