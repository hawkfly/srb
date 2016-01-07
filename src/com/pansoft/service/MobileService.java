package com.pansoft.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pansoft.dao.MobileDao;
import com.pansoft.entity.Goods;
import com.pansoft.entity.Retails;
import com.pansoft.util.FreeMakerUtil;

@Service
public class MobileService {

	@Resource
	private MobileDao md;

	public String creatGoodsPage(String path,String id) {
		// TODO Auto-generated method stub
		Goods goods  = md.getGoodInfo(id);
		Retails retails = md.getReatils(goods.getSj_id());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goods", goods);
		map.put("retails", retails);
		FreeMakerUtil.creatGoodsPage(path, id, map);
		return "success";
	}
	
	public String creatRetailsPage(String path,String id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Retails retails = md.getReatils(id);
		//查询店家商品信息
		List<Goods> goods = md.getGoodsBySj(id);
		map.put("retails", retails);
		map.put("goods", goods);
		FreeMakerUtil.creatRetailsPage(path, id, map);
		return "success";
	}
}
