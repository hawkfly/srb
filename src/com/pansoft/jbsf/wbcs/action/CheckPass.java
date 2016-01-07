package com.pansoft.jbsf.wbcs.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.pansoft.util.Consts;
import com.wbcs.config.component.application.report.ReportBean;
import com.wbcs.jbsf.dao.JBSFDaoTemplate;
import com.wbcs.system.ReportRequest;
import com.wbcs.system.component.application.report.configbean.editablereport.AbsJavaEditActionBean;

public class CheckPass extends AbsJavaEditActionBean{

	@Override
	public void updateData(ReportRequest rrequest,ReportBean rbean,Map<String,String> mRowData,Map<String,String> mParamValues)
	{		
		Connection conn = rrequest.getConnection();//创建与数据库的链接
		JBSFDaoTemplate daoTemplate = new JBSFDaoTemplate(false);
		//页面录入的信息添加到单位字典表中
		final String sql = new StringBuffer("insert into iv_dwzd(F_DWBH,F_DWMC,F_SH,F_JGBH,F_DQBH,F_FR,F_ADDR,F_TEL,F_START_DATE,F_CRDATE,F_CHDATE) values(?,?,?,?,?,?,?,?,?,?,?)").toString();
		final String osql = new StringBuffer("update iv_dwzd set F_JGBH=?,F_DQBH=?,F_FR=?,F_ADDR=?,F_TEL=?,F_CHDATE=?,F_SYZT='1' where F_DWBH=?").toString();
		//把该单位申请时上传的图片信息更新到单位字典表对应的字段
		final String sql1 = new StringBuffer("update iv_dwzd a inner join iv_dwsq s on a.F_DWBH=? and s.F_SQBH=? ")
		.append(" set a.F_PATH1 = s.F_PATH1,a.F_GROUP1 = s.F_GROUP1,a.F_NAME1 = s.F_NAME1,")
		.append("     a.F_PATH2 = s.F_PATH2,a.F_GROUP2 = s.F_GROUP2,a.F_NAME2 = s.F_NAME2,")
		.append("     a.F_PATH3 = s.F_PATH3,a.F_GROUP3 = s.F_GROUP3,a.F_NAME3 = s.F_NAME3;").toString();
		//向用户单位关系表插入相应数据
		final String newsql = new StringBuffer("insert into iv_user_dw(F_YHBH,F_NAME,F_SH,F_DWBH,F_DWMC,F_MANA,F_CRDATE,F_CHDATE,F_SPRBH,F_SPRXM) values(?,?,?,?,?,'1',?,?,?,?)").toString();
		//更新单位申请表中单位编号字段
		final String newsql1 = new StringBuffer("update iv_dwsq set F_DWBH=? where F_SQBH=?").toString();
		//更新用户申请单位表，更改申请状态、申请说明等...
		final String newsql2 = new StringBuffer("update iv_yhsq_dw set F_SQZT='1',F_SPSJ=?,F_DWBH=?,F_SPSM=?,F_SPRBH=?,F_SPRXM=? where F_SQBH = ?").toString();
		
		final Object userid = mParamValues.get("userid");//当前登录的用户编号
		final Object username = mParamValues.get("username");//当前登录的用户名
		final Object sqbh = mParamValues.get("sqbh");//申请编号
		final Object dwbh = mRowData.get("F_DWBH");//单位编号
		final Object dwmc = mRowData.get("F_DWMC");//单位名称
		final Object sh = mRowData.get("F_SH");//税号
		final Object jgbh = mRowData.get("F_JGBH");//机构编号
		final Object dqbh = mRowData.get("F_DQBH");//地区编号
		final Object fr = mRowData.get("F_FR");//法人
		final Object addr = mRowData.get("F_ADDR");//地址
		final Object tel = mRowData.get("F_TEL");//电话
		final Object spsm = mRowData.get("F_SPSM");//审批说明
		Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示格式 
		final Object crtime= df.format(dt);
		
		final Object startdate = mParamValues.get("paramAddtime");//开始时间
		final Object chtime = df.format(dt);
		//查询申请类型，根据类型不同执行不同的sql
		List lst = Db.find("select F_SQLX from iv_dwsq where F_SQBH=?",sqbh);
		Record re = (Record)lst.get(0);
		String type = re.getStr("F_SQLX");//类型包括N,O,S,E四种，但只有N,O状态下进入该类
		//在单位字典中查找该单位编号是否已经存在！
		List list = Db.find("select * from iv_dwzd where F_DWBH=?",dwbh);
		if(list.size()!=0&&"N".equals(type))
		{
			rrequest.getWResponse().getMessageCollector().error("审核失败,单位编号被占用！",false);
			return;
		}
		//把审核通过的单位信息插入到单位字典表中
		List ls = Db.find("select * from iv_yhsq_dw where F_SQBH=?",sqbh);
		Record rec = (Record)ls.get(0);
		final String ivyhbh = rec.getStr("F_SQRBH");
		final String ivyhxm = rec.getStr("F_SQRXM");
		//向iv_usgn表中插入进项和销项两条权限数据
		final String usgnjx = new StringBuffer("insert into iv_usgn(F_GNBH,F_GNMC,F_YHBH,F_NAME,F_SH,F_DWBH,F_DWMC,F_MRSQ,F_CRDATE,F_CHDATE) values('purchase/search-invoice-msg','进项发票功能',?,?,?,?,?,'1',?,?)").toString();
		final String usgnxx = new StringBuffer("insert into iv_usgn(F_GNBH,F_GNMC,F_YHBH,F_NAME,F_SH,F_DWBH,F_DWMC,F_MRSQ,F_CRDATE,F_CHDATE) values('sell/queryPage','销项发票功能',?,?,?,?,?,'1',?,?)").toString();
		//	Db.update("insert into iv_user_dw(F_MANA,F_SH,F_DWMC,F_DWBH,F_CRDATE,F_CHDATE,F_SPRBH,F_SPRXM,F_YHBH,F_NAME)(select distinct '1' as F_MANA,? as F_SH,? as F_DWMC,? as D_DWBH,? as F_CRDATE,? as F_CHDATE,？ as F_SPRBH,？ as F_SPRXM,b.F_SQRBH F_YHBH,b.F_SQRXM F_NAME from iv_user_dw,iv_yhsq_dw b where b.F_SQBH=?)");
		if("".equals(dqbh)||dqbh==null)
		{
			rrequest.getWResponse().getMessageCollector().error("地区不能为空", false);
			return;
		}
		if("".equals(jgbh)||jgbh==null)
		{
			rrequest.getWResponse().getMessageCollector().error("机构代码不能为空", false);
			return;
		}
		if("".equals(fr)||fr==null)
		{
			rrequest.getWResponse().getMessageCollector().error("法人不能为空", false);
			return;
		}
		if("".equals(addr)||addr==null)
		{
			rrequest.getWResponse().getMessageCollector().error("地址不能为空", false);
			return;
		}
		if("".equals(tel)||tel==null)
		{
			rrequest.getWResponse().getMessageCollector().error("电话不能为空", false);
			return;
		}
		if("".equals(dwbh)||dwbh==null)
		{
			rrequest.getWResponse().getMessageCollector().error("审核失败！单位已存在！",false);
			return;
		}
		//新申请的企业
		if("N".equals(type))
		{
			boolean isSuccess = Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
				public boolean run() throws SQLException{
					
					int count1 = Db.update(sql,dwbh, dwmc, sh, jgbh, dqbh, fr, addr, tel,startdate,crtime,chtime);
					int count2 = Db.update(sql1,dwbh,sqbh);
					int count3 = Db.update(newsql,ivyhbh,ivyhxm,sh,dwbh,dwmc,crtime,chtime,userid,username);
					int count4 = Db.update(newsql1,dwbh,sqbh);
					int count5 = Db.update(newsql2,crtime,dwbh,spsm,userid,username,sqbh);
					int count6 = Db.update(usgnjx,ivyhbh,ivyhxm,sh,dwbh,dwmc,crtime,chtime);
					int count7 = Db.update(usgnxx,ivyhbh,ivyhxm,sh,dwbh,dwmc,crtime,chtime);
					return count1!=0&&count2!=0&&count3!=0&&count4!=0&&count5!=0&&count6!=0&&count7!=0;
				}
			});
			if(isSuccess){
				rrequest.getWResponse().getMessageCollector().success("审核已提交",false);
			}else{
				rrequest.getWResponse().getMessageCollector().error("审核失败",false);
			}
		}
		//停用过的企业——>启用
		else if("O".equals(type))
		{
			boolean isSuccess = Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
				public boolean run() throws SQLException{
					
					int count1 = Db.update(osql,jgbh, dqbh, fr, addr, tel,chtime,dwbh);
					int count2 = Db.update(sql1,dwbh,sqbh);
					int count3 = Db.update("update iv_yhsq_dw set F_SQZT='1',F_SPRBH=?,F_SPRXM=?,F_SPSJ=?,F_SPSM=? where F_SQBH=?",userid,username,chtime,spsm,sqbh);
					
					
					return count1!=0&&count2!=0&&count3!=0;
				}
			});
			if(isSuccess){
				rrequest.getWResponse().getMessageCollector().success("审核已提交",false);
			}else{
				rrequest.getWResponse().getMessageCollector().error("审核失败",false);
			}
		}
	}

}
