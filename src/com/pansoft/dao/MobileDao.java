package com.pansoft.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.pansoft.entity.Goods;
import com.pansoft.entity.Retails;

@Component
public class MobileDao  extends SqlMapClientDaoSupport{

	public Goods getGoodInfo(String id) {
		// TODO Auto-generated method stub
		Goods goods = (Goods) this.getSqlMapClientTemplate().queryForObject("m.getGoodsInfo",id);
		return goods;
	}

	public Retails getReatils(String id) {
		// TODO Auto-generated method stub
		Retails retails = (Retails) this.getSqlMapClientTemplate().queryForObject("m.getRetailsInfo",id);
		return retails;
	}

	public List<Goods> getGoodsBySj(String sj_id) {
		// TODO Auto-generated method stub
		List<Goods> goods = this.getSqlMapClientTemplate().queryForList("m.getGoodsBySj", sj_id);
		return goods;
	}

}
