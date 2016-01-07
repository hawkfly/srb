package com.pansoft.jbsf.wbcs.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.pansoft.jbsf.util.Utils;
import com.pansoft.util.Consts;
import com.wbcs.config.component.IComponentConfigBean;
import com.wbcs.jbsf.openout.IAction;
import com.wbcs.system.ReportRequest;

public class IVSaveSelectedNode implements IAction{

	public String executeServerAction(HttpServletRequest arg0,
			HttpServletResponse arg1, List<Map<String, String>> arg2) {
		// TODO Auto-generated method stub
		String nodes = arg2.get(0).get("key");//获取选中的ID的集合
		if("".equals(nodes)||nodes==null)
		{
			return "保存失败";
		}
		final String roleid = arg2.get(0).get("roleid");//获取角色编号
		final String node[] = nodes.split(",");
		
		boolean isSuccess = Db.tx(new IAtom(){
			public boolean run() throws SQLException{
				List list = Db.find("select * from jbsf_roles_authes where roleid=?",roleid);
				int count = 0;
				if(list.size()==0)
				{
					count = 1;
				}
				else
				{
					count = Db.update("delete from jbsf_roles_authes where roleid=?", roleid);
				}
				int count2[] = new int[1000];
				int temp = 1;
				String uuid="";
				for(int i=0;i<node.length;i++)
				{
					uuid = Utils.getUUID();
					count2[i] = Db.update("insert into jbsf_roles_authes values('"+uuid+"','"+roleid+"','"+node[i]+"','write', '0')");
				}
				for(int j=0;j<node.length;j++)
				{
					if(count2[j]==0)
					{
						temp = 0;
					}
				}
				return count!=0 && temp == 1;
			}
		});
		if(isSuccess){
			return "保存成功";
		}else{
			return "保存失败";
		}
	}

	public String executeSeverAction(ReportRequest arg0,
			IComponentConfigBean arg1, List<Map<String, String>> arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}
}
