package com.pansoft.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.pansoft.entity.PayFilterParam;
import com.pansoft.util.Consts;
import org.springframework.stereotype.Service;

import com.pansoft.dao.PayDao;
import com.pansoft.entity.CommunityBuild;
import com.pansoft.entity.PayInfo;
import com.pansoft.entity.Paytype;

@Service
public class PayService {

	@Resource
	private PayDao pd;
    private SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd");

    public List<Paytype> getPayType(String sq_id) {
		// TODO Auto-generated method stub
		return pd.getPayType(sq_id);
	}

	public List<CommunityBuild> getBuildsInfo(String sq_id) {
		// TODO Auto-generated method stub
		return pd.getBuilds(sq_id);
	}

	public CommunityBuild getBuild(String id) {
		// TODO Auto-generated method stub
		return pd.getBuild(id);
	}

	public List<CommunityBuild> getPayinfo(String type,String date, String sq_id, String build_id,
			String unit_id) {
		// TODO Auto-generated method stub
		List<CommunityBuild> list = pd.getQfInfo(type,date,sq_id,build_id,unit_id);
		return list;
	}

	public List<CommunityBuild> getPayinfo(String type, String date,
			String sq_id, String build_id) {
		// TODO Auto-generated method stub
		List<CommunityBuild> list = pd.getQfInfo(type,date,sq_id,build_id);
		return list;
	}

	public List<PayInfo> getPayinfo(String yz_id, String pay_id) {
		// TODO Auto-generated method stub
		return pd.getPayInfo(yz_id,pay_id);
	}

	public Paytype getPayTypeByName(String sq_id, String name) {
		// TODO Auto-generated method stub
		List<Paytype> list = pd.getPayTypeByName(sq_id,name);
		Paytype paytype = null;
		if(list != null && list.size()>0){
			paytype = list.get(0);
		}
		return paytype;
	}
	
	public List<PayInfo> getYzJfxx(String yz_id,String page) {
		List<PayInfo> list = pd.getYzJfxx(yz_id);

        List<PayInfo> temp = new ArrayList<PayInfo>();
        int start = (Integer.valueOf(page)-1)* Consts.yzpayinfo_num_per_page;
        int end = (Integer.valueOf(page))*Consts.yzpayinfo_num_per_page;
        if(end > list.size()){
            end = list.size();
        }
        for(;start<end;start++){
            PayInfo n = list.get(start);
            temp.add(n);
        }

		return temp;
	}

	public Paytype getPayTypeById(String id) {
		// TODO Auto-generated method stub
		Paytype paytype = pd.getPayTypeById(id);
		return paytype;
	}
	
	public String getYzWjf(String yz_id) {
		String list = pd.getYzWjf(yz_id);
		return list;
	}

    public List<PayInfo> getPayedinfo(PayFilterParam filter) {

        String filterSql = "";
        if(filter.getStartDate() !=null && filter.getStartDate().length()>0)
            filterSql += " and pi.real_date >= to_date('"+ filter.getStartDate() +"','yyyy-mm-dd')";
        if(filter.getEndDate() !=null && filter.getEndDate().length()>0)
            filterSql += " and pi.real_date <= to_date('"+ filter.getEndDate()+"','yyyy-mm-dd')";
        if (filter.getPaytype() !=null&& !filter.getPaytype().equals("0"))
            filterSql += " and pi.pay_id = "+ filter.getPaytype();
        filterSql+=" and 1=1 ";
        List<PayInfo> payInfos = pd.getPayInfo(true,filterSql);

        return payInfos;
    }

    public List<PayInfo> getUnPayinfo(PayFilterParam filter) {

        String filterSql = "";
        if(filter.getStartDate() !=null && filter.getStartDate().length()>0)
            filterSql += " and pi.plan_date >= to_date('"+ filter.getStartDate() +"','yyyy-mm-dd')";
        if(filter.getEndDate() !=null && filter.getEndDate().length()>0)
            filterSql += " and pi.plan_date <= to_date('"+ filter.getEndDate()+"','yyyy-mm-dd')";
        if (filter.getPaytype() !=null&& filter.getPaytype().length()>0)
            filterSql += " and pi.pay_id = "+ filter.getPaytype();
        filterSql+=" and 1=1 ";
        List<PayInfo> payInfos = pd.getPayInfo(false,filterSql);

        return payInfos;
    }
}
