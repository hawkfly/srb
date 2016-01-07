package com.pansoft.jbsf.wbcs.action;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.pansoft.jbsf.util.Utils;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVSaveUserAndRole implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		
		new BigDecimal(new Float(1)).multiply(new BigDecimal(100)).floatValue();
		return null;
	}

	public String executeSeverAction(ReportRequest req,
			IComponentConfigBean arg1, List<Map<String, String>> list,
			Map<String, String> map) {
		// TODO Auto-generated method stub
		Object sesid = req.getRequest().getSession().getAttribute("userid");
		if(sesid == null)sesid = "0";
		if(list==null)
		{
			req.getWResponse().getMessageCollector().error("保存失败",false);
			return "请选择一个用户！";
		}
		final String id = list.get(0).get("id");
		String username = list.get(0).get("username");
		String password = map.get("pwd");
		if(map.size()==1)
		{
			map=null;
		}
		String realname = list.get(0).get("realname");
		String email = list.get(0).get("email");
		String islock = list.get(0).get("islock");
		String phone = list.get(0).get("phone");
		String orgid = map.get("orgid");
		String orgname = map.get("orgname");
		
		String userid = sesid+username;//如果用户为新增的，则重新生成用户ID
		//map不为空，说明为该用户选择了角色
		if(map!=null)
		{
			String roles = map.get("keys");
			if(roles!=null)
			{
				final String role[] = roles.split(",");

			//id有值，说明该用户是已经存在的用户
			if(!"".equals(id)&&id!=null)
			{
				//删除该用户的所有角色，并重新添加选中的角色
				boolean isSuccess = Db.tx(new IAtom()
				{
					public boolean run() throws SQLException
					{
						int del = 0,count = 1;
						int temp[] = new int[100];
						List list = Db.find("select * from jbsf_user_roles where userid = ?",id);
						if(list.size()==0)
						{
							del = 1;
						}
						else
						{
							del = Db.update("delete from jbsf_user_roles where userid=?",id);
						}
						for(int i=0;i<role.length;i++)
						{					
							String uuid = Utils.getUUID();					
							temp[i] = Db.update("insert into jbsf_user_roles values(?,?,?)",uuid,id,role[i]);
						}
						for(int j = 0;j<role.length;j++)
						{
							if(temp[j]==0)
							{
								count = 0;
							}
						}
						return del != 0&&count == 1;
					}
				});
				Db.update("update jbsf_user set username=?,password=?,realname=?,email=?,islock=?,phone=? where id =?",username,password,realname,email,islock,phone,id);
				if(isSuccess){
					req.getWResponse().getMessageCollector().success("保存成功",false);
					return "保存成功";
				}else{
					req.getWResponse().getMessageCollector().error("保存失败",false);
					return "保存失败";
				}
			}
			//如果id没有值，说明该用户是新增的用户
			else
			{
				Db.update("insert into jbsf_user(id,username,password,realname,email,islock,phone,orgid,orgname) values(?,?,?,?,?,?,?,?,?)",userid,username,password,realname,email,islock,phone,orgid,orgname);
				for(int i=0;i<role.length;i++)
				{					
					String uuid = Utils.getUUID();					
					Db.update("insert into jbsf_user_roles values(?,?,?)",uuid,userid,role[i]);
				}
			}
		}else{//角色为空说明是新加用户
			Db.update("insert into jbsf_user(id,username,password,realname,email,islock,phone,orgid,orgname) values(?,?,?,?,?,?,?,?,?)",userid,username,password,realname,email,islock,phone,orgid,orgname);
		}
		}
		//map为空，说明没有为该用户分配角色
		else
		{
			//id有值，说明该用户是已经存在的用户
			if(!"".equals(id)&&id!=null)
			{
				Db.update("delete from jbsf_user_roles where userid=?",id);
				Db.update("update jbsf_user set username=?,password=?,realname=?,email=?,islock=?,phone=? where id =?",username,password,realname,email,islock,phone,id);
			}
			//如果id没有值，说明该用户是新增的用户
			else
			{
				Db.update("insert into jbsf_user(id,username,password,realname,email,islock,phone,orgid,orgname) values(?,?,?,?,?,?,?,?,?)",userid,username,password,realname,email,islock,phone,orgid,orgname);
			}
		}
		req.getWResponse().getMessageCollector().success("保存成功",false);
		return "保存成功";
	}

}
