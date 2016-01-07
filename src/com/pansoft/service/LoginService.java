package com.pansoft.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pansoft.dao.LoginDao;
import com.pansoft.entity.Community;
import com.pansoft.entity.Retails;
import com.pansoft.entity.RightInfo;
import com.pansoft.entity.RoleInfo;
import com.pansoft.entity.UserInfo;
import com.pansoft.entity.WyUser;

@Service
public class LoginService {

	@Resource
	private LoginDao ld;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String checkPhMm(UserInfo user){
		String res = ld.checkPhMm(user);
		return res;
	}
	public String checkWyMm(WyUser com){
		String res = ld.checkWyMm(com);
		return res;
	}
	public String checkSjMm(Retails ret){
		String res = ld.checkSjMm(ret);
		return res;
	}
	
	public void XgPhMM(UserInfo user){
		ld.XgPhMM(user);
	}
	public void XgWyMM(WyUser com){
		ld.XgWyMM(com);
	}
	public void XgSjMM(Retails ret){
		ld.XgSjMM(ret);
	}

	public List<Community> getCommUser(Community c) {
		// TODO Auto-generated method stub
		List<Community> list = ld.getUser(c);
		return list;
	}

	public List<Retails> getRetailsUser(Retails c){
		List<Retails> list = ld.getRetailsUser(c);
		return list;
	}
	
	public void logindata(Community c) {
		// TODO Auto-generated method stub
		ld.logindate(c);
	}

    public void logindata(UserInfo ui){
        ld.logindate(ui);
    }
    
    public void logindata(WyUser c) {
		// TODO Auto-generated method stub
		ld.logindate(c);
	}
	
	public UserInfo getUserInfo(UserInfo ui)
    {
        List<UserInfo> list = ld.getYzuser(ui);
        if(list.size()>0)
            return list.get(0);
        return null;
    }

    public UserInfo getUserInfoById(UserInfo ui)
    {
        List<UserInfo> list = ld.getYzuserById(ui);
        if(list.size()>0){
            UserInfo uiret = list.get(0);
            uiret.setJiaofang_str(sdf.format(uiret.getJiaofang()));
            return uiret;
        }
        return null;
    }

	public void logindata(Retails retails) {
		// TODO Auto-generated method stub
		ld.logindate(retails);
	}
	public List<WyUser> getWyUser(WyUser c) {
		// TODO Auto-generated method stub
		List<WyUser> list = ld.getWyUser(c);
		return list;
	}
	public List<RightInfo> getRightInfos(String roles) {
		// TODO Auto-generated method stub
		if(roles == null || "".equals(roles))
			return null;
		roles = "('" + roles.replaceAll(",", "','") + "')";
		List<RoleInfo> ri = ld.getRoleInfos(roles);
		
		String right_ids = "";
		if(ri != null){
			for(RoleInfo r : ri){
				String right = r.getRights();
				if(right != null && !"".equals(right)){
					if("".equals(right_ids)){
						right_ids = right;
					}else{
						right_ids = right_ids + "," +right;
					}
				}
			}
		}
		right_ids ="('"+right_ids.replaceAll(",", "','")+"')";
		List<RightInfo> r_list = new ArrayList<RightInfo>();
		List<RightInfo> list = ld.getRightInfos(right_ids);
		if(list != null){
			for(RightInfo r : list){
				String pid = r.getPid();
				if(pid != null && pid.equals("0")){
					r_list.add(r);
				}else{
					for(RightInfo temp : r_list){
						if(pid.equals(temp.getId())){
							temp.addRights(r);
							break;
						}
					}
				}
			}
		}
		
		return r_list;
	}
}
