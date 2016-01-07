package com.pansoft.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pansoft.dao.RetailsDao;
import com.pansoft.entity.BuyInfo;
import com.pansoft.entity.Goods;
import com.pansoft.entity.Order;
import com.pansoft.entity.OrderRecord;
import com.pansoft.entity.Retails;

@Service
public class RetailsService {

	@Resource
	private RetailsDao rd;

	public List<Retails> getRetails(String sq_id) {
		// TODO Auto-generated method stub
		return rd.getAllRetails(sq_id);
	}
	
	public List<Map<String, Object>> getAndroidRetails(String num){
		List<Map<String, Object>> list = rd.getAndroidRetails(num);
		return list;
	}
	public List<Map<String, Object>> getAndroidGoods(String num){
		List<Map<String, Object>> list = rd.getAndroidGoods(num);
		return list;
	}

	public List<Goods> getGoods(String sj_id) {
		// TODO Auto-generated method stub
		return rd.getGoodsOfSj(sj_id);
	}

	public void addOrderRecord(OrderRecord order,String yz_id) {
		// TODO Auto-generated method stub
		String seq = rd.getSeq();
		
		
		order.setYz_id(yz_id);
		order.setId(seq);
		Date date = Calendar.getInstance().getTime();
		order.setOrder_date(date);
		List<BuyInfo> lists = order.getBuyinfos();
		for(BuyInfo buyinfo : lists){
			buyinfo.setOrder_id(seq);
			buyinfo.setBuy_date(date);
			buyinfo.setState("0");
		}
		rd.addOrderRecord(order);
	}

	public Goods getGoodsById(String id) {
		// TODO Auto-generated method stub
		return rd.getGoodsById(id);
	}
	
	public List<Order> getOrderOfSj(String sj_id){
		return rd.getOrderOfSj(sj_id);
	}

	public void updateOrderState(BuyInfo buyinfo) {
		// TODO Auto-generated method stub
		rd.updateOrderState(buyinfo);
	}

	public List<OrderRecord> getOrderOfUser(String yz_id) {
		// TODO Auto-generated method stub
		List<OrderRecord> list = rd.getOrderOfUser(yz_id);
		for(OrderRecord or : list){
			String order_id = or.getId();
			List<String> states = rd.getStatesOfOrder(order_id);
			if(states.contains("1") || states.contains("0")){
				or.setExt2("ing");
			}else if(states.contains("2")){
				or.setExt2("结束");
			}
		}
		return list;
	}

	public void updateOrderState(String id) {
		// TODO Auto-generated method stub
		rd.updateOrderState(id);
	}

	public Retails getRetailsByid(String sj_id) {
		// TODO Auto-generated method stub
		return rd.getRetailsByid(sj_id);
	}
}
