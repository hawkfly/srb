package com.pansoft.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.pansoft.entity.CommunityBuild;
import com.pansoft.entity.PayInfo;
import com.pansoft.entity.Paytype;

@Component
@SuppressWarnings("unchecked")
public class PayDao extends SqlMapClientDaoSupport{

	public List<Paytype> getPayType(String sq_id) {
		// TODO Auto-generated method stub
		List<Paytype> list = this.getSqlMapClientTemplate().queryForList("pay.paytypeofsq", sq_id);
		return list;
	}

	public List<CommunityBuild> getBuilds(String pid) {
		// TODO Auto-generated method stub
		List<CommunityBuild> list = this.getSqlMapClientTemplate().queryForList("pay.buids", pid);
		return list;
	}

	public CommunityBuild getBuild(String id) {
		// TODO Auto-generated method stub
		CommunityBuild b =(CommunityBuild) this.getSqlMapClientTemplate().queryForObject("pay.build", id);
		return b;
	}

	public List<CommunityBuild> getQfInfo(String type, String date,
			String sq_id, String build_id, String unit_id) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		map.put("pay_id", type);
		map.put("plan_date", date);
		map.put("sq_id", sq_id);
		map.put("louhao", build_id);
		map.put("danyan", unit_id);
		List<CommunityBuild> list = this.getSqlMapClientTemplate().queryForList("pay.getqf1", map);
		return list;
	}

	public List<CommunityBuild> getQfInfo(String type, String date,
			String sq_id, String build_id) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		map.put("pay_id", type);
		map.put("plan_date", date);
		map.put("sq_id", sq_id);
		map.put("louhao", build_id);
		List<CommunityBuild> list = this.getSqlMapClientTemplate().queryForList("pay.getqf2", map);
		return list;
	}

	public List<PayInfo> getPayInfo(String yz_id, String pay_id) {
		// TODO Auto-generated method stub
		PayInfo payinfo = new PayInfo();
		payinfo.setYz_id(yz_id);
		payinfo.setPay_id(pay_id);
		List<PayInfo> list = this.getSqlMapClientTemplate().queryForList("pay.getPayInfo",payinfo);
		return list;
	}
	public List<PayInfo> getYzJfxx(String yz_id) {
		List<PayInfo> list = this.getSqlMapClientTemplate().queryForList("pay.getYzJfxx",yz_id);
		return list;
	}
	public String getYzWjf(String yz_id) {
		String list = (String) this.getSqlMapClientTemplate().queryForObject("pay.getYzWjf",yz_id);
		return list;
	}

	public List<Paytype> getPayTypeByName(String sq_id, String name) {
		// TODO Auto-generated method stub
		Paytype paytype = new Paytype();
		paytype.setSq_id(sq_id);
		paytype.setName(name);
		List<Paytype> list = this.getSqlMapClientTemplate().queryForList("pay.getPayTypeByName", paytype);
		return list;
	}

	public Paytype getPayTypeById(String id) {
		// TODO Auto-generated method stub
		Paytype paytype = (Paytype) this.getSqlMapClientTemplate().queryForObject("pay.getPayTypeById", id);
		return paytype;
	}

    public List<PayInfo> getPayInfo(boolean status,String filter) {
        if(status)
            return this.getSqlMapClientTemplate().queryForList("pay.getPayedInfo",filter);
        else
            return this.getSqlMapClientTemplate().queryForList("pay.getUnPayInfo",filter);
    }
}
