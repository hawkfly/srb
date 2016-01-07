package com.pansoft.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.pansoft.entity.BuyInfo;
import com.pansoft.entity.Goods;
import com.pansoft.entity.Order;
import com.pansoft.entity.OrderRecord;
import com.pansoft.entity.Retails;

@Component
@SuppressWarnings("unchecked")
public class RetailsDao extends SqlMapClientDaoSupport{

	/**
	 * 获取社区内所有商家信息
	 * @param sq_id
	 * @return
	 */
	public List<Retails> getAllRetails(String sq_id){
		List<Retails> list = this.getSqlMapClientTemplate().queryForList("sj.getAllRetails", sq_id);
		return list;
	}
	/**
	 * 获取社区内所有认证商家信息
	 * @param sq_id
	 * @return
	 */
	public List<Map<String, Object>> getAndroidRetails(String num){
		List<Map<String, Object>> list = this.getSqlMapClientTemplate().queryForList("sj.getAndroidRetails",num);
		return list;
	}
	/**
	 * 获取社区内所有促销商品信息
	 * @param sq_id
	 * @return
	 */
	public List<Map<String, Object>> getAndroidGoods(String num){
		List<Map<String, Object>> list = this.getSqlMapClientTemplate().queryForList("sj.getAndroidGoods",num);
		return list;
	}
	
	/**
	 * 获取商家的所有商品信息
	 * @param sj_id
	 * @return
	 */
	public List<Goods> getGoodsOfSj(String sj_id){
		List<Goods> list = this.getSqlMapClientTemplate().queryForList("sj.getGoodsOfSj", sj_id);
		return list;
	}

	public String getSeq(){
		String seq = (String) this.getSqlMapClientTemplate().queryForObject("sj.getNextSeq");
		return seq;
	}
	
	public void addOrderRecord(final OrderRecord order) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().execute(new SqlMapClientCallback(){
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				// TODO Auto-generated method stub
				executor.startBatch();
				executor.insert("sj.insertOrder", order);
				List<BuyInfo> lists = order.getBuyinfos();
				for(BuyInfo data : lists){
					executor.insert("sj.insertBuyinfo", data); 
				}
				int num = executor.executeBatch();
				return num;
			}
		});
	}

	public Goods getGoodsById(String id) {
		// TODO Auto-generated method stub
		Goods goods = (Goods) this.getSqlMapClientTemplate().queryForObject("sj.findGoodbyId", id);
		return goods;
	}

	public List<Order> getOrderOfSj(String sj_id) {
		// TODO Auto-generated method stub
		List<Order> list = this.getSqlMapClientTemplate().queryForList("sj.getOrdersOfsj",sj_id);
		return list;
	}

	public void updateOrderState(BuyInfo buyinfo) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("sj.upOrderState", buyinfo);
	}

	public List<OrderRecord> getOrderOfUser(String yz_id) {
		// TODO Auto-generated method stub
		List<OrderRecord>  list = this.getSqlMapClientTemplate().queryForList("sj.getOrderOfuser", yz_id);
		return list;
	}

	public List<String> getStatesOfOrder(String order_id) {
		// TODO Auto-generated method stub
		List<String>  list = this.getSqlMapClientTemplate().queryForList("sj.getStates", order_id);
		return list;
	}

	public void updateOrderState(String order_id) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("sj.upOrderAllState", order_id);
	}

	public Retails getRetailsByid(String id) {
		// TODO Auto-generated method stub
		Retails r = (Retails) this.getSqlMapClientTemplate().queryForObject("sj.findRetailsByid", id);
		return r;
	}
}
