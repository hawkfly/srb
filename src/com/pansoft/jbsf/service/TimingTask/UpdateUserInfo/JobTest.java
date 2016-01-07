package com.pansoft.jbsf.service.TimingTask.UpdateUserInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jf.log.Logger;
import com.jf.plugin.activerecord.Db;
import com.jf.plugin.activerecord.IAtom;
import com.jf.plugin.activerecord.Record;
import com.pansoft.jbsf.bean.StaffBean;
import com.pansoft.jbsf.util.JbsfHelper;
import com.pansoft.plugins.CtfPlugin;
import com.pansoft.util.Consts;


public class JobTest implements Job {  
    static int a = 0;  
    private Logger logger = Logger.getLogger(CtfPlugin.class);
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	logger.debug(new Date()+" updateStaffInfo task begin now!");
    	JobDetail jobDetail = context.getJobDetail();
    	String name = jobDetail.getName();//当前任务所属用户的ID
    	if("updateUserInfo".equals(name))
    	{
    		Connection conn=null;
    		Statement st = null;
    		ResultSet rs = null;
    		try
    		{
				Class.forName(JbsfHelper.getversion("nkdriver"));
				conn = DriverManager.getConnection(JbsfHelper.getversion("nkurl"),JbsfHelper.getversion("nkuser"),JbsfHelper.getversion("nkpassword"));
				st = conn.createStatement();
				rs = st.executeQuery("select USR_USRID,USR_CAPTION,USR_PASSWORD,USR_DISABLE,USR_EMAIL1,USR_EMAIL2 from PANICS.SSF_USERS@GSNKLINK");
				
				Map<String,StaffBean> map1 = new HashMap<String,StaffBean>();//内控中的源数据
				Map<String,StaffBean> map2 = new HashMap<String,StaffBean>();//待更新的后台系统数据
				while(rs.next())
				{
					StaffBean sb = new StaffBean();
					sb.setId(rs.getString("USR_USRID"));
					sb.setRealName(rs.getString("USR_CAPTION"));
					sb.setEmail(rs.getString("USR_EMAIL1"));
					sb.setStatus(rs.getString("USR_DISABLE"));
					sb.setPassword(rs.getString("USR_PASSWORD"));
					map1.put(rs.getString("USR_USRID"), sb);
				}
				List list = Db.find("select id,username,password,email,islock,realname from jbsf_user");
				
				for(int i=0;i<list.size();i++)
				{
					StaffBean sfb = new StaffBean();
					Record rec = (Record)list.get(i);
					sfb.setId(rec.getStr("username"));
					sfb.setRealName(rec.getStr("realname"));
					sfb.setEmail(rec.getStr("email"));
					sfb.setStatus(rec.getStr("islock"));
					sfb.setOldId(rec.getStr("id"));
					sfb.setPassword(rec.getStr("password"));
					map2.put(rec.getStr("username"),sfb);
				}
				for(String key : map1.keySet())
				{
					//如果我们库中已存在该用户，则只同步状态和密码即可
					if(map2.containsKey(key))
					{
						StaffBean sf = map2.get(key);
						sf.setStatus(map1.get(key).getStatus());
						sf.setPassword(map1.get(key).getPassword());
						map2.put(key, sf);
					}
					//如果不存在，则该用户为新人，为该用户生成我们库中的编号存入old_Id属性中,给定一个初始密码
					else
					{
						StaffBean nsb = map1.get(key);
						nsb.setOldId("0"+nsb.getId());
						nsb.setPassword(map1.get(key).getPassword());
						map2.put(key,nsb);
					}
				}
				final List<String> ls = new ArrayList<String>();
				for(String ky : map2.keySet())
				{
					StaffBean sff = map2.get(ky);
					//把所有用户 不是锁定状态的 全部改成正常状态（包括0和null）
					if(!"1".equals(sff.getStatus()))
					{
						sff.setStatus("0");
					}
					String sql = "insert into jbsf_user(id,username,password,email,realname,islock) values('"+sff.getOldId()+"','"+sff.getId()+"','"+sff.getPassword()+"','"+sff.getEmail()+"','"+sff.getRealName()+"','"+sff.getStatus()+"')";
					ls.add(sql);
				}
				//添加事物
				boolean isSuccess = Db.tx(Consts.TRANSACTION_REPEATABLE_READ,new IAtom(){
					public boolean run() throws SQLException{
						int count1 = Db.update("delete from jbsf_user");
						int[] count2 = Db.batch(ls, ls.size());
						int tmp=0;
						for(int i=0;i<count2.length;i++)
						{
							if(count2[i]<=0)
							{
								tmp=1;
							}
						}
						return count1!=0&&tmp==0;
					}
				});
			} 
    		catch (Exception e) 
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finally
    		{	
    			try
    			{	//关闭连接
    				rs.close();
					st.close();
					conn.close();
				}
    			catch (SQLException e) 
    			{
					e.printStackTrace();
				}
    		}
    	}

    }
    
   
} 